package com.by.blcu.resource.controller;

import com.alibaba.fastjson.JSONObject;
import com.by.blcu.core.aop.CheckToken;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.ServiceException;
import com.by.blcu.resource.model.LiveDetailInfo;
import com.by.blcu.resource.model.LiveModel;
import com.by.blcu.resource.model.LiveRetModel;
import com.by.blcu.resource.service.ILiveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CheckToken
@RestController
@RequestMapping(value = "/live")
@Slf4j
@Api(tags = "直播服务API",description = "包含接口：\n" +
        "1、创建直播间\n" +
        "2、直播回放\n" +
        "3、关闭直播间\n" +
        "4、编辑直播间\n" +
//        "5、查询直播间列表\n" +
        "5、查询直播间详情\n" +
//        "7、查询问答信息\n" +
        "6、获取直播间二维码")
public class LiveController {

    @Autowired
    private ILiveService service;

    @RequestMapping(value = "/createRoom",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "创建直播间")
    RetResult<LiveRetModel> createRoom(@ApiParam(value = "liveModel") @RequestBody LiveModel liveModel, HttpServletRequest request) throws Exception {
        return RetResponse.makeOKRsp(service.createRoom(liveModel,request));
    }

    @RequestMapping(value = "/getPlaybackUrl",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "直播回放")
    RetResult<String> getPlaybackUrl(@ApiParam(value = "直播间房间id【roomId】",required = true) @RequestBody JSONObject obj, HttpServletRequest request) throws Exception {

        if(obj.containsKey("roomId")){
            String roomId = obj.getString("roomId");
            return RetResponse.makeOKRsp(service.getPlaybackUrl(roomId,request));
        }else {
            throw new ServiceException("参数：【roomId】必传！");
        }

    }

    @RequestMapping(value = "/updateRoom",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "编辑直播间")
    RetResult<LiveRetModel> updateRoom(@ApiParam(value = "liveModel") @RequestBody LiveModel liveModel, HttpServletRequest request) throws Exception {
        return RetResponse.makeOKRsp(service.updateRoom(liveModel,request));
    }

    @RequestMapping(value = "/searchRoom",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "查询直播间信息")
    RetResult<LiveDetailInfo> searchRoom(@ApiParam(value = "直播间房间id【roomId】",required = true) @RequestBody JSONObject obj, HttpServletRequest request) throws Exception {

        if(obj.containsKey("roomId")){
            String roomId = obj.getString("roomId");
            return RetResponse.makeOKRsp(service.searchRoom(roomId,request));
        }else {
            throw new ServiceException("参数：【roomId】必传！");
        }

    }

    @RequestMapping(value = "/getRoomImg",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "获取直播间二维码")
    String getRoomImg(@ApiParam(value = "直播间房间id【roomId】",required = true) @RequestBody JSONObject obj, HttpServletRequest request) throws Exception {

        if(obj.containsKey("roomId")){
            String roomId = obj.getString("roomId");
            return service.getRoomImg(roomId,request);
        }else {
            throw new ServiceException("参数：【roomId】必传！");
        }
    }

    @RequestMapping(value = "/closeRoom",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "关闭直播间")
    RetResult<String> closeRoom(@ApiParam(value = "直播间房间id【roomId】",required = true) @RequestBody JSONObject obj, HttpServletRequest request) throws Exception {

        if(obj.containsKey("roomId")){
            String roomId = obj.getString("roomId");
            return RetResponse.makeOKRsp(service.closeRoom(roomId));
        }else {
            throw new ServiceException("参数：【roomId】必传！");
        }
    }

}
