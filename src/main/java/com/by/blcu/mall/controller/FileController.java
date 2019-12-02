package com.by.blcu.mall.controller;

import com.alibaba.fastjson.JSONObject;
import com.by.blcu.core.aop.CheckToken;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.ServiceException;
import com.by.blcu.core.utils.*;
import com.by.blcu.mall.model.File;
import com.by.blcu.mall.service.FileService;
import com.by.blcu.mall.service.RedisService;
import com.by.blcu.mall.vo.SaveVideoPlayModelVo;
import com.by.blcu.mall.vo.VideoInfoVo;
import com.by.blcu.resource.dto.VideoInfo;
import com.by.blcu.resource.model.GetAuthorModel;
import com.by.blcu.resource.model.SaveVideoPlayModel;
import com.by.blcu.resource.service.IVideoInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: FileController类
 * @author 李程
 * @date 2019/07/30 13:20
 */
@RestController
@CheckToken
@Slf4j
@RequestMapping("/file")
public class FileController {

    @Resource
    private RedisService redisService;

    @Resource
    private IVideoInfoService videoInfoService;

    @Value("${open.authorizationUrl}")
    private String authorizationUrl;

    @Value("${open.videoOutTime}")
    private int videoOutTime;

    @Value("${open.getVideoUrl}")
    private String getVideoUrl;

    @Value("${open.appkey}")
    private String appkey;

    @Value("${open.appsecret}")
    private String appsecret;

    @Autowired
    private FastDFSClientWrapper dfsClient;

    @Resource
    private FileService fileService;

    @PostMapping("/insert")
    public RetResult<Integer> insert(File file) throws Exception{
        file.setFileId(ApplicationUtils.getUUID());
        Integer state = fileService.insert(file);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/deleteById")
    public RetResult<Integer> deleteById(@RequestParam String id) throws Exception {
        Integer state = fileService.deleteById(id);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/update")
    public RetResult<Integer> update(File file) throws Exception {
        Integer state = fileService.update(file);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/selectByFileId")
    public RetResult<File> selectByFileId(@RequestParam String fileId) throws Exception {
        File file = fileService.selectByFileId(fileId);
        return RetResponse.makeOKRsp(file);
    }

    @PostMapping("/selectById")
    public RetResult<File> selectById(@RequestParam String id) throws Exception {
        File file = fileService.selectById(id);
        return RetResponse.makeOKRsp(file);
    }

    @PostMapping("/upload")
    public RetResult<File> upload(HttpServletRequest httpServletRequest) throws Exception {
        Map<String,String> map = UploadActionUtil.uploadFile(httpServletRequest);
        File file = new File();
        file.setFileName(map.get("fileName"));
        file.setFilePath(map.get("filePath"));
        file.setFileSize(map.get("fileSize"));
        file.setFileId(ApplicationUtils.getUUID());
        fileService.insert(file);
        return RetResponse.makeOKRsp(file);
    }

    @PostMapping("/fdfs_upload")
    public RetResult<Object> fdfsUpload(@RequestParam("file") MultipartFile file,
                                        RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            return RetResponse.makeErrRsp("Please select a file to upload");
        }
        String fileUrl="";
        File fileSave = new File();
        try {
            fileUrl = dfsClient.uploadFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        fileSave.setFilePath(fileUrl);
        fileSave.setFileName(file.getOriginalFilename());
        fileSave.setFileSize(String.valueOf(file.getSize()));
        fileSave.setFileId(ApplicationUtils.getUUID());
        fileSave.setFileTime(new Date());
        fileSave.setIsdelete(1);
        fileSave.setIsvalidity(1);
        Integer state = fileService.insert(fileSave);
        if (state != 1){
            return RetResponse.makeErrRsp("请重新上传！");
        }
        return RetResponse.makeOKRsp(fileSave);
    }

    @PostMapping("/fdfs_uploads")
    public RetResult<List> fdfsUploads(@RequestParam("files") MultipartFile[] files,
                                        RedirectAttributes redirectAttributes, @RequestParam("commodityId") String commodityId) {
        if (files.length < 1) {
            return RetResponse.makeErrRsp("Please select a file to upload");
        }
        String fileUrl="";
        String fileName = "";
        File fileSave = new File();
        List fileList = new ArrayList();
        try {
            for(MultipartFile file : files){
                fileName = file.getOriginalFilename();
                fileUrl = dfsClient.uploadFile(file);
                if(StringUtils.isBlank(fileUrl)) {
                    throw new ServiceException("文件["+fileName+"]上传失败");
                }
                fileSave.setCommodityId(commodityId);
                fileSave.setFilePath(fileUrl);
                fileSave.setFileName(file.getOriginalFilename());
                fileSave.setFileSize(String.valueOf(file.getSize()));
                fileSave.setFileId(ApplicationUtils.getUUID());
                fileSave.setFileTime(new Date());
                fileSave.setIsdelete(1);
                fileSave.setIsvalidity(1);
                Integer state = fileService.insert(fileSave);
                if (state != 1){
                    dfsClient.deleteFile(fileUrl);
                    return RetResponse.makeErrRsp("请重新上传！");
                }
                fileList.add(fileUrl);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return RetResponse.makeOKRsp(fileList);
    }
    @RequestMapping(value = "/getAuthorization", method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "获取授权",notes = "获取授权",httpMethod = "GET")
    public RetResult<VideoInfoVo> getAuthorization(HttpServletRequest httpServletRequest, @RequestBody JSONObject obj)throws Exception{
        if(obj.containsKey("extendName") && !StringUtils.isBlank("extendName") && obj.containsKey("videoName") && !StringUtils.isBlank("videoName")){
            String extendName = obj.getString("extendName");
            String videoName=obj.getString("videoName");
            if(StringUtils.isEmpty(extendName))
                extendName=authorizationUrl+"?extendName=mp4&storeType=VE";
            else
                extendName=authorizationUrl+"?extendName="+extendName+"&storeType=VE";
            int userId = Integer.valueOf(httpServletRequest.getAttribute("userId").toString()).intValue();
            if(userId==-1){
                log.info("无效的用户，请注册或登录");
                return RetResponse.makeErrRsp("无效的用户，请注册或登录");
            }
            if(StringUtils.isEmpty(videoName)){
                log.info("视频名称不能为空");
                return RetResponse.makeErrRsp("视频名称不能为空");
            }
            VideoInfoVo videoInfoVo = fileService.getAuthorization(extendName, userId, videoName, appkey, appsecret);
            return  RetResponse.makeOKRsp(videoInfoVo);
        }else {
            return RetResponse.makeErrRsp("未传入视频");
        }
    }

    @RequestMapping(value = "/saveVideoPlayUrl", method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "视频上传成功回调",notes = "视频上传成功回调",httpMethod = "POST")
    public RetResult<VideoInfoVo> saveVideoPlayUrl(HttpServletRequest httpServletRequest,@Valid @RequestBody SaveVideoPlayModelVo saveVideoPlayModelVo)throws Exception{
        int userId = Integer.valueOf(httpServletRequest.getAttribute("userId").toString()).intValue();
        if(userId==-1){
            log.info("无效的用户，请注册或登录");
            return RetResponse.makeErrRsp("无效的用户，请注册或登录");
        }
        if(!StringUtils.isEmpty(saveVideoPlayModelVo.getVideoInfoId())
                &&!StringUtils.isEmpty(saveVideoPlayModelVo.getVideoInfoName())){
            if(StringUtils.isEmpty(saveVideoPlayModelVo.getSize()))
                saveVideoPlayModelVo.setSize("");
            VideoInfoVo videoInfoVo = fileService.saveVideoPlayUrl(saveVideoPlayModelVo.getVideoInfoId(), saveVideoPlayModelVo.getVideoInfoName(), userId, saveVideoPlayModelVo.getSize(), saveVideoPlayModelVo.getDuration());
            if (null != videoInfoVo) {
                String mapKey = videoInfoVo.getFileId();
                String mapValue = videoInfoVo.getUrl();
                redisService.setWithExpire(mapKey, mapValue, videoOutTime * 60);
            }
            return RetResponse.makeOKRsp(videoInfoVo);
        }else {
            return RetResponse.makeErrRsp("请传入视频ID和视频名");
        }
    }

    @RequestMapping(value = "/getPlayUrl", method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "根据视频id获取视频播放地址",notes = "根据视频id获取视频播放地址",httpMethod = "POST")
    public RetResult getPlayUrl(@RequestBody JSONObject obj)throws Exception{
        if(!obj.containsKey("videoInfoId")|| obj.getInteger("videoInfoId")==null){
            log.info("videoInfoId 错误");
            return RetResponse.makeErrRsp("未找到该视频");
        }
        VideoInfo videoInfo = videoInfoService.selectByPrimaryKey(obj.getInteger("videoInfoId"));
        VideoInfoVo videoInfoVo = MapAndObjectUtils.ObjectClone(videoInfo, VideoInfoVo.class);
        if(null==videoInfoVo || StringUtils.isEmpty(videoInfoVo.getFileId())){
            log.info("请求数据错误:"+obj.getInteger("videoInfoId"));
        }
        String fileId = videoInfoVo.getFileId();
        String url=null;
        if(!redisService.hasKey(fileId)){
            String resUrl=getVideoUrl.replaceAll("\\{fileId\\}",fileId);
            log.info("获取录播视频url:"+resUrl);
            HttpResponse httpResponse = HttpReqUtil.getOpentReq(resUrl + "?orderType=ASC&delayMinute="+videoOutTime,appkey,appsecret);
            Map<String, Object> responsemap = HttpReqUtil.parseHttpResponse(httpResponse);
            Map<String, Object> payload = (Map<String, Object>)responsemap.get("payload");
            url = payload.get("url").toString();
            log.info("获取到的播放地址:"+url);
            videoInfoVo.setUrl(url);
            redisService.setWithExpire(fileId,url,videoOutTime*60);
            videoInfoService.updateByPrimaryKeySelective(videoInfoVo);
        }else {
            url= redisService.get(fileId);
        }
        return RetResponse.makeOKRsp(MapUtils.initMap("url",url));
    }
}