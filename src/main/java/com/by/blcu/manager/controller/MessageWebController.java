package com.by.blcu.manager.controller;

import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.manager.common.StringHelper;
import com.by.blcu.manager.model.WebMessage;
import com.by.blcu.manager.model.WebMessageCategory;
import com.by.blcu.manager.model.WebMessageConsum;
import com.by.blcu.manager.model.extend.UserMessageExtend;
import com.by.blcu.manager.model.sql.InputMessage;
import com.by.blcu.manager.model.sql.InputMessageCategory;
import com.by.blcu.manager.model.sql.InputMessageConsum;
import com.by.blcu.manager.service.WebMessageCategoryService;
import com.by.blcu.manager.service.WebMessageConsumService;
import com.by.blcu.manager.service.WebMessageService;
import com.by.blcu.manager.umodel.MessageUnReadDetail;
import com.by.blcu.manager.umodel.MessageUnReadModel;
import com.by.blcu.manager.umodel.TeacherShowModel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jsoup.select.Collector;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 前台消息相关接口
 */
@RestController
@RequestMapping("/messageWeb")
@Api(tags = "前端消息接口API", description = "包含接口：\n" +
        "1、消息分类-获取消息分类【getMessageCategory】\n" +
        "2、消息-根据消息分类，分页获取消息列表【getMessageByCategoryId】\n" +
        "3、消息-获取未读消息数-详细【getMessageUnReadCount】\n" +
        "4、消息-获取未读消息数-总数【getUnReadMessage】\n" +
        "5、消息-阅读消息【readMessage】\n")
public class MessageWebController extends ManagerBase {

    @Resource
    private WebMessageService webMessageService;
    @Resource
    private WebMessageCategoryService webMessageCategoryService;
    @Resource
    private WebMessageConsumService webMessageConsumService;

    @ApiOperation(value = "消息分类-获取消息分类", notes = "获取消息分类")
    @GetMapping("/getMessageCategory")
    public RetResult<List<WebMessageCategory>> getMessageCategory() throws Exception {
       InputMessageCategory search =new InputMessageCategory();
       List<WebMessageCategory> list =webMessageCategoryService.findCategoryList(search,userSessionHelper);
       return RetResponse.makeOKRsp(list);
    }

    @ApiOperation(value = "消息-根据消息分类，分页获取消息列表", notes = "根据消息分类，分页获取消息列表")
    @GetMapping("/getMessageByCategoryId")
    public RetResult<PageInfo<UserMessageExtend>> getMessageByCategoryId(InputMessage search) throws Exception {
        if (search == null) {
            return RetResponse.makeErrRsp("参数不能为空");
        }
        Integer page = Integer.valueOf(1);
        Integer size = Integer.valueOf(1);
        if (search.getPage() == null || search.getPage().intValue() < 0) {
            page =0;
        } else {
            page = search.getPage();
        }
        if (search.getSize() == null || search.getSize().intValue() < 0) {
            size = 0;
        } else {
            size = search.getSize();
        }
        search.setUserName(userSessionHelper.getUserName());
        Integer count = this.webMessageService.findUserMessageListCount(search,userSessionHelper);
        PageInfo<UserMessageExtend> pageInfo = null;
        if (count.intValue() < 1) {
            pageInfo = new PageInfo<UserMessageExtend>();
        } else {

            PageHelper.startPage(page.intValue(), size.intValue());
            List<UserMessageExtend> list = this.webMessageService.findUserMessageList(search,userSessionHelper);
            pageInfo = new PageInfo<UserMessageExtend>(list);
            pageInfo.setTotal(count.intValue());
        }
        return RetResponse.makeOKRsp(pageInfo);
    }

    @ApiOperation(value = "消息-获取未读消息数-详细", notes = "获取各分类未读消息数及总数")
    @GetMapping("/getMessageUnReadCount")
    public RetResult<MessageUnReadModel> getMessageUnReadCount(InputMessage search) throws Exception{
        InputMessageConsum searchModel=new InputMessageConsum();
        MessageUnReadModel result =new MessageUnReadModel();
        if(search!=null){
            if(StringHelper.IsNullOrWhiteSpace(search.getCategoryId())){
                //查所有分类，即总数
                search.setUserName(userSessionHelper.getUserName());
                search.setIsRead(false);
                List<UserMessageExtend> list = webMessageService.findUserMessageList(search,userSessionHelper);

                if(StringHelper.IsNullOrEmpty(list)){
                    result.setTotal(0L);
                }
                else{
                    result.setTotal((long) list.size());
                    Map<String,Long> mapList = list.stream().collect(Collectors.groupingBy(UserMessageExtend::getCategoryId, Collectors.counting()));
                    List<MessageUnReadDetail> messageList =new ArrayList<MessageUnReadDetail>();
                    for (Map.Entry<String, Long> entry : mapList.entrySet()) {
                        MessageUnReadDetail detail = new MessageUnReadDetail();
                        detail.setCategoryId(entry.getKey());
                        detail.setTotal(entry.getValue());
                        messageList.add(detail);
                    }
                    result.setDetailList(messageList);
                }
            }else{
                search.setCategoryId(search.getCategoryId());
                search.setUserName(userSessionHelper.getUserName());
                search.setIsRead(false);
                Integer count = webMessageService.findUserMessageListCount(search,userSessionHelper);
                result.setTotal((long)count);
            }
        }
        return RetResponse.makeOKRsp(result);
    }

    @ApiOperation(value = "消息-阅读消息", notes = "阅读消息")
    @PostMapping("/readMessage")
    public RetResult<Integer> readMessage(@RequestBody InputMessage model)throws Exception{
        if(model==null){
            return  RetResponse.makeErrRsp("参数不能为空");
        }
        WebMessageConsum webMessageConsum =new WebMessageConsum();
        webMessageConsum.setMessageId(model.getMessageId());
        return webMessageConsumService.readConsum(webMessageConsum,userSessionHelper);
    }

    @ApiOperation(value = "消息-获取未读消息数-总数", notes = "获取未读消息总数")
    @GetMapping("/getUnReadMessage")
    public RetResult<Integer> getUnReadMessage() throws Exception{
        InputMessage search=new InputMessage();
        MessageUnReadModel result =new MessageUnReadModel();

        //查所有分类，即总数
        search.setUserName(userSessionHelper.getUserName());
        search.setIsRead(false);
        Integer count = webMessageService.findUserMessageListCount(search,userSessionHelper);
        result.setTotal((long)count);

        return RetResponse.makeOKRsp(count);
    }



}
