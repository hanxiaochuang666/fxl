package com.by.blcu.manager.controller;

import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.utils.ApplicationUtils;
import com.by.blcu.manager.model.WebMessage;
import com.by.blcu.manager.model.sql.InputMessage;
import com.by.blcu.manager.service.WebMessageService;
import com.by.blcu.manager.umodel.TeacherShowModel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
* @Description: WebMessageController类
* @author 耿鹤闯
* @date 2019/09/19 10:19
*/
@RestController
@RequestMapping("/webMessage")
@Api(tags = "Web消息接口API", description = "包含接口：\n" +
        "1、消息管理-添加消息【addMessage】\n" +
        "2、消息管理-删除消息【deleteMessage】\n" +
        "3、消息管理-修改消息【updateMessage】\n" +
        "4、消息管理-根据消息Id，获取消息【selectMessageById】\n"+
        "5、消息管理-分页获取消息列表【list】\n")
public class WebMessageController extends ManagerBase {

    @Resource
    private WebMessageService webMessageService;

    @ApiOperation(value = "消息管理-添加消息", notes = "添加消息")
    @PostMapping("/addMessage")
    @RequiresPermissions("message:message")
    public RetResult<Integer> addMessage(@RequestBody WebMessage webMessage) throws Exception{
        return webMessageService.addMessage(webMessage,userSessionHelper);
    }
    @ApiOperation(value = "消息管理-删除消息", notes = "删除消息")
    @PostMapping("/deleteMessage")
    @RequiresPermissions("message:message")
    public RetResult<Integer> deleteMessage(@RequestBody InputMessage model) throws Exception {
        return webMessageService.deleteMessageByIdList(model,userSessionHelper);
    }
    @ApiOperation(value = "消息管理-修改消息", notes = "修改消息")
    @PostMapping("/updateMessage")
    @RequiresPermissions("message:message")
    public RetResult<Integer> updateMessage(@RequestBody WebMessage webMessage) throws Exception {
        return webMessageService.updateMessage(webMessage,userSessionHelper);
    }
    @ApiOperation(value = "消息管理-根据消息Id，获取消息", notes = "根据消息Id，获取消息")
    @GetMapping("/selectMessageById")
    @RequiresPermissions("message:message")
    public RetResult<WebMessage> selectMessageById(@RequestParam String messageId) throws Exception {
        WebMessage webMessage = webMessageService.selectMessageById(messageId,userSessionHelper);
        return RetResponse.makeOKRsp(webMessage);
    }
    @ApiOperation(value = "消息管理-分页获取消息列表", notes = "分页获取消息列表")
    @GetMapping("/list")
    @RequiresPermissions("message:message")
    public RetResult<PageInfo<WebMessage>> list(InputMessage search) throws Exception {
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

        Integer count = this.webMessageService.findMessageListCount(search,userSessionHelper);
        PageInfo<WebMessage> pageInfo = null;
        if (count.intValue() < 1) {
            pageInfo = new PageInfo<WebMessage>();
        } else {

            PageHelper.startPage(page.intValue(), size.intValue());
            List<WebMessage> list = this.webMessageService.findMessageList(search,userSessionHelper);
            pageInfo = new PageInfo<WebMessage>(list);
            pageInfo.setTotal(count.intValue());
        }
        return RetResponse.makeOKRsp(pageInfo);
    }


}