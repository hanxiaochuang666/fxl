package com.by.blcu.course.service;

import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.ServiceException;
import com.by.blcu.core.universal.IBaseService;
import com.by.blcu.course.dto.CourseModelRelation;
import com.by.blcu.course.dto.CourseModelType;
import com.by.blcu.course.model.CategoryAndModel;
import com.by.blcu.course.model.CourseViewModel;

import java.util.List;
import java.util.Set;

public interface ICourseService extends IBaseService {


    List<CourseModelType> getCourseModelInfo(Integer courseId) throws ServiceException;

    Integer addCourseInfoModelAndCategory(Integer userId, CategoryAndModel model, String orgCode) throws Exception;

    void deleteById(Integer id) throws ServiceException;

    RetResult deleteCourseById(Integer id) throws ServiceException;

    RetResult batchDeleteCourse(String[] ids) throws ServiceException;

    /**
     * 模块更新删除
     * @param model
     * @param courseModelRelations
     * @throws ServiceException
     */
    void compareListAndUpdate(CategoryAndModel model, List<CourseModelRelation> courseModelRelations) throws Exception;

    /**
     * 学生购买课程资源同步
     * @param courseIdLst
     * @param student
     * @return
     */
    RetResult syncCourseResources(List<Integer> courseIdLst,Integer student);

    /**
     * 校验课程是否允许编辑
     * courseId 课程ID
     * status   审核状态
     * @return
     * @throws Exception
     */
    RetResult verifyCourseAllowsEditing(CourseViewModel course) throws Exception;

    /**
     * 资源编辑 初始化课程审核状态
     * @param courseId
     * @return
     */
    Boolean changeCourseStatus(Integer courseId, Integer userId);

    /**
     * 判断courseId 集合是否可以上架
     * @param courseId
     * @return
     */
    boolean isCheckPass(Set<Integer> courseId);
}