package com.by.blcu.mall.service.impl;

import com.by.blcu.core.ret.ServiceException;
import com.by.blcu.core.utils.ApplicationUtils;
import com.by.blcu.core.utils.MapAndObjectUtils;
import com.by.blcu.core.utils.StringUtils;
import com.by.blcu.core.utils.SubListUtil;
import com.by.blcu.course.dto.Course;
import com.by.blcu.course.service.ICourseService;
import com.by.blcu.mall.dao.CommodityInfoMapper;
import com.by.blcu.mall.dao.MallMealInfoMapper;
import com.by.blcu.mall.model.*;
import com.by.blcu.mall.service.*;
import com.by.blcu.core.universal.AbstractService;
import com.by.blcu.mall.vo.*;
import com.by.blcu.manager.model.ManagerOrganization;
import com.by.blcu.manager.model.ManagerTeacher;
import com.by.blcu.manager.service.ManagerOrganizationService;
import com.by.blcu.manager.service.ManagerTeacherService;
import com.by.blcu.manager.service.SsoUserService;
import com.by.blcu.resource.dao.IMyFavoriteDao;
import com.by.blcu.mall.service.IMyFavoriteService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

/**
* @Description: CommodityInfoService接口实现类
* @author 李程
* @date 2019/07/29 11:03
*/
@Service
@CacheConfig(cacheNames = {"commodityInfo"})
public class CommodityInfoServiceImpl extends AbstractService<CommodityInfo> implements CommodityInfoService {

    @Resource
    private CommodityInfoMapper commodityInfoMapper;

    @Resource
    private CourseCategoryInfoService courseCategoryInfoService;

    @Resource
    private SsoUserService ssoUserService;

    @Resource
    private ManagerTeacherService managerTeacherService;

    @Resource
    private FileService fileService;

    @Resource
    private MallOrderInfoService mallOrderInfoService;

    @Resource
    private MallCommodityLecturerService mallCommodityLecturerService;

    @Resource
    private IMallOrderPaymentService mallOrderPaymentService;

    @Resource
    private IMyFavoriteDao myFavoriteDao;

    @Resource
    private RedisService redisService;

    @Resource
    private ICourseService courseService;

    @Resource
    private MallMealInfoService mallMealInfoService;

    @Resource
    private ManagerOrganizationService managerOrganizationService;

    @Override
    public PageInfo<CommodityInfoFileVo> selectListAll(Integer page, Integer size,CommodityInfo commodityInfo) {
        PageHelper.startPage(page, size);
        List<CommodityInfoVo> list = commodityInfoMapper.selectListAll(commodityInfo);
        PageInfo<CommodityInfoVo> pageInfo1 = new PageInfo<CommodityInfoVo>(list);
        PageInfo<CommodityInfoFileVo> pageInfoVo = new PageInfo<CommodityInfoFileVo>();
        List<CommodityInfoFileVo> commodityInfoNew = new ArrayList<>();
        if(list != null && list.size() > 0){
            for(CommodityInfoVo commodityInfoVo : list){
                List<CommodityLecturer> commodityLecturerList = commodityInfoVo.getCommodityLecturerList();
                for(CommodityLecturer commodityLecturer: commodityLecturerList){
                    if(!StringUtils.isBlank(commodityLecturer.getLecturer())){
                        ManagerTeacher managerTeacher = managerTeacherService.selectTeacherByTeacherIdInter(commodityLecturer.getLecturer());
                        if(null != managerTeacher){
                            commodityLecturer.setLecturerName(managerTeacher.getTeacherName());
                        }
                    }
                }
                File file = fileService.selectByFileId(commodityInfoVo.getPicturePath());
                File file1 = fileService.selectByFileId(commodityInfoVo.getAuditionVideoPath());
                CommodityInfoFileVo commodityInfoFileVo = MapAndObjectUtils.ObjectClone(commodityInfoVo, CommodityInfoFileVo.class);
                commodityInfoFileVo.setCommodityLecturerList(commodityInfoVo.getCommodityLecturerList());
                if(file != null){
                    commodityInfoFileVo.setPicturePathView(file.getFilePath());
                }
                if(file1 != null){
                    commodityInfoFileVo.setAuditionVideoPathView(file1.getFileName());
                }
                if(!StringUtils.isBlank(commodityInfoVo.getOrgCode())){
                    ManagerOrganization managerOrganization = managerOrganizationService.selectOrganizationByOrgCode(commodityInfoVo.getOrgCode());
                    if(null != managerOrganization && !StringUtils.isBlank(managerOrganization.getOrganizationName())){
                        commodityInfoFileVo.setOrgName(managerOrganization.getOrganizationName());
                    }
                }
                //判断是否套餐
                if(null != commodityInfoVo.getCourseType() && commodityInfoVo.getCourseType() == 1){
                    String s = commodityInfoVo.getCommodityId();
                    List<CommodityInfoFileVo> commodityInfoFileVos = fillInChild(s);
                    commodityInfoFileVo.setCommodityInfoFileVoList(commodityInfoFileVos);
                }
                commodityInfoNew.add(commodityInfoFileVo);
            }
            //List<CommodityInfoVo> commodityInfoVos = updateVoToLecturerName(list);
            PageInfo<CommodityInfoFileVo> pageInfo = new PageInfo<CommodityInfoFileVo>(commodityInfoNew);
            pageInfo.setTotal(pageInfo1.getTotal());
            return pageInfo;
        }else{
            return pageInfoVo;
        }
    }

    @Override
    @Transactional
    public String updateComStatusByCommodityId(List<String> commodityIdList,Integer commodityStatus) {
        //上架同步课程
//        if(commodityStatus == 1){
//            for(String s:commodityIdList){
//                List<MallOrderInfoVo> mallOrderInfoVos = mallOrderInfoService.selectMallOrderInfoVoByCommodityId(s);
//                if(null != mallOrderInfoVos && mallOrderInfoVos.size()>0){
//                    for(MallOrderInfoVo mallOrderInfoVo : mallOrderInfoVos){
//                        try {
//                            Boolean aBoolean = mallOrderPaymentService.syncCourseResourcesForShelf(mallOrderInfoVo.getOrderId(), mallOrderInfoVo.getUserName());
//                            if(!aBoolean){
//                                return "1";
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//        }
        List<String> list = new ArrayList<String>();
        for(String commodityId:commodityIdList){
            CommodityInfoVo commodityInfoVo = commodityInfoMapper.selectStatusByCommodityIdAndIsRecommend(commodityId);
//            if(commodityInfoVo != null){
//                list.add(commodityInfoVo.getCommodityId());
//            }
            if(null == commodityInfoVo){
                return "2";
            }
            if(commodityStatus == 0 && commodityInfoVo.getCommodityStatus() == 2){
                throw new ServiceException( "包含未上架商品，不能直接下架！");
            }
            if(commodityStatus == 1){
                List<Integer> courseList = new ArrayList<Integer>();
                if(commodityInfoVo.getCourseType() == 1){
                    MallMealInfo mallMealInfo = new MallMealInfo();
                    mallMealInfo.setComCommodityId(commodityInfoVo.getCommodityId());
                    List<MallMealInfo> select = mallMealInfoService.select(mallMealInfo);
                    for (MallMealInfo mallMealInfoNew : select){
                        CommodityInfoVo commodityInfoVo1 = commodityInfoMapper.selectStatusByCommodityId(mallMealInfoNew.getCommodityId());
                        if(null != commodityInfoVo1){
                            //套餐上架时判断里面是否有未上架商品
                            if(commodityInfoVo1.getCommodityStatus() == 0){
                                throw new ServiceException( "套餐里包含未上架商品！");
                            }
                            courseList.add(Integer.valueOf(commodityInfoVo1.getCourseId()));
                        }
                    }
                }else {
                    if(StringUtils.isBlank(commodityInfoVo.getCourseId())){
                        throw new ServiceException( commodityInfoVo.getCourseName() + "商品未绑定课程！");
                    }
                    courseList.add(Integer.valueOf(commodityInfoVo.getCourseId()));
                }
                //看商品里是否有未审核过的课程
                Set set = new HashSet(courseList);
                boolean checkPass = courseService.isCheckPass(set);
                if(!checkPass){
                    throw new ServiceException( "商品里课程有未通过审核的，不能上架，请联系审核人员！");
                }
            }
            list.add(commodityInfoVo.getCommodityId());
        }
        if(list != null && list.size() > 0){
            Integer integer = commodityInfoMapper.updateComStatusByCommodityId(list, commodityStatus);
            if(null != integer && integer > 0 ){
                return "3";
            }
        }
        return "0";
    }

    @Override
    public List<CourseCategoryCommodityVo> selectListRecursion() {
//        List<CourseCategoryCommodityVo> listRecursion = redisService.getList("ListRecursion", CourseCategoryCommodityVo.class);
//        if(null != listRecursion && listRecursion.size()>0){
//            return listRecursion;
//        }
        System.out.println("没使用缓存！");
        List<CourseCategoryInfo> listAll = new ArrayList<>();
        List<CourseCategoryInfo> list = courseCategoryInfoService.selectAllByCommodityStatus();
        //分类去重
        List<CourseCategoryInfo> collect = list.stream().collect(collectingAndThen(toCollection(() -> new TreeSet<CourseCategoryInfo>(comparing(n -> n.getCcId()))), ArrayList::new));
        listAll.addAll(collect);
        if(collect != null && collect.size() > 0){
            CourseCategoryInfo courseCategoryInfo = new CourseCategoryInfo();
            //把一级分类添加进来
            for(CourseCategoryInfo courseCategoryInfoNew : collect){
                courseCategoryInfo.setCcId(courseCategoryInfoNew.getParentId());
                CourseCategoryInfo courseCategoryInfo1 = courseCategoryInfoService.selectOne(courseCategoryInfo);
                addList(listAll, courseCategoryInfo1);
            }
            List<CourseCategoryCommodityVo> listVo = new ArrayList<CourseCategoryCommodityVo>();
            for(CourseCategoryInfo courseCategoryInfoAll : listAll){
                CourseCategoryCommodityVo courseCategoryInfoVo = MapAndObjectUtils.ObjectClone(courseCategoryInfoAll,CourseCategoryCommodityVo.class);
                listVo.add(courseCategoryInfoVo);
            }
            List<CourseCategoryCommodityVo> courseCategoryCommodityVos = comparingByParentIdAndCcSort(listVo);
//            redisService.setList("ListRecursion",listGetStreeCommodityVo(listVo));
            return listGetStreeCommodityVo(courseCategoryCommodityVos);
        }else{
            throw new ServiceException( "符合条件的商品不存在！");
        }
    }

    //根据两个字段排序list
    public List<CourseCategoryCommodityVo> comparingByParentIdAndCcSort(List<CourseCategoryCommodityVo> list){
        List<CourseCategoryCommodityVo> list1 = list.stream().sorted(Comparator.comparing(CourseCategoryCommodityVo::getParentId).thenComparing(CourseCategoryCommodityVo::getCcSort)).collect(Collectors.toList());
        return list1;
    }


    @Override
    public PageInfo<CommodityInfoFileVo> listByCcId(Integer page,Integer size,CommodityInfo commodityInfo) {
        PageHelper.startPage(page, size);
        List<CommodityInfoVo> commodityInfos = commodityInfoMapper.listByCcId(commodityInfo);
        PageInfo<CommodityInfoVo> pageInfo1 = new PageInfo<CommodityInfoVo>(commodityInfos);
        if (commodityInfos != null && commodityInfos.size() > 0){
            List<CommodityInfoFileVo> commodityInfoNew = new ArrayList<>();
            for(CommodityInfoVo commodityInfoVo : commodityInfos){
                List<CommodityLecturer> commodityLecturerList = commodityInfoVo.getCommodityLecturerList();
                for(CommodityLecturer commodityLecturer: commodityLecturerList){
                    if(!StringUtils.isBlank(commodityLecturer.getLecturer())){
                        ManagerTeacher managerTeacher = managerTeacherService.selectTeacherByTeacherIdInter(commodityLecturer.getLecturer());
                        if(null != managerTeacher){
                            commodityLecturer.setLecturerName(managerTeacher.getTeacherName());
                        }
                    }
                }
                //CommodityInfoVo commodityInfoVo1 = updateLecturerName(commodityInfoVo);
                File file = fileService.selectByFileId(commodityInfoVo.getPicturePath());
                File file1 = fileService.selectByFileId(commodityInfoVo.getAuditionVideoPath());
                CommodityInfoFileVo commodityInfoFileVo = MapAndObjectUtils.ObjectClone(commodityInfoVo, CommodityInfoFileVo.class);
                commodityInfoFileVo.setCommodityLecturerList(commodityInfoVo.getCommodityLecturerList());
                if(file != null){
                    commodityInfoFileVo.setPicturePathView(file.getFilePath());
                }
                if(file1 != null){
                    commodityInfoFileVo.setAuditionVideoPathView(file1.getFileName());
                }
                //判断是否套餐
                if(null != commodityInfoVo.getCourseType() && commodityInfoVo.getCourseType() == 1){
                    String s = commodityInfoVo.getCommodityId();
                    List<CommodityInfoFileVo> commodityInfoFileVos = fillInChild(s);
                    commodityInfoFileVo.setCommodityInfoFileVoList(commodityInfoFileVos);
                }
                commodityInfoNew.add(commodityInfoFileVo);
            }
            PageInfo<CommodityInfoFileVo> pageInfo = new PageInfo<CommodityInfoFileVo>(commodityInfoNew);
            pageInfo.setTotal(pageInfo1.getTotal());
            return pageInfo;
        } else {
            throw new ServiceException( "该分类下商品不存在！");
        }
    }

    @Override
    public PageInfo<CommodityInfoFileVo> listByCourseName(Integer page,Integer size,CommodityInfo commodityInfo) {
        PageHelper.startPage(page, size);
        List<CommodityInfoVo> commodityInfos = commodityInfoMapper.listByCourseName(commodityInfo);
        PageInfo<CommodityInfoVo> pageInfo1 = new PageInfo<CommodityInfoVo>(commodityInfos);
        if (commodityInfos != null && commodityInfos.size() > 0){
            List<CommodityInfoFileVo> commodityInfoNew = new ArrayList<>();
            for(CommodityInfoVo commodityInfoVo : commodityInfos){
                List<CommodityLecturer> commodityLecturerList = commodityInfoVo.getCommodityLecturerList();
                for(CommodityLecturer commodityLecturer: commodityLecturerList){
                    if(!StringUtils.isBlank(commodityLecturer.getLecturer())){
                        ManagerTeacher managerTeacher = managerTeacherService.selectTeacherByTeacherIdInter(commodityLecturer.getLecturer());
                        if(null != managerTeacher){
                            commodityLecturer.setLecturerName(managerTeacher.getTeacherName());
                        }
                    }
                }
                //CommodityInfoVo commodityInfoVo1 = updateLecturerName(commodityInfoVo);
                File file = fileService.selectByFileId(commodityInfoVo.getPicturePath());
                File file1 = fileService.selectByFileId(commodityInfoVo.getAuditionVideoPath());
                CommodityInfoFileVo commodityInfoFileVo = MapAndObjectUtils.ObjectClone(commodityInfoVo, CommodityInfoFileVo.class);
                commodityInfoFileVo.setCommodityLecturerList(commodityInfoVo.getCommodityLecturerList());
                if(file != null){
                    commodityInfoFileVo.setPicturePathView(file.getFilePath());
                }
                if(file1 != null){
                    commodityInfoFileVo.setAuditionVideoPathView(file1.getFileName());
                }
                if(!StringUtils.isBlank(commodityInfoVo.getOrgCode())){
                    ManagerOrganization managerOrganization = managerOrganizationService.selectOrganizationByOrgCode(commodityInfoVo.getOrgCode());
                    if(null != managerOrganization && !StringUtils.isBlank(managerOrganization.getOrganizationName())){
                        commodityInfoFileVo.setOrgName(managerOrganization.getOrganizationName());
                    }
                }
                //判断是否套餐
                if(null != commodityInfoVo.getCourseType() && commodityInfoVo.getCourseType() == 1){
                    String s = commodityInfoVo.getCommodityId();
                    List<CommodityInfoFileVo> commodityInfoFileVos = fillInChild(s);
                    commodityInfoFileVo.setCommodityInfoFileVoList(commodityInfoFileVos);
                }
                commodityInfoNew.add(commodityInfoFileVo);
            }
            PageInfo<CommodityInfoFileVo> pageInfo = new PageInfo<CommodityInfoFileVo>(commodityInfoNew);
            pageInfo.setTotal(pageInfo1.getTotal());
            return pageInfo;
        } else {
            throw new ServiceException( "该分类下商品不存在！");
        }
    }

    @Override
    public Integer deleteChildCommodity(String comCommodityId, String commodityId) {
        Integer integer = mallMealInfoService.deleteChildCommodity(comCommodityId, commodityId);
        return integer;
    }

    @Override
    public List<String> selectLecturerList() {
        return commodityInfoMapper.selectLecturerList();
    }

    @Override
    public List<CommodityInfoFileVo> selectByLessonType() {
        List<CommodityInfoVo> list = commodityInfoMapper.selectByLessonType();
        if(list != null && list.size() > 0){
            List<CommodityInfoFileVo> commodityInfoVos = new ArrayList();
            for(CommodityInfoVo commodityInfoVo : list){
                List<CommodityLecturer> commodityLecturerList = commodityInfoVo.getCommodityLecturerList();
                for(CommodityLecturer commodityLecturer: commodityLecturerList){
                    if(!StringUtils.isBlank(commodityLecturer.getLecturer())){
                        ManagerTeacher managerTeacher = managerTeacherService.selectTeacherByTeacherIdInter(commodityLecturer.getLecturer());
                        if(null != managerTeacher){
                            commodityLecturer.setLecturerName(managerTeacher.getTeacherName());
                        }
                    }
                }
                //CommodityInfoVo commodityInfoVo1 = updateLecturerName(commodityInfoVo);
                File file = fileService.selectByFileId(commodityInfoVo.getPicturePath());
                File file1 = fileService.selectByFileId(commodityInfoVo.getAuditionVideoPath());
                CommodityInfoFileVo commodityInfoFileVo = MapAndObjectUtils.ObjectClone(commodityInfoVo, CommodityInfoFileVo.class);
                commodityInfoFileVo.setCommodityLecturerList(commodityInfoVo.getCommodityLecturerList());
                if(file != null){
                    commodityInfoFileVo.setPicturePathView(file.getFilePath());
                }
                if(file1 != null){
                    commodityInfoFileVo.setAuditionVideoPathView(file1.getFileName());
                }
                //判断是否套餐
                if(null != commodityInfoVo.getCourseType() && commodityInfoVo.getCourseType() == 1){
                    String s = commodityInfoVo.getCommodityId();
                    List<CommodityInfoFileVo> commodityInfoFileVos = fillInChild(s);
                    commodityInfoFileVo.setCommodityInfoFileVoList(commodityInfoFileVos);
                }
                commodityInfoVos.add(commodityInfoFileVo);
            }
            return commodityInfoVos;
        }else{
            return null;
        }
    }

    @Override
    public List<CommodityInfoFileVo> selectByAudition() {
        List<CommodityInfoVo> list = commodityInfoMapper.selectByAudition();
        if(list != null && list.size() > 0){
            //List<CommodityInfoVo> commodityInfoVos = updateToLecturerName(list);
            List<CommodityInfoFileVo> commodityInfoFileVos = new ArrayList();
            for(CommodityInfoVo commodityInfoVo : list){
                List<CommodityLecturer> commodityLecturerList = commodityInfoVo.getCommodityLecturerList();
                for(CommodityLecturer commodityLecturer: commodityLecturerList){
                    if(!StringUtils.isBlank(commodityLecturer.getLecturer())){
                        ManagerTeacher managerTeacher = managerTeacherService.selectTeacherByTeacherIdInter(commodityLecturer.getLecturer());
                        if(null != managerTeacher){
                            commodityLecturer.setLecturerName(managerTeacher.getTeacherName());
                        }
                    }
                }
                String ccName = courseCategoryInfoService.selectCcNameByCcId(commodityInfoVo.getCcId());
                commodityInfoVo.setCcName(ccName);
                File file = fileService.selectByFileId(commodityInfoVo.getPicturePath());
                File file1 = fileService.selectByFileId(commodityInfoVo.getAuditionVideoPath());
                CommodityInfoFileVo commodityInfoFileVo = MapAndObjectUtils.ObjectClone(commodityInfoVo, CommodityInfoFileVo.class);
                commodityInfoFileVo.setCommodityLecturerList(commodityInfoVo.getCommodityLecturerList());
                if(file != null){
                    commodityInfoFileVo.setPicturePathView(file.getFilePath());
                }
                if(file1 != null){
                    commodityInfoFileVo.setAuditionVideoPathView(file1.getFileName());
                }
                commodityInfoFileVos.add(commodityInfoFileVo);
            }
            return commodityInfoFileVos;
        }else{
            throw new ServiceException( "符合条件的商品不存在！");
        }
    }

    //填充子商品
    public List<CommodityInfoFileVo> fillInChild(String commodityId){
        List<MallMealInfo> select = mallMealInfoService.selectByComCommodityId(commodityId);
        List<CommodityInfoFileVo> list = new ArrayList<CommodityInfoFileVo>();
        if(null != select){
            for (MallMealInfo mallMealInfo1 : select){
                CommodityInfoVo commodityInfoVoNew = commodityInfoMapper.selectByCommodityIdNoCommodityStatus(mallMealInfo1.getCommodityId());
                List<CommodityLecturer> commodityLecturerList = commodityInfoVoNew.getCommodityLecturerList();
                for(CommodityLecturer commodityLecturer: commodityLecturerList){
                    if(!StringUtils.isBlank(commodityLecturer.getLecturer())){
                        ManagerTeacher managerTeacher = managerTeacherService.selectTeacherByTeacherIdInter(commodityLecturer.getLecturer());
                        if(null != managerTeacher){
                            commodityLecturer.setLecturerName(managerTeacher.getTeacherName());
                        }
                    }
                }
                //CommodityInfoVo commodityInfoVo1 = updateLecturerName(commodityInfoVo);
                String ccName = courseCategoryInfoService.selectCcNameByCcId(commodityInfoVoNew.getCcId());
                commodityInfoVoNew.setCcName(ccName);
                File file = fileService.selectByFileId(commodityInfoVoNew.getPicturePath());
                File file1 = fileService.selectByFileId(commodityInfoVoNew.getAuditionVideoPath());
                CommodityInfoFileVo commodityInfoFileVo = MapAndObjectUtils.ObjectClone(commodityInfoVoNew, CommodityInfoFileVo.class);
                commodityInfoFileVo.setCommodityLecturerList(commodityInfoVoNew.getCommodityLecturerList());
                if(file != null){
                    commodityInfoFileVo.setPicturePathView(file.getFilePath());
                }
                if(file1 != null){
                    commodityInfoFileVo.setAuditionVideoPathView(file1.getFileName());
                }
                if(!StringUtils.isBlank(commodityInfoVoNew.getOrgCode())){
                    ManagerOrganization managerOrganization = managerOrganizationService.selectOrganizationByOrgCode(commodityInfoVoNew.getOrgCode());
                    if(null != managerOrganization && !StringUtils.isBlank(managerOrganization.getOrganizationName())){
                        commodityInfoFileVo.setOrgName(managerOrganization.getOrganizationName());
                    }
                }
                list.add(commodityInfoFileVo);
            }
        }
        return list;
    }

    @Override
    public CommodityInfoFileVo selectByCommodityId(String commodityId) {
        CommodityInfoVo commodityInfoVo = commodityInfoMapper.selectByCommodityId(commodityId);
        if (commodityInfoVo != null){
            List<CommodityLecturer> commodityLecturerList = commodityInfoVo.getCommodityLecturerList();
            for(CommodityLecturer commodityLecturer: commodityLecturerList){
                if(!StringUtils.isBlank(commodityLecturer.getLecturer())){
                    ManagerTeacher managerTeacher = managerTeacherService.selectTeacherByTeacherIdInter(commodityLecturer.getLecturer());
                    if(null != managerTeacher){
                        commodityLecturer.setLecturerName(managerTeacher.getTeacherName());
                    }
                }
            }
            //CommodityInfoVo commodityInfoVo1 = updateLecturerName(commodityInfoVo);
            String ccName = courseCategoryInfoService.selectCcNameByCcId(commodityInfoVo.getCcId());
            commodityInfoVo.setCcName(ccName);
            File file = fileService.selectByFileId(commodityInfoVo.getPicturePath());
            File file1 = fileService.selectByFileId(commodityInfoVo.getAuditionVideoPath());
            CommodityInfoFileVo commodityInfoFileVo = MapAndObjectUtils.ObjectClone(commodityInfoVo, CommodityInfoFileVo.class);
            commodityInfoFileVo.setCommodityLecturerList(commodityInfoVo.getCommodityLecturerList());
            if(file != null){
                commodityInfoFileVo.setPicturePathView(file.getFilePath());
            }
            if(file1 != null){
                commodityInfoFileVo.setAuditionVideoPathView(file1.getFileName());
            }
            if(!StringUtils.isBlank(commodityInfoVo.getOrgCode())){
                ManagerOrganization managerOrganization = managerOrganizationService.selectOrganizationByOrgCode(commodityInfoVo.getOrgCode());
                if(null != managerOrganization && !StringUtils.isBlank(managerOrganization.getOrganizationName())){
                    commodityInfoFileVo.setOrgName(managerOrganization.getOrganizationName());
                }
            }
            //判断是否套餐
            if(null != commodityInfoVo.getCourseType() && commodityInfoVo.getCourseType() == 1){
                String s = commodityInfoVo.getCommodityId();
                List<CommodityInfoFileVo> commodityInfoFileVos = fillInChild(s);
                commodityInfoFileVo.setCommodityInfoFileVoList(commodityInfoFileVos);
            }
            return commodityInfoFileVo;
        }
        return new CommodityInfoFileVo();
    }

    @Override
    public CommodityInfoFileVo selectByCommodityIdNoStatus(String commodityId) {
        CommodityInfoVo commodityInfoVo = commodityInfoMapper.selectByCommodityIdNoStatus(commodityId);
        if (commodityInfoVo != null){
            List<CommodityLecturer> commodityLecturerList = commodityInfoVo.getCommodityLecturerList();
            for(CommodityLecturer commodityLecturer: commodityLecturerList){
                if(!StringUtils.isBlank(commodityLecturer.getLecturer())){
                    ManagerTeacher managerTeacher = managerTeacherService.selectTeacherByTeacherIdInter(commodityLecturer.getLecturer());
                    if(null != managerTeacher){
                        commodityLecturer.setLecturerName(managerTeacher.getTeacherName());
                    }
                }
            }
            //CommodityInfoVo commodityInfoVo1 = updateLecturerName(commodityInfoVo);
            String ccName = courseCategoryInfoService.selectCcNameByCcId(commodityInfoVo.getCcId());
            commodityInfoVo.setCcName(ccName);
            File file = fileService.selectByFileId(commodityInfoVo.getPicturePath());
            File file1 = fileService.selectByFileId(commodityInfoVo.getAuditionVideoPath());
            CommodityInfoFileVo commodityInfoFileVo = MapAndObjectUtils.ObjectClone(commodityInfoVo, CommodityInfoFileVo.class);
            commodityInfoFileVo.setCommodityLecturerList(commodityInfoVo.getCommodityLecturerList());
            if(file != null){
                commodityInfoFileVo.setPicturePathView(file.getFilePath());
            }
            if(file1 != null){
                commodityInfoFileVo.setAuditionVideoPathView(file1.getFileName());
            }
            return commodityInfoFileVo;
        }
        return new CommodityInfoFileVo();
    }

    @Override
    public CommodityInfoVo selectByCommodityIdNoCommodityStatus(String commodityId) {
        return commodityInfoMapper.selectByCommodityIdNoCommodityStatus(commodityId);
    }

    @Override
    public CommodityInfoFileVo selectCarByCommodityId(String commodityId) {
        CommodityInfoVo commodityInfoVo = commodityInfoMapper.selectByCommodityId(commodityId);
        if (commodityInfoVo != null){
            List<CommodityLecturer> commodityLecturerList = commodityInfoVo.getCommodityLecturerList();
            for(CommodityLecturer commodityLecturer: commodityLecturerList){
                if(!StringUtils.isBlank(commodityLecturer.getLecturer())){
                    ManagerTeacher managerTeacher = managerTeacherService.selectTeacherByTeacherIdInter(commodityLecturer.getLecturer());
                    if(null != managerTeacher){
                        commodityLecturer.setLecturerName(managerTeacher.getTeacherName());
                    }
                }
            }
            //CommodityInfoVo commodityInfoVo1 = updateLecturerName(commodityInfoVo);
            String ccName = courseCategoryInfoService.selectCcNameByCcId(commodityInfoVo.getCcId());
            commodityInfoVo.setCcName(ccName);
            File file = fileService.selectByFileId(commodityInfoVo.getPicturePath());
            File file1 = fileService.selectByFileId(commodityInfoVo.getAuditionVideoPath());
            CommodityInfoFileVo commodityInfoFileVo = MapAndObjectUtils.ObjectClone(commodityInfoVo, CommodityInfoFileVo.class);
            commodityInfoFileVo.setCommodityLecturerList(commodityInfoVo.getCommodityLecturerList());
            if(file != null){
                commodityInfoFileVo.setPicturePathView(file.getFilePath());
            }
            if(file1 != null){
                commodityInfoFileVo.setAuditionVideoPathView(file1.getFileName());
            }
            return commodityInfoFileVo;
        }
        return null;
    }

    @Override
    public Integer updateMealTypeByCommodityId(String commodityId, String mealType) {
        return commodityInfoMapper.updateMealTypeByCommodityId(commodityId,mealType);
    }

    //list<CourseCategoryInfo>添加ccId不同的实体
    public List<CourseCategoryInfo> addList(List<CourseCategoryInfo> listAll,CourseCategoryInfo courseCategoryInfo){
        boolean flag = true;
        if(listAll.size() > 0){
            for(CourseCategoryInfo courseCategoryInfoNew : listAll){
                if(courseCategoryInfoNew.getCcId().equals(courseCategoryInfo.getCcId())){
                    flag = false;
                    break;
                }else {
                    flag = true;
                }
            }
            if(flag){
                listAll.add(courseCategoryInfo);
            }
        }else {
            listAll.add(courseCategoryInfo);
        }
        return listAll;
    }

    @Override
    public List<CourseCategoryCommodityVo> selectListRecursionByOrgCode(String orgCode) {
        List<CourseCategoryInfo> listAll = new ArrayList<>();
        List<CourseCategoryInfo> list = courseCategoryInfoService.selectListRecursionByOrgCode(orgCode);
        List<CourseCategoryInfo> collect = list.stream().collect(collectingAndThen(toCollection(() -> new TreeSet<CourseCategoryInfo>(comparing(n -> n.getCcId()))), ArrayList::new));
        listAll.addAll(collect);
        if(list != null && list.size() > 0){
            CourseCategoryInfo courseCategoryInfo = new CourseCategoryInfo();
            for(CourseCategoryInfo courseCategoryInfoNew : collect){
                courseCategoryInfo.setCcId(courseCategoryInfoNew.getParentId());
                CourseCategoryInfo courseCategoryInfo1 = courseCategoryInfoService.selectOne(courseCategoryInfo);
                addList(listAll, courseCategoryInfo1);
            }
            List<CourseCategoryCommodityVo> listVo = new ArrayList<CourseCategoryCommodityVo>();
            for(CourseCategoryInfo courseCategoryInfoAll : listAll){
                CourseCategoryCommodityVo courseCategoryInfoVo = MapAndObjectUtils.ObjectClone(courseCategoryInfoAll,CourseCategoryCommodityVo.class);
                listVo.add(courseCategoryInfoVo);
            }
            List<CourseCategoryCommodityVo> courseCategoryCommodityVos = comparingByParentIdAndCcSort(listVo);
//            redisService.setList("ListRecursion",listGetStreeCommodityVo(listVo));
            return listGetStreeCommodityVoByOrgCode(courseCategoryCommodityVos,orgCode);
        }else{
            throw new ServiceException( "符合条件的商品不存在！");
        }
    }

    @Override
    public Integer childCommodityUpdateUpSort(String comCommodityId, String commodityId) {
        MallMealInfo mallMealInfo = new MallMealInfo();
        mallMealInfo.setComCommodityId(comCommodityId);
        mallMealInfo.setCommodityId(commodityId);
        List<MallMealInfo> select = mallMealInfoService.select(mallMealInfo);
        if(select != null && select.size() > 0){
            //查询上一条记录
            MallMealInfo mallMealInfo1 = mallMealInfoService.moveUpChildCommodity(select.get(0).getChildCommoditySort(),select.get(0).getComCommodityId());
            //最上面的不能上移
            if(null==mallMealInfo1){return 0;}
            //交换position的值
            int temp=select.get(0).getChildCommoditySort();
            select.get(0).setChildCommoditySort(mallMealInfo1.getChildCommoditySort());
            mallMealInfo1.setChildCommoditySort(temp);
            //更新到数据库中
            mallMealInfoService.update(select.get(0));
            mallMealInfoService.update(mallMealInfo1);
            return 1;
        }else{
            throw new ServiceException( "该套餐不存在或已经删除！");
        }
    }

    @Override
    public Integer childCommodityUpdateDownSort(String comCommodityId, String commodityId) {
        MallMealInfo mallMealInfo = new MallMealInfo();
        mallMealInfo.setComCommodityId(comCommodityId);
        mallMealInfo.setCommodityId(commodityId);
        List<MallMealInfo> select = mallMealInfoService.select(mallMealInfo);
        if(select != null && select.size() > 0){
            //查询上一条记录
            MallMealInfo mallMealInfo1 = mallMealInfoService.moveDownChildCommodity(select.get(0).getChildCommoditySort(),select.get(0).getComCommodityId());
            //最上面的不能上移
            if(null==mallMealInfo1){return 0;}
            //交换position的值
            int temp=select.get(0).getChildCommoditySort();
            select.get(0).setChildCommoditySort(mallMealInfo1.getChildCommoditySort());
            mallMealInfo1.setChildCommoditySort(temp);
            //更新到数据库中
            mallMealInfoService.update(select.get(0));
            mallMealInfoService.update(mallMealInfo1);
            return 1;
        }else{
            throw new ServiceException( "该套餐不存在或已经删除！");
        }
    }


    @Override
    public CommodityInfoVo selectStatusByCommodityId(String commodityId) {
        CommodityInfoVo commodityInfoVo = commodityInfoMapper.selectStatusByCommodityId(commodityId);
        return commodityInfoVo;
    }

    @Override
    public List<CommodityInfo> selectByCourseId(String courseId) {
        return commodityInfoMapper.selectByCourseId(courseId);
    }

    @Override
    public PageInfo<CommodityInfoVo> listByRecommend(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<CommodityInfoVo> commodityInfoVos = commodityInfoMapper.listByRecommend();
        PageInfo<CommodityInfoVo> pageInfo = new PageInfo<CommodityInfoVo>(commodityInfoVos);
        return pageInfo;
    }

    @Override
    public Integer updateSortByCommodityId(String commodityId, Integer sort) {
        return commodityInfoMapper.updateSortByCommodityId(commodityId,sort);
    }

    @Override
    @Transactional
    public Integer updateIsRecommendByCommodityId(List<String> commodityIdList) {
        Integer integer = updateIsRecommendToFalse();
        List<String> list = new ArrayList<String>();
        for(String commodityId:commodityIdList){
            CommodityInfoVo commodityInfoVo = commodityInfoMapper.selectStatusByCommodityId(commodityId);
            if(commodityInfoVo != null){
                if(commodityInfoVo.getCommodityStatus()==1){
                    list.add(commodityInfoVo.getCommodityId());
                }
            }
        }
        if(list !=null && list.size() > 0){
            Integer integer1 = commodityInfoMapper.updateIsRecommendByCommodityId(list);
            if(null != integer && null != integer1){
                return integer1;
            }
        }
        return 0;
    }

    @Override
    public Integer updateIsRecommendToFalse() {
        Integer integer = commodityInfoMapper.updateIsRecommendToFalse();
        return integer;
    }

    @Override
    public Integer deleteIsRecommendByCommodityId(String commodityId) {
        return commodityInfoMapper.deleteIsRecommendByCommodityId(commodityId);
    }

    @Override
    public Integer selectCountByCommodityId() {
        return commodityInfoMapper.selectCountByCommodityId();
    }

    @Override
    public Integer selectCountByCommodityIdType() {
        return commodityInfoMapper.selectCountByCommodityIdType();
    }

    @Override
    public Integer updateRecommendSortByCommodityId(String commodityId, Integer recommendSort) {
        return commodityInfoMapper.updateRecommendSortByCommodityId(commodityId,recommendSort);
    }

    @Override
    public List<String> selectcommoditydIListByIsRecommend(Integer lessonType) {
        return commodityInfoMapper.selectcommoditydIListByIsRecommend(lessonType);
    }

    @Override
    public Integer updateCourseIntroduceByCommodityId(String commodityId, String courseIntroduce) {
        return commodityInfoMapper.updateCourseIntroduceByCommodityId(commodityId,courseIntroduce);
    }

    @Override
    public Integer updateCourseIdByCommodityId(String commodityId, String courseId) {
        return commodityInfoMapper.updateCourseIdByCommodityId(commodityId,courseId);
    }

    @Override
    public CommodityInfoVo selectEditByCommodityId(String commodityId) {
        CommodityInfoVo commodityInfoVo = commodityInfoMapper.selectEditByCommodityId(commodityId);
        if (commodityInfoVo != null){
            List<CommodityLecturer> commodityLecturerList = commodityInfoVo.getCommodityLecturerList();
            for(CommodityLecturer commodityLecturer: commodityLecturerList){
                if(!StringUtils.isBlank(commodityLecturer.getLecturer())){
                    ManagerTeacher managerTeacher = managerTeacherService.selectTeacherByTeacherIdInter(commodityLecturer.getLecturer());
                    if(null != managerTeacher){
                        commodityLecturer.setLecturerName(managerTeacher.getTeacherName());
                    }
                }
            }
            //CommodityInfoVo commodityInfoVo1 = updateLecturerName(commodityInfoVo);
            String ccName = courseCategoryInfoService.selectCcNameByCcId(commodityInfoVo.getCcId());
            commodityInfoVo.setCcName(ccName);
            return commodityInfoVo;
        } else {
            throw new ServiceException( "商品不存在！");
        }
    }

    @Override
    @Transactional
    public Integer saveCommodity(CommodityInfo commodityInfo, List<CommodityLecturer> commodityLecturerList) {
        //判断商品名是否重名
        List<CommodityInfoVo> commodityInfoVos = commodityInfoMapper.checkCourseName(commodityInfo.getCcId(), commodityInfo.getCourseName().trim());
        if(null != commodityInfoVos && commodityInfoVos.size() > 0){
            throw new ServiceException( "商品名已存在！");
        }
        Integer insert = 0;
        Integer integer = commodityInfoMapper.saveCommodity(commodityInfo);
        MallCommodityLecturer mallCommodityLecturer = new MallCommodityLecturer();
        mallCommodityLecturer.setCommodityId(commodityInfo.getCommodityId());
        for(CommodityLecturer commodityLecturer : commodityLecturerList){
            mallCommodityLecturer.setClId(ApplicationUtils.getUUID());
            mallCommodityLecturer.setLecturer(commodityLecturer.getLecturer());
            insert = mallCommodityLecturerService.insert(mallCommodityLecturer);
        }
        if(integer != null && insert > 0){
            return integer;
        }else{
            throw new ServiceException( "保存失败！");
        }
    }

    @Override
    @Transactional
    public Integer updateCommodity(CommodityInfo commodityInfo, List<CommodityLecturer> commodityLecturerList,String commodityId) {
        //判断商品名是否重名
        List<CommodityInfoVo> commodityInfoVos = commodityInfoMapper.checkCourseName(commodityInfo.getCcId(), commodityInfo.getCourseName().trim());
        if(null != commodityInfoVos && commodityInfoVos.size() > 0 && !commodityId.equals(commodityInfoVos.get(0).getCommodityId())){
            throw new ServiceException( "商品名已存在！");
        }
        Integer insert = 0;
        Integer update = update(commodityInfo);
        MallCommodityLecturer mallCommodityLecturer = new MallCommodityLecturer();
        mallCommodityLecturerService.deleteByCommodityId(commodityInfo.getCommodityId());
        mallCommodityLecturer.setCommodityId(commodityInfo.getCommodityId());
        for(CommodityLecturer commodityLecturer : commodityLecturerList){
            mallCommodityLecturer.setClId(ApplicationUtils.getUUID());
            mallCommodityLecturer.setLecturer(commodityLecturer.getLecturer());
            insert = mallCommodityLecturerService.insert(mallCommodityLecturer);
        }
        if(update != null && insert > 0){
            return update;
        }else{
            throw new ServiceException( "更新失败！");
        }
    }

    @Override
    public PageInfo<CommodityInfoFileVo> listByecturer(Integer page, Integer size, String lecturer) {
        //PageHelper.startPage(page, size);
        List<CommodityInfoVo> commodityInfos = commodityInfoMapper.listByecturer();
        //PageInfo<CommodityInfoVo> pageInfo1 = new PageInfo<CommodityInfoVo>(commodityInfos);
        if (commodityInfos != null && commodityInfos.size() > 0){
            commodityInfos =  commodityInfos.stream().filter(t->(t.getCommodityLecturerList()!=null && t.getCommodityLecturerList().size()>0))
                    .filter(t->t.getCommodityLecturerList().stream().filter(m->m.getLecturer()!=null && m.getLecturer().equals(lecturer)).count()>0).collect(Collectors.toList());
            List<CommodityInfoFileVo> commodityInfoNew = new ArrayList<>();
            for(CommodityInfoVo commodityInfoVo : commodityInfos){
                List<CommodityLecturer> commodityLecturerList = commodityInfoVo.getCommodityLecturerList();
                for(CommodityLecturer commodityLecturer: commodityLecturerList){
                    if(!StringUtils.isBlank(commodityLecturer.getLecturer())){
                        ManagerTeacher managerTeacher = managerTeacherService.selectTeacherByTeacherIdInter(commodityLecturer.getLecturer());
                        if(null != managerTeacher){
                            commodityLecturer.setLecturerName(managerTeacher.getTeacherName());
                        }
                    }
                }
                //CommodityInfoVo commodityInfoVo1 = updateLecturerName(commodityInfoVo);
                File file = fileService.selectByFileId(commodityInfoVo.getPicturePath());
                File file1 = fileService.selectByFileId(commodityInfoVo.getAuditionVideoPath());
                CommodityInfoFileVo commodityInfoFileVo = MapAndObjectUtils.ObjectClone(commodityInfoVo, CommodityInfoFileVo.class);
                commodityInfoFileVo.setCommodityLecturerList(commodityInfoVo.getCommodityLecturerList());
                if(file != null){
                    commodityInfoFileVo.setPicturePathView(file.getFilePath());
                }
                if(file1 != null){
                    commodityInfoFileVo.setAuditionVideoPathView(file1.getFileName());
                }
                if(!StringUtils.isBlank(commodityInfoVo.getOrgCode())){
                    ManagerOrganization managerOrganization = managerOrganizationService.selectOrganizationByOrgCode(commodityInfoVo.getOrgCode());
                    if(null != managerOrganization && !StringUtils.isBlank(managerOrganization.getOrganizationName())){
                        commodityInfoFileVo.setOrgName(managerOrganization.getOrganizationName());
                    }
                }
                //判断是否套餐
                if(null != commodityInfoVo.getCourseType() && commodityInfoVo.getCourseType() == 1){
                    String s = commodityInfoVo.getCommodityId();
                    List<CommodityInfoFileVo> commodityInfoFileVos = fillInChild(s);
                    commodityInfoFileVo.setCommodityInfoFileVoList(commodityInfoFileVos);
                }
                commodityInfoNew.add(commodityInfoFileVo);
            }
            SubListUtil<CommodityInfoFileVo> sListUtil = new SubListUtil<>(page, size, commodityInfoNew);
            PageInfo<CommodityInfoFileVo> pageInfo = new PageInfo<CommodityInfoFileVo>(sListUtil.getList());
            pageInfo.setTotal(sListUtil.getTotal());
            pageInfo.setPages(sListUtil.getPage());
            pageInfo.setPageSize(sListUtil.getPageSize());
            return pageInfo;
        } else{
            throw new ServiceException( "该老师没有对应商品！");
        }
    }

    @Override
    public PageInfo<CommodityInfoFileVo> selectListByCommodityInfoCenterVo(Integer page, Integer size,CommodityInfoCenterVo commodityInfoCenterVo) {
        PageHelper.startPage(page, size);
        List<CommodityInfoVo> commodityInfoVos = commodityInfoMapper.selectListByCommodityInfoCenterVo(commodityInfoCenterVo);
        PageInfo<CommodityInfoVo> pageInfo1 = new PageInfo<CommodityInfoVo>(commodityInfoVos);
        if (commodityInfoVos != null && commodityInfoVos.size() > 0){
            //List<CommodityInfoVo> commodityInfoVos1 = updateVoToLecturerName(commodityInfoVos);
            List<CommodityInfoFileVo> commodityInfoFileVos = new ArrayList();
            for(CommodityInfoVo commodityInfoVo : commodityInfoVos){
                List<CommodityLecturer> commodityLecturerList = commodityInfoVo.getCommodityLecturerList();
                for(CommodityLecturer commodityLecturer: commodityLecturerList){
                    if(!StringUtils.isBlank(commodityLecturer.getLecturer())){
                        ManagerTeacher managerTeacher = managerTeacherService.selectTeacherByTeacherIdInter(commodityLecturer.getLecturer());
                        if(null != managerTeacher){
                            commodityLecturer.setLecturerName(managerTeacher.getTeacherName());
                        }
                    }
                }
                File file = fileService.selectByFileId(commodityInfoVo.getPicturePath());
                File file1 = fileService.selectByFileId(commodityInfoVo.getAuditionVideoPath());
                CommodityInfoFileVo commodityInfoFileVo = MapAndObjectUtils.ObjectClone(commodityInfoVo, CommodityInfoFileVo.class);
                commodityInfoFileVo.setCommodityLecturerList(commodityInfoVo.getCommodityLecturerList());
                if(file != null){
                    commodityInfoFileVo.setPicturePathView(file.getFilePath());
                }
                if(file1 != null){
                    commodityInfoFileVo.setAuditionVideoPathView(file1.getFileName());
                }
                commodityInfoFileVos.add(commodityInfoFileVo);
            }
            PageInfo<CommodityInfoFileVo> pageInfo = new PageInfo<CommodityInfoFileVo>(commodityInfoFileVos);
            pageInfo.setTotal(pageInfo1.getTotal());
            return pageInfo;
        } else{
            throw new ServiceException( "没有对应商品！");
        }
    }

    @Override
    @Transactional
    public String deleteListByCommodityId(List<String> commodityIdList) {
        List<String> list = new ArrayList<String>();
        for(String commodityId:commodityIdList){
            CommodityInfoVo commodityInfoVo = commodityInfoMapper.selectStatusByCommodityIdAndIsRecommend(commodityId);
            //判断推荐及上架
            if(null == commodityInfoVo || commodityInfoVo.getCommodityStatus() == 1){
                return "2";
            }
            //判断非套餐商品
            if(null != commodityInfoVo.getCourseType()){
                if(commodityInfoVo.getCourseType() != 1){
                    MallMealInfo mallMealInfo = new MallMealInfo();
                    mallMealInfo.setCommodityId(commodityId);
                    List<MallMealInfo> select = mallMealInfoService.select(mallMealInfo);
                    if(null != select && select.size() > 0){
                        for(MallMealInfo mallMealInfoNew : select){
                            CommodityInfoVo commodityInfoVo1 = commodityInfoMapper.selectStatusByCommodityId(mallMealInfoNew.getComCommodityId());
                            if(null != commodityInfoVo1){
                                throw new ServiceException( "选中商品中有被套餐引用的，不能删除！");
                            }
                        }
                    }
                }
            }else{
                throw new ServiceException( "该商品为问题商品！");
            }
            list.add(commodityInfoVo.getCommodityId());
        }
        if(list !=null && list.size() > 0){
            Integer integer = commodityInfoMapper.deleteListByCommodityId(list);
            if(integer > 0){
                return "1";
            }
        }
        return "0";
    }

    /*//从用户表查对应的老师名字，更新进去
    public CommodityInfoVo updateLecturerName(CommodityInfoVo commodityInfoVo){
        Set result = new HashSet();
        //result.add(commodityInfoVo.getLecturer());
        Map map = managerTeacherService.selectLectureNameByUserId(result);
        if(map != null){
           // commodityInfoVo.setLecturerName((String) map.get(commodityInfoVo.getLecturer()));
        }
        return commodityInfoVo;
    }*/


    //从用户表查对应的老师名字，更新进去
    /*public List<CommodityInfoVo> updateToLecturerName(List<CommodityInfo> list){
        List<String> listS = new ArrayList<String>();
        List<CommodityInfoVo> listCom = new ArrayList<CommodityInfoVo>();
        for(CommodityInfo commodityInfo : list){
            listS.add(commodityInfo.getLecturer());
        }
        Set result = new HashSet(listS);
        Map map = managerTeacherService.selectLectureNameByUserId(result);
        for(CommodityInfo commodityInfo : list){
            CommodityInfoVo commodityInfoVo = MapAndObjectUtils.ObjectClone(commodityInfo, CommodityInfoVo.class);
            commodityInfoVo.setLecturerName((String) map.get(commodityInfo.getLecturer()));
            listCom.add(commodityInfoVo);
        }

        return listCom;
    }*/

    //从用户表查对应的老师名字，更新进去
    public List<CommodityInfoVo> updateVoToLecturerName(List<CommodityInfoVo> list){
        List<String> listS = new ArrayList<String>();
        List<CommodityInfoVo> listCom = new ArrayList<CommodityInfoVo>();
        for(CommodityInfoVo commodityInfoVo : list){
            //listS.add(commodityInfoVo.getLecturer());
        }
        Set result = new HashSet(listS);
        Map map = managerTeacherService.selectLectureNameByUserId(result);
        for(CommodityInfoVo commodityInfoVo : list){
            //commodityInfoVo.setLecturerName((String) map.get(commodityInfoVo.getLecturer()));
            listCom.add(commodityInfoVo);
        }

        return listCom;
    }

    //list转map
    public Map<String, List<CommodityInfoVo>> list4Map(List<CommodityInfoVo> list) {

        Map<String, List<CommodityInfoVo>> map = new HashMap<String, List<CommodityInfoVo>>();

        if ((list != null) && (list.size() != 0)) {
            for (CommodityInfoVo test : list) {
                List<CommodityInfoVo> testList = map.get(test.getCcId());
                if (testList == null) {
                    testList = new ArrayList<CommodityInfoVo>();
                }
                testList.add(test);
                map.put(test.getCcId(), testList);
            }
        }
        return map;
    }
    //生成树状数据
    private  List<CourseCategoryCommodityVo> listGetStreeCommodityVo(List<CourseCategoryCommodityVo> list) {
        //查询出商品添加到树形分类里面
        List<CommodityInfoVo> commodityInfos = commodityInfoMapper.listByCommodityStatus();
        if(commodityInfos != null && commodityInfos.size()>0){
            for(CommodityInfoVo commodityInfoVo : commodityInfos){
                List<CommodityLecturer> commodityLecturerList = commodityInfoVo.getCommodityLecturerList();
                for(CommodityLecturer commodityLecturer: commodityLecturerList){
                    if(!StringUtils.isBlank(commodityLecturer.getLecturer())){
                        ManagerTeacher managerTeacher = managerTeacherService.selectTeacherByTeacherIdInter(commodityLecturer.getLecturer());
                        if(null != managerTeacher){
                            commodityLecturer.setLecturerName(managerTeacher.getTeacherName());
                        }
                    }
                }
            }
            //List<CommodityInfoVo> commodityInfoVos = updateToLecturerName(commodityInfos);
            Map<String, List<CommodityInfoVo>> listMap = list4Map(commodityInfos);
            List<CourseCategoryCommodityVo> treeList = new ArrayList<CourseCategoryCommodityVo>();
            for (CourseCategoryCommodityVo tree : list) {
                //找到根
                if ("0".equals(tree.getParentId())) {
                    List<CommodityInfoVo> commodityInfos1 = listMap.get(tree.getCcId());
                    tree.setCommodityInfoList(commodityInfos1);
                    treeList.add(tree);
                }
                //找到子
                for (CourseCategoryCommodityVo treeNode : list) {
                    if (treeNode.getParentId().equals(tree.getCcId())) {
                        if (tree.getChildren() == null) {
                            tree.setChildren(new ArrayList<CourseCategoryCommodityVo>());
                        }
                        List<CommodityInfoVo> commodityInfos2 = listMap.get(treeNode.getCcId());
                        treeNode.setCommodityInfoList(commodityInfos2);
                        tree.getChildren().add(treeNode);
                    }
                }
            }
            return treeList;
        } else {
            throw new ServiceException( "没有对应树状数据！");
        }
    }

    //生成树状数据
    private  List<CourseCategoryCommodityVo> listGetStreeCommodityVoByOrgCode(List<CourseCategoryCommodityVo> list,String orgCode) {
        List<CommodityInfoVo> commodityInfos = commodityInfoMapper.listByCommodityStatusByOrgCode(orgCode);
        if(commodityInfos != null && commodityInfos.size()>0){
            for(CommodityInfoVo commodityInfoVo : commodityInfos){
                List<CommodityLecturer> commodityLecturerList = commodityInfoVo.getCommodityLecturerList();
                for(CommodityLecturer commodityLecturer: commodityLecturerList){
                    if(!StringUtils.isBlank(commodityLecturer.getLecturer())){
                        ManagerTeacher managerTeacher = managerTeacherService.selectTeacherByTeacherIdInter(commodityLecturer.getLecturer());
                        if(null != managerTeacher){
                            commodityLecturer.setLecturerName(managerTeacher.getTeacherName());
                        }
                    }
                }
            }
            //List<CommodityInfoVo> commodityInfoVos = updateToLecturerName(commodityInfos);
            Map<String, List<CommodityInfoVo>> listMap = list4Map(commodityInfos);
            List<CourseCategoryCommodityVo> treeList = new ArrayList<CourseCategoryCommodityVo>();
            for (CourseCategoryCommodityVo tree : list) {
                //找到根
                if ("0".equals(tree.getParentId())) {
                    List<CommodityInfoVo> commodityInfos1 = listMap.get(tree.getCcId());
                    tree.setCommodityInfoList(commodityInfos1);
                    treeList.add(tree);
                }
                //找到子
                for (CourseCategoryCommodityVo treeNode : list) {
                    if (treeNode.getParentId().equals(tree.getCcId())) {
                        if (tree.getChildren() == null) {
                            tree.setChildren(new ArrayList<CourseCategoryCommodityVo>());
                        }
                        List<CommodityInfoVo> commodityInfos2 = listMap.get(treeNode.getCcId());
                        treeNode.setCommodityInfoList(commodityInfos2);
                        tree.getChildren().add(treeNode);
                    }
                }
            }
            return treeList;
        } else {
            throw new ServiceException( "没有对应树状数据！");
        }
    }
}