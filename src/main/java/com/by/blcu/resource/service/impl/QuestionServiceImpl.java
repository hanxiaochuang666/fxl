package com.by.blcu.resource.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.by.blcu.core.aop.CourseCheck;
import com.by.blcu.core.constant.Constants;
import com.by.blcu.core.ret.ServiceException;
import com.by.blcu.core.universal.BaseServiceImpl;
import com.by.blcu.core.universal.IBaseDao;
import com.by.blcu.core.utils.*;
import com.by.blcu.course.dao.ICatalogDao;
import com.by.blcu.course.dao.ICourseDao;
import com.by.blcu.course.dto.Catalog;
import com.by.blcu.course.dto.Course;
import com.by.blcu.course.dto.CourseDetail;
import com.by.blcu.course.model.KnowledgePointNode;
import com.by.blcu.course.service.ICourseDetailService;
import com.by.blcu.course.service.courseCheck.CourseCheckModel;
import com.by.blcu.resource.dao.*;
import com.by.blcu.resource.dto.*;
import com.by.blcu.resource.model.QuestionCountModel;
import com.by.blcu.resource.model.QuestionExcelModel;
import com.by.blcu.resource.model.QuestionModel;
import com.by.blcu.resource.service.ILearnActiveService;
import com.by.blcu.resource.service.IQuestionService;
import com.by.blcu.resource.service.IResourcesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service("questionService")
@Slf4j
public class QuestionServiceImpl extends BaseServiceImpl implements IQuestionService {

    @Resource
    private IQuestionDao questionDao;
    @Resource
    private IQuestionTypeDao questionTypeDao;
    @Resource
    private ICourseDao courseDao;
    @Resource
    private ICatalogDao catalogDao;
    @Resource
    private ITestPaperQuestionDao testPaperQuestionDao;
    @Resource
    private ITestPaperDao testPaperDao;
    @Autowired
    private ITestResultDao testResultDao;
    @Resource(name = "courseDetailService")
    private ICourseDetailService courseDetailService;
    @Autowired
    private ITestResultDetailDao testResultDetailDao;


    @Resource(name="learnActiveService")
    private ILearnActiveService learnActiveService;

    @Resource(name="resourcesService")
    private IResourcesService resourcesService;

    @Value("${excel.questionFields}")
    private String questionFields;

    @Override
    protected IBaseDao getDao() {
        return this.questionDao;
    }

    @Override
    @Transactional
    public Integer insertQuestion(QuestionModel model, HttpServletRequest httpServletRequest) throws Exception {

        int userId = Integer.valueOf(httpServletRequest.getAttribute("userId").toString());
        // 如果是完形填空题，阅读理解题或者配对题先插入小题
        StringBuilder childQuestionIds = new StringBuilder();
        String childIds = "";
        List<QuestionModel> models = new ArrayList<>();
        models = model.getModelList();
        if (!CommonUtils.listIsEmptyOrNull(models)) {
            for (QuestionModel m : models) {
                if(haveSaved(model,userId,true)){ // 判断是否重复了
                    log.info("子题已存在!!!"+model.toString());
                    throw new ServiceException("该子题已存在！"+model.getQuestionBody());
                }else{
                    childQuestionIds.append(insert(m, userId, childQuestionIds.toString(), true)).append(";");
                }
            }
            if(!StringUtils.isEmpty(childQuestionIds) && childQuestionIds.length() > 0){
                childIds = childQuestionIds.substring(0, childQuestionIds.length() - 1);
            }
            log.info("子题主键id字符串===================" + childIds);
        }
        // 插入主题
        if(haveSaved(model,userId,false)){ // 判断是否重复了
            log.info("试题已存在!!!"+model.toString());
            throw new ServiceException("该试题已存在！");
        }
        Integer fatherId = insert(model, userId, childIds, false);
        if(!StringUtils.isEmpty(childIds)){
            // 更新子题，设置父题id
            updateChild(fatherId,childIds);
        }
        return fatherId;
    }

    @Override
    @Transactional
    @CourseCheck
    public void editQuestion(QuestionModel model, HttpServletRequest httpServletRequest, CourseCheckModel courseCheckModel) throws Exception {
        checkQuestionUser(httpServletRequest,model.getQuestionId());
        int userId = Integer.valueOf(httpServletRequest.getAttribute("userId").toString());
        Question question = selectByPrimaryKey(model.getQuestionId());
        if (null == question) {
            throw new ServiceException( "试题不存在！");
        }
        Date date = new Date();
        Map<String,Object> map = new HashMap<>();
        map.put("questionId", question.getQuestionId());
        List<TestPaperQuestion> testPaperQuestions = testPaperQuestionDao.selectList(map);
        if (!CommonUtils.listIsEmptyOrNull(testPaperQuestions)) {
            Set<Integer> testPaperSet=new HashSet<>();
            for (TestPaperQuestion testPaperQuestion : testPaperQuestions) {
                Integer testPagerId = testPaperQuestion.getTestPagerId();
                testPaperSet.add(testPagerId);
            }
            //添加试卷是否上架的判断
            for (Integer testPagerId : testPaperSet) {
                /*boolean usedResources = resourcesService.isUsedMall(testPagerId, null, 0, 1);
                if (usedResources) {
                    List<TestPaper> papers = testPaperDao.selectByPrimaryKey(testPagerId);
                    String testName = "";
                    if(!CommonUtils.listIsEmptyOrNull(papers)){
                        testName = papers.get(0).getName();
                    }
                    log.info("试题所属试卷【"+testPagerId+"】关联的课程已经上架，不能编辑");
                    throw new ServiceException("试题所属试卷【" + testName + "】关联的课程已经上架，不能编辑");
                }*/
                TestPaper testPaper = testPaperDao.selectByPrimaryKey(testPagerId);
                testPaper.setUpdateTime(date);
                testPaper.setUpdateUser(userId);
                testPaper.setExportStuPath("");
                testPaperDao.updateByPrimaryKeySelective(testPaper);
                if(0==courseCheckModel.getIsUpper())
                    resourcesService.syncResources(testPagerId,null,0,1);
                /*List<TestResult> testResultLst = testResultDao.selectList(MapUtils.initMap("testPaperId", testPaper.getTestPaperId()));
                if(null!=testResultLst&&testResultLst.size()>0){
                    for (TestResult testResult : testResultLst) {
                        int i = testResult.getStatus().intValue();
                        if (i < 4 && i >= 2) {
                            //重置已批改的分数
                            testResult.setObjectiveScore(null);
                            testResult.setStatus(1);
                            List<TestResultDetail> selectList = testResultDetailDao.selectList(MapUtils.initMap("testResultId", testResult.getTestResultId()));
                            if (null != selectList && selectList.size() > 0) {
                                for (TestResultDetail testResultDetail : selectList) {
                                    testResultDetail.setScore(0);
                                    testResultDetail.setComment(null);
                                    //重置已修改题的答案
                                    if (testResultDetail.getQuestionId().intValue() == question.getQuestionId().intValue()) {
                                        testResultDetail.setGiveAnswer(null);
                                    }
                                }
                            }
                            //回退学习进度
                            Map<String, Object> objectMap = MapUtils.initMap("content", testPagerId + "");
                            objectMap.put("maxType", 1);
                            List<Resources> objects = resourcesService.selectList(objectMap);
                            if (null != objects && objects.size() > 0) {
                                int resourcesId = objects.get(0).getResourcesId().intValue();
                                List<CourseDetail> courseDetailList = courseDetailService.selectList(MapUtils.initMap("resourcesId", resourcesId));
                                if (null != courseDetailList && courseDetailList.size() > 0) {
                                    for (CourseDetail courseDetail : courseDetailList) {
                                        Map<String, Object> initMapss = MapUtils.initMap("courseId", courseDetail.getCourseId());
                                        initMapss.put("courseDetailId", courseDetail.getCourseDetailId());
                                        initMapss.put("studentId", testResult.getStudentId());
                                        List<LearnActive> objects1 = learnActiveService.selectList(initMapss);
                                        if (null != objects1 && objects1.size() > 0) {
                                            LearnActive learnActive = objects1.get(0);
                                            learnActive.setLearnFlag(0);
                                            learnActive.setLearnTime(null);
                                            learnActiveService.updateByPrimaryKeySelective(learnActive);
                                        }
                                    }
                                }
                            }

                        }
                    }
                }*/
            }
        }
        update(model, userId,false);
        updateTestPaperTime(model.getQuestionId());
        List<QuestionModel> models = model.getModelList();
        if(!CommonUtils.listIsEmptyOrNull(models)){
            for (QuestionModel questionModel : models) {
                // 判断id，如果是1表示加了一个，则执行插入
                // 否则执行更新操作
                Question fatherQuestion = questionDao.selectByPrimaryKey(model.getQuestionId());
                if("1".equals(questionModel.getStatus())){

                    if(haveSaved(questionModel,userId,true)){ // 判断是否重复了
                        log.info("试题编辑逻辑--------子题已存在!!!"+questionModel.toString());
                        throw new ServiceException("该子题已存在！");
                    }
                    Integer childId = insert(questionModel, userId, "", true);

                    fatherQuestion.setQuestionOpt(fatherQuestion.getQuestionOpt()+";"+childId);
                    try {
                        questionDao.updateByPrimaryKeySelective(fatherQuestion);
                    }catch (Exception e){
                        log.error("试题更新异常，异常信息："+e);
                        throw new ServiceException("试题更新异常,字段信息过长！(汉字最长不超过7000个，字母最长不超过50000个)");
                    }
                }else{
                    update(questionModel,userId,true);
                }
            }
        }
    }

    @Override
    public List<QuestionCountModel> selectQuestionListCount(HttpServletRequest httpServletRequest, Map<String, Object> map) throws Exception {

        List<Map<QuestionType, Integer>> questionCountList = new ArrayList<>();
        List<QuestionCountModel> questionCountModels = new ArrayList<>();
        /*if (!havePower(httpServletRequest)){ // 没有权限就只能看自己的试题
            map.put("createUserId", userId);
        }*/
        List<Question> questions = new ArrayList<>();
        getQuestionsByPoint(map, questions);
        // 根据题干,答案和试题类型去重
        questions = questions.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                new TreeSet<>(Comparator.comparing(q->q.getQuestionBody()+q.getQuestionAnswer()+q.getQuestionType()))), ArrayList::new));
        // 然后根据试题类型统计数量
        List<QuestionType> types = questionTypeDao.selectList(MapUtils.initMap("status","1"));
        if(CommonUtils.listIsEmptyOrNull(types)){
            throw new ServiceException("试题类型数据为空!!");
        }
        types.forEach(t->{
            Map m = new HashMap();
            m.put(t,0);
            questionCountList.add(m);
        });
        for (Map<QuestionType, Integer> m : questionCountList) {
            QuestionType type = m.keySet().iterator().next();
            questions.forEach(q->{
                log.info("试题分类id【{}】，试题类型【{}】",type.getQuestionTypeId(),q.getQuestionType());
                if(type.getQuestionTypeId() == q.getQuestionType()){
                    log.info("map中的计数【{}】->【{}】",type.toString(),m.get(type));
                    m.put(type,m.get(type)+1);
                }
            });
        }
        questionCountList.forEach(m->{
            QuestionType type = m.keySet().iterator().next();
            QuestionCountModel countModel = new QuestionCountModel();
            countModel = MapAndObjectUtils.ObjectClone(type,QuestionCountModel.class);
            countModel.setCount(m.get(type));
            questionCountModels.add(countModel);
        });
        return questionCountModels;
    }

    private void getQuestionsByPoint(Map<String, Object> map, List<Question> questions) {
        String points = String.valueOf(map.get("points"));
        Integer courseId = Integer.parseInt(String.valueOf(map.get("courseId")));
        if (StringUtils.isEmpty(points) || "-1".equals(points) || "null".equals(points)) {// 知识点全选
            log.info("知识点全选。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。");
            // 查询知识点
            // 根据课程id查询  知识点更新为空
            Map<String, Object> pMap = new HashMap<>();
            pMap.put("courseId", courseId);
            pMap.put("questionType", map.get("questionType"));
            pMap.put("questionBody", map.get("questionBody"));
            pMap.put("difficultyLevel", map.get("difficultyLevel"));
            questions.addAll(questionDao.selectListByCourseAndType(pMap));
        } else {
            log.info("选取部分知识点！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！");
            if(!points.contains(";")){
                points+=";";
            }
            String[] pointStr = points.split(";");
            if (pointStr.length > 0) {
                for (String s : pointStr) {
                    map.put("knowledgePoints", s);
                    List<Question> list = questionDao.selectListByPoints(map);
                    questions.addAll(list);
                }
                // 筛选必须满足所有知识点
                /*Iterator<Question> iterator = questions.iterator();
                while (iterator.hasNext()){
                    Question q = iterator.next();
                    for (String s : pointStr) {
                        if(!q.getKnowledgePoints().contains(s)){
                            iterator.remove();
                        }
                    }
                }*/
            }
        }
    }

    @Override
    public Map selectQuestionList(HttpServletRequest httpServletRequest, Map<String, Object> map) throws Exception {

        int createUserId = Integer.valueOf(httpServletRequest.getAttribute("userId").toString());
        /*if (!havePower(httpServletRequest)){// 没有权限就只能查询自己的试题
            map.put("createUserId", userId);
        }*/
        map.put("createUserId", createUserId);
        return selectByPointAndName(map);
    }

    @Override
    @Transactional
    public void importQuestion(Map<String, Object> paraMap, MultipartFile file, HttpServletRequest httpServletRequest) throws Exception {

        if (!havePower(httpServletRequest)) {
//            throw new ServiceException( "没有权限！");
        }
        ImportParams importParams = new ImportParams();
        importParams.setHeadRows(1);// 表头
        importParams.setTitleRows(0);
        // 验证字段
        importParams.setNeedVerfiy(true);
        // 验证标题是否正确
        importParams.setImportFields(questionFields.split(","));
        try {
            ExcelImportResult<QuestionExcelModel> result = ExcelImportUtil.importExcelMore(file.getInputStream(), QuestionExcelModel.class,
                    importParams);
            List<QuestionExcelModel> successList = result.getList();
            successList.removeIf(model -> StringUtils.isEmpty(model.getQuestionType()) && StringUtils.isEmpty(model.getQuestionBody())
                    && StringUtils.isEmpty(model.getQuestionOpt()) && StringUtils.isEmpty(model.getQuestionAnswer()));
            List<QuestionExcelModel> failList = result.getFailList();
            failList.removeIf(model -> StringUtils.isEmpty(model.getQuestionType()) && StringUtils.isEmpty(model.getQuestionBody())
                    && StringUtils.isEmpty(model.getQuestionOpt()) && StringUtils.isEmpty(model.getQuestionAnswer()));
            if (!CommonUtils.listIsEmptyOrNull(failList)) {
                throw new ServiceException("上传失败：" + failList.size() + "条，请检查excel内容是否规范！");
            }
            insertExcelQuestion(successList, paraMap, httpServletRequest);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("上传出错：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteQuestion(HttpServletRequest httpServletRequest, Integer id,Integer fatherId) throws Exception {

        Map<String,Object> map = new HashMap<>();

        if(-1 == fatherId){// 表示删除单题
            map.put("questionId", id);
            List<TestPaperQuestion> testPaperQuestions = testPaperQuestionDao.selectList(map);
            if (!CommonUtils.listIsEmptyOrNull(testPaperQuestions)) {
                throw new ServiceException( "该试题有绑定的试卷，暂时不能删除！");
            }
            // 如果是带子题的，那么要关联删除子题
            deleteChildQuestion(fatherId);
            // 删除单个试题
            questionDao.deleteByPrimaryKey(id);

        }else{// 删除带父题的子题
            map.put("questionId", fatherId);
            List<TestPaperQuestion> testPaperQuestions = testPaperQuestionDao.selectList(map);
            if (!CommonUtils.listIsEmptyOrNull(testPaperQuestions)) {
                throw new ServiceException( "该试题所属父题有绑定的试卷，暂时不能删除！");
            }
            Question fatherQuestion = questionDao.selectByPrimaryKey(fatherId);
            if(null == fatherQuestion ){
                throw new ServiceException("根据fateherId【"+fatherId+"】未查询到试题！");
            }
            // 删除子题
            questionDao.deleteByPrimaryKey(id);
            // 更新父题的字段
            String childs = fatherQuestion.getQuestionOpt();
            if(!StringUtils.isEmpty(childs) && childs.contains(""+id)){
                if(childs.startsWith(""+id)){
                    childs = childs.replace(""+id,"");
                    if(childs.contains(";")){
                        childs = childs.substring(1,childs.length());
                    }
                }else{
                    childs = childs.replace(";"+id,"");
                }
            }
            fatherQuestion.setQuestionOpt(childs);
            questionDao.updateByPrimaryKeySelective(fatherQuestion);
        }

    }

    // 判断是否有子题，如果有子题就删除
    private void deleteChildQuestion(Integer id){

        Question question = questionDao.selectByPrimaryKey(id);
        if(null != question){
            QuestionType type = questionTypeDao.selectByPrimaryKey(question.getQuestionType());
            if(type != null && CommonUtils.hasChildQuestion(type.getCode())){
                String childIds = question.getQuestionOpt();
                if(!StringUtils.isEmpty(childIds)){
                    if(!childIds.contains(";")){
                        childIds+=";";
                    }
                    String [] childId = childIds.split(";");
                    for (String s : childId) {
                        questionDao.deleteByPrimaryKey(Integer.valueOf(s));
                    }
                }
            }
        }
    }


    @Override
    @Transactional
    public void deleteQuestionList(HttpServletRequest httpServletRequest, String questionIdStr) throws Exception {
        int userId = Integer.valueOf(httpServletRequest.getAttribute("userId").toString());
        if(userId==-1){
            log.info("无效的用户，请注册或登录");
            throw new ServiceException("无效的用户，请注册或登录");
        }
        if(!questionIdStr.contains(";")){
            questionIdStr+=";";
        }
        String [] ids = questionIdStr.split(";");
        for (String id : ids) {
            Map<String,Object> map = new HashMap<>();
            map.put("questionId", id);
            boolean operation = CommonUtils.isOperationDao(userId, Integer.valueOf(id), questionDao);
            if(!operation){
                log.info("自己只能操作自己的试题,试题id"+id+",用户id"+userId);
                throw new ServiceException("自己只能操作自己的试题,试题id"+id+",用户id"+userId);
            }
            List<TestPaperQuestion> testPaperQuestions = testPaperQuestionDao.selectList(map);
            if (!CommonUtils.listIsEmptyOrNull(testPaperQuestions)) {
                log.info("该试题有绑定的试卷，暂时不能删除！"+id);
                throw new ServiceException( "该试题有绑定的试卷，暂时不能删除！");
            }
            deleteChildQuestion(Integer.parseInt(id));
            questionDao.deleteByPrimaryKey(Integer.parseInt(id));
        }
    }

    private int insert(QuestionModel model, int userId, String childQuestionIds, boolean isChild) throws ServiceException {

        List<QuestionType> types = new ArrayList<>();
        Map<String, Object> paraMap = new HashMap<>();
        paraMap.put("code", model.getQuestionTypeCode());
        QuestionType type = new QuestionType();
        type.setCode(model.getQuestionTypeCode());
        types = questionTypeDao.selectList(paraMap);
        if (CommonUtils.listIsEmptyOrNull(types)) {
            throw new ServiceException( "未查询到该试题类型【" + model.getQuestionTypeCode() + "】，请核实！");
        }

        Question question = new Question();
        question.setCategoryOne(model.getCategoryOne());
        question.setCategoryTwo(model.getCategoryTwo());
        question.setCourseId(model.getCourseId());
        question.setDifficultyLevel(model.getDifficultyLevel());
        question.setCreateTime(DateUtils.now());
        // 知识点如果是-1表示全部的知识点
        question.setKnowledgePoints(model.getKnowledgePoints());
        question.setOrgCode(model.getOrgCode());
        question.setCreateUser(userId);
        question.setQuestionBody(StringUtils.isEmpty(model.getQuestionBody())?model.getQuestionBody():model.getQuestionBody().trim());
        question.setQuestionAnswer(StringUtils.isEmpty(model.getQuestionAnswer())?model.getQuestionAnswer():model.getQuestionAnswer().trim());
        if (CommonUtils.hasChildQuestion(model.getQuestionTypeCode())) {
            question.setQuestionOpt(childQuestionIds);
        } else {
            question.setQuestionOpt(StringUtils.isEmpty(model.getQuestionOpt())?model.getQuestionOpt():model.getQuestionOpt().trim());
        }
        question.setQuestionResolve(StringUtils.isEmpty(model.getQuestionResolve())?model.getQuestionResolve():model.getQuestionResolve().trim());
        question.setQuestionSound(model.getQuestionSound());
        if (isChild) {
            question.setQuestionType(100 + types.get(0).getQuestionTypeId());
        } else {
            question.setQuestionType(types.get(0).getQuestionTypeId());
        }
        question.setUpdateUser(userId);
        question.setUpdateTime(DateUtils.now());
        try {
            insertSelective(question);
        }catch (Exception e){
            e.printStackTrace();
            log.error("试题插入异常，异常信息："+e);
            throw new ServiceException("数据插入异常,字段信息过长！(汉字最长不超过7000个，字母最长不超过50000个)");
        }
        log.info("主键id【{}】", question.getQuestionId());
        return question.getQuestionId();
    }

    private void updateChild(Integer fatherId,String childIds){

        String [] childId = childIds.split(";");
        for (String s : childId) {
            Question q = new Question();
            q.setQuestionId(Integer.parseInt(s));
            q.setParentQuestionId(fatherId);
            try {
                questionDao.updateByPrimaryKeySelective(q);
            }catch (Exception e){
                e.printStackTrace();
                log.error("数据更新异常，异常信息："+e);
                throw new ServiceException("试题更新异常,字段信息过长！(汉字最长不超过7000个，字母最长不超过50000个)");
            }

        }

    }

    private void update(QuestionModel model, int userId,boolean isChild) throws Exception {

        List<QuestionType> types = new ArrayList<>();
        Map<String, Object> paraMap = new HashMap<>();
        paraMap.put("code", model.getQuestionTypeCode());
        QuestionType type = new QuestionType();
        type.setCode(model.getQuestionTypeCode());
        types = questionTypeDao.selectList(paraMap);
        if (CommonUtils.listIsEmptyOrNull(types)) {
            throw new ServiceException( "未查询到该试题类型【" + model.getQuestionTypeCode() + "】，请核实！");
        }
        Question question = new Question();
        question.setQuestionId(model.getQuestionId());
        question.setCategoryOne(model.getCategoryOne());
        question.setCategoryTwo(model.getCategoryTwo());
        question.setCourseId(model.getCourseId());
        question.setDifficultyLevel(model.getDifficultyLevel());
        question.setUpdateTime(DateUtils.now());
        question.setKnowledgePoints(model.getKnowledgePoints());
        question.setOrgCode(model.getOrgCode());
        question.setQuestionBody(model.getQuestionBody());
        question.setQuestionAnswer(model.getQuestionAnswer());
        question.setQuestionOpt(model.getQuestionOpt());
        question.setQuestionResolve(model.getQuestionResolve());
        question.setQuestionSound(model.getQuestionSound());
        if(isChild){
            question.setQuestionType(types.get(0).getQuestionTypeId()+100);
        }else{
            question.setQuestionType(types.get(0).getQuestionTypeId());
        }
        question.setUpdateUser(userId);
        try {
            updateByPrimaryKeySelective(question);
        }catch (Exception e){
            e.printStackTrace();
            log.error("数据更新异常，异常信息："+e);
            throw new ServiceException("数据更新异常,字段信息过长！(汉字最长不超过7000个，字母最长不超过50000个)");
        }
    }


    private void updateTestPaperTime(int id) {

        Map<String,Object> map = new HashMap<>();
        map.put("questionId", id);
        List<TestPaperQuestion> testPaperQuestions = testPaperQuestionDao.selectList(map);
        if (!CommonUtils.listIsEmptyOrNull(testPaperQuestions)){
            testPaperQuestions.forEach(t->{
                TestPaper testPaper = testPaperDao.selectByPrimaryKey(t.getTestPagerId());
                if (null != testPaper){
                    testPaper.setUpdateTime(DateUtils.now());
                    testPaperDao.updateByPrimaryKeySelective(testPaper);
                }
            });
        }
    }

    private Map selectByPointAndName(Map<String, Object> map) throws ServiceException {

        Integer pageSize = StringUtils.isEmpty(map.get("pageSize"))?10:Integer.valueOf(String.valueOf(map.get("pageSize")));
        Integer page = StringUtils.isEmpty(map.get("page"))?1:Integer.valueOf(String.valueOf(map.get("page")));
        Long total = 0L;
        List<Question> questions = new ArrayList<>();
        String questionType = String.valueOf(map.get("questionType"));
        String difficultyLevel = String.valueOf(map.get("difficultyLevel"));
        if (StringUtils.isEmpty(questionType) || "-1".equals(questionType) || "null".equals(questionType)) { // 试题类型全选
            map.remove("questionType");
        } else {
            String typeStrs [] = null;
            if(!questionType.contains(";")){
                questionType+=";";
            }
            typeStrs = questionType.split(";");
            map.put("questionType", Arrays.asList(typeStrs));
        }
        if (StringUtils.isEmpty(difficultyLevel) || "-1".equals(difficultyLevel) || "null".equals(difficultyLevel)) { // 难度全部
            map.remove("difficultyLevel");
        }

        // 根据分类查询试题
        Map<String,Object> questionMap = new HashMap<>();
        questionMap.put("categoryOne",map.get("categoryOne"));
        questionMap.put("categoryTwo",map.get("categoryTwo"));
        questionMap.put("courseId",map.get("courseId"));
        questionMap.put("createUser",map.get("createUserId"));
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>用户id【{}】",map.get("createUserId"));
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>查询试题中根据用户查询课程的参数【{}】",map);
        List<Course> courses = courseDao.selectList(questionMap);
        if(!CommonUtils.listIsEmptyOrNull(courses)){
            for (Course cours : courses) {
                map.put("courseId",cours.getCourseId());
                getQuestionsByPoint(map, questions);
            }
        }else{
            if(StringUtils.isEmpty(map.get("categoryOne"))){
                Map<String,Object> pmap = new HashMap<>();
                pmap.put("createUser",map.get("createUserId"));
                courses = courseDao.selectList(pmap);
                if(!CommonUtils.listIsEmptyOrNull(courses)){
                    for (Course cours : courses) {
                        map.put("courseId",cours.getCourseId());
                        getQuestionsByPoint(map, questions);
                    }
                }
            }
        }

        if(questions.size() == 0){
            throw new ServiceException("该用户下没有试题!");
        }
        // 根据题干,答案和试题类型去重
        questions = questions.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                new TreeSet<>(Comparator.comparing(q->q.getQuestionBody()+q.getQuestionAnswer()+q.getQuestionType()))), ArrayList::new));
        // 查询满足所有知识点的
        total = (long) questions.size();
        // 根据主键倒序排序
//        questions.sort(Comparator.comparing(Question::getQuestionId));
        questions = questions.stream().sorted((q1,q2)->q2.getQuestionId().compareTo(q1.getQuestionId())).collect(Collectors.toList());
        // 截取数据
        int endSize =  pageSize * page;
        int startSize =  endSize - pageSize;
        if(endSize >= total){
            endSize = Math.toIntExact(total);
        }
        questions = questions.subList(startSize,endSize);
        List<QuestionModel> questionModels = changeCourseNameAndPointName(questions);
        Map<String,Object> retMap = new HashMap<>();
        retMap.put("total",total);
        retMap.put("questions",questionModels);
        return retMap;
    }

    private List<QuestionModel> changeCourseNameAndPointName(List<Question> questions) {
        List<QuestionModel> questionModels = new ArrayList<>();
        if (!CommonUtils.listIsEmptyOrNull(questions)) {
            questions.forEach(question -> {
                QuestionModel questionModel = new QuestionModel();
                questionModel = MapAndObjectUtils.ObjectClone(question, QuestionModel.class);
                if (!StringUtils.isEmpty(question.getQuestionType())) {
                    Integer typeId = question.getQuestionType();
                    if (typeId > 100) {
                        typeId -= 100;
                    }
                    QuestionType type = questionTypeDao.selectByPrimaryKey(typeId);
                    questionModel.setQuestionTypeCode(type.getCode());
                }
                questionModels.add(questionModel);

            });
        }
        for (QuestionModel model : questionModels) {
            if (CommonUtils.hasChildQuestion(model.getQuestionTypeCode())) {
                // 查询子题
                List<QuestionModel> models = new ArrayList<>();
                String childs = model.getQuestionOpt();
                if (!StringUtils.isEmpty(childs)) {
                    if (!childs.contains(";")) {
                        childs += ";";
                    }
                    String[] childStr = childs.split(";");
                    for (String s : childStr) {
                        QuestionModel questionModel = new QuestionModel();
                        Question questionTmp = questionDao.selectByPrimaryKey(Integer.valueOf(s));
                        if(null == questionTmp){
                            throw new ServiceException("根据试题id【"+s+"】未查询到子题，请联系管理员！");
                        }
                        questionModel = MapAndObjectUtils.ObjectClone(questionTmp, QuestionModel.class);
                        if (!StringUtils.isEmpty(questionTmp.getQuestionType())) {
                            Integer typeId = questionTmp.getQuestionType();
                            if (typeId > 100) {
                                typeId -= 100;
                            }
                            QuestionType type = questionTypeDao.selectByPrimaryKey(typeId);
                            questionModel.setQuestionTypeCode(type.getCode());
                        }
                        // 根据课程id查名字
                        Course course = courseDao.selectByPrimaryKey(questionModel.getCourseId());
                        questionModel.setCourseName(course.getName());
                        // 根据知识点id查知识点名称
                        String points = questionModel.getKnowledgePoints();
                        questionModel.setKnowledgePoints(getPointNameById(points));
                        try {
                            questionModel.setCreateTimeStr(DateUtils.date2String(questionModel.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        models.add(questionModel);
                    }
                }
                model.setModelList(models);
            }
            // 根据课程id查名字
            Course course = courseDao.selectByPrimaryKey(model.getCourseId());
            model.setCourseName(course.getName());
            // 根据知识点id查知识点名称
            String points = model.getKnowledgePoints();
            model.setKnowledgePoints(getPointNameById(points));
            try {
                model.setCreateTimeStr(DateUtils.date2String(model.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return questionModels;
    }

    private void checkQuestionUser(HttpServletRequest httpServletRequest,Integer questionId) throws Exception{

        int userId = Integer.valueOf(httpServletRequest.getAttribute("userId").toString());
        Map<String,Object> paraMap = new HashMap<>();
        paraMap.put("createUser", userId);
        paraMap.put("questionId", questionId);
        List<Question> questions = questionDao.selectList(paraMap);
        if (CommonUtils.listIsEmptyOrNull(questions)){
            throw new ServiceException("该用户不是试题的创建者，不能对本题进行操作！");
        }
    }

    private void insertExcelQuestion(List<QuestionExcelModel> successList, Map<String, Object> paraMap, HttpServletRequest httpServletRequest) throws Exception {

        Integer courseId = Integer.parseInt(String.valueOf(paraMap.get("courseId")));

        List<QuestionExcelModel> childQuestions = new ArrayList<>();
        List<QuestionExcelModel> singleQuestions = new ArrayList<>();
        for (QuestionExcelModel excelModel : successList) {
            if("配对题".equals(excelModel.getQuestionType()) || "完形填空题".equals(excelModel.getQuestionType())
                    || "阅读理解题".equals(excelModel.getQuestionType()) || "综合题".equals(excelModel.getQuestionType())){
                // 配对题，综合题，完形填空题，阅读理解题的序列不能为空
                if(StringUtils.isEmpty(excelModel.getQuestionSec())){
                    throw new ServiceException("试题序号为【"+excelModel.getOrder()+"】的试题报错：带子题的题型，试题序列必填");
                }
            }
            String opt = excelModel.getQuestionOpt();
            if(!StringUtils.isEmpty(opt)){
                opt = opt.replace("；",";").trim();
                excelModel.setQuestionOpt(opt);
            }
            String answer = excelModel.getQuestionAnswer();
            if(!StringUtils.isEmpty(answer)){
                answer = answer.replace("；",";").trim();
                excelModel.setQuestionAnswer(answer);
            }
        }
        // 去掉全为空的列
        successList.removeIf(model -> StringUtils.isEmpty(model.getQuestionType()) && StringUtils.isEmpty(model.getQuestionBody())
                && StringUtils.isEmpty(model.getQuestionOpt()) && StringUtils.isEmpty(model.getQuestionAnswer()));
        // 规则校验
        checkQuestionRule(successList);
        childQuestions = successList.stream().filter(s ->
                !StringUtils.isEmpty(s.getChildQuestionType()) && !CommonUtils.isSingleQuestion(s.getQuestionType()))
                .collect(Collectors.toList());
        singleQuestions = successList.stream().filter(s ->
                StringUtils.isEmpty(s.getChildQuestionType()) && CommonUtils.isSingleQuestion(s.getQuestionType()))
                .collect(Collectors.toList());
        Map<String, Object> pointMap = new HashMap<>();
        pointMap.put("courseId", courseId);
        List<Course> courses = courseDao.selectList(pointMap);
        if (CommonUtils.listIsEmptyOrNull(courses)) {
            throw new ServiceException( "未查询到该课程！");
        }
        // 独立的题导入
        insertSingleQuestion(httpServletRequest, singleQuestions, paraMap);
        // 带子题的导入
        insertChildQuestion(httpServletRequest, childQuestions, paraMap);
    }

    private void insertSingleQuestion(HttpServletRequest httpServletRequest, List<QuestionExcelModel> singleQuestions,
                                      Map<String, Object> paraMap) throws Exception {

        List<QuestionModel> singleModels = new ArrayList<>();
        String categoryOne = String.valueOf(paraMap.get("categoryOne"));
        String categoryTwo = String.valueOf(paraMap.get("categoryTwo"));
        Integer courseId = Integer.parseInt(String.valueOf(paraMap.get("courseId")));
        String orgCode = String.valueOf(paraMap.get("orgCode"));

        for (QuestionExcelModel ss : singleQuestions) {
            if(StringUtils.isEmpty(ss.getQuestionType())){
                throw new ServiceException("试题序号为【"+ss.getOrder()+"】的试题报错：试题类型不能为空！");
            }
            QuestionType type = getQuestionTypeByName(ss.getQuestionType());
            QuestionModel cModel = new QuestionModel();
            cModel.setQuestionTypeCode(type.getCode());
            cModel.setOrder(ss.getOrder());
            // 题干
            if (!StringUtils.isEmpty(ss.getQuestionBody())) {
                cModel.setQuestionBody(ss.getQuestionBody().trim());
            }else{
                throw new ServiceException("试题序号为【"+ss.getOrder()+"】的试题报错：题干不能为空！");
            }
            cModel.setCategoryOne(categoryOne);
            cModel.setCategoryTwo(categoryTwo);
            cModel.setCourseId(courseId);
            cModel.setOrgCode(orgCode);
            // 难度
            if (!StringUtils.isEmpty(ss.getDifficultyLevel())) {
                cModel.setDifficultyLevel(ss.getDifficultyLevel());
            }else{
                throw new ServiceException("试题序号为【"+ss.getOrder()+"】的试题报错：难度不能为空！");
            }
            // 解析
            if (!StringUtils.isEmpty(ss.getQuestionResolve())) {
                cModel.setQuestionResolve(ss.getQuestionResolve().trim());
            }else{
//                throw new ServiceException("试题序号为【"+ss.getOrder()+"】的试题报错：解析不能为空！");
                cModel.setQuestionResolve("略");
            }
            // 选项
            if (!StringUtils.isEmpty(ss.getQuestionOpt())) {
                if("PANDUAN".equals(type.getCode())){
                    // 判断题 默认选项
                    ss.setQuestionOpt("正确;错误");
                    JSONArray array = new JSONArray();
                        String[] opts = ss.getQuestionOpt().split(";");
                        if (opts.length > 0) {
                            for (String opt : opts) {
                                JSONObject object = new JSONObject();
                                object.put("option", StringUtils.isEmpty(opt.trim()) ? opt : opt.trim());
                                array.add(object);
                            }
                        }

                    cModel.setQuestionOpt(array.toJSONString());
                }else{
                    cModel.setQuestionOpt(getJsonOption(ss.getQuestionOpt().trim()));
                }
            }else{
                if(!"FANYI".equals(type.getCode()) && !"JISUAN".equals(type.getCode()) && !"TIANKONG".equals(type.getCode())
                        && !"WENDA".equals(type.getCode())){
                    throw new ServiceException("试题序号为【"+ss.getOrder()+"】的试题报错：选项不能为空！");
                }
            }
            // 答案
            if (!StringUtils.isEmpty(ss.getQuestionAnswer())) {
                if(("DANXUAN".equals(type.getCode()) || "TIAOCUO".equals(type.getCode())) && ss.getQuestionAnswer().contains(";")){
                    throw new ServiceException("试题序号为【"+ss.getOrder()+"】的试题报错：单选题、挑错题，答案有且只能有一个！");
                }
                if(!"FANYI".equals(type.getCode()) && !"WENDA".equals(type.getCode())
                        && !"JISUAN".equals(type.getCode())){
                    checkAnswer(ss,ss.getQuestionAnswer().trim(),ss.getQuestionOpt(),type.getCode());
                }
                cModel.setQuestionAnswer(getAnswerJson(getReplaceString(ss.getQuestionAnswer().trim()), type.getCode(),ss.getQuestionOpt()));
            }else{
                if(!"WENDA".equals(type.getCode())){
                    throw new ServiceException("试题序号为【"+ss.getOrder()+"】的试题报错：答案不能为空！");
                }
            }
            // 课程结构
            if (!StringUtils.isEmpty(ss.getKnowledgePoints())) {
                cModel.setKnowledgePoints(getPointsByName(ss.getKnowledgePoints().trim(), courseId));
            }else{
                cModel.setKnowledgePoints(getAllChildPoints(courseId));
//                throw new ServiceException("试题序号为【"+ss.getOrder()+"】的试题报错：所属课程结构不能为空！");
            }
            singleModels.add(cModel);
        }
        if (singleModels.size() > 0) {
            for (QuestionModel m : singleModels) {
                insertExcelQuestions(urlEncode(m), httpServletRequest);
            }
        }
    }

    // 检查答案是否在选项中存在
    private void checkAnswer(QuestionExcelModel model,String answer ,String option,String type){

        if("DANXUAN".equals(type) || "TIAOCUO".equals(type)){
            answer = answer.toUpperCase();
        }
        if(StringUtils.isEmpty(option)){
            return;
        }else{
            if(!"PANDUAN".equals(type)){
                StringBuilder builder = new StringBuilder();
                String[] opts = option.split("["+Constants.WORLD_KEY+"](\\.|。)");
                opts = CommonUtils.removeEmptyArray(opts);
                if (opts.length > 0) {
                    for (int i = 0; i < opts.length; i++) {
                        builder.append(Constants.WORLD_MAP.get(i)).append(".").append(opts[i].trim()).append(";");
                    }
                }
                option  = builder.toString();
            }
        }
        if(!answer.contains(";")){
            answer+=";";
        }
        String [] answers = answer.split(";");
        String [] options = option.split(";");
        if(answer.length() > 0){
            for (String s : answers) {
                boolean flag = false;
                for(String o:options){
                    o = StringUtils.isEmpty(o)?o:o.trim();
                    if(o.startsWith(s.trim())){
                        flag = true;
                    }
                }
                if(!flag){
                    throw new ServiceException("试题序号为【"+model.getOrder()+"】的试题中,答案：【"+s+"】不在选项【"+option+"】中,请核实模板内容！");
                }
            }
        }

    }

    private void insertChildQuestion(HttpServletRequest httpServletRequest, List<QuestionExcelModel> childQuestions,
                                     Map<String, Object> paraMap) throws Exception {

        List<QuestionModel> mainModels = new ArrayList<>();
        List<QuestionExcelModel> mains = new ArrayList<>();
        List<QuestionExcelModel> childs = new ArrayList<>();

        String categoryOne = String.valueOf(paraMap.get("categoryOne"));
        String categoryTwo = String.valueOf(paraMap.get("categoryTwo"));
        Integer courseId = Integer.parseInt(String.valueOf(paraMap.get("courseId")));
        String orgCode = String.valueOf(paraMap.get("orgCode"));

        if(CommonUtils.listIsEmptyOrNull(childQuestions)){
            return;
        }
        mains = childQuestions.stream().filter(c -> StringUtils.isEmpty(c.getChildQuestionType()) || "主题".equals(c.getChildQuestionType())).collect(Collectors.toList());
        childs = childQuestions.stream().filter(c -> !StringUtils.isEmpty(c.getChildQuestionType()) && !"主题".equals(c.getChildQuestionType())).collect(Collectors.toList());


        if(CommonUtils.listIsEmptyOrNull(mains)){
            throw new ServiceException("带子题型的每道大题，必须有一个主题！");
        }
        for (QuestionExcelModel s : mains) {
            List<QuestionModel> modelList = new ArrayList<>();
            if(StringUtils.isEmpty(s.getQuestionType())){
                throw new ServiceException("试题序号为【"+s.getOrder()+"】的试题报错：试题类型不能为空！");
            }
            QuestionType questionType = getQuestionTypeByName(s.getQuestionType());
            QuestionModel model = new QuestionModel();
            model.setOrder(s.getOrder());
            model.setQuestionTypeCode(questionType.getCode());
            // 题干
            if (!StringUtils.isEmpty(s.getQuestionBody())) {
                model.setQuestionBody(s.getQuestionBody().trim());
            }else{
                throw new ServiceException("试题序号为【"+s.getOrder()+"】的试题报错：题干不能为空！");
            }

            model.setCategoryOne(categoryOne);
            model.setCategoryTwo(categoryTwo);
            model.setCourseId(courseId);
            model.setOrgCode(orgCode);
            // 难度
            if (!StringUtils.isEmpty(s.getDifficultyLevel())) {
                model.setDifficultyLevel(s.getDifficultyLevel());
            }else{
                throw new ServiceException("试题序号【"+s.getOrder()+"】的试题报错：难度不能为空！");
            }
            // 解析
            if (!StringUtils.isEmpty(s.getQuestionResolve())) {
                model.setQuestionResolve(s.getQuestionResolve().trim());
            }else{
                model.setQuestionResolve("略");
            }
            if ("PEIDUI".equals(questionType.getCode())) {
                if (!StringUtils.isEmpty(s.getQuestionOpt())) {
                    model.setQuestionAnswer(getJsonOption(s.getQuestionOpt().trim()));
                }else{
                    throw new ServiceException("试题序号【"+s.getOrder()+"】的试题报错：选项不能为空！");
                }
                for (QuestionExcelModel ss : childs) {
                    if(StringUtils.isEmpty(ss.getQuestionAnswer())){
                        throw new ServiceException("试题序号【"+ss.getQuestionAnswer()+"】的试题报错：小题答案不能为空！");
                    }
                    if ("配对题".equals(ss.getQuestionType()) && s.getQuestionSec().equals(ss.getQuestionSec())) {
                        checkAnswer(ss,ss.getQuestionAnswer().trim(),s.getQuestionOpt(),"");
                        ss.setQuestionOpt(s.getQuestionOpt());
                        modelList.add(getModel(ss, categoryOne, categoryTwo, courseId, orgCode));
                    }
                }
                model.setModelList(modelList);
            }
            if ("WANXINGTIANKONG".equals(questionType.getCode())) {
                for (QuestionExcelModel ss : childs) {
                    if ("完形填空题".equals(ss.getQuestionType()) && s.getQuestionSec().equals(ss.getQuestionSec())) {
                        checkAnswer(ss,ss.getQuestionAnswer(),ss.getQuestionOpt(),"DANXUAN");
                        modelList.add(getModel(ss, categoryOne, categoryTwo, courseId, orgCode));
                    }
                }
                model.setModelList(modelList);
            }
            if ("YUEDULIJIE".equals(questionType.getCode())) {
                for (QuestionExcelModel ss : childs) {
                    if ("阅读理解题".equals(ss.getQuestionType()) && s.getQuestionSec().equals(ss.getQuestionSec())) {
                        checkAnswer(ss,ss.getQuestionAnswer(),ss.getQuestionOpt(),"DANXUAN");
                        modelList.add(getModel(ss, categoryOne, categoryTwo, courseId, orgCode));
                    }
                }
                model.setModelList(modelList);
            }
            if ("ZONGHE".equals(questionType.getCode())) {
                for (QuestionExcelModel ss : childs) {
                    if ("综合题".equals(ss.getQuestionType()) && s.getQuestionSec().equals(ss.getQuestionSec())) {
                        checkAnswer(ss,ss.getQuestionAnswer(),ss.getQuestionOpt(),"DANXUAN");
                        modelList.add(getModel(ss, categoryOne, categoryTwo, courseId, orgCode));
                    }
                }
                model.setModelList(modelList);
            }
            // 课程结构
            if (!StringUtils.isEmpty(s.getKnowledgePoints())) {
                model.setKnowledgePoints(getPointsByName(s.getKnowledgePoints(), courseId));
            }else{
                model.setKnowledgePoints(getAllChildPoints(courseId));
//                throw new ServiceException("试题序号为【"+s.getOrder()+"】的试题报错：所属课程结构不能为空！");
            }
            mainModels.add(model);
        }
        if (mainModels.size() > 0) {
            for (QuestionModel m : mainModels) {
                insertExcelQuestions(urlEncode(m), httpServletRequest);
            }
        }
    }

    // 获取子题的model
    private QuestionModel getModel(QuestionExcelModel ss, String categoryOne, String categoryTwo,
                                   Integer courseId, String orgCode) throws Exception {
        QuestionType type = getQuestionTypeByName(ss.getChildQuestionType());
        QuestionModel cModel = new QuestionModel();
        if(!"DANXUAN".equals(type.getCode())){
            throw new ServiceException("子题序号为：【"+ss.getOrder()+"】的试题报错：子题只能是单选题！");
        }
        cModel.setQuestionTypeCode(type.getCode());
        cModel.setQuestionBody(ss.getQuestionBody());
        cModel.setCategoryOne(categoryOne);
        cModel.setCategoryTwo(categoryTwo);
        cModel.setCourseId(courseId);
        cModel.setOrgCode(orgCode);
//        cModel.setDifficultyLevel(ss.getDifficultyLevel());
        if(!StringUtils.isEmpty(ss.getQuestionResolve())){
            cModel.setQuestionResolve(ss.getQuestionResolve().trim());
        }else{
            cModel.setQuestionResolve("略");
            /*if(!"配对题".equals(ss.getQuestionType())){
                throw new ServiceException("子题序号为【"+ss.getOrder()+"】的试题报错：解析不能为空！");
            }*/
        }
        if (!StringUtils.isEmpty(ss.getQuestionOpt())) {
            if(!"配对题".equals(ss.getQuestionType())){
                cModel.setQuestionOpt(getJsonOption(ss.getQuestionOpt().trim()));
            }
        }else{
            if(!"配对题".equals(ss.getQuestionType())){
                throw new ServiceException("子题序号为【"+ss.getOrder()+"】的试题报错：选项不能为空！");
            }
        }
        if (!StringUtils.isEmpty(ss.getQuestionAnswer())) {
            if(("DANXUAN".equals(type.getCode()) || "TIAOCUO".equals(type.getCode())) && ss.getQuestionAnswer().contains(";")){
                throw new ServiceException("子题序号为【"+ss.getOrder()+"】的试题报错：单选题,挑错题，答案只能有一个！");
            }
            cModel.setQuestionAnswer(getAnswerJson(getReplaceString(ss.getQuestionAnswer().trim()), type.getCode(),ss.getQuestionOpt()));
        }else{
            throw new ServiceException("子题序号为【"+ss.getOrder()+"】的试题报错：子题答案不能为空！");
        }
        if (!StringUtils.isEmpty(ss.getKnowledgePoints())) {
//            cModel.setKnowledgePoints(getPointsByName(ss.getKnowledgePoints(), courseId));
        }
        return cModel;
    }


    private QuestionType getQuestionTypeByName(String name) throws ServiceException {
        List<QuestionType> types = new ArrayList<>();
        Map<String, Object> paraMap = new HashMap<>();
        paraMap.put("name", name);
        types = questionTypeDao.selectList(paraMap);
        if (CommonUtils.listIsEmptyOrNull(types)) {
            throw new ServiceException( "未查询到该试题类型【" + name + "】，请核实！");
        }
        return types.get(0);
    }

    private String getAnswerJson(String answer, String questionType,String option) {

        String answerS = "";
        StringBuilder answerStr = new StringBuilder();
        switch (questionType) {
            case "TIANKONG":
                String[] ans = answer.split(";");
                if (ans.length > 0) {
                    for (String an : ans) {
                        answerStr.append(StringUtils.isEmpty(an)?an:an.trim()).append("#&&&#");
                    }
                }
                answerS = answerStr.toString().substring(0, answerStr.length() - 5);
                break;
            case "DUOXUAN":
            case "XUANCITIANKONG":
                answer = answer.toUpperCase();
                StringBuilder builders = new StringBuilder();
                String[] options = option.split("["+Constants.WORLD_KEY+"]\\.");
                options = CommonUtils.removeEmptyArray(options);
                if (options.length > 0) {
                    for (int i = 0; i < options.length; i++) {
                        builders.append(Constants.WORLD_MAP.get(i)).append(".").append(options[i].trim()).append(";");
                    }
                }
                option  = builders.toString();
                String [] optArrys = option.split(";");


                String[] anss = answer.split(";");
                if (anss.length > 0) {
                    for (String an : anss) {
                        for(String o:optArrys){
                            o = StringUtils.isEmpty(o)?o:o.trim();
                            if(o.startsWith(an)){
                                an = o;
                                answerStr.append(an).append("#&&&#");
                                break;
                            }
                        }
                    }
                }
                answerS = answerStr.toString().substring(0, answerStr.length() - 5);
                break;
            case "DANXUAN":
            case "TIAOCUO":
                answer = answer.toUpperCase();
                StringBuilder builder = new StringBuilder();
                String[] opts = option.split("["+Constants.WORLD_KEY+"]\\.");
                opts = CommonUtils.removeEmptyArray(opts);
                if (opts.length > 0) {
                    for (int i = 0; i < opts.length; i++) {
                        builder.append(Constants.WORLD_MAP.get(i)).append(".").append(opts[i].trim()).append("|");
                    }
                }
                option  = builder.toString();
                String [] optArry = option.split("\\|");
                for(String o:optArry){
                    o = StringUtils.isEmpty(o)?o:o.trim();
                    if(o.startsWith(answer)){
                        answer = o;
                        break;
                    }
                }
                answerS = answerStr.append(answer).toString();
                break;
            default:
                answerS = answerStr.append(StringUtils.isEmpty(answer)?answer:answer.trim()).toString();

        }
        return answerS;
    }

    private String getJsonOption(String option) {

        JSONArray array = new JSONArray();
        String[] opts = option.split("[" + Constants.WORLD_KEY + "]\\.");
        opts = CommonUtils.removeEmptyArray(opts);
        if (opts.length > 0) {
            for (int i = 0; i < opts.length; i++) {
                JSONObject object = new JSONObject();
                object.put("option", Constants.WORLD_MAP.get(i) + "." + opts[i].trim());
                array.add(object);
            }
        }
        log.info("选项转换成JSON格式=================" + array.toJSONString());
        return array.toJSONString();
    }


    // 获取目录下所有子节点
    private String getAllChildPoints(Integer courseId) {

        Map<String,Object> pMap = new HashMap<>();
        pMap.put("courseId",courseId);
        List<Catalog> catalogs = catalogDao.selectChildNode(pMap);
        if(CommonUtils.listIsEmptyOrNull(catalogs)){
            throw new ServiceException("课程下没有目录，请先建立课程目录！");
        }
        StringBuilder ids = new StringBuilder();
        for (Catalog catalog : catalogs) {
            ids.append(catalog.getCatalogId()).append(";");
        }
        String retName = ids.toString();
        if (retName.length() > 0) {
            retName = getReplaceString(retName);
        }
        log.info("转换后的知识点主键列表=================" + retName);
        return retName;

    }

    private String getPointsByName(String pointsName, Integer courseId) {

        log.info("知识点列表=================" + pointsName);
        pointsName = getReplaceString(pointsName);
        if(!pointsName.contains(";")){
            pointsName+=";";
        }
        StringBuilder names = new StringBuilder();
        Set<Integer> ids = new HashSet<>();
        String[] points = pointsName.split(";");
        Map<String, Object> paraMap = new HashMap<>();
        if (points.length > 0) {
            for (String pointName : points) {
                List<Catalog> details = new ArrayList<>();
                paraMap.put("courseId", courseId);
                paraMap.put("name", StringUtils.isEmpty(pointName)?pointName:pointName.trim());
                details = catalogDao.selectList(paraMap);
                if (!CommonUtils.listIsEmptyOrNull(details)) {
                    List<KnowledgePointNode> nodes = new ArrayList<>();
                    Map<String, Object> pMap = new HashMap<>();
                    pMap.put("_sort_line", "sort");
                    pMap.put("parentId", details.get(0).getCatalogId());
                    pMap.put("_order_", "ASC");
                    pMap.put("courseId", courseId);
                    KnowledgePointNode nodeTmp = new KnowledgePointNode();
                    nodeTmp.setId(details.get(0).getCatalogId());
                    nodeTmp.setName(details.get(0).getName());
                    nodeTmp.setSort(details.get(0).getSort());
                    nodes.add(nodeTmp);
                    nodes = getChildNodes(nodes,pMap);
                    for (KnowledgePointNode node : nodes) {
                        List<KnowledgePointNode> list = node.getNodes();
                        // 一级目录
                        if(CommonUtils.listIsEmptyOrNull(list)){
                            ids.add(node.getId());
                        }else{
                            for (KnowledgePointNode pointNode : list) {
                                // 二级目录
                                List<KnowledgePointNode> list2 = pointNode.getNodes();
                                if(CommonUtils.listIsEmptyOrNull(list2)) {
                                    ids.add(node.getId());
                                }else{
                                    for (KnowledgePointNode knowledgePointNode : list2) {
                                        // 三级目录
                                        ids.add(node.getId());
                                    }
                                }
                            }
                        }
                    }
                    for (Integer id : ids) {
                        names.append(id).append(";");
                    }
                }else{
                    throw new ServiceException("根据课程结构【"+pointName+"】未查询到对应目录，请确认模板是否填写正确或者课程是否匹配！");
                }
            }
        }
        String retName = names.toString();
        if (retName.length() > 0) {
            retName = getReplaceString(retName);
        }
        log.info("转换后的知识点主键列表=================" + retName);
        return retName;

    }

    private static String getReplaceString(String pointsName) {
        if(!StringUtils.isEmpty(pointsName)){
            pointsName = pointsName.replace("；",";").replaceAll(";+",";");
            // 去重
            if(!StringUtils.isEmpty(pointsName) && pointsName.contains(";")){
                List<String> pointsList = CollectionUtils.arrayToList(pointsName.split(";"));
                if(!CommonUtils.listIsEmptyOrNull(pointsList)){
                    List<String> newList = CommonUtils.removeStringListDupli(pointsList);
                    StringBuilder points = new StringBuilder();
                    for (String s : newList) {
                        points.append(";").append(s);
                    }
                    pointsName = points.toString();
                }
            }
            while(pointsName.startsWith(";")){
                pointsName = pointsName.substring(1,pointsName.length());
            }
            while (pointsName.endsWith(";")){
                pointsName = pointsName.substring(0,pointsName.length()-1);
            }
        }
        return pointsName;
    }

    private String getPointNameById(String points) {
        StringBuilder pointsName = new StringBuilder();
        String retName = "";
        if(!StringUtils.isEmpty(points)){
            if(!points.contains(";")){
                points+=";";
            }
            String [] pointsStr = points.split(";");
            for (String s : pointsStr) {
                Catalog catalog = catalogDao.selectByPrimaryKey(Integer.valueOf(s));
                if (catalog != null){
                    pointsName.append(catalog.getName()).append(";");
                }
            }
            retName = pointsName.toString();
            if (retName.length() > 0) {
                retName = retName.substring(0, retName.length() - 1);
            }
        }
        return retName;
    }

    private List<KnowledgePointNode> getChildNodes(List<KnowledgePointNode> nodes,Map<String, Object> paraMap) {

        List<Catalog> lists = new ArrayList<>();
        lists = catalogDao.selectList(paraMap);
        if (!CommonUtils.listIsEmptyOrNull(lists)) {
            lists.forEach(catalog -> {
                KnowledgePointNode nodeTmp = new KnowledgePointNode();
                nodeTmp.setId(catalog.getCatalogId());
                nodeTmp.setName(catalog.getName());
                nodeTmp.setSort(catalog.getSort());
                nodeTmp.setCatalogStatus(catalog.getStatus());
                paraMap.put("_sort_line", "sort");
                paraMap.put("parentId", catalog.getCatalogId());
                paraMap.put("_order_", "ASC");
                paraMap.put("courseId", catalog.getCourseId());
                List<KnowledgePointNode> list = getChildNodes(nodes,paraMap);
                if(!CommonUtils.listIsEmptyOrNull(list)){
                    nodeTmp.setNodes(list);
                }
                nodes.add(nodeTmp);
            });
        }
        return nodes;
    }

    /**
     * 校验导入的试题模板是否有问题
     * @param successList 导入的数据列表
     * @throws ServiceException
     */
    private void checkQuestionRule(List<QuestionExcelModel> successList) throws ServiceException{

        for (QuestionExcelModel model : successList) {
            if(StringUtils.isEmpty(model.getOrder())){
                throw new ServiceException("试题序号不能为空，请检查导入模板！");
            }
            // 替换选项中的中文的分号
            if (!StringUtils.isEmpty(model.getQuestionOpt()) && (model.getQuestionOpt().contains(";") || model.getQuestionOpt().contains("；"))){
                String opt = model.getQuestionOpt().replace("；",";");
                while(opt.startsWith(";")){
                    opt = opt.substring(1,opt.length());
                }
                while (opt.endsWith(";")){
                    opt = opt.substring(0,opt.length()-1);
                }
                model.setQuestionOpt(opt);
            }
            // 替换答案中的中文的分号
            if (!StringUtils.isEmpty(model.getQuestionAnswer()) && (model.getQuestionAnswer().contains(";") || model.getQuestionAnswer().contains("；"))){
                String answer = model.getQuestionAnswer().replace("；",";");
                while(answer.startsWith(";")){
                    answer = answer.substring(1,answer.length());
                }
                while (answer.endsWith(";")){
                    answer = answer.substring(0,answer.length()-1);
                }
                model.setQuestionAnswer(answer);
            }
            // 替换课程结构中的中文分号以及前后分号
            if (!StringUtils.isEmpty(model.getKnowledgePoints()) && (model.getKnowledgePoints().contains(";") || model.getKnowledgePoints().contains("；"))){
                String points = getReplaceString(model.getKnowledgePoints());
                model.setKnowledgePoints(points);
            }
        }
        List<QuestionExcelModel> childQuestions = new ArrayList<>();
        List<QuestionExcelModel> singleQuestions = new ArrayList<>();
        childQuestions = successList.stream().filter(s ->
                !StringUtils.isEmpty(s.getChildQuestionType()) || "配对题".equals(s.getQuestionType()) || "阅读理解题".equals(s.getQuestionType())
                        || "完形填空题".equals(s.getQuestionType()) || "综合题".equals(s.getQuestionType()))
                .collect(Collectors.toList());
        singleQuestions = successList.stream().filter(s ->
                StringUtils.isEmpty(s.getChildQuestionType()) && !"配对题".equals(s.getQuestionType()) && !"阅读理解题".equals(s.getQuestionType())
                        || "完形填空题".equals(s.getQuestionType()) || "综合题".equals(s.getQuestionType()))
                .collect(Collectors.toList());
        // 校验单题的规则
        for (QuestionExcelModel singleQuestion : singleQuestions) {
            if("选词填空题".equals(singleQuestion.getQuestionType()) || "填空题".equals(singleQuestion.getQuestionType())){
                String questionBody = singleQuestion.getQuestionBody();
                if(StringUtils.isEmpty(questionBody)){
                    throw new ServiceException("序号为【"+singleQuestion.getOrder()+"】的试题：题干不能为空！");
                }
                questionBody = questionBody.replace("()","<input />").replace("（）","<input />")
                        .replace("(）","<input />").replace("（)","<input />");
                if(!questionBody.contains("<input />")){
                    throw new ServiceException("序号为【"+singleQuestion.getOrder()+"】的试题：题干中没有填空（），请检查模板！");
                }
                Pattern pattern = Pattern.compile("<input />");
                Matcher matcher = pattern.matcher(questionBody);

                int bodys = 0;
                while(matcher.find()) {
                    bodys++;
                }
                String answer = singleQuestion.getQuestionAnswer();
                if(!answer.contains(";")){
                    answer+=";";
                }
                answer = answer.replaceAll(";+",";");
                while(answer.startsWith(";")){
                    answer = answer.substring(1,answer.length());
                }
                while (answer.endsWith(";")){
                    answer = answer.substring(0,answer.length()-1);
                }
                String [] answers = answer.split(";");
                if(answers.length != bodys){
                    throw new ServiceException("序号为【"+singleQuestion.getOrder()+"】的试题：填空数量与答案数量不一致，填空数量【"+bodys+"】，答案数量【"+answers.length+"】！");
                }
            }
        }
        // 校验带子题的规则
        for (QuestionExcelModel childQuestion : childQuestions) {
            if(StringUtils.isEmpty(childQuestion.getQuestionType())){
                throw new ServiceException("序号为【"+childQuestion.getOrder()+"】的试题：父题型不能为空！");
            }
            if(StringUtils.isEmpty(childQuestion.getChildQuestionType())){
                throw new ServiceException("序号为【"+childQuestion.getOrder()+"】的试题：子题型不能为空！");
            }
            if(!"配对题".equals(childQuestion.getQuestionType()) && !"阅读理解题".equals(childQuestion.getQuestionType())
                    && !"完形填空题".equals(childQuestion.getQuestionType()) && !"综合题".equals(childQuestion.getQuestionType())){
                throw new ServiceException("序号为【"+childQuestion.getOrder()+"】的试题：只有配对题，综合题，阅读理解题，完形填空题有子题型！");
            }
            if(StringUtils.isEmpty(childQuestion.getQuestionSec())){
                throw new ServiceException("序号为【"+childQuestion.getOrder()+"】的试题：试题序列不能为空，同一试题主题与子题试题序列应保持一致！");
            }
            if(!StringUtils.isEmpty(childQuestion.getChildQuestionType()) && !"主题".equals(childQuestion.getChildQuestionType())){
                if(("阅读理解题".equals(childQuestion.getQuestionType()) || "完形填空题".equals(childQuestion.getQuestionType()) || "综合题".equals(childQuestion.getQuestionType()))
                        && (StringUtils.isEmpty(childQuestion.getQuestionAnswer()) || StringUtils.isEmpty(childQuestion.getQuestionOpt()))){
                    throw new ServiceException("序号为【"+childQuestion.getOrder()+"】的试题：选项和答案必填！");
                }
                if("配对题".equals(childQuestion.getQuestionType()) && (StringUtils.isEmpty(childQuestion.getQuestionAnswer()) || StringUtils.isEmpty(childQuestion.getQuestionBody()))){
                    throw new ServiceException("序号为【"+childQuestion.getOrder()+"】的试题：题干和答案必填！");
                }
            }
        }
    }

    private boolean havePower(HttpServletRequest httpServletRequest) throws Exception {

        boolean havePower = false;
        int userId = Integer.valueOf(httpServletRequest.getAttribute("userId").toString());
        // TODO 调接口校验有没有权限

        return havePower;
    }

    /**
     *
     * @param m 试题模型
     * @param userId 用户id
     * @param isChild 是否子标题
     * @return
     */
    private boolean haveSaved(QuestionModel m,Integer userId,boolean isChild){

        List<QuestionType> types = new ArrayList<>();
        Map<String, Object> paraMap = new HashMap<>();
        paraMap.put("code", m.getQuestionTypeCode());
        types = questionTypeDao.selectList(paraMap);
        if (CommonUtils.listIsEmptyOrNull(types)) {
            throw new ServiceException( "未查询到该试题类型【" + m.getQuestionTypeCode() + "】，请核实！");
        }
        // 试题的题干，选项，试题类型都相同就表示重复了
        String questionBody = m.getQuestionBody();
        String questionOpt = m.getQuestionOpt();
        Map<String, Object> paraMap1 = new HashMap<>();
        if(!StringUtils.isEmpty(questionBody)){
            questionBody = questionBody.trim();
        }
        if(!StringUtils.isEmpty(questionOpt)){
            questionOpt = questionOpt.trim();
        }
        paraMap1.put("questionBody", questionBody);
        paraMap1.put("questionOpt", questionOpt);
        if(isChild){
            paraMap1.put("questionType",types.get(0).getQuestionTypeId()+100);
        }else{
            paraMap1.put("questionType",types.get(0).getQuestionTypeId());
        }
        paraMap1.put("createUserId", userId);
        paraMap1.put("courseId", m.getCourseId());
        List<Question> questions = questionDao.selectList(paraMap1);
        if(!CommonUtils.listIsEmptyOrNull(questions)){
            return true;
        }
        return false;
    }

    private QuestionModel urlEncode(QuestionModel model) {
        // 进行url编码
        String questionBody = model.getQuestionBody();
        if(StringUtils.isEmpty(questionBody)){
            questionBody = "<p><br></p>";
        }
        questionBody = questionBody.replace("()","<input />").replace("（）","<input />")
        .replace("(）","<input />").replace("（)","<input />");
        try {
            // 对题干进行url编码
            questionBody = URLEncoder.encode(questionBody, "UTF-8").replace("+","%20");
            model.setQuestionBody(questionBody);
            // 对答案进行url编码
            String questionAnswer = model.getQuestionAnswer();
            if (!StringUtils.isEmpty(questionAnswer)) {
                model.setQuestionAnswer(URLEncoder.encode(questionAnswer.trim(), "UTF-8").replace("+","%20"));
            }
            // 对解析进行url编码
            String questionResolve = model.getQuestionResolve();
            if (!StringUtils.isEmpty(questionResolve)) {
                model.setQuestionResolve(URLEncoder.encode(questionResolve.trim(), "UTF-8").replace("+","%20"));
            }
            // 对选项进行url编码
            String opt = model.getQuestionOpt();
            if (!StringUtils.isEmpty(opt)) {
                model.setQuestionOpt(URLEncoder.encode(opt.trim(), "UTF-8").replace("+","%20"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        List<QuestionModel> questionModels = model.getModelList();
        if (!CommonUtils.listIsEmptyOrNull(questionModels)) {
            questionModels.forEach(this::urlEncode);
        }
        return model;
    }

    private QuestionModel urlDecode(QuestionModel model) {
        // 进行url编码
        String questionBody = model.getQuestionBody();
        questionBody = questionBody.replace("()","<input />");
        try {
            // 对题干进行url反编码
            model.setQuestionBody(URLDecoder.decode(questionBody, "UTF-8"));
            // 对答案进行url反编码
            String questionAnswer = model.getQuestionAnswer();
            if (!StringUtils.isEmpty(questionAnswer)) {
                model.setQuestionAnswer(URLDecoder.decode(questionAnswer, "UTF-8"));
            }
            // 对解析进行url反编码
            String questionResolve = model.getQuestionResolve();
            if (!StringUtils.isEmpty(questionResolve)) {
                model.setQuestionResolve(URLDecoder.decode(questionResolve, "UTF-8"));
            }
            // 对选项进行url反编码
            String opt = model.getQuestionOpt();
            if (!StringUtils.isEmpty(opt)) {
                model.setQuestionOpt(URLDecoder.decode(opt, "UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        List<QuestionModel> questionModels = model.getModelList();
        if (!CommonUtils.listIsEmptyOrNull(questionModels)) {
            questionModels.forEach(this::urlDecode);
        }
        return model;
    }

    @Transactional
    public void insertExcelQuestions(QuestionModel model, HttpServletRequest httpServletRequest) throws Exception {

        int userId = Integer.valueOf(httpServletRequest.getAttribute("userId").toString());
        // 如果是完形填空题，阅读理解题，综合题或者配对题先插入小题
        StringBuilder childQuestionIds = new StringBuilder();
        String childIds = "";
        List<QuestionModel> models = new ArrayList<>();
        models = model.getModelList();
        if (!CommonUtils.listIsEmptyOrNull(models)) {
            for (QuestionModel m : models) {
//                if(haveSaved(model,userId,true)){ // 判断是否重复了
//                    log.info("子题【"+model.getOrder()+"】已存在!");
////                    throw new ServiceException("子题【"+model.getOrder()+"】已存在!");
//                }else{
                    try {
                        childQuestionIds.append(insert(m, userId, childQuestionIds.toString(), true)).append(";");
                    }catch (Exception e){
                        e.printStackTrace();
                        log.error("试题插入异常，异常信息："+e);
                        throw new ServiceException("试题插入异常,字段信息过长！(汉字最长不超过7000个，字母最长不超过50000个)");
                    }

//                }
            }
            if(!StringUtils.isEmpty(childQuestionIds) && childQuestionIds.length() > 0){
                childIds = childQuestionIds.substring(0, childQuestionIds.length() - 1);
            }
            log.info("子题主键id字符串===================" + childIds);
        }
        // 插入主题
        if(haveSaved(model,userId,false)){ // 判断是否重复了
            log.info("试题【"+model.getOrder()+"】已存在!");
            throw new ServiceException("试题【"+model.getOrder()+"】已存在!");
        }
        Integer fatherId = insert(model, userId, childIds, false);
        if(!StringUtils.isEmpty(childIds)){
            // 更新子题，设置父题id
            updateChild(fatherId,childIds);
        }
    }

    @Override
    public QuestionModel selectQuestion(Integer questionId) throws Exception {

        // 先查询主题
        QuestionModel model = new QuestionModel();
        Question question = questionDao.selectByPrimaryKey(questionId);
        if (null == question) {
            throw new ServiceException("试题不存在!");
        }
        model = MapAndObjectUtils.ObjectClone(question, QuestionModel.class);
        // 如果是完形填空题，阅读理解题，综合题或者配对题要查询附属的小题
        QuestionType questionType = questionTypeDao.selectByPrimaryKey(question.getQuestionType());
        if(null == questionType){
            throw new ServiceException("试题类型【"+question.getQuestionType()+"】未配置，请联系管理员进行配置！");
        }
        if (CommonUtils.hasChildQuestion(questionType.getCode())) {
            // 查询子题
            List<QuestionModel> models = new ArrayList<>();
            String childs = question.getQuestionOpt();
            if (!StringUtils.isEmpty(childs)) {
                if (!childs.contains(";")) {
                    childs += ";";
                }
                String[] childStr = childs.split(";");
                for (String s : childStr) {
                    QuestionModel questionModel = new QuestionModel();
                    Question questionTmp = questionDao.selectByPrimaryKey(Integer.valueOf(s));
                    questionModel = MapAndObjectUtils.ObjectClone(questionTmp, QuestionModel.class);
                    if (!StringUtils.isEmpty(questionTmp.getQuestionType())) {
                        Integer typeId = questionTmp.getQuestionType();
                        if (typeId > 100) {
                            typeId -= 100;
                        }
                        QuestionType type = questionTypeDao.selectByPrimaryKey(typeId);
                        questionModel.setQuestionTypeCode(type.getCode());
                    }
                    // 根据课程id查名字
                    Course course = courseDao.selectByPrimaryKey(questionModel.getCourseId());
                    questionModel.setCourseName(course.getName());
                    // 张羽要求传知识点id就行
                    String points = questionModel.getKnowledgePoints();
//                    questionModel.setKnowledgePoints(getPointNameById(points));
                    questionModel.setKnowledgePoints(points);
                    // 转换时间格式
                    questionModel.setCreateTimeStr(DateUtils.date2String(questionModel.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
                    models.add(questionModel);
                }
            }
            model.setModelList(models);
        }
        if (!StringUtils.isEmpty(question.getQuestionType())) {
            Integer typeId = question.getQuestionType();
            if (typeId > 100) {
                typeId -= 100;
            }
            QuestionType type = questionTypeDao.selectByPrimaryKey(typeId);
            model.setQuestionTypeCode(type.getCode());
        }
        // 根据课程id查名字
        Course course = courseDao.selectByPrimaryKey(model.getCourseId());
        model.setCourseName(course.getName());
        // 根据知识点id查知识点名称
        String points = model.getKnowledgePoints();
//        model.setKnowledgePoints(getPointNameById(points));
        model.setKnowledgePoints(points);
        model.setCreateTimeStr(DateUtils.date2String(model.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
        return model;
    }
}