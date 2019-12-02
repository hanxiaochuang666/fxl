package com.by.blcu.resource.service;

import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.universal.IBaseService;
import com.by.blcu.resource.model.StudentCourseModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;

public interface ITestResultService extends IBaseService {
    /**
     * 同步学生作业
     * @param
     * @param courseId
     * @param studentId
     * @return
     */
    RetResult syncTestPaper(Integer courseId, Integer studentId);

    /**
     * 关联作业后的回调
     * @param courseId
     * @param testPaperId
     * @return
     */
    RetResult addTestCallBack(Integer courseId,Integer testPaperId,Integer oldTestPaperId,List<Integer> studentList);

    /**
     * 学生作业测试列表查询
     * @param useType
     * @param studentId
     * @param courseId
     * @return
     */
    List<StudentCourseModel> getStudentCourseLst(Integer useType, int studentId, String studentName, Integer courseId,Boolean isMyCourse)throws Exception;

    /**
     *用于检测试卷的定时任务
     */
    void checkTestResultStatus();

    /**
     * 学生端导出试卷
     * @param httpServletRequest
     * @param response
     * @param testPagerId
     */
    void exporTestPaperById(HttpServletRequest httpServletRequest, HttpServletResponse response, int testPagerId);

    /**
     * 试卷关联修改后同步资源
     * @param courseId
     * @param insertTestPager
     * @param deletePager
     */
    void syncStudentTestPager(Integer courseId,Integer courseDetailId,Integer insertTestPager,Integer deletePager);
}