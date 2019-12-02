package com.by.blcu.course.service.courseCheck;

import com.by.blcu.course.model.CatalogModel;
import com.by.blcu.mall.model.File;
import com.by.blcu.resource.dto.TestPaperQuestion;
import com.by.blcu.resource.dto.VideoInfo;
import com.by.blcu.resource.model.FileViewModel;
import com.by.blcu.resource.model.QuestionModel;
import com.by.blcu.resource.model.VideoInfoVO;

import java.util.List;

public interface ICourseChangeCheck {
    /**
     * 新增目录判断
     * @param catalogModel
     * @param courseCheckModel
     */
    void addKnowledgePointsCheck(CatalogModel catalogModel,CourseCheckModel courseCheckModel)throws Exception;

    /**
     * 编辑目录判断
     */
    void editKnowledgePointsCheck(int pointDetailId, String name,CourseCheckModel courseCheckModel)throws Exception;

    /**
     * 试卷关联修改判断
     * @param name
     * @param testPaperLst
     */
    void optTestPaperCheck(String name, List<Integer> testPaperLst,CourseCheckModel courseCheckModel)throws Exception;

    /**
     * 视频关联修改判断
     * @param videoInfoVO
     * @param courseCheckModel
     */
    void videoCheck(VideoInfoVO videoInfoVO,CourseCheckModel courseCheckModel)throws Exception;

    /**
     * 富文本编辑判断
     * @param fileViewModel
     * @param courseCheckModel
     * @throws Exception
     */
    void saveRichTextCheck(FileViewModel fileViewModel,CourseCheckModel courseCheckModel)throws Exception;

    /**
     * 文件关联修改判断
     * @param file
     * @param courseCheckModel
     */
    void fileCheck(File file,FileViewModel fileViewModels,CourseCheckModel courseCheckModel)throws Exception;

    /**
     * 试卷试题修改判断
     */
    void saveTestPaperQuestionCheck(List<TestPaperQuestion> testPaperQuestionLst,CourseCheckModel courseCheckModel)throws Exception;

    /**
     * 试题编辑修改判断
     */
    void editQuestionCheck(QuestionModel questionModel,CourseCheckModel courseCheckModel)throws Exception;

    /**
     * 视频名称修改判断
     * @throws Exception
     */
    void editVideoInfoCheck(VideoInfo videoInfo,CourseCheckModel courseCheckModel)throws Exception;
}
