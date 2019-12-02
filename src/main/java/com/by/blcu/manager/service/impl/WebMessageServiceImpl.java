package com.by.blcu.manager.service.impl;

import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.universal.AbstractService;
import com.by.blcu.core.utils.ApplicationUtils;
import com.by.blcu.manager.common.ManagerHelper;
import com.by.blcu.manager.common.StringHelper;
import com.by.blcu.manager.common.UserSessionHelper;
import com.by.blcu.manager.dao.WebMessageMapper;
import com.by.blcu.manager.model.ManagerLog;
import com.by.blcu.manager.model.WebMessage;
import com.by.blcu.manager.model.WebMessageConsum;
import com.by.blcu.manager.model.extend.UserMessageExtend;
import com.by.blcu.manager.model.sql.InputMessage;
import com.by.blcu.manager.model.sql.ScopeEnum;
import com.by.blcu.manager.service.WebMessageConsumService;
import com.by.blcu.manager.service.WebMessageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.util.UriUtils;

import javax.annotation.Resource;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;

/**
* @Description: WebMessageService接口实现类
* @author 耿鹤闯
* @date 2019/09/19 10:19
*/
@Service
public class WebMessageServiceImpl extends AbstractService<WebMessage> implements WebMessageService {

    @Resource
    private WebMessageMapper webMessageMapper;
    @Resource
    private WebMessageConsumService webMessageConsumService;

    public List<WebMessage> findMessageList(InputMessage model,UserSessionHelper helper){
        if(!StringHelper.IsNullOrWhiteSpace(model.getContent())){
            model.setContent(HtmlUtils.htmlEscape(model.getContent(),"UTF-8"));
        }
        if(!helper.getOrgType().equals(ManagerHelper.OrgType)){
            model.setOrgCode(helper.getOrgCode());
        }
        List<WebMessage> result = webMessageMapper.findMessageList(model);
        if(!StringHelper.IsNullOrEmpty(result)){
            result.forEach(item->{
                item.setContent(HtmlUtils.htmlUnescape(item.getContent()));
            });
        }
        return result;
    }
    public Integer findMessageListCount(InputMessage model,UserSessionHelper helper){
        if(!helper.getOrgType().equals(ManagerHelper.OrgType)){
            model.setOrgCode(helper.getOrgCode());
        }
        return  webMessageMapper.findMessageListCount(model);
    }
    public RetResult<Integer> deleteMessageByIdList(InputMessage model, UserSessionHelper helper){
        if(model==null ||  StringHelper.IsNullOrEmpty(model.getMessageIdList())){
            return RetResponse.makeErrRsp("[消息Id列表]不能为空");
        }
        if(!helper.getOrgType().equals(ManagerHelper.OrgType)){
            model.setOrgCode(helper.getOrgCode());
        }
        Date dateTime =new Date();
        model.setModifyBy(helper.getUserName());
        model.setModifyTime(dateTime);
        Integer state =  webMessageMapper.deleteMessageByIdList(model);
        return RetResponse.makeOKRsp(state);
    }

    public RetResult<Integer> addMessage(WebMessage model, UserSessionHelper helper){
        if(model==null || StringHelper.IsNullOrWhiteSpace(model.getContent()) || StringHelper.IsNullOrWhiteSpace(model.getCategoryId()) || StringHelper.IsNullOrZero(model.getScope())){
            return RetResponse.makeErrRsp("[消息类别，内容，发送范围]不能为空");
        }
        if(StringHelper.IsNullOrZero(model.getSort())){
            model.setSort(0);
        }

        if(!helper.getOrgType().equals(ManagerHelper.OrgType)){
            model.setOrgCode(helper.getOrgCode());
        }

        model.setContent(HtmlUtils.htmlEscape(model.getContent(), "UTF-8"));
        model.setMessageId(ApplicationUtils.getUUID());
        Date datetime =new Date();
        model.setCreateTime(datetime);
        model.setCreateBy(helper.getUserName());
        model.setModifyTime(datetime);
        model.setModifyBy(helper.getUserName());
        Integer state = webMessageMapper.insert(model);

        switch (model.getScope()){
            case 1:
                //所有
                webMessageConsumService.sendMessageAll(model.getMessageId(),helper);
                break;
            case 2:
                //商品分类
                webMessageConsumService.sendMessgaeBuyer(model.getMessageId(),model.getCcId(),null,helper);
                break;
            case 3:
                //商品
                webMessageConsumService.sendMessgaeBuyer(model.getMessageId(),model.getCcId(),model.getCommodityId(),helper);
                break;
             default:
                 break;
        }
        return RetResponse.makeOKRsp(state);
    }

    public RetResult<Integer> updateMessage(WebMessage model, UserSessionHelper helper){
        if(model==null || StringHelper.IsNullOrWhiteSpace(model.getMessageId())){
            return RetResponse.makeErrRsp("[消息Id]不能为空");
        }
        InputMessage checkModel =new InputMessage();
        checkModel.setMessageId(model.getMessageId());

        if(!helper.getOrgType().equals(ManagerHelper.OrgType)){
            checkModel.setOrgCode(helper.getOrgCode());
        }

        Integer exit = webMessageMapper.findMessageListCount(checkModel);

        if(exit<1){
            return RetResponse.makeErrRsp("消息不存在");
        }
        if(!StringHelper.IsNullOrWhiteSpace(model.getContent())){
            model.setContent(HtmlUtils.htmlEscape(model.getContent(), "UTF-8"));
        }
        Date datetime =new Date();
        model.setOrgCode(null);
        model.setCreateTime(null);
        model.setCreateBy(null);
        model.setModifyTime(datetime);
        model.setModifyBy(helper.getUserName());
        Integer state = webMessageMapper.updateByPrimaryKeySelective(model);

        switch (model.getScope()){
            case 1:
                //所有
                webMessageConsumService.sendMessageAll(model.getMessageId(),helper);
                break;
            case 2:
                //商品分类
                webMessageConsumService.sendMessgaeBuyer(model.getMessageId(),model.getCcId(),null,helper);
                break;
            case 3:
                //商品
                webMessageConsumService.sendMessgaeBuyer(model.getMessageId(),model.getCcId(),model.getCommodityId(),helper);
                break;
            default:
                break;
        }
        return RetResponse.makeOKRsp(state);

    }

    public WebMessage selectMessageById(String messageId,UserSessionHelper helper){
        if(StringHelper.IsNullOrWhiteSpace(messageId)){
            return null;
        }
        InputMessage checkModel =new InputMessage();
        checkModel.setMessageId(messageId);

        if(!helper.getOrgType().equals(ManagerHelper.OrgType)){
            checkModel.setOrgCode(helper.getOrgCode());
        }

        List<WebMessage> list = webMessageMapper.findMessageList(checkModel);
        if(StringHelper.IsNullOrEmpty(list)){
            return null;
        }
        WebMessage result  = list.get(0);
        result.setContent(HtmlUtils.htmlUnescape(result.getContent()));
        return result;
    }

    public List<UserMessageExtend> findUserMessageList(InputMessage model, UserSessionHelper helper){

        if(!helper.getOrgType().equals(ManagerHelper.OrgType)){
            model.setOrgCode(helper.getOrgCode());
        }

        List<UserMessageExtend> result = webMessageMapper.findUserMessageList(model);
        if(!StringHelper.IsNullOrEmpty(result)){
            result.forEach(t->{
                t.setContent(HtmlUtils.htmlUnescape(t.getContent()));
            });
        }
        return result;
    }
    public Integer findUserMessageListCount(InputMessage model, UserSessionHelper helper){

        if(!helper.getOrgType().equals(ManagerHelper.OrgType)){
            model.setOrgCode(helper.getOrgCode());
        }

        return  webMessageMapper.findUserMessageListCount(model);
    }

}