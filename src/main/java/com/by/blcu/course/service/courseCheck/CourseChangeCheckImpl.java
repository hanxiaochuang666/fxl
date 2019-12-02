package com.by.blcu.course.service.courseCheck;

import com.alibaba.fastjson.JSON;
import com.by.blcu.core.contentSecurity.ContentSecurityImpl;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.ServiceException;
import com.by.blcu.core.utils.MapUtils;
import com.by.blcu.core.utils.StringUtils;
import com.by.blcu.course.dto.AutomaticCheck;
import com.by.blcu.course.dto.CourseDetail;
import com.by.blcu.course.model.AliCheckStatusEnum;
import com.by.blcu.course.model.CatalogModel;
import com.by.blcu.course.service.IAutomaticCheckService;
import com.by.blcu.course.service.ICourseDetailService;
import com.by.blcu.mall.model.File;
import com.by.blcu.resource.dto.Question;
import com.by.blcu.resource.dto.Resources;
import com.by.blcu.resource.dto.TestPaperQuestion;
import com.by.blcu.resource.dto.VideoInfo;
import com.by.blcu.resource.model.FileViewModel;
import com.by.blcu.resource.model.QuestionModel;
import com.by.blcu.resource.model.TestPaperCheckModel;
import com.by.blcu.resource.model.VideoInfoVO;
import com.by.blcu.resource.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("courseChangeCheck")
@Slf4j
public class CourseChangeCheckImpl implements ICourseChangeCheck{

    @Resource(name = "contentSecurity")
    private ContentSecurityImpl contentSecurityImpl;

    @Resource(name="testPaperService")
    private ITestPaperService testPaperService;

    @Resource
    private IResourcesService resourcesService;

    @Resource(name="videoInfoService")
    private IVideoInfoService videoInfoService;

    @Resource
    private ICourseDetailService courseDetailService;

    @Resource(name="automaticCheckService")
    private IAutomaticCheckService automaticCheckService;

    @Autowired
    private IQuestionService questionService;

    @Resource(name="testPaperQuestionService")
    private ITestPaperQuestionService testPaperQuestionService;

    @Value("${alibaba.replaceFromUrl}")
    private String replaceFromUrl;
    @Value("${alibaba.replaceUrl}")
    private String replaceUrl;

    @Override
    public void addKnowledgePointsCheck(CatalogModel catalogModel, CourseCheckModel courseCheckModel)throws Exception{
        String name = catalogModel.getName();
        Map<Integer,String> catalogTextMap=new HashMap<>();
        catalogTextMap.put(9999,name);
        courseCheckModel.setChangeType(2);
        courseCheckModel.setContent(name);
        List<AutomaticCheck> automaticChecks=null;
        try {
            automaticChecks = contentSecurityImpl.TextCheck(catalogTextMap);
        } catch (Exception e) {
            throw  new ServiceException("修改的目录名:" + name+"审核失败");
        }
        boolean isPassAll=true;
        for (AutomaticCheck automaticCheck : automaticChecks) {
            String antispam = automaticCheck.getAntispam();
            if (AliCheckStatusEnum.NORMAL.getStatus().equals(antispam)) {
            } else {
                isPassAll=false;
                break;
            }
        }
        if(!isPassAll){
            throw  new ServiceException("修改的目录名:" + name+"不合法，请重新修改");
        }else{
            courseCheckModel.setSyncFlag(1);
        }

    }

    @Override
    public void editKnowledgePointsCheck(int pointDetailId, String name, CourseCheckModel courseCheckModel)throws Exception {
        Map<Integer,String> catalogTextMap=new HashMap<>();
        catalogTextMap.put(9999,name);
        courseCheckModel.setChangeType(2);
        courseCheckModel.setContent(name);
        List<AutomaticCheck> automaticChecks=null;
        try {
            automaticChecks = contentSecurityImpl.TextCheck(catalogTextMap);
        } catch (Exception e) {
            throw  new ServiceException("修改的目录名:" + name+"审核失败");
        }
        boolean isPassAll=true;
        for (AutomaticCheck automaticCheck : automaticChecks) {
            String antispam = automaticCheck.getAntispam();
            if (AliCheckStatusEnum.NORMAL.getStatus().equals(antispam)) {
            } else {
                isPassAll=false;
                break;
            }
        }
        if(!isPassAll){
            throw  new ServiceException("修改的目录名:【" + name+"】不合法，请重新修改");
        }else{
            courseCheckModel.setSyncFlag(1);
        }

    }

    @Override
    public void optTestPaperCheck(String name, List<Integer> testPaperLst,CourseCheckModel courseCheckModel) throws Exception{
        if(!StringUtils.isEmpty(name)){
            Map<Integer,String> catalogTextMap=new HashMap<>();
            catalogTextMap.put(9999,name);
            courseCheckModel.setChangeType(2);
            courseCheckModel.setContent(name);
            List<AutomaticCheck> automaticChecks=null;
            try {
                automaticChecks= contentSecurityImpl.TextCheck(catalogTextMap);
            } catch (Exception e) {
                throw  new ServiceException("修改的试卷名:" + name+"审核失败");
            }
            boolean isPassAll=true;
            for (AutomaticCheck automaticCheck : automaticChecks) {
                String antispam = automaticCheck.getAntispam();
                if (AliCheckStatusEnum.NORMAL.getStatus().equals(antispam)) {
                } else {
                    isPassAll=false;
                    break;
                }
            }
            if(!isPassAll){
                throw  new ServiceException("修改的试卷名:【" + name+"】不合法，请重新修改");
            }else{
                courseCheckModel.setSyncFlag(1);
            }

        }

        if(null!=testPaperLst && testPaperLst.size()>0){
            StringBuilder temp=new StringBuilder();
            List<String> voiceLst=new ArrayList<>();
            Map<Integer,String> resourcesTextMap=new HashMap<>();
            Map<String, Object> maxType = MapUtils.initMap("maxType", 1);
            boolean isPassAll=true;
            for (Integer integer : testPaperLst) {
                temp.append(integer).append(",");
                maxType.put("content", integer);
                List<Resources> resourcesList= resourcesService.selectList(maxType);
                if(null!=resourcesList) {
                    Integer checkStatus = resourcesList.get(0).getCheckStatus();
                    if(checkStatus==1 ||checkStatus==3){
                        isPassAll=false;
                    }else if(checkStatus==0){
                        TestPaperCheckModel checkInfo = testPaperService.getCheckInfo(integer, voiceLst);
                        if (null != checkInfo)
                            resourcesTextMap.put(integer, JSON.toJSONString(checkInfo));
                    } else {
                      //通过的什么也不做，在资源修改时，记得及时修改更新时间和审核状态
                    }
                }
            }
            List<AutomaticCheck> automaticCheckList4 = contentSecurityImpl.TextCheck(resourcesTextMap);
            if(null!=automaticCheckList4 && automaticCheckList4.size()>0) {
                for (AutomaticCheck automaticCheck : automaticCheckList4) {
                    String antispam = automaticCheck.getAntispam();
                    if (AliCheckStatusEnum.NORMAL.getStatus().equals(antispam)) {
                    } else {
                        isPassAll = false;
                        break;
                    }
                }
            }
            if(!isPassAll){
                throw  new ServiceException("修改的试卷不合法，请重新修改");
            }else{
                String s = temp.toString();
                courseCheckModel.setContent(s.substring(0,s.length()-1));
                courseCheckModel.setSyncFlag(0);
            }
        }
    }

    @Override
    public void videoCheck(VideoInfoVO videoInfoVO, CourseCheckModel courseCheckModel)throws Exception {
        VideoInfo videoInfo = videoInfoService.selectByPrimaryKey(videoInfoVO.getVideoInfoId());

        if(null!=videoInfo
                && !videoInfo.getBucketName().equals("ACCA")
                && !StringUtils.isEmpty(videoInfo.getUrl())){
            Integer videoInfoId = videoInfo.getVideoInfoId();
            Map<String, Object> initMap = MapUtils.initMap("type", 2);
            initMap.put("content", String.valueOf(videoInfoId));
            List<Resources> resourcesLst = resourcesService.selectList(initMap);
            if(null!=resourcesLst&& resourcesLst.size()>0){
                Resources resources = resourcesLst.get(0);
                if(2==resources.getCheckStatus() || 4==resources.getCheckStatus()){
                    return;
                }else if(1==resources.getCheckStatus() || 3==resources.getCheckStatus()){
                    throw new ServiceException("修改的视频审核不通过");
                }
            }else {
                throw new ServiceException("修改的视频资源数据错误"+videoInfoId);
            }
            List<String> videoLst=new ArrayList<>();
            videoLst.add(videoInfo.getUrl());
            List<AutomaticCheck> automaticCheckList2 = contentSecurityImpl.VideoAsyncCheck(videoLst);
            AutomaticCheck automaticCheck1 = automaticCheckList2.get(0);
            Integer courseId = videoInfoVO.getCourseId();
            Integer catalogId = videoInfoVO.getCatalogId();
            Map<String, Object> objectMap = MapUtils.initMap("courseId", courseId);
            objectMap.put("catalogId",catalogId);
            List<CourseDetail> courseDetailList = courseDetailService.selectList(objectMap);
            /**
             * bak5 存储回滚的数据表
             * bak4 存储的回滚的数据 （新增时，不需要）
             * bak3 存储的回滚时需要删除的数据
             */
            automaticCheck1.setBak5("course_detail");
            if(null!=courseDetailList && courseDetailList.size()>0)
                automaticCheck1.setBak4(JSON.toJSONString(courseDetailList.get(0)));
            CourseDetail courseDetail = new CourseDetail();
            courseDetail.setCourseId(courseId);
            courseDetail.setCatalogId(catalogId);
            courseDetail.setResourcesId(resourcesLst.get(0).getResourcesId());
            automaticCheck1.setBak3(JSON.toJSONString(courseDetail));
            automaticCheckService.insertSelective(automaticCheck1);
        }
        courseCheckModel.setSyncFlag(1);
    }

    @Override
    public void saveRichTextCheck(FileViewModel fileViewModel, CourseCheckModel courseCheckModel)throws Exception{
        String richText = fileViewModel.getRichText();
        Map<Integer,String> catalogTextMap=new HashMap<>();
        catalogTextMap.put(9999,richText);
        courseCheckModel.setChangeType(3);
        courseCheckModel.setContent(richText);
        List<AutomaticCheck> automaticChecks=null;
        try {
            automaticChecks = contentSecurityImpl.TextCheck(catalogTextMap);
        } catch (Exception e) {
            throw  new ServiceException("修改的文本:" + richText+"审核失败");
        }
        boolean isPassAll=true;
        for (AutomaticCheck automaticCheck : automaticChecks) {
            String antispam = automaticCheck.getAntispam();
            if (AliCheckStatusEnum.NORMAL.getStatus().equals(antispam)) {
            } else {
                isPassAll=false;
                break;
            }
        }
        if(!isPassAll){
            throw  new ServiceException("修改的文本:【" + richText+"】不合法，请重新修改");
        }else{
            courseCheckModel.setSyncFlag(1);
        }
    }

    @Override
    public void fileCheck(File file,FileViewModel fileViewModels, CourseCheckModel courseCheckModel)throws Exception {
        List<String> fileLst=new ArrayList<>();
        //String s = file.getFilePath().replaceFirst(replaceFromUrl, replaceUrl);
        fileLst.add(file.getFilePath());
        List<AutomaticCheck> automaticCheckList1=null;
        try {
            automaticCheckList1 = contentSecurityImpl.FileAsyncCheck(fileLst);
        } catch (Exception e) {
            throw  new ServiceException("文件:" + fileViewModels.getFileName()+"审核失败");
        }
        AutomaticCheck automaticCheck = automaticCheckList1.get(0);
        String courseId = fileViewModels.getCourseId();
        String catalogId = fileViewModels.getCatalogId();
        Map<String, Object> courseIdMap = MapUtils.initMap("courseId", Integer.valueOf(courseId));
        /**
         * bak5 存储回滚的数据表
         * bak4 存储的回滚的数据 （新增时，不需要）
         * bak3 存储的回滚时需要删除的数据
         */
        automaticCheck.setBak5("file");
        if(!"0".equals(catalogId)) {
            courseIdMap.put("catalogId", Integer.valueOf(catalogId));
            List<CourseDetail> objects1 = courseDetailService.selectList(courseIdMap);
            if (null != objects1 && objects1.size() > 0) {
                CourseDetail courseDetail = objects1.get(0);
                automaticCheck.setBak4(JSON.toJSONString(courseDetail));
            }
        }
        CourseDetail courseDetail = new CourseDetail();
        courseDetail.setCourseId(Integer.valueOf(courseId));
        courseDetail.setCatalogId(Integer.valueOf(catalogId));
        courseDetail.setBak1(file.getFileId());
        automaticCheck.setBak3(JSON.toJSONString(courseDetail));
        automaticCheckService.insertSelective(automaticCheck);
        courseCheckModel.setSyncFlag(1);
    }

    @Override
    public void saveTestPaperQuestionCheck(List<TestPaperQuestion> testPaperQuestionLst, CourseCheckModel courseCheckModel) throws Exception{
        TestPaperCheckModel temp=new TestPaperCheckModel();
        List<Map<String,String>> questionCheckLst =new ArrayList<>();
        int testpaperId=-1;
        for (TestPaperQuestion testPaperQuestion : testPaperQuestionLst) {
            if(testpaperId<0)
                testpaperId=testPaperQuestion.getTestPagerId().intValue();
            Integer questionId = testPaperQuestion.getQuestionId();
            Question question = questionService.selectByPrimaryKey(questionId);
            Map<String,String> tempMap=new HashMap<>();
            if(!StringUtils.isEmpty(question.getQuestionBody()))
                tempMap.put("questionBody", URLDecoder.decode(question.getQuestionBody(), "UTF-8"));
            if(!StringUtils.isEmpty(question.getQuestionOpt()))
                tempMap.put("questionOpt",URLDecoder.decode(question.getQuestionOpt(), "UTF-8"));
            if(!StringUtils.isEmpty(question.getQuestionAnswer()))
                tempMap.put("questionAnswer",URLDecoder.decode(question.getQuestionAnswer(), "UTF-8"));
            if(!StringUtils.isEmpty(question.getQuestionResolve()))
                tempMap.put("questionResolve",URLDecoder.decode(question.getQuestionResolve(), "UTF-8"));
            questionCheckLst.add(tempMap);
        }
        temp.setQuestionCheckLst(questionCheckLst);

        Map<Integer,String> resourcesTextMap=new HashMap<>();
        resourcesTextMap.put(testpaperId, JSON.toJSONString(temp));
        List<AutomaticCheck> automaticCheckList4 = contentSecurityImpl.TextCheck(resourcesTextMap);
        boolean isPassAll=true;
        if(null!=automaticCheckList4 && automaticCheckList4.size()>0) {
            for (AutomaticCheck automaticCheck : automaticCheckList4) {
                String antispam = automaticCheck.getAntispam();
                if (AliCheckStatusEnum.NORMAL.getStatus().equals(antispam)) {
                } else {
                    isPassAll = false;
                    break;
                }
            }
        }
        if(!isPassAll){
            throw  new ServiceException("修改的试卷不合法，请重新修改");
        }else{
            courseCheckModel.setSyncFlag(0);
        }
    }

    @Override
    public void editQuestionCheck(QuestionModel questionModel, CourseCheckModel courseCheckModel)throws Exception {
        Map<String,String> questionCheckMap =new HashMap<>();
        Map<Integer,String> resourcesTextMap=new HashMap<>();
        if(!StringUtils.isEmpty(questionModel.getQuestionBody())){
            questionCheckMap.put("questionBody", URLDecoder.decode(questionModel.getQuestionBody(), "UTF-8"));
        }
        if(!StringUtils.isEmpty(questionModel.getQuestionOpt())){
            questionCheckMap.put("questionOpt", URLDecoder.decode(questionModel.getQuestionOpt(), "UTF-8"));
        }
        if(!StringUtils.isEmpty(questionModel.getQuestionAnswer())){
            questionCheckMap.put("questionAnswer", URLDecoder.decode(questionModel.getQuestionAnswer(), "UTF-8"));
        }
        if(!StringUtils.isEmpty(questionModel.getQuestionResolve())){
            questionCheckMap.put("questionResolve", URLDecoder.decode(questionModel.getQuestionResolve(), "UTF-8"));
        }
        resourcesTextMap.put(8888,JSON.toJSONString(questionCheckMap));
        List<AutomaticCheck> automaticCheckList4 = contentSecurityImpl.TextCheck(resourcesTextMap);
        boolean isPassAll=true;
        if(null!=automaticCheckList4 && automaticCheckList4.size()>0) {
            for (AutomaticCheck automaticCheck : automaticCheckList4) {
                String antispam = automaticCheck.getAntispam();
                if (AliCheckStatusEnum.NORMAL.getStatus().equals(antispam)) {
                } else {
                    isPassAll = false;
                    break;
                }
            }
        }
        if(!isPassAll){
            throw  new ServiceException("修改的试题不合法，请重新修改");
        }else{
            courseCheckModel.setSyncFlag(0);
        }
    }

    @Override
    public void editVideoInfoCheck(VideoInfo videoInfo, CourseCheckModel courseCheckModel)throws Exception{
        Map<Integer,String> catalogTextMap=new HashMap<>();
        catalogTextMap.put(videoInfo.getVideoInfoId(),videoInfo.getVideoName());
        courseCheckModel.setChangeType(2);
        courseCheckModel.setContent(videoInfo.getVideoName());
        List<AutomaticCheck> automaticChecks=null;
        try {
            automaticChecks = contentSecurityImpl.TextCheck(catalogTextMap);
        } catch (Exception e) {
            throw  new ServiceException("修改的视频名称:" + videoInfo.getVideoName()+"审核失败");
        }
        boolean isPassAll=true;
        for (AutomaticCheck automaticCheck : automaticChecks) {
            String antispam = automaticCheck.getAntispam();
            if (AliCheckStatusEnum.NORMAL.getStatus().equals(antispam)) {
            } else {
                isPassAll=false;
                break;
            }
        }
        if(!isPassAll){
            log.info("修改的视频名称:【" + videoInfo.getVideoName()+"】不合法，请重新修改");
            throw  new ServiceException("修改的视频名称:【" + videoInfo.getVideoName()+"】不合法，请重新修改");
        }else{
            courseCheckModel.setSyncFlag(1);
        }
    }
}
