package com.by.blcu.resource.controller;

import com.alibaba.fastjson.JSONObject;
import com.by.blcu.core.aop.CheckToken;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.ServiceException;
import com.by.blcu.core.utils.*;
import com.by.blcu.course.service.ICourseDetailService;
import com.by.blcu.course.service.ICourseService;
import com.by.blcu.course.service.courseCheck.CourseCheckModel;
import com.by.blcu.mall.model.File;
import com.by.blcu.mall.model.ResourceFile;
import com.by.blcu.mall.service.FileService;
import com.by.blcu.resource.dto.Resources;
import com.by.blcu.resource.model.FileViewModel;
import com.by.blcu.resource.model.ResourceTypeEnum;
import com.by.blcu.resource.service.IResourcesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@RestController
@CheckToken
@RequestMapping("/coreFile")
@Slf4j
@Api(tags = "教师课程资源文件管理API",description = "包含接口：\n"
            +"1、查询课程资料列表\n"
            +"2、上传课程资料（包含目录资料）\n"
            +"3、上传目录图片（富文本）\n"
            +"4、更新资料名称\n"
            +"5、删除课程资料\n"
            +"6、批量删除课程资料\n"
            +"7、存储更新富文本资源\n"
            +"8、获取富文本资源\n"
            +"9、获取目录下文档\n"
            +"10、文档正确命名下载\n"

            +"11、文件上传\n"
            +"12、根据url删除文件\n"
            +"13、根据fileId查询实体")
public class ResourceFileController {

    @Autowired
    private FastDFSClientWrapper dfsClient;
    @Resource
    private FileService fileService;
    @Resource
    private IResourcesService resourcesService;
    @Resource
    private ICourseDetailService courseDetailService;
    @Resource
    private ICourseService courseService;

    // region 教师端课程资源接口

    @PostMapping("/list")
    @ApiOperation(httpMethod = "POST", value = "查询课程资料列表")
    RetResult<List<ResourceFile>> queryFileWithPage(@RequestBody FileViewModel fileViewModel) throws Exception{

        if(StringUtils.isBlank(fileViewModel.getCourseId()))
            throw new ServiceException("课程ID不能为空");
        if(StringUtils.isEmpty(fileViewModel.getModelType()))
            throw new ServiceException("模块ID不能为空");

        long count = 0;
        List<ResourceFile> files = null;
        try {
            Map<String, Object> param = MapAndObjectUtils.ObjectToMap2(fileViewModel);
            CommonUtils.queryParamOpt(param);
            param.put("commodityId", fileViewModel.getCourseId());
            count = fileService.selectCount(param);
            if(count < 1){
                return RetResponse.makeRsp(null,count);
            }
            files = fileService.selectList(param);
        } catch (Exception e) {
            log.error("查询资料列表失败", e);
            throw new ServiceException(e.getMessage());
        }
        return RetResponse.makeRsp(files,count);
    }

    @PostMapping("/upload")
    @RequiresPermissions("teacher")
    @ApiOperation(httpMethod = "POST", value="上传课程资料（包含目录文档）")
    RetResult uploadResourcesFile (@ApiParam(value = "资源文件",required = true) @RequestParam("file") MultipartFile multiFile,
                                   @ApiParam(value = "课程ID",required = true) @RequestParam String courseId,
                                   @ApiParam(value = "资源文件名称")@RequestParam(required = false) String fileName,
                                   @ApiParam(value = "模块ID",required = true)@RequestParam String modelType,
                                   @ApiParam(value = "目录ID 默认为0")@RequestParam(required = false) String catalogId,
                                   HttpServletRequest request) throws Exception {
        String userId = request.getAttribute("userId").toString();

        //0.文件大小限制，文件类型限制，文件数量限制
        if(!StringUtils.isBlank(fileName) && fileName.length() > 120){
            throw new ServiceException("资源名称超长！最长：120字");
        }
        //1.获取上传文件信息
        if(multiFile.isEmpty())
            throw new ServiceException("请选择一个文件！");

        //2.验证是否存在重名资源
        if(!StringUtils.isBlank(fileName)){
            Map<String, Object> initMap = MapUtils.initMap("title", fileName);
            initMap.put("type", ResourceTypeEnum.DATA.getTypeCode());
            initMap.put("create_user", userId);
            List resourcesList = resourcesService.selectList(initMap);
            if(resourcesList != null && resourcesList.size() > 0){
                throw new ServiceException("已存在同名资源！");
            }
        }

        Map map = new HashMap();
        FileViewModel fileViewModel = new FileViewModel();
        if(!StringUtils.isBlank(fileName)) fileViewModel.setFileName(fileName);
        fileViewModel.setCourseId(courseId);
        fileViewModel.setModelType(modelType);
        if(!StringUtils.isEmpty(catalogId))
            fileViewModel.setCatalogId(catalogId);
        Integer state = -1;
        String fileRealName = "";
        try {
            fileRealName = multiFile.getOriginalFilename();  //是否需要判断查询该文件是否重复上传

            String fileUrl = dfsClient.uploadFile(multiFile);
            if(StringUtils.isBlank(fileUrl))
                throw new ServiceException("文件["+fileRealName+"]上传失败");

            //3.创建资源文件
            File resourceFile = new File();
            resourceFile.setFileId(ApplicationUtils.getUUID());
            resourceFile.setCommodityId(fileViewModel.getCourseId());//将课程ID传入
            resourceFile.setFileName(fileRealName);
            resourceFile.setFileType(SystemUtils.getFileType(fileRealName));//文件分类
            resourceFile.setFilePath(fileUrl);
            resourceFile.setFileSize(String.valueOf(multiFile.getSize()));
            resourceFile.setIsdelete(1);  //是否删除（待定）
            resourceFile.setIsvalidity(1);//是否有效（待定）
            resourceFile.setFileTime(new Date());
            CourseCheckModel courseCheckModel = new CourseCheckModel();
            state = fileService.insertResourceFile(resourceFile, fileViewModel, request,courseCheckModel);
            if(state != 1) {
                throw new ServiceException("上传失败，请重新上传");
            }

            //4.资源变更 初始化课程审核状态
            if(0==courseCheckModel.getIsUpper())
                courseService.changeCourseStatus(Integer.valueOf(courseId), Integer.valueOf(userId));
            map.put("resourcesId", resourceFile.getBak2());
            map.put("content", resourceFile.getFileId());
        } catch (Exception e) {
            log.error("资料文件上传失败！",e);
            throw new ServiceException(e.getMessage());
        }

        return RetResponse.makeOKRsp(map);
    }

    @PostMapping("/uploadOnlyPic")
    @RequiresPermissions("teacher")
    @ApiOperation(httpMethod = "POST", value="上传目录图片（富文本）")
    RetResult uploadPicFile (@ApiParam(value = "资料文件",required = true) @RequestParam("file") MultipartFile[] multiFiles,
                                   @ApiParam(value = "课程ID",required = true) @RequestParam String courseId,
                                   @ApiParam(value = "目录ID",required = true) @RequestParam String catalogId,
                                   HttpServletRequest request) throws Exception {

        //0.文件大小限制，文件类型限制，文件数量限制

        //1.获取上传文件信息
        if(multiFiles.length < 1)
            throw new ServiceException("请选择一个文件！");
        if(StringUtils.isBlank(courseId))
            throw new ServiceException("课程ID不能为空");

        String fileName = "";
        String fileUrl = "";
        List fileList = new ArrayList();
        try {
            for(MultipartFile file : multiFiles){
                fileName = file.getOriginalFilename();  //是否需要判断查询该文件是否重复上传

                fileUrl = dfsClient.uploadFile(file);
                if(StringUtils.isBlank(fileUrl)) {
                    throw new ServiceException("文件["+fileName+"]上传失败");
                }
                fileList.add(fileUrl);

                //2.创建资源文件
                File resourceFile = new File();
                resourceFile.setFileId(ApplicationUtils.getUUID());
                resourceFile.setCommodityId(courseId);//将课程ID传入
                if(!StringUtils.isBlank(catalogId)) resourceFile.setBak3(catalogId);//将目录ID传入
                resourceFile.setFileName(fileName);
                resourceFile.setFileType(SystemUtils.getFileType(fileName));//文件分类
                resourceFile.setFilePath(fileUrl);
                resourceFile.setFileSize(String.valueOf(file.getSize()));
                resourceFile.setIsdelete(1);  //是否删除（待定）
                resourceFile.setIsvalidity(1);//是否有效（待定）
                resourceFile.setFileTime(new Date());
                Integer state = fileService.insert(resourceFile);
                if(state != 1) {
                    dfsClient.deleteFile(fileUrl);
                    throw new ServiceException("目录资料文件上传失败，请重新上传");
                }
            }
        } catch (Exception e) {
            log.error("目录资料文件上传失败！",e);
            throw new ServiceException(e.getMessage());
        }

        return RetResponse.makeOKRsp(fileList);
    }

    @PostMapping(value = "/update")
    @RequiresPermissions("teacher")
    @ApiOperation(value = "更新课程资料名称")
    public RetResult<Integer> updateResource(@Valid @RequestBody FileViewModel fileViewModel, HttpServletRequest request) throws Exception{
        String userId = request.getAttribute("userId").toString();

        if(StringUtils.isEmpty(fileViewModel.getResourcesId()))
            throw new ServiceException("资源ID不能为空");
        if(StringUtils.isEmpty(fileViewModel.getFileName()))
            throw new ServiceException("资料名称不能为空");
        if(fileViewModel.getFileName().length() > 120)
            throw new ServiceException("资源名称超长！最长：120字");

        //验证是否存在重名资源
        if(!StringUtils.isBlank(fileViewModel.getFileName())){
            Map<String, Object> initMap = MapUtils.initMap("title", fileViewModel.getFileName());
            initMap.put("type", ResourceTypeEnum.DATA.getTypeCode());
            initMap.put("create_user", userId);
            List resourcesList = resourcesService.selectList(initMap);
            if(resourcesList != null && resourcesList.size() > 0){
                throw new ServiceException("已存在同名资源！");
            }
        }

        Integer state = -1;
        try {
            Resources resource = resourcesService.selectByPrimaryKey(Integer.valueOf(fileViewModel.getResourcesId()));
            if(resource == null)
                throw new ServiceException("不存在该资源");
            resource.setTitle(fileViewModel.getFileName());
            resource.setUpdateUser(Integer.valueOf(userId));
            resource.setUpdateTime(new Date());
            state = resourcesService.updateByPrimaryKeySelective(resource);
        } catch (Exception e) {
            log.error("更新资源失败", e);
            throw new ServiceException(e.getMessage());
        }
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping(value = "/delete")
    @RequiresPermissions("teacher")
    @ApiOperation(value = "删除课程资料")
    public RetResult deleteResourceFile(@RequestBody FileViewModel fileViewModel, HttpServletRequest request) throws Exception{
        Integer userId = Integer.valueOf(request.getAttribute("userId").toString());

        if(StringUtils.isEmpty(fileViewModel.getResourcesId()))
            throw new ServiceException("资源ID不能为空");
        if(StringUtils.isEmpty(fileViewModel.getFileId()))
            throw new ServiceException("文件ID不能为空");

        File file = fileService.selectById(fileViewModel.getFileId());
        if(file == null)
            throw new ServiceException("该文件不存在");

        Integer state = -1;
        try {
            //1.删除文件
            dfsClient.deleteFile(file.getFilePath());
        } catch (Exception e) {
            log.error("课程资料文件删除失败！文件名："+file.getFileName()+"，文件地址："+file.getFilePath(),e);
        }finally {
            fileService.deleteById(file.getFileId());
            //2.删除资源表
            resourcesService.deleteByPrimaryKey(Integer.valueOf(fileViewModel.getResourcesId()));
            //3.删除课程详情表
            Map param = MapUtils.initMap("resourcesId",fileViewModel.getResourcesId());
            state = courseDetailService.deleteByParams(param);
        }
        //4.删除资源文件 更新课程审核状态
        if(state > 0 && !StringUtils.isBlank(fileViewModel.getCourseId())){
            courseService.changeCourseStatus(Integer.valueOf(fileViewModel.getCourseId()), userId);
        }
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping(value = "/batchDelete")
    @RequiresPermissions("teacher")
    @ApiOperation(value = "批量删除课程资料")
    public RetResult batchDeleteResourceFile(@RequestBody FileViewModel fileViewModel, HttpServletRequest request) throws Exception{
        Integer userId = Integer.valueOf(request.getAttribute("userId").toString());

        //ids 通过 , 拼接
        if(StringUtils.isEmpty(fileViewModel.getResourcesId()))
            throw new ServiceException("资源ID不能为空");
        if(StringUtils.isEmpty(fileViewModel.getFileId()))
            throw new ServiceException("文件ID不能为空");

        Integer state = -1;
        try {
            //1.删除服务器文件
            String[] ids = fileViewModel.getFileId().split(",");
            for(String fileId : ids){
                if(!StringUtils.isBlank(fileId)){
                    File file = fileService.selectById(fileId);
                    dfsClient.deleteFile(file.getFilePath());
                    fileService.deleteById(fileId);
                }
            }

            //2.删除资源表
            String[] rids = fileViewModel.getResourcesId().split(",");
            for(String rid : rids){
                if(!StringUtils.isBlank(rid)){
                    resourcesService.deleteByPrimaryKey(Integer.valueOf(rid));
                    //3.删除课程详情表
                    Map param = MapUtils.initMap("resourcesId", rid);
                    state = courseDetailService.deleteByParams(param);
                }
            }

        } catch (Exception e) {
            log.error("课程资料删除失败！",e);
            throw new ServiceException(e.getMessage());
        }

        //4.删除资源文件 更新课程审核状态
        if(state > 0 && !StringUtils.isBlank(fileViewModel.getCourseId())){
            courseService.changeCourseStatus(Integer.valueOf(fileViewModel.getCourseId()), userId);
        }
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/saveRichText")
    @RequiresPermissions("teacher")
    @ApiOperation(httpMethod = "POST", value="存储更新富文本资源")
    public RetResult saveRichText(@RequestBody FileViewModel fileViewModel, HttpServletRequest request) throws Exception{

        //1.富文本传什么  - courseId，catalogId(bak3存放)，richText，resourceId(bak2存放)，modelType（不传就默认）
        if(StringUtils.isBlank(fileViewModel.getCourseId()))
            throw new ServiceException("课程ID不能为空");
        if(StringUtils.isBlank(fileViewModel.getCatalogId()))
            throw new ServiceException("目录ID不能为空");

        try {
            Integer resourcesId = resourcesService.saveRichText(fileViewModel, request,new CourseCheckModel());
            Map param = MapUtils.initMap("resourcesId",resourcesId);
            param.put("content", fileViewModel.getRichText());
            return RetResponse.makeOKRsp(param);
        } catch (Exception e) {
            log.error("富文本资料新增失败！",e);
            throw new ServiceException(e.getMessage());
        }
    }

    @PostMapping("/getRichText")
    @RequiresPermissions("teacher")
    @ApiOperation(httpMethod = "POST", value = "获取富文本资源")
    public RetResult getRichText(@RequestBody FileViewModel fileViewModel){
        if(StringUtils.isBlank(fileViewModel.getResourcesId())){
            throw new ServiceException("资源ID不能为空");
        }
        Resources resources = null;
        Map param = MapUtils.initMap("resourcesId",fileViewModel.getResourcesId());
        List<Resources> resourcesList = resourcesService.selectList(param);
        if(resourcesList != null && resourcesList.size() > 0){
            resources = resourcesList.get(0);
        }else{
            throw new ServiceException("该资源不存在，资源ID：" + fileViewModel.getResourcesId());
        }

        return RetResponse.makeOKRsp(resources);
    }

    @PostMapping("/get")
    @RequiresPermissions("teacher")
    @ApiOperation(httpMethod = "POST", value = "获取目录下文档")
    public RetResult getDocByResourcesId(@RequestBody FileViewModel fileViewModel) throws Exception{
        if(StringUtils.isBlank(fileViewModel.getResourcesId())){
            throw new ServiceException("资源ID不能为空");
        }
        Resources resources = null;
        File file = null;
        Map param = MapUtils.initMap("resourcesId",fileViewModel.getResourcesId());
        List<Resources> resourcesList = resourcesService.selectList(param);
        if(resourcesList != null && resourcesList.size() > 0){
            resources = resourcesList.get(0);
            file = fileService.selectByFileId(resources.getContent());
            if(file == null){
                throw new ServiceException("该文档不存在，文档ID："+resources.getContent());
            }
        }else{
            throw new ServiceException("该资源不存在，资源ID：" + fileViewModel.getResourcesId());
        }

        return RetResponse.makeOKRsp(file);
    }

    @PostMapping("/download")
    @ApiOperation(httpMethod = "POST", value = "下载正确名称资源")
    public void downloadResources(@RequestBody JSONObject obj, HttpServletRequest request, HttpServletResponse response) throws Exception{
        if(!obj.containsKey("fileId")){
            throw new ServiceException("文件ID不能为空！");
        }
        String fileId = obj.getString("fileId");
        File file = fileService.selectById(fileId);
        if(file == null)
            throw new ServiceException("该文件不存在！文件ID：" + fileId);

        String fileName = file.getFileName();
        if(obj.containsKey("title")){
            if(fileName.contains(".")){
                String[] fileArr = fileName.split("\\.");
                fileName = obj.getString("title") +"."+ fileArr[1];
            }else{
                fileName = obj.getString("title");
            }
        }

        dfsClient.webDownFile(request, response, file.getFilePath(), fileName);
    }

    //endregion


    // region 原文件接口

    //@PostMapping(value = "/deleteByUrl",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/deleteByUrl", method = {RequestMethod.DELETE, RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "根据url删除文件")
    public RetResult<Integer> deleteByUrl(@RequestBody JSONObject object) throws Exception {
        File file = null;
        String url = object.getString("url");
        if(StringUtils.isBlank(url)){
            throw new ServiceException("传入url不能为空");
        }
        try {
            dfsClient.deleteFile(url);
            file = new File();
            file.setFilePath(url);
            file = fileService.selectOne(file);
            if(file == null){
                throw new ServiceException("未查询到该文件");
            }
            file.setIsdelete(0);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
        return RetResponse.makeOKRsp(fileService.update(file));
    }

    @GetMapping(value = "/selectByFileId",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "根据fileId查询实体")
    public RetResult<File> selectByFileId(@ApiParam(value = "文件id",required = true) @RequestParam String id) throws Exception {
        File file = fileService.selectById(id);
        return RetResponse.makeOKRsp(file);
    }

    @PostMapping(value = "/uploadFile",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "文件上传")
    public RetResult<File> fdfsUpload(@ApiParam(value = "file文件",required = true) @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return RetResponse.makeErrRsp("请选择一个文件！");
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
        fileSave.setFileType(SystemUtils.getFileType(fileSave.getFileName()));
        Integer state = fileService.insert(fileSave);
        if (state != 1){
            return RetResponse.makeErrRsp("请重新上传！");
        }
        return RetResponse.makeOKRsp(fileSave);
    }

    //endregion

}
