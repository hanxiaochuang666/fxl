package com.by.blcu.resource.service;

import com.by.blcu.core.universal.IBaseService;
import com.by.blcu.course.dto.Course;
import com.by.blcu.course.service.courseCheck.CourseCheckModel;
import com.by.blcu.resource.dto.TestPaper;
import com.by.blcu.resource.model.TestPaperCheckModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;

public interface ITestPaperService extends IBaseService {
    /**
     * 删除试卷及试卷附属内容
     * @param id
     */
    void deleteTestPaperById(int id);

    /**
     * 批量删除试卷
     * @param testPagerIds
     */
    int deleteTestPaperBatch(String testPagerIds,int userId)throws Exception;

    /**
     * 创建新的试卷
     * @return
     */
    int createTestPaper(TestPaper testPaper);

    /**
     * 根据试卷id判断该试卷是否可以编辑
     * @param testPaperId
     * @return
     */
    boolean isEdit(int testPaperId);

    /**
     * 编辑试卷
     * @param testPaper
     * @return
     */
    int editTestPaper(TestPaper testPaper, CourseCheckModel courseCheckModel);

    /**
     * 获取试卷的检测对象
     * @param testPaperId
     * @return
     */
    TestPaperCheckModel getCheckInfo(int testPaperId,List<String> voiceLst)throws Exception;

    /**
     * 根據試卷ID查詢其綁定的課程ID
     * @param testPaperId
     * @return
     */
    Set<Integer> getCourseLstByTestPaperId(int testPaperId);

    /**
     * 试卷导出
     */
    void exporTestPaperById(HttpServletRequest httpServletRequest, HttpServletResponse response,int testPagerId);
}