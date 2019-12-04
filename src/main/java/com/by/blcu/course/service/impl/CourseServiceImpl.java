package com.by.blcu.course.service.impl;

import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.ServiceException;
import com.by.blcu.core.universal.BaseServiceImpl;
import com.by.blcu.core.universal.IBaseDao;
import com.by.blcu.core.utils.MapUtils;
import com.by.blcu.core.utils.StringUtils;
import com.by.blcu.course.dao.*;
import com.by.blcu.course.dto.Course;
import com.by.blcu.course.dto.CourseDetail;
import com.by.blcu.course.dto.CourseModelRelation;
import com.by.blcu.course.dto.CourseModelType;
import com.by.blcu.course.model.CategoryAndModel;
import com.by.blcu.course.model.CourseViewModel;
import com.by.blcu.course.model.KnowledgePointNode;
import com.by.blcu.course.service.ICatalogService;
import com.by.blcu.course.service.ICourseService;
import com.by.blcu.mall.model.CommodityInfo;
import com.by.blcu.mall.service.CommodityInfoService;
import com.by.blcu.resource.dto.LearnActive;
import com.by.blcu.resource.service.ILearnActiveService;
import com.by.blcu.resource.service.IResourcesService;
import com.by.blcu.resource.service.ITestResultService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service("courseService")
@Slf4j
public class CourseServiceImpl extends BaseServiceImpl implements ICourseService {

    @Resource
    private ICourseDao courseDao;
    @Resource
    private ICourseModelTypeDao courseModelTypeDao;
    @Resource
    private ICatalogDao catalogDao;
    @Autowired
    private ICatalogService catalogService;
    @Resource
    private ICourseDetailDao courseDetailDao;
    @Resource
    private ICourseModelRelationDao courseModelRelationDao;
    @Resource
    private IResourcesService resourcesService;

    @Resource
    private CommodityInfoService commodityInfoService;

    @Resource(name = "testResultService")
    private ITestResultService testResultService;

    @Resource(name = "learnActiveService")
    private ILearnActiveService learnActiveService;

    @Override
    protected IBaseDao getDao() {
        return this.courseDao;
    }

    @Override
    public List<CourseModelType> getCourseModelInfo(Integer courseId) throws ServiceException {
        List<CourseModelType> modelList = null;
        if (StringUtils.isEmpty(courseId)) {
            modelList = courseModelTypeDao.getAllModel();
        } else {
            modelList = courseModelTypeDao.getModelByCourseId(courseId);
        }
        return modelList;
    }

    @Override
    @Transactional
    public Integer addCourseInfoModelAndCategory(Integer userId, CategoryAndModel model, String orgCode) throws Exception {
        Integer courseId = -1;

        if (StringUtils.isEmpty(model.getCourseId())) {
            //注意，课程名称不可重复
            Map<String, Object> param = MapUtils.initMap("nameReal", model.getName());
            param.put("createUser", userId);//限制创建人课程中不允许重名课程
            long selectCount = courseDao.selectCount(param);
            if (selectCount > 0) {
                throw new ServiceException("已存在同名课程：" + model.getName());
            }

            //1.创建课程表
            Course course = new Course();
            course.setName(model.getName());
            course.setCategoryOne(model.getCategoryOne());
            course.setCategoryTwo(model.getCategoryTwo());
            course.setStatus(0);    //后续使用全局变量定义    状态(0:保存1:提交审核2::审核通过3:审核未通过)
            course.setOrgCode(orgCode);     //机构 ID，已变更为字符型，待定
            course.setCreateUser(userId);
            course.setCreateTime(new Date());
            course.setUpdateUser(userId);
            course.setUpdateTime(new Date());
            int state = courseDao.insertSelective(course);
            if (state < 0)
                throw new ServiceException("课程创建入库失败！");

            //2.创建课程模块关系
            courseId = course.getCourseId();
            int insertState = -1;
            for (Integer modelId : model.getModelList()) {
                CourseModelRelation relation = new CourseModelRelation();
                relation.setCourseId(courseId);
                relation.setCourseModelType(modelId);
                insertState = courseModelRelationDao.insertSelective(relation);
            }
        } else {
            //1.课程查询
            Map<String, Object> param = MapUtils.initMap("courseId", model.getCourseId());

            List<Course> courseList = courseDao.selectList(param);
            if (courseList == null || courseList.size() == 0) {
                throw new ServiceException("该课程不存在！课程ID：" + model.getCourseId());
            }
            //2.课程更新
            Course courseNew = courseList.get(0);
            if (!courseNew.getName().equals(model.getName())) {
                Map<String, Object> nameParam = MapUtils.initMap("nameReal", model.getName());
                nameParam.put("createUser", userId);//限制创建人课程中不允许重名课程
                long selectCount = courseDao.selectCount(nameParam);
                if (selectCount > 0) {
                    throw new ServiceException("已存在同名课程：" + model.getName());
                }
            }

            courseId = courseNew.getCourseId();
            if (!StringUtils.isBlank(model.getName())) courseNew.setName(model.getName());
            if (!StringUtils.isBlank(model.getCategoryOne())) courseNew.setCategoryOne(model.getCategoryOne());
            if (!StringUtils.isBlank(model.getCategoryTwo())) courseNew.setCategoryTwo(model.getCategoryTwo());
            courseNew.setStatus(0); //编辑即变更为待审核状态
            int state = courseDao.updateByPrimaryKeySelective(courseNew);

            //3.查询课程下关联模块
            if (model.getModelList() != null && model.getModelList().size() > 0) {
                Map<String, Object> modelParam = MapUtils.initMap("courseId", model.getCourseId());
                List<CourseModelRelation> courseModelRelations = courseModelRelationDao.selectList(modelParam);
                if (courseModelRelations != null && courseModelRelations.size() > 0) {
                    compareListAndUpdate(model, courseModelRelations);
                }
            }


        }

        return courseId;
    }

    @Transactional
    public void compareListAndUpdate(CategoryAndModel model, List<CourseModelRelation> courseModelRelations) throws Exception {
        Integer courseId = model.getCourseId();
        Set<Integer> modelSet = model.getModelList().stream().collect(Collectors.toSet());
        Set<Integer> relationSet = courseModelRelations.stream().map(CourseModelRelation::getCourseModelType)
                .collect(Collectors.toSet());
        //新增模块
        for (Integer addType : modelSet) {
            if (!relationSet.contains(addType)) {
                CourseModelRelation relation = new CourseModelRelation();
                relation.setCourseId(courseId);
                relation.setCourseModelType(addType);
                int state = courseModelRelationDao.insertSelective(relation);
            }
        }
        //删除模块
        for (CourseModelRelation delType : courseModelRelations) {
            Map param = MapUtils.initMap("courseId", delType.getCourseId());
            param.put("modelType", delType.getCourseModelType());
            if (!modelSet.contains(delType.getCourseModelType())) {
                int del = courseModelRelationDao.deleteByPrimaryKey(delType.getCourseModelRelationId());
                //移除课程详情关联资源
                List<CourseDetail> courseDetails = courseDetailDao.selectList(param);
                if (courseDetails != null && courseDetails.size() > 0) {
                    for (CourseDetail detail : courseDetails) {
                        resourcesService.removeByResourceType(detail.getResourcesId());
                    }
                }
                //移除课程详情
                courseDetailDao.deleteByParams(param);

            }

        }

    }


    @Override
    public RetResult deleteCourseById(Integer id) throws ServiceException {
        //1.获取课程审核状态
        Course course = selectByPrimaryKey(id);
        int state = course.getStatus().intValue();
        if (state == 1) {
            return RetResponse.makeErrRsp("课程正在审核中，不可删除！");
        }

        //2.课程是否关联商品，商品是否存在，是否上架（是否可以定义，只要关联商品化，就不可删除）
        List<CommodityInfo> commodityInfos = commodityInfoService.selectByCourseId(String.valueOf(id));
        if (commodityInfos == null || (commodityInfos != null && commodityInfos.size() == 0)) {
            deleteById(id);
        } else {
            return RetResponse.makeErrRsp("当前课程已关联商品，不可删除！商品名称：" + commodityInfos.get(0).getCourseName());
        }
        return RetResponse.makeOKRsp();
    }

    @Override
    public RetResult batchDeleteCourse(String[] ids) throws ServiceException {
        //1.批量删除，也需要重复上面的步骤
        for (String id : ids) {
            //2.获取课程审核状态
            Course course = selectByPrimaryKey(id);
            int state = course.getStatus().intValue();
            if (state == 1) {
                return RetResponse.makeErrRsp("课程正在审核中，不可删除！");
            }

            List<CommodityInfo> commodityInfos = commodityInfoService.selectByCourseId(id);
            if (commodityInfos == null || (commodityInfos != null && commodityInfos.size() == 0)) {
                deleteById(Integer.valueOf(id));
            } else {
                return RetResponse.makeErrRsp("当前课程已关联商品，不可删除！商品名称：" + commodityInfos.get(0).getCourseName());
            }
        }
        return RetResponse.makeOKRsp();
    }

    @Override
    @Transactional
    public RetResult syncCourseResources(List<Integer> courseIdLst, Integer student) {
        Map<String, Object> initMap = MapUtils.initMap();
        for(Integer courseId:courseIdLst){
            testResultService.syncTestPaper(courseId, student);
            initMap.put("studentId",student);
            List<CourseDetail> courseId1 = courseDetailDao.selectList(MapUtils.initMap("courseId", courseId));
            KnowledgePointNode knowledgePoints = catalogService.getKnowledgePoints(courseId,null);
            Map<Integer, Integer> catalogSortMap = getCatalogSortMap(knowledgePoints);
            for (CourseDetail courseDetail : courseId1) {
                if (!catalogSortMap.containsKey(courseDetail.getCatalogId()))
                    continue;
                if (courseDetail.getCatalogId().intValue() == 0)
                    continue;
                initMap.put("courseId", courseId);
                initMap.put("courseDetailId", courseDetail.getCourseDetailId());
                List<LearnActive> list = learnActiveService.selectList(initMap);
                if (null == list || list.size() <= 0) {
                    LearnActive learnActive = new LearnActive();
                    learnActive.setCourseId(courseId);
                    learnActive.setCourseDetailId(courseDetail.getCourseDetailId());
                    learnActive.setStudentId(student);
                    learnActive.setLearnFlag(0);
                    learnActive.setSort(catalogSortMap.get(courseDetail.getCatalogId()));
                    learnActiveService.insertSelective(learnActive);
                } else {
                    LearnActive learnActive = list.get(0);
                    LearnActive learnActive1 = new LearnActive();
                    learnActive1.setSort(catalogSortMap.get(courseDetail.getCatalogId()));
                    learnActive1.setLearnActiveId(learnActive.getLearnActiveId());
                    learnActiveService.updateByPrimaryKeySelective(learnActive1);
                }
            }
        }
        return RetResponse.makeOKRsp();
    }

    @Override
    public RetResult verifyCourseAllowsEditing(CourseViewModel course) throws Exception {
        //1.校验传入参数
        if (StringUtils.isEmpty(course.getCourseId()) || StringUtils.isEmpty(course.getStatus())) {
            throw new ServiceException("课程ID与审核状态不可为空！");
        }

        //2.审核状态校验
        int state = course.getStatus(); //0:保存 1:提交审核 2:审核通过 3:审核未通过
        if (state == 1) {
            throw new ServiceException("课程正在审核中，不可编辑！");
        }

        /*//3.关联商品校验
        List<CommodityInfo> commodityInfos = commodityInfoService.selectByCourseId(String.valueOf(course.getCourseId()));
        if (commodityInfos != null && commodityInfos.size() > 0) {
            CommodityInfo commodityInfo = commodityInfos.get(0);
            if (commodityInfo.getCommodityStatus().intValue() == 1) {
                throw new ServiceException("当前课程关联商品已上架！商品名称：" + commodityInfo.getCourseName());
            }
        }*/

        return RetResponse.makeOKRsp();
    }

    @Override
    @Transactional
    public Boolean changeCourseStatus(Integer courseId, Integer userId) {
        Boolean flag = false;
        Map<String, Object> param = MapUtils.initMap("courseId", courseId);
        List<Course> courseList = courseDao.selectList(param);
        if (courseList != null && courseList.size() > 0) {
            Course course = courseList.get(0);
            if (course.getStatus().intValue() != 0) {
                course.setStatus(0);
                course.setUpdateUser(userId);
                course.setUpdateTime(new Date());
                courseDao.updateByPrimaryKey(course);
            }
            flag = true;
        }
        return flag;
    }

    @Override
    public boolean isCheckPass(Set<Integer> courseId) {
        for (Integer integer : courseId) {
            Course course = courseDao.selectByPrimaryKey(integer);
            if(null==course)
                return false;
            if(2!=course.getStatus().intValue())
                return false;
        }
        return true;
    }

    private Map<Integer, Integer> getCatalogSortMap(KnowledgePointNode knowledgePoints) {
        List<Integer> CatalogLst = new ArrayList<>();
        treeToLst(knowledgePoints.getNodes(), CatalogLst);
        Map<Integer, Integer> resMap = new HashMap<>();
        int i = 0;
        for (Integer integer : CatalogLst) {
            resMap.put(integer, ++i);
        }
        return resMap;
    }

    private void treeToLst(List<KnowledgePointNode> nodes, List<Integer> CatalogLst) {
        for (KnowledgePointNode node : nodes) {
            if (node.getNodes() != null && node.getNodes().size() > 0) {
                treeToLst(node.getNodes(), CatalogLst);
            } else {
                CatalogLst.add(node.getId());
            }
        }
    }

    @Override
    @Transactional
    public void deleteById(Integer id) throws ServiceException {

        Map<String, Object> param = MapUtils.initMap("courseId", id);

/*        //1.删除课程模块关系
        courseModelRelationDao.deleteByParams(param);

        //2.删除课程目录
        catalogDao.deleteByParams(param);

        //3.删除关联资源 - 查询关联资源
        List<CourseDetail> courseDetails = courseDetailDao.selectList(param);
        if(courseDetails != null && courseDetails.size() > 0){
            for(CourseDetail detail : courseDetails){
                resourcesService.deleteByPrimaryKey(detail.getResourcesId());
            }
        }

        //4.删除课程详情
        courseDetailDao.deleteByParams(param);*/

        //5.最后删除课程 使用逻辑删 更新删除时间
//      courseDao.deleteByPrimaryKey(id);
        courseDao.logicDeleteByPrimaryKey(id);
    }

}