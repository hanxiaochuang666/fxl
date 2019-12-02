package com.by.blcu.course.service.impl;

import com.by.blcu.core.aop.CheckToken;
import com.by.blcu.core.aop.CourseCheck;
import com.by.blcu.core.ret.ServiceException;
import com.by.blcu.core.universal.BaseServiceImpl;
import com.by.blcu.core.universal.IBaseDao;
import com.by.blcu.core.utils.MapUtils;
import com.by.blcu.core.utils.StringUtils;
import com.by.blcu.course.dao.ICatalogDao;
import com.by.blcu.course.dao.ICourseDetailDao;
import com.by.blcu.course.dto.CourseDetail;
import com.by.blcu.course.dto.TaskModel;
import com.by.blcu.course.model.TaskViewModel;
import com.by.blcu.course.service.ICourseDetailService;
import com.by.blcu.course.service.ICourseService;
import com.by.blcu.course.service.courseCheck.CourseCheckModel;
import com.by.blcu.resource.dao.ITestPaperDao;
import com.by.blcu.resource.dto.Resources;
import com.by.blcu.resource.dto.TestPaper;
import com.by.blcu.resource.model.ResourceTypeEnum;
import com.by.blcu.resource.service.IResourcesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@CheckToken
@Service("courseDetailService")
public class CourseDetailServiceImpl extends BaseServiceImpl implements ICourseDetailService {

    @Resource
    private ICourseDetailDao courseDetailDao;
    @Resource
    private IResourcesService resourcesService;
    @Resource
    private ITestPaperDao testPaperDao;
    @Resource
    private ICatalogDao catalogDao;
    @Resource
    private ICourseService courseService;

    @Override
    protected IBaseDao getDao() {
        return this.courseDetailDao;
    }

    @Override
    public List<TaskModel> getTaskList(TaskViewModel taskViewModel) throws Exception {
        List modelTypes = new ArrayList();
        modelTypes.add(taskViewModel.getModelType());
        taskViewModel.setModelTypes(modelTypes);
        return courseDetailDao.getTaskList(taskViewModel);
    }

    @Override
    @Transactional
    @CourseCheck
    public Integer addTask(TaskViewModel taskViewModel, HttpServletRequest request, CourseCheckModel courseCheckModel) throws Exception {
        Integer userId = Integer.valueOf(request.getAttribute("userId").toString());
        Integer courseId = taskViewModel.getCourseId();//课程ID

        List<String> testPapers = new ArrayList<String>();
        List<Resources> resourcesList = new ArrayList<Resources>();
        Resources resourceNew = null;
        //存在资源ID即为修改操作
        if (StringUtils.isEmpty(taskViewModel.getResourcesId())) {
            //0.验证传入试卷 是否存在关联资源信息 区分多个ID传入
            if (taskViewModel.getTestPaperId().contains(",")) {
                testPapers = Arrays.asList(taskViewModel.getTestPaperId().split(","));
            } else {
                testPapers.add(taskViewModel.getTestPaperId());
            }

            for (String testPaperId : testPapers) {
                Map<String, Object> param = MapUtils.initMap("content", testPaperId);
                List<Resources> resources = resourcesService.selectList(param);
                if (resources == null || resources.size() < 1) {
                    throw new ServiceException("试卷缺失关联资源，试卷ID：" + testPaperId);
                } else {
                    for (Resources r : resources) {
                        if (ResourceTypeEnum.TEST.getTypeCode().intValue() == r.getType().intValue()
                                || ResourceTypeEnum.TASK.getTypeCode().intValue() == r.getType().intValue()) {
                            resourcesList.add(r);
                            resourceNew = r;
                        }
                    }
                }
            }
        } else {
            resourceNew = resourcesService.selectByPrimaryKey(taskViewModel.getResourcesId());
        }

        if (resourceNew == null) {
            throw new ServiceException("试卷缺失关联资源，试卷ID：" + taskViewModel.getTestPaperId());
        }

        if (taskViewModel.getCatalogId() != 0) {
            //1.目录下 已存在资源关联
            Map<String, Object> detailParam = MapUtils.initMap("catalogId", taskViewModel.getCatalogId());
            List<CourseDetail> details = courseDetailDao.selectList(detailParam);
            if (details != null && details.size() > 0) {
                CourseDetail detailOrigin = details.get(0);
                if (detailOrigin.getResourcesId().intValue() != resourceNew.getResourcesId().intValue()) {

                    //2.更新资源名称
                   /*Catalog catalog = catalogDao.selectByPrimaryKey(taskViewModel.getCatalogId());
                    if(catalog != null){
                        resourceNew.setTitle(catalog.getName());
                        resourceNew.setUpdateTime(new Date());
                        resourceNew.setUpdateUser(userId);
                    }else{
                        throw new ServiceException("该目录不存在！目录ID：" + catalog.getCatalogId());
                    }*/

                    //3.根据资源类型清除原 资源关联
                    resourcesService.removeByResourceType(detailOrigin.getResourcesId());
                    detailOrigin.setResourcesId(resourceNew.getResourcesId());
                    detailOrigin.setUpdateUser(userId);
                    detailOrigin.setUpdateTime(new Date());
                    courseDetailDao.updateByPrimaryKey(detailOrigin);
                    /*resourcesService.updateByPrimaryKeySelective(resourceNew);*/
                }
                //移除多余 CourseDetail
                for (int i = 1; i < details.size(); i++) {
                    courseDetailDao.deleteByPrimaryKey(details.get(i).getCourseDetailId());
                }

            } else {
                //目录下 新建课程详情关联资源
                CourseDetail courseDetail = new CourseDetail();
                courseDetail.setCourseId(taskViewModel.getCourseId());
                courseDetail.setModelType(taskViewModel.getModelType());
                courseDetail.setCatalogId(taskViewModel.getCatalogId());
                courseDetail.setResourcesId(resourceNew.getResourcesId());
                courseDetail.setCreateTime(new Date());
                courseDetail.setCreateUser(userId);
                courseDetailDao.insertSelective(courseDetail);
            }

        } else {
            //判断是否为修改操作 需要传入 resourcesId 然后关联修改 testPaper
            if (StringUtils.isBlank(taskViewModel.getTaskName())) {
                //1.模块下关联作业测试
                for (Resources r : resourcesList) {
                    CourseDetail courseDetail = new CourseDetail();
                    courseDetail.setCourseId(taskViewModel.getCourseId());
                    courseDetail.setModelType(taskViewModel.getModelType());
                    courseDetail.setCatalogId(0);//表示非绑定在模块上的资源
                    courseDetail.setResourcesId(r.getResourcesId());
                    courseDetail.setCreateTime(new Date());
                    courseDetail.setCreateUser(userId);
                    courseDetailDao.insertSelective(courseDetail);
                }
            } else {
                //2.修改关联作业测试名称
                if (resourceNew != null && !StringUtils.isBlank(resourceNew.getContent())) {
                    //3.试卷相关操作 统一不可创建重名试卷
                    TestPaper testPaper = testPaperDao.selectByPrimaryKey(Integer.valueOf(resourceNew.getContent()));
                    if (testPaper != null) {
                        if (!taskViewModel.getTaskName().equals(testPaper.getName())) {
                            Map<String, Object> initMap = MapUtils.initMap("name", taskViewModel.getTaskName());
                            initMap.put("createUser", userId);
                            List<TestPaper> testPaperList = testPaperDao.selectList(initMap);
                            if(testPaperList != null && testPaperList.size() > 0){
                                for(TestPaper paper : testPaperList){
                                    if(paper.getName().equals(taskViewModel.getTaskName())){
                                        log.info("试卷名称为:【" + taskViewModel.getTaskName() + "】的试卷已经存在！");
                                        throw new ServiceException("试卷名称为:【" + taskViewModel.getTaskName() + "】的试卷已经存在！");
                                    }
                                }
                            }

                            testPaper.setName(taskViewModel.getTaskName());
                            testPaperDao.updateByPrimaryKeySelective(testPaper);
                            resourceNew.setUpdateTime(new Date());//为审核做准备
                            resourceNew.setUpdateUser(userId);
                            resourcesService.updateByPrimaryKeySelective(resourceNew);
                            /**
                             * 编辑操作不再牵扯到课程的审核操作了
                             * update by jiaodongdong at 2019.11.11
                             */
                            //courseService.changeCourseStatus(courseId, userId);
                            return resourceNew.getResourcesId();

                        }else{
                            return resourceNew.getResourcesId();
                        }

                    }else{
                        throw new ServiceException("传入试卷信息不存在！试卷ID：" + resourceNew.getContent());
                    }

                } else {
                    throw new ServiceException("传入的资源ID：" + taskViewModel.getResourcesId() + " 不存在！");
                }

            }

        }
        return resourceNew.getResourcesId();
    }

    @Override
    @Transactional
    public Integer deleteTask(TaskViewModel taskViewModel, HttpServletRequest request) throws Exception {
        Integer userId = Integer.valueOf(request.getAttribute("userId").toString());

        Map<String, Object> param = MapUtils.initMap("resourcesId", taskViewModel.getResourcesId());
        param.put("courseId", taskViewModel.getCourseId());
        List<CourseDetail> courseDetails = courseDetailDao.selectList(param);

        if(courseDetails != null && courseDetails.size() > 0){
            courseService.changeCourseStatus(taskViewModel.getCourseId(), userId);
        }else{
            return -1;
        }

        return courseDetailDao.deleteByParams(param);
    }

    @Override
    public List<TestPaper> getTestPaperList(TaskViewModel taskViewModel, Integer useType, HttpServletRequest request) throws Exception {
        Integer userId = Integer.valueOf(request.getAttribute("userId").toString());
        List modelTypes = new ArrayList();
        modelTypes.add(taskViewModel.getModelType());
        if (1 == taskViewModel.getModelType()) {
            modelTypes.add(3);
            modelTypes.add(4);
        } else {
            modelTypes.add(1);
        }
        taskViewModel.setModelTypes(modelTypes);
        List<TaskModel> taskModels = courseDetailDao.getTaskList(taskViewModel);
        List<String> testPaperIds = new ArrayList<>();
        if (taskModels != null) {
            taskModels.forEach(task -> testPaperIds.add(task.getTestPaperId()));
        }

        Map<String, Object> param = MapUtils.initMap("createUser", userId);
        param.put("courseId", taskViewModel.getCourseId());
        param.put("useType", useType);
        param.put("status", 0);//试卷状态 0可用 1编辑 2作答
        if (testPaperIds.size() > 0) {
            param.put("notEntityKeyValues", testPaperIds);//已添加课程
        }
        return testPaperDao.selectList(param);
    }


}