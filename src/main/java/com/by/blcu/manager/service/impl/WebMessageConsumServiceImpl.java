package com.by.blcu.manager.service.impl;

import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.universal.AbstractService;
import com.by.blcu.core.utils.ApplicationUtils;
import com.by.blcu.mall.service.MallOrderInfoService;
import com.by.blcu.mall.vo.MallOrderInfoVo;
import com.by.blcu.manager.common.StringHelper;
import com.by.blcu.manager.common.UserSessionHelper;
import com.by.blcu.manager.dao.WebMessageConsumMapper;
import com.by.blcu.manager.model.SsoUser;
import com.by.blcu.manager.model.WebMessage;
import com.by.blcu.manager.model.WebMessageConsum;
import com.by.blcu.manager.model.sql.InputMessage;
import com.by.blcu.manager.model.sql.InputMessageConsum;
import com.by.blcu.manager.service.SsoUserService;
import com.by.blcu.manager.service.WebMessageConsumService;
import com.by.blcu.manager.umodel.UserSearchModel;
import javafx.application.Application;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 耿鹤闯
 * @Description: WebMessageConsumService接口实现类
 * @date 2019/09/19 10:19
 */
@Service
public class WebMessageConsumServiceImpl extends AbstractService<WebMessageConsum> implements WebMessageConsumService {

    @Resource
    private WebMessageConsumMapper webMessageConsumMapper;
    @Resource
    private SsoUserService ssoUserService;
    @Resource
    private MallOrderInfoService mallOrderInfoService;

    public List<WebMessageConsum> findConsumList(InputMessageConsum model, UserSessionHelper helper) {
        return webMessageConsumMapper.findConsumList(model);
    }

    public Integer findConsumListCount(InputMessageConsum model, UserSessionHelper helper) {
        return webMessageConsumMapper.findConsumListCount(model);
    }

    public RetResult<Integer> addConsum(WebMessageConsum model, UserSessionHelper helper) {
        if (model == null || StringHelper.IsNullOrWhiteSpace(model.getMessageId())) {
            return RetResponse.makeErrRsp("[消息Id]不能为空");
        }
        model.setConsumId(ApplicationUtils.getUUID());
        model.setMessageId(model.getMessageId());
        model.setUserId(helper.getUserId());
        model.setUserName(helper.getUserName());
        Date datetime = new Date();
        model.setCreateTime(datetime);
        Integer state = webMessageConsumMapper.insert(model);
        return RetResponse.makeOKRsp(state);
    }
    public RetResult<Integer> readConsum(WebMessageConsum model,UserSessionHelper helper){
        if (model == null || StringHelper.IsNullOrWhiteSpace(model.getMessageId())) {
            return RetResponse.makeErrRsp("[消息Id]不能为空");
        }
        model.setUserName(helper.getUserName());
        Integer state = webMessageConsumMapper.readConsum(model);
        return RetResponse.makeOKRsp(state);
    }

    public WebMessageConsum selectConsumById(String consumId, UserSessionHelper helper) {
        if (StringHelper.IsNullOrWhiteSpace(consumId)) {
            return null;
        }
        InputMessageConsum checkModel = new InputMessageConsum();
        checkModel.setConsumId(consumId);
        List<WebMessageConsum> list = webMessageConsumMapper.findConsumList(checkModel);
        if (StringHelper.IsNullOrEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

    //region 发送消息

    /**
     * 给所有人发送消息
     *
     * @param messageId
     */
    @Async
    public Boolean sendMessageAll(String messageId, UserSessionHelper helper) {
        Boolean result=true;
        //获取所有的人员
        int batchSize = 1000;
        UserSearchModel search = new UserSearchModel();
        List<String> userNameList=new ArrayList<String>();
        List<SsoUser> userList = ssoUserService.selectListAnd(search);
        if (!StringHelper.IsNullOrEmpty(userList)) {

            //对比需要发送的人
            InputMessageConsum checkSend= new InputMessageConsum();
            checkSend.setMessageId(messageId);
            List<WebMessageConsum> checkResult = webMessageConsumMapper.findConsumList(checkSend);
            if(!StringHelper.IsNullOrEmpty(checkResult)){
                //存在已发送过的人
                List<String> hasSendList = checkResult.stream().map(t->t.getUserName()).collect(Collectors.toList());
                //已收到的学生就不再发
                userList = userList.stream().filter(t->!hasSendList.contains(t.getUserName())).collect(Collectors.toList());
            }
            userNameList =userList.stream().map(t->t.getUserName()).collect(Collectors.toList());
        }
        result = _sendUserMessage(batchSize,messageId,userNameList);
        return result;
    }

    /**
     * 给已经购买了课程或分类下的课程的人发送消息
     *
     * @param messageId
     */

    @Async
    public Boolean sendMessgaeBuyer(String messageId, String ccId, String commodityId, UserSessionHelper helper) {
        Boolean result=true;
        //获取所有的人员
        int batchSize = 1000;
        UserSearchModel search = new UserSearchModel();
        List<MallOrderInfoVo> mallOrderInfoVoList = mallOrderInfoService.selectMallOrderInfoVoList();
        if(StringHelper.IsNullOrEmpty(mallOrderInfoVoList)){
            return false;
        }
        List<String> userNameList=new ArrayList<String>();
        if (!StringHelper.IsNullOrWhiteSpace(commodityId)) {
            //针对商品发送
            userNameList = mallOrderInfoVoList.stream().filter(t->t.getMallCommodityOrderVoList()!=null
                    && t.getMallCommodityOrderVoList().stream().filter(m->m.getCommodityInfoFileVo()!=null && m.getCommodityId().equals(commodityId)).count()>0)
                    .map(MallOrderInfoVo::getUserName).collect(Collectors.toList());
        }
        else{
            //针对分类发送
            userNameList = mallOrderInfoVoList.stream().filter(t->t.getMallCommodityOrderVoList()!=null
                    && t.getMallCommodityOrderVoList().stream().filter(m->m.getCommodityInfoFileVo()!=null && m.getCommodityInfoFileVo().getCcId().equals(ccId)).count()>0)
                    .map(MallOrderInfoVo::getUserName).collect(Collectors.toList());
        }

        List<String> insertList=new ArrayList<String>();
        List<String> deleteList=new ArrayList<String>();

        //对比需要发送的人
        InputMessageConsum checkSend= new InputMessageConsum();
        checkSend.setMessageId(messageId);
        List<WebMessageConsum> checkResult = webMessageConsumMapper.findConsumList(checkSend);
        if(!StringHelper.IsNullOrEmpty(checkResult)){
            //已发送过的人
            List<String> hasConsumIds = checkResult.stream().map(t->t.getUserName()).collect(Collectors.toList());
            insertList = new ArrayList<String>(userNameList);
            insertList.removeAll(hasConsumIds);

            deleteList = new ArrayList<String>(hasConsumIds);
            deleteList.removeAll(userNameList);
        }

        //已收到旧消息但新消息不属于接收范围的学生，删掉消息
        if(!StringHelper.IsNullOrEmpty(deleteList)){
            List<String> deleteUserNameList = deleteList;
            List<String> deleteIdList  =checkResult.stream().filter(t->deleteUserNameList.contains(t.getUserName())).map(t->t.getConsumId()).collect(Collectors.toList());
            InputMessageConsum deleteModel = new InputMessageConsum();
            deleteModel.setConsumIdList(deleteIdList);
            webMessageConsumMapper.deleteConsum(deleteModel);
        }

        //只发送新加的用户
        result = _sendUserMessage(batchSize,messageId,insertList);
        return result;
    }

    private boolean _sendUserMessage(int batchSize, String messageId,List<String> userNameList){
        if(StringHelper.IsNullOrEmpty(userNameList)){
            return true;
        }
        boolean result =true;
        Date datetime = new Date();
        List<WebMessageConsum> list = new ArrayList<>();
        int start = 0;
        for (Integer i = 0; i < userNameList.size(); i++) {
            String userName = userNameList.get(i);
            WebMessageConsum model = new WebMessageConsum();
            model.setCreateTime(datetime);
            model.setUserName(userName);
            model.setUserId("");
            model.setConsumId(ApplicationUtils.getUUID());
            model.setIsRead(false);
            model.setMessageId(messageId);
            list.add(model);
            if ((i + 1) % batchSize == 0) {
                int state = webMessageConsumMapper.insertConsumList(list);
                if(state<1){
                    result=false;
                }
                list.clear();
            }
        }
        if (!StringHelper.IsNullOrEmpty(list)) {
            int state = webMessageConsumMapper.insertConsumList(list);
            if(state<1){
                result=false;
            }
        }
        return result;
    }

    //endregion

}