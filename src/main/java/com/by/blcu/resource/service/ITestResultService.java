package com.by.blcu.resource.service;

import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.universal.IBaseService;
import com.by.blcu.resource.model.StudentCourseModel;

import java.util.List;

public interface ITestResultService extends IBaseService {
    /**
     * 同步学生作业
     * @param userId
     * @param courseId
     * @param studentId
     * @return
     */
    RetResult syncTestPaper(Integer userId, Integer courseId, Integer studentId);

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
    List<StudentCourseModel> getStudentCourseLst(Integer useType, Integer studentId, Integer courseId)throws Exception;

    /**
     *用于检测试卷的定时任务
     */
    void checkTestResultStatus();
}