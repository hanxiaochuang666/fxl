package com.by.blcu.course.service.impl;

import com.by.blcu.core.aop.CheckToken;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.ServiceException;
import com.by.blcu.core.utils.*;
import com.by.blcu.course.dao.IAutomaticCheckDao;
import com.by.blcu.course.dao.ICatalogDao;
import com.by.blcu.course.dao.ICourseDetailDao;
import com.by.blcu.course.dto.AutomaticCheck;
import com.by.blcu.course.dto.Catalog;
import com.by.blcu.course.dto.CourseDetail;
import com.by.blcu.course.model.CatalogAndResourceModel;
import com.by.blcu.course.model.PreLiveInfoModel;
import com.by.blcu.course.model.ResourceModel;
import com.by.blcu.course.model.StudentCourseModel;
import com.by.blcu.course.service.IStudentCourseService;
import com.by.blcu.mall.service.MallOrderInfoService;
import com.by.blcu.mall.vo.CommodityInfoFileVo;
import com.by.blcu.mall.vo.MallCommodityOrderVo;
import com.by.blcu.mall.vo.MallOrderInfoVo;
import com.by.blcu.resource.dao.ILearnActiveDao;
import com.by.blcu.resource.dao.ILiveTelecastDao;
import com.by.blcu.resource.dao.IResourcesDao;
import com.by.blcu.resource.dto.LearnActive;
import com.by.blcu.resource.dto.LiveTelecast;
import com.by.blcu.resource.dto.Resources;
import com.by.blcu.resource.model.LiveDetailInfo;
import com.by.blcu.resource.model.ResourceTypeEnum;
import com.by.blcu.resource.model.ResourcesViewModel;
import com.by.blcu.resource.service.ILiveService;
import com.by.blcu.resource.service.IResourcesService;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

@Service
@Slf4j
@CheckToken
public class StudentCourseServiceImpl implements IStudentCourseService {

    @Value("${openOffice.port}")
    private Integer port;
    @Value("${openOffice.host}")
    private String host;
    @Value("${openOffice.pdfFilePath}")
    private String pdfFilePath;

    @Autowired
    private ICatalogDao catalogDao;

    @Autowired
    private ILearnActiveDao learnActiveDao;

    @Autowired
    private IAutomaticCheckDao checkDao;

    @Autowired
    private ILiveTelecastDao liveTelecastDao;

    @Autowired
    private IResourcesDao resourcesDao;

    @Autowired
    private ICourseDetailDao detailDao;

    @Autowired
    private MallOrderInfoService orderInfoService;

    @Resource
    private IResourcesService resourcesService;

    @Autowired
    private FastFileStorageClient storageClient;

    @Autowired
    private ILiveService liveService;

    @Override
    public RetResult selectStudentCourse(Integer type, HttpServletRequest request,Integer size,Integer index) throws ServiceException {

        Integer pageSize = StringUtils.isEmpty(size)?10:size;
        Integer pageIndex = StringUtils.isEmpty(index)?1:index;
        String userName = StringUtils.obj2Str(request.getAttribute("username"));
        List<MallOrderInfoVo> orderInfoVos = orderInfoService.selectMallOrderInfoVoByUserName(userName);

        // 创建日期修改一下改为订单的创建日期
        orderInfoVos.forEach(o->{
            Date orderDate = o.getCreateTime();
            List<MallCommodityOrderVo> list = o.getMallCommodityOrderVoList();
            if(!CommonUtils.listIsEmptyOrNull(list)){
                list.forEach(m-> m.getCommodityInfoFileVo().setCreateTime(orderDate));
            }
        });

        List<MallCommodityOrderVo> mallCommodityOrderVos = new ArrayList<>();
        orderInfoVos.forEach(o-> mallCommodityOrderVos.addAll(o.getMallCommodityOrderVoList()));
        if(CommonUtils.listIsEmptyOrNull(mallCommodityOrderVos)){
            throw new ServiceException("根据用户【"+userName+"】未查询到购买的商品信息！请联系管理员！");
        }
        mallCommodityOrderVos.sort(Comparator.comparing(mallCommodityOrderVo -> mallCommodityOrderVo.getCommodityInfoFileVo().getCreateTime()));

        // 在学的
        List<StudentCourseModel> courseModelsY = new ArrayList<>();
        // 过期的
        List<StudentCourseModel> courseModelsN = new ArrayList<>();

        for (MallCommodityOrderVo vo : mallCommodityOrderVos) {
            setCourseModel(courseModelsY, courseModelsN, vo);
        }
        // 分页
        if(1 == type){// 在学
            SubListUtil<StudentCourseModel> sListUtil = new SubListUtil<>(pageIndex, pageSize, courseModelsY);
            return RetResponse.makeRsp(sListUtil,sListUtil.getTotal());
        }else{// 过期
            SubListUtil<StudentCourseModel> sListUtil = new SubListUtil<>(pageIndex, pageSize, courseModelsN);
            return RetResponse.makeRsp(sListUtil,sListUtil.getTotal());
        }
    }

    private void setCourseModel(List<StudentCourseModel> courseModelsY, List<StudentCourseModel> courseModelsN, MallCommodityOrderVo vo) {

        Integer isPackage = vo.getCommodityInfoFileVo().getCourseType();
        StudentCourseModel studentCourseModel = new StudentCourseModel();

        studentCourseModel.setCcName(vo.getCommodityInfoFileVo().getCourseName());//显示课程名称
        studentCourseModel.setCommodityId(vo.getCommodityId());
        studentCourseModel.setCreateTime(vo.getCommodityInfoFileVo().getCreateTime());
        studentCourseModel.setLessonType(vo.getCommodityInfoFileVo().getLessonType());
        studentCourseModel.setPicturePath(vo.getCommodityInfoFileVo().getPicturePathView());
        studentCourseModel.setIsPackage(isPackage);
        studentCourseModel.setCommodityLecturerList(vo.getCommodityInfoFileVo().getCommodityLecturerList());

        // 计算有效期，每月按照30天算
        Integer validity = vo.getCommodityInfoFileVo().getValidity();
        Date createDate = vo.getCommodityInfoFileVo().getCreateTime();
        Date endDate = null;
        if (StringUtils.isEmpty(validity)) {
            endDate = DateUtils.change(createDate, 5, 0);
        } else if (validity >= 0) {
            endDate = DateUtils.change(createDate, 5, 30 * validity);
        }
        if (1 == isPackage) {// 套餐
            List<StudentCourseModel> studentCourseModels = getChileMallOrderInfo(vo);
            studentCourseModel.setChildList(studentCourseModels);
            // 套餐的话，学习进度把各个课程都加起来算
            setStudySchedule(studentCourseModel, vo);

        } else {
            studentCourseModel.setCourseId(vo.getCommodityInfoFileVo().getCourseId());
            if (StringUtils.isBlank(vo.getCommodityInfoFileVo().getCourseId())) {
                throw new ServiceException("当前购买商品：" + vo.getCommodityInfoFileVo().getCourseName() + ",尚未绑定课程！");
            }
            Map<String, Object> map = selectStudySchedule(Integer.valueOf(vo.getCommodityInfoFileVo().getCourseId()));
            if (map.isEmpty())
                return;
            if (StringUtils.isEmpty(map.get("learnCount")) || StringUtils.isEmpty(map.get("courseCount"))) {
                throw new ServiceException("商品：" + vo.getCommodityInfoFileVo().getCourseName() + ",关联课程信息有误！");
            }
            Integer havaStudyClass = Integer.valueOf(StringUtils.obj2Str(map.get("learnCount")));
            Integer totalClass = Integer.valueOf(StringUtils.obj2Str(map.get("courseCount")));
            studentCourseModel.setHavaStudyClass("" + havaStudyClass);
            studentCourseModel.setTotalClass("" + totalClass);
            if (StringUtils.isEmpty(havaStudyClass) || 0 == havaStudyClass) {
                studentCourseModel.setStudySchedule("0%");
            } else {
                NumberFormat numberFormat = NumberFormat.getInstance();
                // 设置精确到小数点后2位
                numberFormat.setMaximumFractionDigits(2);
                String result = numberFormat.format((float) havaStudyClass / (float) totalClass * 100);
                studentCourseModel.setStudySchedule(result + "%");
            }

        }
        if (-1 == validity) {// 永久有效
            studentCourseModel.setValidity("永久有效");
            courseModelsY.add(studentCourseModel);
        } else {
            if (DateUtils.dateCompare(endDate, DateUtils.now()) < 0) {
                // 表示过期了
                courseModelsN.add(studentCourseModel);
            } else {
                // 表示有效
                String differTime = "";
                try {
                    differTime = DateUtils.differTime(DateUtils.toStringYmdHmsWthH(DateUtils.now()), DateUtils.toStringYmdHmsWthH(endDate), "yyyy-MM-dd HH:mm:ss");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                studentCourseModel.setValidity(differTime);
                courseModelsY.add(studentCourseModel);
            }
        }
    }

    private void setStudySchedule(StudentCourseModel studentCourseModel, MallCommodityOrderVo vo) {

        List<CommodityInfoFileVo> fileVoList = vo.getCommodityInfoFileVo().getCommodityInfoFileVoList();
        if (!CommonUtils.listIsEmptyOrNull(fileVoList)) {
            Integer havaStudyClass = 0;
            Integer totalClass = 0;
            for (CommodityInfoFileVo infoFileVo : fileVoList) {
                if (StringUtils.isBlank(infoFileVo.getCourseId())) {
                    throw new ServiceException("当前购买商品：" + infoFileVo.getCourseName() + ",尚未绑定课程！");
                }
                Map<String, Object> map = selectStudySchedule(Integer.valueOf(infoFileVo.getCourseId()));
                if (map.isEmpty())
                    continue;
                if (StringUtils.isEmpty(map.get("learnCount")) || StringUtils.isEmpty(map.get("courseCount"))) {
                    throw new ServiceException("商品：" + infoFileVo.getCourseName() + ",关联课程信息有误！");
                }
                havaStudyClass += Integer.valueOf(StringUtils.obj2Str(map.get("learnCount")));
                totalClass += Integer.valueOf(StringUtils.obj2Str(map.get("courseCount")));

            }
            studentCourseModel.setHavaStudyClass("" + havaStudyClass);
            studentCourseModel.setTotalClass("" + totalClass);
            if (StringUtils.isEmpty(havaStudyClass) || 0 == havaStudyClass) {
                studentCourseModel.setStudySchedule("0%");
            } else {
                NumberFormat numberFormat = NumberFormat.getInstance();
                // 设置精确到小数点后2位
                numberFormat.setMaximumFractionDigits(2);
                String result = numberFormat.format((float) havaStudyClass / (float) totalClass * 100);
                studentCourseModel.setStudySchedule(result + "%");
            }
        }
    }

    // 获取套餐中的子商品列表
    private List<StudentCourseModel> getChileMallOrderInfo(MallCommodityOrderVo vo) {

        List<StudentCourseModel> models = new ArrayList<>();
        if (!CommonUtils.listIsEmptyOrNull(vo.getCommodityInfoFileVo().getCommodityInfoFileVoList())) {
            List<CommodityInfoFileVo> commodityInfoFileVoList = vo.getCommodityInfoFileVo().getCommodityInfoFileVoList();
            for (CommodityInfoFileVo fileVo : commodityInfoFileVoList) {
                StudentCourseModel studentCourseModel = new StudentCourseModel();
                studentCourseModel.setCourseId(fileVo.getCourseId());
                studentCourseModel.setCcName(fileVo.getCourseName());//显示课程名称
                studentCourseModel.setCommodityId(vo.getCommodityId());
                studentCourseModel.setCommodityLecturerList(fileVo.getCommodityLecturerList());
                studentCourseModel.setCreateTime(fileVo.getCreateTime());
                studentCourseModel.setLessonType(fileVo.getLessonType());
                studentCourseModel.setPicturePath(fileVo.getPicturePathView());
                studentCourseModel.setIsPackage(fileVo.getCourseType());
                if (StringUtils.isBlank(fileVo.getCourseId())) {
                    throw new ServiceException("当前购买商品：" + fileVo.getCourseName() + ",尚未绑定课程！");
                }
                Map<String, Object> map = selectStudySchedule(Integer.valueOf(fileVo.getCourseId()));
                if (!map.isEmpty()) {
                    if (StringUtils.isEmpty(map.get("learnCount")) || StringUtils.isEmpty(map.get("courseCount"))) {
                        throw new ServiceException("商品：" + fileVo.getCourseName() + ",关联课程信息有误！");
                    }
                    Integer havaStudyClass = Integer.valueOf(StringUtils.obj2Str(map.get("learnCount")));
                    Integer totalClass = Integer.valueOf(StringUtils.obj2Str(map.get("courseCount")));
                    studentCourseModel.setHavaStudyClass("" + havaStudyClass);
                    studentCourseModel.setTotalClass("" + totalClass);
                    if (StringUtils.isEmpty(havaStudyClass) || 0 == havaStudyClass) {
                        studentCourseModel.setStudySchedule("0%");
                    } else {
                        NumberFormat numberFormat = NumberFormat.getInstance();
                        // 设置精确到小数点后2位
                        numberFormat.setMaximumFractionDigits(2);
                        String result = numberFormat.format((float) havaStudyClass / (float) totalClass * 100);
                        studentCourseModel.setStudySchedule(result + "%");
                    }
                }
                models.add(studentCourseModel);
            }
        }
        return models;
    }

    @Override
    public Map<String, Object> selectStudySchedule(Integer courseId) throws ServiceException {

        Map<String,Object> retMap = new HashMap<>();
        // 先查课程目录的所有课时
        Map<String,Object> pMap = new HashMap<>();
        pMap.put("courseId",courseId);
        List<Catalog> catalogs = catalogDao.selectChildNode(pMap);
        if(!CommonUtils.listIsEmptyOrNull(catalogs)){
            Integer courseCount = catalogs.size();
            retMap.put("courseCount",courseCount);
            // 再查所有已学习的课时
            pMap.put("learnFlag","1");
            Long learnCount = learnActiveDao.selectCount(pMap);
            retMap.put("learnCount",learnCount);
        }else{
            log.error("根据课程id【"+courseId+"】未查询到课时");
            //throw new ServiceException("根据课程id【"+courseId+"】未查询到课时");
            Integer courseCount = catalogs.size();
            retMap.put("courseCount",courseCount);
            retMap.put("learnCount","0");
        }
        return retMap;
    }

    @Override
    public List<CatalogAndResourceModel> selectCourseCatalog(Integer status,Integer courseId,String userType,HttpServletRequest request) throws ServiceException {

        int studentId = Integer.valueOf(request.getAttribute("userId").toString());
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..查询课程目录中的用户id【{}】",studentId);
        Map<String, Object> map = new HashMap<>();
        map.put("courseId", courseId);
        List<CatalogAndResourceModel> nodes = new ArrayList<>();
        Map<String, Object> paraMap = new HashMap<>();
        paraMap.put("_sort_line", "sort");
        paraMap.put("parentId", "0");
        paraMap.put("_order_", "ASC");
        paraMap.put("courseId", courseId);
        paraMap.put("status", status);
        nodes = getChildNodes(paraMap,userType,studentId);
        return nodes;

    }

    private List<CatalogAndResourceModel> getChildNodes(Map<String, Object> paraMap,String userType,int studentId) {

        List<CatalogAndResourceModel> nodes = new ArrayList<>();
        List<Catalog> lists = new ArrayList<>();
        lists = catalogDao.selectList(paraMap);
        if (!CommonUtils.listIsEmptyOrNull(lists)) {
            lists.forEach(catalog -> {
                CatalogAndResourceModel nodeTmp = new CatalogAndResourceModel();
                //添加标记位 是否为已审核过目录
                nodeTmp.setIsOriginal(false);
                if(!StringUtils.isEmpty(catalog.getCheckTime())){
                    nodeTmp.setIsOriginal(true);
                }
                nodeTmp.setId(catalog.getCatalogId());
                nodeTmp.setName(catalog.getName());
                nodeTmp.setSort(catalog.getSort());
                nodeTmp.setCatalogStatus(catalog.getStatus());
                if("M".equals(userType.toUpperCase())){
                    Integer checkStatus = catalog.getCheckStatus();
                    nodeTmp.setCheckStatus(String.valueOf(checkStatus));
                    if(1 == checkStatus){// 机审不通过
                        // 查询不通过的原因
                        if(!StringUtils.isEmpty(catalog.getCheckId())){
                            AutomaticCheck automaticCheck = checkDao.selectByPrimaryKey(catalog.getCheckId());
                            if(null != automaticCheck){
                                StringBuilder result = new StringBuilder();
                                result = result.append(automaticCheck.getPornDetail()).append(";").append(automaticCheck.getAntispamDetail()).append(";")
                                        .append(automaticCheck.getAdDetail()).append(";").append(automaticCheck.getTerrorismDetail());
                                nodeTmp.setCheckResult(result.toString());
                            }
                        }
                    }
                }
                // 查询资源以及资源的审核结果
                List<CourseDetail> details = detailDao.selectList(MapUtils.initMap("catalogId",catalog.getCatalogId()));
                if(!CommonUtils.listIsEmptyOrNull(details)){
                    // 查询学习进度,如果是老师，是查不到学习进度的，所以就是空的，只有学生有学习进度
                    Map<String,Object> map = MapUtils.initMap("studentId",studentId);
                    map.put("courseDetailId",details.get(0).getCourseDetailId());
                    List<LearnActive> actives = learnActiveDao.selectList(map);
                    if(!CommonUtils.listIsEmptyOrNull(actives)){
                        nodeTmp.setLearnFlag((0 == actives.get(0).getLearnFlag())?"N":"Y");
                    }
                    List<Resources> resources1 = resourcesDao.selectList(MapUtils.initMap("resourcesId",details.get(0).getResourcesId()));
//                List<Map<String,Object>> retList = catalogDao.selectResourceAndCheckResult(MapUtils.initMap("catalogId", catalog.getCatalogId()));
                    if(!CommonUtils.listIsEmptyOrNull(resources1)){
                        Resources resources = resources1.get(0);
                        Integer type = resources.getType();
                        ResourceModel resourceModel = new ResourceModel();
                        resourceModel.setContent(resources.getContent());
                        resourceModel.setTitle(resources.getTitle());
                        resourceModel.setResourcesId(resources.getResourcesId());
                        resourceModel.setResourceType(type);
                        if("M".equals(userType.toUpperCase())){
                            Integer status = resources.getCheckStatus();
                            resourceModel.setCheckStatus(String.valueOf(status));
                            if(1 == status) {// 审核不通过
                                // 查询不通过的原因
                                if(!StringUtils.isEmpty(catalog.getCheckId())){
                                    AutomaticCheck automaticCheck = checkDao.selectByPrimaryKey(catalog.getCheckId());
                                    if(null != automaticCheck){
                                        StringBuilder result = new StringBuilder();
                                        result = result.append(automaticCheck.getPornDetail()).append(";").append(automaticCheck.getAntispamDetail()).append(";")
                                                .append(automaticCheck.getAdDetail()).append(";").append(automaticCheck.getTerrorismDetail());
                                        resourceModel.setCheckResult(result.toString());
                                    }
                                }
                            }
                        }
                        if(3 == type){
                            // 查询直播的内容
                            List<LiveTelecast> liveTelecasts = liveTelecastDao.selectList(MapUtils.initMap("romeId",resources.getContent()));
                            if(!CommonUtils.listIsEmptyOrNull(liveTelecasts)){
                                resourceModel.setLiveStartTime(liveTelecasts.get(0).getStartTime());
                                resourceModel.setLiveEndTime(liveTelecasts.get(0).getEndTime());
                                if(1 == DateUtils.dateCompare(liveTelecasts.get(0).getStartTime(),DateUtils.now())){// 表示直播未开始
                                    resourceModel.setLiveResult("（直播未开始）");
                                    resourceModel.setLiveStatus(0);
                                }else if(-1 == DateUtils.dateCompare(liveTelecasts.get(0).getStartTime(),DateUtils.now())){
                                    if(1 == DateUtils.dateCompare(liveTelecasts.get(0).getEndTime(),DateUtils.now())){
                                        resourceModel.setLiveResult("（正在直播）");
                                        resourceModel.setLiveStatus(1);
                                    }else if(-1 == DateUtils.dateCompare(liveTelecasts.get(0).getEndTime(),DateUtils.now())){
                                        resourceModel.setLiveResult("（直播已结束）");
                                        resourceModel.setLiveStatus(2);
                                    }
                                }
                            }else{
                                log.error("根据直播间id【"+resources.getContent()+"】未查询到直播间详情");
                            }
                        }
                        nodeTmp.setResourceModel(resourceModel);
                    }
                }
                paraMap.put("_sort_line", "sort");
                paraMap.put("parentId", catalog.getCatalogId());
                paraMap.put("_order_", "ASC");
                paraMap.put("courseId", catalog.getCourseId());
                paraMap.put("status", catalog.getStatus());
                List<CatalogAndResourceModel> list = getChildNodes(paraMap,userType,studentId);
                if(!CommonUtils.listIsEmptyOrNull(list)){
                    nodeTmp.setNodes(list);
                }
                nodes.add(nodeTmp);
            });
        }
        return nodes;
    }

    @Override
    public List<PreLiveInfoModel> selectPreLiveInfo(Integer courseId) throws ServiceException {

        List<PreLiveInfoModel> models = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("courseId", courseId);
        Map<String, Object> paraMap = new HashMap<>();
        paraMap.put("_sort_line", "sort");
        paraMap.put("parentId", "0");
        paraMap.put("_order_", "ASC");
        paraMap.put("courseId", courseId);
        paraMap.put("status", "1");
        selectChildNodes(paraMap,models);
        models.sort(Comparator.comparing(PreLiveInfoModel::getBeginTime));
        return models;
    }

    private void selectChildNodes(Map<String, Object> paraMap,List<PreLiveInfoModel> models) {

        List<Catalog> lists = new ArrayList<>();
        lists = catalogDao.selectList(paraMap);
        if (!CommonUtils.listIsEmptyOrNull(lists)) {
            lists.forEach(catalog -> {
                // 查询资源以及资源的审核结果
                List<CourseDetail> details = detailDao.selectList(MapUtils.initMap("catalogId",catalog.getCatalogId()));
                if(!CommonUtils.listIsEmptyOrNull(details)){
                    List<Resources> resources1 = resourcesDao.selectList(MapUtils.initMap("resourcesId",details.get(0).getResourcesId()));
                    if(!CommonUtils.listIsEmptyOrNull(resources1)){
                        Resources resources = resources1.get(0);
                        Integer type = resources.getType();
                        if(3 == type){
                            PreLiveInfoModel liveInfoModel = new PreLiveInfoModel();
                            liveInfoModel.setCatalogName(catalog.getName());
                            liveInfoModel.setCatalogId(catalog.getCatalogId());
                            liveInfoModel.setResourceId(resources.getResourcesId());
                            // 查询直播的内容
                            List<LiveTelecast> liveTelecasts = liveTelecastDao.selectList(MapUtils.initMap("romeId",resources.getContent()));
                            if(!CommonUtils.listIsEmptyOrNull(liveTelecasts)){
                                liveInfoModel.setBeginTime(liveTelecasts.get(0).getStartTime());
                                liveInfoModel.setEndTime(liveTelecasts.get(0).getEndTime());
                                if(1 == DateUtils.dateCompare(liveTelecasts.get(0).getStartTime(),DateUtils.now())){// 表示直播未开始
                                    liveInfoModel.setDesc("（直播未开始）");
                                    liveInfoModel.setStatus(0);
                                }else if(-1 == DateUtils.dateCompare(liveTelecasts.get(0).getStartTime(),DateUtils.now())){
                                    if(1 == DateUtils.dateCompare(liveTelecasts.get(0).getEndTime(),DateUtils.now())){
                                        liveInfoModel.setDesc("（正在直播）");
                                        liveInfoModel.setStatus(1);
                                    }else if(-1 == DateUtils.dateCompare(liveTelecasts.get(0).getEndTime(),DateUtils.now())){
                                        liveInfoModel.setDesc("（直播已结束）");
                                        liveInfoModel.setStatus(2);
                                    }
                                }
                            }else{
                                throw new ServiceException("根据直播间id【"+resources.getContent()+"】未查询到直播间详情");
                            }

                            models.add(liveInfoModel);
                        }
                    }
                }
                paraMap.put("_sort_line", "sort");
                paraMap.put("parentId", catalog.getCatalogId());
                paraMap.put("_order_", "ASC");
                paraMap.put("courseId", catalog.getCourseId());
                paraMap.put("status", "1");
                selectChildNodes(paraMap,models);
            });
        }
    }

    @Override
    public ResourcesViewModel keepStudy(Integer courseId, HttpServletRequest request) throws Exception {

        int studentId = Integer.valueOf(request.getAttribute("userId").toString());
        Map<String,Object> map = MapUtils.initMap("courseId",courseId);
        map.put("learnFlag","0");
        map.put("studentId",studentId);
        List<LearnActive> actives = new ArrayList<>();
        actives = learnActiveDao.selectList(map);
        actives.sort(Comparator.comparingInt(LearnActive::getSort));
        if(!CommonUtils.listIsEmptyOrNull(actives)){
            LearnActive active = actives.get(0);
            // 1、展示学习资源回显
            CourseDetail detail = detailDao.selectByPrimaryKey(active.getCourseDetailId());
            if(null == detail){
                throw new ServiceException("根据学习记录未查询到学习资源！课程详情id【"+active.getCourseDetailId()+"】");
            }
            ResourcesViewModel resourcesViewModel = new ResourcesViewModel();
            resourcesViewModel.setCatalogId(""+detail.getCatalogId());
            resourcesService.getResourcesByCatalogId(resourcesViewModel, studentId);
            // 2、记录学习进度
            active.setLearnFlag(1);
            learnActiveDao.updateByPrimaryKeySelective(active);
            return resourcesViewModel;

        }else{
            map.put("learnFlag","1");
            actives = learnActiveDao.selectList(map);
            actives.sort(Comparator.comparingInt(LearnActive::getSort));
            if(CommonUtils.listIsEmptyOrNull(actives)){
                throw new ServiceException("未查询到学习进度！");
            }
            LearnActive active = actives.get(0);
            // 1、展示学习资源回显
            CourseDetail detail = detailDao.selectByPrimaryKey(active.getCourseDetailId());
            if(null == detail){
                throw new ServiceException("根据学习记录未查询到学习资源！课程详情id【"+active.getCourseDetailId()+"】");
            }
            ResourcesViewModel resourcesViewModel = new ResourcesViewModel();
            resourcesViewModel.setCatalogId(""+detail.getCatalogId());
            resourcesService.getResourcesByCatalogId(resourcesViewModel, studentId);
            // 2、记录学习进度
            active.setLearnFlag(1);
            learnActiveDao.updateByPrimaryKeySelective(active);
            return resourcesViewModel;
        }
    }

    @Override
    public ResourcesViewModel beginStudy(String type,Integer catalogId, HttpServletRequest request) throws Exception {

        int studentId = Integer.valueOf(request.getAttribute("userId").toString());
        List<CourseDetail> details = detailDao.selectList(MapUtils.initMap("catalogId",catalogId));
        if (CommonUtils.listIsEmptyOrNull(details)){
            throw new ServiceException("该目录下未绑定资源！");
        }
        // 1、展示学习资源回显
        ResourcesViewModel resourcesViewModel = new ResourcesViewModel();
        resourcesViewModel.setCatalogId(""+catalogId);
        resourcesService.getResourcesByCatalogId(resourcesViewModel, studentId);
        // 2、判断是学生还是老师
        if("C".equals(type.toUpperCase())){
            CourseDetail courseDetail = details.get(0);
            Map<String,Object> map = MapUtils.initMap("studentId",studentId);
            map.put("courseDetailId",courseDetail.getCourseDetailId());
            List<LearnActive> actives = learnActiveDao.selectList(map);
            if(CommonUtils.listIsEmptyOrNull(actives)){
                throw new ServiceException("未查询到该学生的学习进度！");
            }
            LearnActive active = actives.get(0);
            // 记录学习进度
            active.setLearnFlag(1);
            learnActiveDao.updateByPrimaryKeySelective(active);
        }
        // 3、如果是直播的，那这里判断直播时间，如果已经结束需要取一下回放地址
        if("3".equals(resourcesViewModel.getType())){
            LiveTelecast liveTelecast = (LiveTelecast)resourcesViewModel.getData();
            Date startTime = liveTelecast.getStartTime();
            Date endTime = liveTelecast.getEndTime();
            if(1 == DateUtils.dateCompare(startTime,DateUtils.now())){// 表示直播未开始
                log.info(liveTelecast.getRomeId()+"（直播未开始）>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            }else if(-1 == DateUtils.dateCompare(startTime,DateUtils.now())){
                if(1 == DateUtils.dateCompare(endTime,DateUtils.now())){
                    log.info(liveTelecast.getRomeId()+"（正在直播）>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                }else if(-1 == DateUtils.dateCompare(endTime,DateUtils.now())){
                    log.info(liveTelecast.getRomeId()+"（直播已结束）>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                    // 调用查询直播回放的接口
                    try{
                        String playBackUrl = liveService.getPlaybackUrl(liveTelecast.getRomeId(), request);
                        liveTelecast.setStuUrl(playBackUrl);
                        resourcesViewModel.setData(liveTelecast);
                    }catch (Exception e){
                        throw new ServiceException("直播已结束，未查询到回放视频！");
                    }
                }
            }
        }

        return resourcesViewModel;
    }

    @Override
    public void filePreview(String fileUrl, String fileName, HttpServletRequest httpServletRequest, HttpServletResponse response) throws Exception {

        String[] fileNameStr = fileName.split("\\.");
        if (fileNameStr.length <= 0) {
            throw new ServiceException("文件名没有后缀，不能确定文件类型！");
        }
        //支持中文名称
        try {
            fileName = URLEncoder.encode(fileNameStr[0]+".pdf", "UTF-8");
            if (httpServletRequest.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } else {
                fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setContentType("application/pdf");
//        response.setContentType("application/force-download");
        response.setHeader("Access-Control-Expose-Headers", "fileName");
        response.setHeader("fileName", fileName);
        response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
        response.setHeader(HttpHeaders.ACCEPT_RANGES, "bytes");
        if (fileNameStr[0].contains("pdf")) {
            try {
                OutputStream outputStream = response.getOutputStream();
                File htmlOutputFile = new File(fileUrl);
                byte[] pdfFileBytes = FileUtils.readFileToByteArray(htmlOutputFile);
                response.setHeader(HttpHeaders.CONTENT_LENGTH, "" + pdfFileBytes.length);
                IOUtils.write(pdfFileBytes, outputStream);
                outputStream.close();
            } catch (Exception e) {
                log.error("Non IO Exception: Get File from Fast DFS failed", e);
            }
        } else {
            // 先根据fileId查询文件url
            Office2HtmlOrPdfUtil coc2HtmlUtil = Office2HtmlOrPdfUtil.getDoc2HtmlUtilInstance();
            InputStream inputStream = HttpReqUtil.getInputStream(fileUrl);
            String htmlFilePath = coc2HtmlUtil.file2pdf(inputStream, pdfFilePath, fileNameStr[0], "." + fileNameStr[1], host, port);
            try {
                OutputStream outputStream = response.getOutputStream();
                File htmlOutputFile = new File(htmlFilePath);
                byte[] pdfFileBytes = FileUtils.readFileToByteArray(htmlOutputFile);
                response.setHeader(HttpHeaders.CONTENT_LENGTH, "" + pdfFileBytes.length);
                IOUtils.write(pdfFileBytes, outputStream);
                outputStream.close();
            } catch (Exception e) {
                log.error("Non IO Exception: Get File from Fast DFS failed", e);
            }
        }
    }
}
