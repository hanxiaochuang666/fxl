package com.by.blcu.resource.service.impl;

import com.by.blcu.core.aop.CourseCheck;
import com.by.blcu.core.ret.RetCode;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.ServiceException;
import com.by.blcu.core.universal.BaseServiceImpl;
import com.by.blcu.core.universal.IBaseDao;
import com.by.blcu.core.utils.*;
import com.by.blcu.course.XWPFFactory.XwptFactory;
import com.by.blcu.course.XWPFFactory.impl.*;
import com.by.blcu.course.XWPFFactory.model.DocModel;
import com.by.blcu.course.XWPFFactory.model.DocQueModel;
import com.by.blcu.course.dto.Course;
import com.by.blcu.course.dto.CourseDetail;
import com.by.blcu.course.service.ICourseDetailService;
import com.by.blcu.course.service.ICourseService;
import com.by.blcu.course.service.courseCheck.CourseCheckModel;
import com.by.blcu.resource.dao.*;
import com.by.blcu.resource.dto.*;
import com.by.blcu.resource.model.QuestionTypeCountModel;
import com.by.blcu.resource.model.TestPaperQuestionResModel;
import com.by.blcu.resource.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

@Service("testPaperQuestionService")
@Slf4j
public class TestPaperQuestionServiceImpl extends BaseServiceImpl implements ITestPaperQuestionService {
    @Autowired
    private ITestPaperQuestionDao testPaperQuestionDao;

    @Autowired
    private IQuestionDao questionDao;

    @Autowired
    private IQuestionTypeDao questionTypeDao;

    @Autowired
    private ITestPaperFormatDao testPaperFormatDao;

    @Autowired
    private ITestPaperDao testPaperDao;

    @Autowired
    private ITestResultDetailDao testResultDetailDao;
    @Resource
    private ICourseService courseService;

    @Autowired
    private FastDFSClientWrapper dfsClient;

    @Resource(name="testPaperService")
    private ITestPaperService testPaperService;

    @Resource(name="resourcesService")
    private IResourcesService resourcesService;

    @Resource(name="testResultService")
    private ITestResultService testResultService;

    @Resource(name = "courseDetailService")
    private ICourseDetailService courseDetailService;

    @Resource(name="learnActiveService")
    private ILearnActiveService learnActiveService;

    @Value("${testPaperWord.filePath}")
    private  String testPaperWordPath;

    @Override
    protected IBaseDao getDao() {
        return this.testPaperQuestionDao;
    }

    @Override
    public List<TestPaperQuestionResModel> queryTestPaper(int testPaperId, Integer isNeedAnswer) throws Exception {
        Map<String, Object> initMap = MapUtils.initMap("testPagerId", testPaperId);
        initMap.put("_sort_line","sort");
        initMap.put("_order_","asc");
        List<TestPaperQuestionResModel> res=new ArrayList<>();
        List<TestPaperQuestion> objects = getDao().selectList(initMap);
        List<Map<String,Object>> objLst=new ArrayList<>();
        int lastQuestionType=-1;
        int perScore=-1;
        //1.试题信息统一处理
        for (TestPaperQuestion object : objects) {
            if(null!=isNeedAnswer &&isNeedAnswer>0)
                object.setResolve(null);
            Map<String, Object> stringObjectMap = MapAndObjectUtils.ObjectToMap(object);
            Question que = questionDao.selectByPrimaryKey(object.getQuestionId());
            int questionType1 = que.getQuestionType().intValue();
            QuestionType questionType= questionTypeDao.selectByPrimaryKey(questionType1);
            if(que!=null) {
                /*// 对题干进行反编码
                String questionBody = que.getQuestionBody();
                que.setQuestionBody(URLDecoder.decode(questionBody, "UTF-8"));
                // 对答案进行反编码
                String questionAnswer = que.getQuestionAnswer();
                if (!StringUtils.isEmpty(questionAnswer)) {
                    que.setQuestionAnswer(URLDecoder.decode(questionAnswer, "UTF-8"));
                }
                // 对解析进行反编码
                String questionResolve = que.getQuestionResolve();
                if (!StringUtils.isEmpty(questionResolve)) {
                    que.setQuestionResolve(URLDecoder.decode(questionResolve, "UTF-8"));
                }
                // 对选项进行反编码
                String opt = que.getQuestionOpt();
                if (!StringUtils.isEmpty(opt)) {
                    que.setQuestionOpt(URLDecoder.decode(opt, "UTF-8"));
                }*/
                stringObjectMap.put("testPaperQuestionId",object.getTestPaperQuestionId());
                stringObjectMap.put("questionBody", que.getQuestionBody());
                stringObjectMap.put("questionSound", que.getQuestionSound());
                stringObjectMap.put("questionOpt", que.getQuestionOpt());
                if(null!=isNeedAnswer &&isNeedAnswer>0) {
                    stringObjectMap.put("questionAnswer", que.getQuestionAnswer());
                    stringObjectMap.put("questionResolve", que.getQuestionResolve());
                }
                if(questionType.getCode().equals("PEIDUI")){
                    stringObjectMap.put("questionAnswer", que.getQuestionAnswer());
                }
                stringObjectMap.put("questionType", que.getQuestionType());
                if (lastQuestionType != que.getQuestionType()) {
                    lastQuestionType = que.getQuestionType();
                    Map<String, Object> map = MapUtils.initMap("testPaperId", testPaperId);
                    map.put("questionType", questionType.getQuestionTypeId());
                    List<TestPaperFormat> objects1 = testPaperFormatDao.selectList(map);
                    if (objects1 == null || objects1.size() <= 0)
                        continue;
                    TestPaperFormat testPaperFormat = objects1.get(0);
                    int questionNum = testPaperFormat.getQuestionNum().intValue();
                    int score = testPaperFormat.getQuestionSpec();
                    perScore = score;
                    TestPaperQuestionResModel testPaperQuestionResModel = new TestPaperQuestionResModel();
                    testPaperQuestionResModel.setScore(score * questionNum);
                    testPaperQuestionResModel.setQuestionType(lastQuestionType);
                    testPaperQuestionResModel.setQuestionTypeName(questionType.getName());
                    res.add(testPaperQuestionResModel);
                }
                stringObjectMap.put("score", perScore);
                if(CommonUtils.hasChildQuestion(questionType.getCode())){
                    List<Map<String, Object>> childQuestionByOpt = getChildQuestionByOpt(que.getQuestionOpt(), isNeedAnswer, perScore);
                    stringObjectMap.put("modelList",childQuestionByOpt);
                }
                objLst.add(stringObjectMap);
            }
        }
        //2.试题重新排序
        int sort=0;
        for (Map<String, Object> stringObjectMap : objLst) {
            if(stringObjectMap.containsKey("modelList")){
                stringObjectMap.remove("sort");
                List<Map<String, Object>> temp=(List<Map<String, Object>>)stringObjectMap.get("modelList");
                for (Map<String, Object> objectMap : temp) {
                    objectMap.put("sort",++sort);
                }
            }else{
                stringObjectMap.put("sort",++sort);
            }
        }
        //3.试题信息分组打包
        for (TestPaperQuestionResModel re : res) {
            List<Map<String, Object>> lst=new ArrayList<>();
            for (Map<String, Object> map : objLst) {
                int questionType = (int)map.get("questionType");
                if(questionType==re.getQuestionType()){
                    lst.add(map);
                }
            }
            re.setQuestionLst(lst);
        }
        return res;
    }

    /**
     * 获取题目的子题
     * @param QuestionOpt
     * @param isNeedAnswer
     * @param score
     * @return
     * @throws Exception
     */
    private List<Map<String, Object>> getChildQuestionByOpt(String QuestionOpt,Integer isNeedAnswer,int score)throws Exception{
        if(!QuestionOpt.contains(";"))
            QuestionOpt=QuestionOpt+";";
        String[] split = QuestionOpt.split(";");
        List<String> strings = Arrays.asList(split);
        List<Question> list = questionDao.selectList(MapUtils.initMap("entityKeyValues", strings));
        List<Map<String, Object>> resList=new ArrayList<>();
        int size = list.size();
        if(size<=0)
            return null;
        int perScore=score/size;
        int elseScore=score%size;
        int i=0;
        for (Question que : list) {
            Map<String, Object> resMap = MapUtils.initMap();
            if(que==null)
                continue;
            // 对题干进行反编码
            String questionBody = que.getQuestionBody();
            que.setQuestionBody(URLDecoder.decode(questionBody, "UTF-8"));
            // 对答案进行反编码
            String questionAnswer = que.getQuestionAnswer();
            if (!StringUtils.isEmpty(questionAnswer)) {
                que.setQuestionAnswer(URLDecoder.decode(questionAnswer, "UTF-8"));
            }
            // 对解析进行反编码
            String questionResolve = que.getQuestionResolve();
            if (!StringUtils.isEmpty(questionResolve)) {
                que.setQuestionResolve(URLDecoder.decode(questionResolve, "UTF-8"));
            }
            // 对选项进行反编码
            String opt = que.getQuestionOpt();
            if (!StringUtils.isEmpty(opt)) {
                que.setQuestionOpt(URLDecoder.decode(opt, "UTF-8"));
            }
            resMap.put("questionBody", que.getQuestionBody());
            resMap.put("questionSound", que.getQuestionSound());
            resMap.put("questionOpt", que.getQuestionOpt());
            resMap.put("questionId",que.getQuestionId());
            if(null!=isNeedAnswer &&isNeedAnswer>0) {
                resMap.put("questionAnswer", que.getQuestionAnswer());
                resMap.put("questionResolve", que.getQuestionResolve());
            }
            resMap.put("questionType", que.getQuestionType()-100);
            QuestionType questionType = questionTypeDao.selectByPrimaryKey(que.getQuestionType() - 100);
            if(null!=questionType){
                resMap.put("questionTypeName", questionType.getName());
            }
            if(++i==size)
                resMap.put("score",perScore+elseScore);
            else
                resMap.put("score",perScore);
            resList.add(resMap);
        }
        return resList;
    }

    @Override
    @CourseCheck
    @Transactional(rollbackFor=ServiceException.class)
    public RetResult saveTestPaperQuestion(List<TestPaperQuestion> testPaperQuestionLst, int userId, CourseCheckModel courseCheckModel) throws Exception {

        //1.检测试卷组成是否符合规范
        List<Integer> idLst=new ArrayList<>();
        int testPaperId=-1;
        for (TestPaperQuestion testPaperQuestion : testPaperQuestionLst) {
            if(idLst.contains(testPaperQuestion.getQuestionId())){
                log.info("试题id"+testPaperQuestion.getQuestionId()+"重复，请重新选择");
                return RetResponse.makeRsp(RetCode.SUCCESS.code,"试题id"+testPaperQuestion.getQuestionId()+"重复，请重新选择");
            }
            idLst.add(testPaperQuestion.getQuestionId());
            if(testPaperId<0&&null!=testPaperQuestion.getTestPagerId())
                testPaperId=testPaperQuestion.getTestPagerId().intValue();
        }
        boolean operation = CommonUtils.isOperation(userId, testPaperId, testPaperService);
        if(!operation){
            log.info("只能自己修改自己的试卷");
            return RetResponse.makeRsp(RetCode.SUCCESS.code,"只能自己修改自己的试卷");
        }
        List<QuestionTypeCountModel> questionTypeCountModels = questionDao.queryQuestionTypeCount(idLst);
        Map<String, Object> initMap = MapUtils.initMap("testPaperId", testPaperId);
        List<TestPaperFormat> list = testPaperFormatDao.selectList(initMap);
        int commonNum=0;
        for (TestPaperFormat format : list) {
            boolean isExistType=false;
            for (QuestionTypeCountModel questionTypeCountModel : questionTypeCountModels) {
                if(format.getQuestionType().intValue()==questionTypeCountModel.getQuestionType()){
                    isExistType=true;
                    commonNum++;
                    if(format.getQuestionNum()!=questionTypeCountModel.getCountNum()) {
                        QuestionType questionType = questionTypeDao.selectByPrimaryKey(questionTypeCountModel.getQuestionType());
                        log.info(questionType.getName()+"数量与试卷设置数目不匹配");
                        return RetResponse.makeRsp(RetCode.BUSINESS_ERROR.code,questionType.getName()+"数量与试卷设置数目不匹配");
                    }

                }
            }
            if(!isExistType){
                Integer questionNum = format.getQuestionNum();
                if(questionNum>0) {
                    QuestionType questionType = questionTypeDao.selectByPrimaryKey(format.getQuestionType());
                    log.info(questionType.getName() + "数量与试卷设置数目不匹配");
                    return RetResponse.makeRsp(RetCode.BUSINESS_ERROR.code, questionType.getName() + "数量与试卷设置数目不匹配");
                }
            }
        }
        if(commonNum!=questionTypeCountModels.size()){
            log.info("试题数量与试卷设置数目不匹配");
            return RetResponse.makeRsp(RetCode.BUSINESS_ERROR.code,"试题数量与试卷设置数目不匹配");
        }
        List<TestPaperQuestion> selectList = testPaperQuestionDao.selectList(MapUtils.initMap("testPagerId", testPaperId));
        Map<Integer, TestPaperQuestion> integerTestPaperQuestionHashMap = new HashMap<>();
        boolean isUpdate=false;
        if(null!=selectList && selectList.size()>0) {
            isUpdate=true;
            for (TestPaperQuestion testPaperQuestion : selectList) {
                integerTestPaperQuestionHashMap.put(testPaperQuestion.getQuestionId(), testPaperQuestion);
            }
        }
        //testPaperQuestionDao.deleteByParams(MapUtils.initMap("testPagerId", testPaperId));
        //2.插入新的试题
        for (TestPaperQuestion testPaperQuestion : testPaperQuestionLst) {
            if (!integerTestPaperQuestionHashMap.containsKey(testPaperQuestion.getQuestionId())) {
                testPaperQuestion.setTestPaperQuestionId(null);
                testPaperQuestionDao.insertSelective(testPaperQuestion);
            }else {
                TestPaperQuestion paperQuestion = integerTestPaperQuestionHashMap.get(testPaperQuestion.getQuestionId());
                paperQuestion.setSort(testPaperQuestion.getSort());
                testPaperQuestionDao.updateByPrimaryKeySelective(paperQuestion);
                integerTestPaperQuestionHashMap.remove(testPaperQuestion.getQuestionId());
            }
        }
        //3.删除原来的试题
        if(!integerTestPaperQuestionHashMap.isEmpty()) {
            for (Map.Entry<Integer, TestPaperQuestion> entry : integerTestPaperQuestionHashMap.entrySet()) {
                TestPaperQuestion value = entry.getValue();
                testPaperQuestionDao.deleteByPrimaryKey(value.getTestPaperQuestionId());
            }
        }
        TestPaper testPaper = testPaperDao.selectByPrimaryKey(testPaperId);
        if(isUpdate){
            //编辑
            /*boolean usedMall = resourcesService.isUsedMall(testPaperId, null, 0, 1);
            if (usedMall) {
                log.info("试卷id为" + testPaperId + "的试卷关联的课程已经上架，不能编辑");
                throw new ServiceException("试卷id为" + testPaperId + "的试卷关联的课程已经上架，不能编辑");
            }*/
            Date date = new Date();
            Map<String, Object> content = MapUtils.initMap("content", testPaperId + "");
            //maxType
            content.put("maxType",1);
            List<Resources> objects = resourcesService.selectList(content);
            if(null==objects|| objects.size()<=0){
                log.info("试卷无资源描述信息，testPaperId:"+testPaperId);
                throw new ServiceException("试卷无资源描述信息，testPaperId:"+testPaperId);
            }
            testPaper.setExportStuPath("");
            testPaper.setUpdateUser(userId);
            testPaper.setUpdateTime(date);
            Resources resources = new Resources();
            resources.setResourcesId(objects.get(0).getResourcesId());
            resources.setUpdateTime(date);
            resources.setUpdateUser(userId);
            resourcesService.updateByPrimaryKeySelective(resources);
            Map<String, Object> objectMap = MapUtils.initMap("resourcesId", objects.get(0).getResourcesId());
            List<CourseDetail> objects1 = courseDetailService.selectList(objectMap);
            if(null!=objects1&&objects1.size()>0) {
                if(0==courseCheckModel.getIsUpper()) {
                    for (CourseDetail courseDetail : objects1) {
                        Integer courseId = courseDetail.getCourseId();
                        Course course = new Course();
                        course.setCourseId(courseId);
                        course.setStatus(0);
                        courseService.updateByPrimaryKeySelective(course);
                    }
                }
            }
        }
        log.info("试卷id为"+testPaperId+"的试题保存成功");
        testPaper.setStatus(0);
        testPaperDao.updateByPrimaryKeySelective(testPaper);

        return RetResponse.makeOKRsp();
    }

    @Override
    @Transactional(rollbackFor=ServiceException.class)
    public RetResult intellectPaper(int testPaperId, String knowledges) throws Exception {
        TestPaper testPaper = testPaperDao.selectByPrimaryKey(testPaperId);
        //1.检测创建者id是否有使用本机构下的同级类目的课程其他老师的题的权限？

        //2.查询试卷组成;
        Map<String, Object> initMap = MapUtils.initMap("testPaperId", testPaperId);
        initMap.put("_sort_line","question_type");
        initMap.put("_order_","asc");
        List<TestPaperFormat> testPaperFormatLst = testPaperFormatDao.selectList(initMap);
        //3.查询自己可用的试题（根据知识点,试题类型和用户id,及一级类目,二级类目，课程id）
        initMap.clear();
        initMap.put("categoryOne",testPaper.getCategoryOne());
        initMap.put("categoryTwo",testPaper.getCategoryTwo());
        initMap.put("courseid",testPaper.getCourseId());
        initMap.put("createUser",testPaper.getCreateUser());
        LinkedHashMap<String, List<Question>> questionTypeMap =new LinkedHashMap<>();
        for (TestPaperFormat testPaperFormat : testPaperFormatLst) {
            initMap.put("questionType",testPaperFormat.getQuestionType());
            List<Question> questionList = questionDao.selectList(initMap);
            if(questionList.size()<testPaperFormat.getQuestionNum()){
                log.info("可用试题数量不够:(试题id)"+testPaperFormat.getTestPaperId());
                return RetResponse.makeRsp(RetCode.BUSINESS_ERROR.code,"可用试题数量不够:(试题id)"+testPaperFormat.getTestPaperId());
            }
            questionTypeMap.put(testPaperFormat.getQuestionType()+"",questionList);
        }
        LinkedHashMap<String, Set<Integer>> questionLstByKonwledge = getQuestionLstByKonwledge(questionTypeMap, knowledges);
        log.info("可用试题按照试题类型和知识点分组:"+questionLstByKonwledge.toString());
        LinkedHashMap<String, Integer> formatPerKonwledgeNum = getFormatPerKonwledgeNum(questionLstByKonwledge,testPaperFormatLst);
        log.info("试卷试题知识点分配:"+formatPerKonwledgeNum.toString());
        List<Integer> quesRes = getQuesRes(formatPerKonwledgeNum, questionLstByKonwledge);
        log.info("获取随机组卷的试题结果集:"+quesRes.toString());
        int num=0;
        //删除旧的试卷
        initMap.clear();
        initMap.put("testPagerId", testPaperId);
        testPaperQuestionDao.deleteByParams(initMap);
        for (Integer quesRe : quesRes) {
            TestPaperQuestion testPaperQuestion = new TestPaperQuestion();
            testPaperQuestion.setQuestionId(quesRe);
            testPaperQuestion.setSort(++num);
            testPaperQuestion.setTestPagerId(testPaperId);
            testPaperQuestionDao.insertSelective(testPaperQuestion);
        }

        TestPaper testPaper1=new TestPaper();
        testPaper1.setTestPaperId(testPaper.getTestPaperId());
        testPaper1.setIsScore(testPaper.getIsScore());
        testPaper1.setStatus(0);
        testPaperDao.updateByPrimaryKeySelective(testPaper1);
        return  RetResponse.makeOKRsp();
    }

    @Override
    public String createNewWord(int testPaperId,boolean isExportAnswer,boolean isExportReslove) throws Exception {
        TestPaper testPaper = testPaperDao.selectByPrimaryKey(testPaperId);
        XwptFactory xwptFactory=new XwptFactory();
        String resPath=testPaperWordPath+testPaper.getName()+".docx";
        File file=new File(resPath);
        if(file.exists()&&file.isFile())
            file.delete();
        xwptFactory.setDocName(resPath);
        DocModel docModel = new DocModel();
        docModel.setQuestionTypeName(testPaper.getName());
        docModel.setTotalScore(testPaper.getTotalScore());
        docModel.setIsScore(testPaper.getIsScore());
        //写入试卷标题
        xwptFactory.addDocModel(docModel);
        xwptFactory.addXwptWrite(new HeaderXwptWriteImpl());
        //1.按类型及sort打包数据;LinkHashMap<String,LinkHashSet<Question>>  string 是问题类型code
        List<TestPaperQuestionResModel> testPaperQuestionResModels = this.queryTestPaper(testPaperId,1);
        //2.按不同题型分别写入;
        int i=0;
        for (TestPaperQuestionResModel testPaperQuestionResModel : testPaperQuestionResModels) {
            List<Map<String, Object>> questionLst = testPaperQuestionResModel.getQuestionLst();
            for (Map<String, Object> stringObjectMap : questionLst) {
                if(stringObjectMap.containsKey("questionBody")&& null!=stringObjectMap.get("questionBody")) {
                    stringObjectMap.put("questionBody", URLDecoder.decode(stringObjectMap.get("questionBody").toString(), "UTF-8"));
                }
                if(stringObjectMap.containsKey("questionOpt")&& null!=stringObjectMap.get("questionOpt")) {
                    stringObjectMap.put("questionOpt", URLDecoder.decode(stringObjectMap.get("questionOpt").toString(), "UTF-8"));
                }
                if(stringObjectMap.containsKey("questionAnswer")&& null!=stringObjectMap.get("questionAnswer")) {
                    stringObjectMap.put("questionAnswer", URLDecoder.decode(stringObjectMap.get("questionAnswer").toString(), "UTF-8"));
                }
                if(stringObjectMap.containsKey("questionResolve")&& null!=stringObjectMap.get("questionResolve")) {
                    stringObjectMap.put("questionResolve", URLDecoder.decode(stringObjectMap.get("questionResolve").toString(), "UTF-8"));
                }
            }
            switch(testPaperQuestionResModel.getQuestionTypeName().trim()){
                case "单选题":
                    DocModel danxuanti = danxuanti(testPaperQuestionResModel,"单选题",++i);
                    danxuanti.setIsScore(testPaper.getIsScore());
                    xwptFactory.addDocModel(danxuanti);
                    xwptFactory.addXwptWrite(new SingleChoiceXwptWriteImpl());
                    break;
                case "选错题":
                    DocModel xuancuo = danxuanti(testPaperQuestionResModel,"选错题",++i);
                    xuancuo.setIsScore(testPaper.getIsScore());
                    xwptFactory.addDocModel(xuancuo);
                    xwptFactory.addXwptWrite(new SingleChoiceXwptWriteImpl());
                    break;
                case "多选题":
                    DocModel danxuanti1 = danxuanti(testPaperQuestionResModel, "多选题",++i);
                    danxuanti1.setIsScore(testPaper.getIsScore());
                    xwptFactory.addDocModel(danxuanti1);
                    xwptFactory.addXwptWrite(new CheckBoxXwptWriteImpl());
                    break;
                case "判断题":
                    DocModel panduan = danxuanti(testPaperQuestionResModel, "判断题", ++i);
                    panduan.setIsScore(testPaper.getIsScore());
                    xwptFactory.addDocModel(panduan);
                    xwptFactory.addXwptWrite(new SingleChoiceXwptWriteImpl());
                    break;
                case "填空题":
                    DocModel tiankong = danxuanti(testPaperQuestionResModel, "填空题", ++i);
                    tiankong.setIsScore(testPaper.getIsScore());
                    xwptFactory.addDocModel(tiankong);
                    xwptFactory.addXwptWrite(new CompletionXwptWriteImpl());
                    break;
                case "完形填空题":
                    List<DocModel> wanxing = zonghe(testPaperQuestionResModel, "完形填空题", ++i);
                    log.info("完形填空题的I"+i);
                    for (DocModel model : wanxing) {
                        log.info("完形填空题的题号:"+model.getSort());
                        model.setIsScore(testPaper.getIsScore());
                        xwptFactory.addDocModel(model);
                        xwptFactory.addXwptWrite(new SynthesisXwptWriteImpl());
                    }
                    break;
                case "阅读理解题":
                    List<DocModel> yuedu = zonghe(testPaperQuestionResModel, "阅读理解题", ++i);
                    log.info("阅读理解题的I"+i);
                    for (DocModel model : yuedu) {
                        log.info("阅读理解题的题号:"+model.getSort());
                        model.setIsScore(testPaper.getIsScore());
                        xwptFactory.addDocModel(model);
                        xwptFactory.addXwptWrite(new SynthesisXwptWriteImpl());
                    }
                    break;
                case "综合题":
                    List<DocModel> zonghe = zonghe(testPaperQuestionResModel, "综合题", ++i);
                    log.info("综合题的I"+i);
                    for (DocModel model : zonghe) {
                        log.info("综合题的题号:"+model.getSort());
                        model.setIsScore(testPaper.getIsScore());
                        xwptFactory.addDocModel(model);
                        xwptFactory.addXwptWrite(new SynthesisXwptWriteImpl());
                    }
                    break;
                case "配对题":
                    List<DocModel> peidui = peidui(testPaperQuestionResModel, "配对题", ++i);
                    for (DocModel model : peidui) {
                        model.setIsScore(testPaper.getIsScore());
                        xwptFactory.addDocModel(model);
                        xwptFactory.addXwptWrite(new MatchingXwptWriteImpl());
                    }
                    break;
                case "选词填空题":
                    List<DocModel> xuancitiankong = xuancitiankong(testPaperQuestionResModel, "选词填空", ++i);
                    for (DocModel model : xuancitiankong) {
                        model.setIsScore(testPaper.getIsScore());
                        xwptFactory.addDocModel(model);
                        xwptFactory.addXwptWrite(new CheckWordXwptWriteImpl());
                    }
                    break;
                case "翻译题":
                    DocModel fanyi= danxuanti(testPaperQuestionResModel, "翻译题", ++i);
                    fanyi.setIsScore(testPaper.getIsScore());
                    xwptFactory.addDocModel(fanyi);
                    xwptFactory.addXwptWrite(new CompletionXwptWriteImpl());
                    break;
                case "计算题":
                    DocModel jisuan = danxuanti(testPaperQuestionResModel, "计算题", ++i);
                    jisuan.setIsScore(testPaper.getIsScore());
                    xwptFactory.addDocModel(jisuan);
                    xwptFactory.addXwptWrite(new CompletionXwptWriteImpl());
                    break;
                case "问答题":
                    DocModel weida = danxuanti(testPaperQuestionResModel, "问答题", ++i);
                    weida.setIsScore(testPaper.getIsScore());
                    xwptFactory.addDocModel(weida);
                    xwptFactory.addXwptWrite(new EssayXwptWriteImpl());
                    break;
            }
        }
        //是否导出答案，默认导出
        xwptFactory.setExportAnswer(isExportAnswer);
        //是否导出解析，默认导出
        xwptFactory.setExportReslove(isExportReslove);
        xwptFactory.writeObject();
        //将file存储到fastDfs
        resPath= dfsClient.localUploadFile(file, "docx");
        file.delete();
        return resPath;
    }


    //选词填空
    private List<DocModel> xuancitiankong(TestPaperQuestionResModel testPaperQuestionResModel,String questionTypeName,int sort){
        List<DocModel> docModelList=new ArrayList<>();
        int i=0;
        for (Map<String, Object> stringObjectMap : testPaperQuestionResModel.getQuestionLst()) {
            DocModel docModel = new DocModel();
            if (++i == 1)
                docModel.setQuestionTypeName(questionTypeName);
            else
                docModel.setQuestionTypeName(null);
            docModel.setTotalScore(testPaperQuestionResModel.getScore());
            docModel.setPerScore(testPaperQuestionResModel.getScore()/testPaperQuestionResModel.getQuestionLst().size());
            docModel.setSort(sort);
            docModel.setNumber(Integer.valueOf(stringObjectMap.get("sort").toString()));
            docModel.setSynthesisStr(stringObjectMap.get("questionBody").toString());
            docModel.setMatchOptStr(stringObjectMap.get("questionOpt").toString());
            docModel.setMatchReslove(stringObjectMap.get("questionResolve").toString());
            List<DocQueModel> questionLst7=new ArrayList<>();
            DocQueModel docQueModel7=new DocQueModel();
            docQueModel7.setNumber(Integer.valueOf(stringObjectMap.get("sort").toString()));
            if(!StringUtils.isEmpty(stringObjectMap.get("questionAnswer"))) {
                String questionAnswer = stringObjectMap.get("questionAnswer").toString();
                StringBuilder res=new StringBuilder();
                if(questionAnswer.contains("#&&&#")) {
                    String[] split1 = questionAnswer.split("#&&&#");
                    for (String ss : split1) {
                        res.append(ss);
                        res.append(";");
                    }
                    String sp = res.toString();
                    docQueModel7.setQuestionAnswer(sp.substring(0,sp.length()-1));
                }else {
                    docQueModel7.setQuestionAnswer(questionAnswer);
                }

            }
            docQueModel7.setScore(testPaperQuestionResModel.getScore());
            questionLst7.add(docQueModel7);
            docModel.setQuestionLst(questionLst7);
            docModelList.add(docModel);
        }
        return docModelList;
    }
    //配对题
    private List<DocModel> peidui(TestPaperQuestionResModel testPaperQuestionResModel,String questionTypeName,int sort){
        List<DocModel> docModelList=new ArrayList<>();
        int i=0;
        for (Map<String, Object> stringObjectMap : testPaperQuestionResModel.getQuestionLst()) {
            DocModel docModel=new DocModel();
            if(++i==1)
                docModel.setQuestionTypeName(questionTypeName);
            else
                docModel.setQuestionTypeName(null);
            docModel.setTotalScore(testPaperQuestionResModel.getScore());
            docModel.setPerScore(testPaperQuestionResModel.getScore()/testPaperQuestionResModel.getQuestionLst().size());
            docModel.setSort(sort);
            docModel.setNumber(Integer.valueOf(stringObjectMap.get("sort").toString()));
            docModel.setSynthesisStr(stringObjectMap.get("questionBody").toString());
            docModel.setMatchOptStr(stringObjectMap.get("questionAnswer").toString());
            docModel.setMatchReslove(stringObjectMap.get("questionResolve").toString());
            String questionOpt = stringObjectMap.get("questionOpt").toString();
            String[] split = questionOpt.split(";");
            int length=split.length;
            int score=Integer.valueOf(stringObjectMap.get("score").toString()).intValue();
            int perScore=score/length;
            int yushu=score%length;
            int j=0;
            List<DocQueModel> questionLst=new ArrayList<>();
            for (String s : split) {
                Question que = questionDao.selectByPrimaryKey(Integer.valueOf(s));
                DocQueModel docQueModel=new DocQueModel();
                docQueModel.setNumber(++j);
                docQueModel.setQuestionBody(que.getQuestionBody());
                docQueModel.setQuestionAnswer(que.getQuestionAnswer());
                if(j==length)
                    docQueModel.setScore(perScore+yushu);
                else
                    docQueModel.setScore(perScore);
                //docQueModel.setQuestionReslove(stringObjectMap.get("questionResolve").toString());
                questionLst.add(docQueModel);
            }
            docModel.setQuestionLst(questionLst);
            docModelList.add(docModel);
        }
        return docModelList;
    }

    //综合题
    private List<DocModel> zonghe(TestPaperQuestionResModel testPaperQuestionResModel,String questionTypeName,int sort ){
        List<DocModel> docModelList=new ArrayList<>();
        int i=0;
        for (Map<String, Object> stringObjectMap : testPaperQuestionResModel.getQuestionLst()) {
            DocModel docModel1=new DocModel();
            if(++i==1)
                docModel1.setQuestionTypeName(questionTypeName);
            else
                docModel1.setQuestionTypeName(null);
            docModel1.setTotalScore(testPaperQuestionResModel.getScore());
            docModel1.setPerScore(testPaperQuestionResModel.getScore()/testPaperQuestionResModel.getQuestionLst().size());
            docModel1.setSort(sort);
            docModel1.setNumber(Integer.valueOf(stringObjectMap.get("sort").toString()));
            docModel1.setSynthesisStr(stringObjectMap.get("questionBody").toString());
            String questionOpt = stringObjectMap.get("questionOpt").toString();
            String[] split = questionOpt.split(";");
            int length=split.length;
            int score=Integer.valueOf(stringObjectMap.get("score").toString()).intValue();
            int perScore=score/length;
            int yushu=score%length;
            int j=0;
            List<DocQueModel> questionLst5=new ArrayList<>();
            for (String s : split) {
                Question que = questionDao.selectByPrimaryKey(Integer.valueOf(s));
                DocQueModel docQueModel5=new DocQueModel();
                docQueModel5.setNumber(++j);
                docQueModel5.setQuestionBody(que.getQuestionBody());
                docQueModel5.setQuestionOpt(que.getQuestionOpt());
                if(!StringUtils.isEmpty(que.getQuestionAnswer())) {
                    String questionAnswer = que.getQuestionAnswer();
                    StringBuilder res=new StringBuilder();
                    if(questionAnswer.contains("#&&&#")) {
                        String[] split1 = questionAnswer.split("#&&&#");
                        for (String ss : split1) {
                            res.append(ss);
                            res.append(";");
                        }
                        String sp = res.toString();
                        docQueModel5.setQuestionAnswer(sp.substring(0,sp.length()-1));
                    }else {
                        docQueModel5.setQuestionAnswer(questionAnswer);
                    }

                }
                if(j==length)
                    docQueModel5.setScore(perScore+yushu);
                else
                    docQueModel5.setScore(perScore);
                docQueModel5.setQuestionReslove(que.getQuestionResolve());
                questionLst5.add(docQueModel5);
            }
            docModel1.setQuestionLst(questionLst5);
            docModelList.add(docModel1);
        }

        return docModelList;

    }
    //单选(复用与多选，判断，填空)
    private DocModel danxuanti(TestPaperQuestionResModel testPaperQuestionResModel,String questionTypeName,int sort ){
        DocModel docModel1=new DocModel();
        docModel1.setQuestionTypeName(questionTypeName);
        docModel1.setTotalScore(testPaperQuestionResModel.getScore());
        docModel1.setSort(sort);
        List<DocQueModel> questionLst1=new ArrayList<>();
        for (Map<String, Object> stringObjectMap : testPaperQuestionResModel.getQuestionLst()) {
            DocQueModel docQueModel=new DocQueModel();
            docQueModel.setNumber(Integer.valueOf(stringObjectMap.get("sort").toString()));
            if(!StringUtils.isEmpty(stringObjectMap.get("questionSound"))
                    && StringUtils.isEmpty(stringObjectMap.get("questionBody"))){
                docQueModel.setQuestionBody("听力题");
            }else {
                docQueModel.setQuestionBody(stringObjectMap.get("questionBody").toString());
            }
            if(!StringUtils.isEmpty(stringObjectMap.get("questionOpt"))) {
                docQueModel.setQuestionOpt(stringObjectMap.get("questionOpt").toString());
            }
            if(!StringUtils.isEmpty(stringObjectMap.get("questionAnswer"))) {
                String questionAnswer = stringObjectMap.get("questionAnswer").toString();
                StringBuilder res=new StringBuilder();
                if(questionAnswer.contains("#&&&#")) {
                    String[] split = questionAnswer.split("#&&&#");
                    for (String s : split) {
                        res.append(s);
                        res.append(";");
                    }
                    String s = res.toString();
                    docQueModel.setQuestionAnswer(s.substring(0,s.length()-1));
                }else {
                    docQueModel.setQuestionAnswer(questionAnswer);
                }

            }
            docQueModel.setScore(Integer.valueOf(stringObjectMap.get("score").toString()));
            if(!StringUtils.isEmpty(stringObjectMap.get("resolve"))){
                docQueModel.setQuestionReslove(stringObjectMap.get("questionResolve").toString()+"&&&"+stringObjectMap.get("resolve").toString());
            }else{
                docQueModel.setQuestionReslove(stringObjectMap.get("questionResolve").toString());
            }
            questionLst1.add(docQueModel);
        }
        docModel1.setQuestionLst(questionLst1);
        return docModel1;
    }
    /**
     * 分配每种知识点对应的题数
     * @param testPaperFormatLst
     * @return
     */
    private LinkedHashMap<String,Integer> getFormatPerKonwledgeNum(LinkedHashMap<String, Set<Integer>> questionLstByKonwledge,List<TestPaperFormat> testPaperFormatLst){
        LinkedHashMap<String,Integer> resMap =new LinkedHashMap<>();
        Map<Integer,String> questionTypeMap=new HashMap<>();
        for (Map.Entry<String, Set<Integer>> entry : questionLstByKonwledge.entrySet()) {
            String key = entry.getKey();
            String[] split1 = key.split("###");
            Set<Integer> value = entry.getValue();
            if(value.size()>0){
                Integer integer = Integer.valueOf(split1[0]);
                if(questionTypeMap.containsKey(integer)){
                    String s = questionTypeMap.get(integer);
                    s+=split1[1]+";";
                    questionTypeMap.put(integer,s);
                }else {
                    questionTypeMap.put(integer,split1[1]+";");
                }
            }
        }
        for (TestPaperFormat testPaperFormat : testPaperFormatLst) {
            int questionNum = testPaperFormat.getQuestionNum().intValue();
            String s = questionTypeMap.get(testPaperFormat.getQuestionType());
            if(questionNum==0||StringUtils.isEmpty(s))
                continue;
            String[] split = s.split(";");
            int length = split.length;
            if(length==1)
                resMap.put(testPaperFormat.getQuestionType()+"###"+split[0],questionNum);
            else {
                if(questionNum==1) {
                    int i = new Random().nextInt(length);
                    resMap.put(testPaperFormat.getQuestionType() + "###" + split[i], 1);
                    continue;
                }
                int i=questionNum/length;
                int j=i/2;
                int k=0;
                int n=0;
                for (int m=0;m<length;m++) {
                    if(m==length-1)
                        k=questionNum-n;
                    else {
                        k = j + new Random().nextInt(i);
                        Set<Integer> integers = questionLstByKonwledge.get(testPaperFormat.getQuestionType() + "###" + split[m]);
                        int size = integers.size();
                        if(size<k){
                            k=new Random().nextInt(size);
                        }
                    }

                    resMap.put(testPaperFormat.getQuestionType()+"###"+split[m],k);
                    n+=k;
                }
            }
        }
        return resMap;
    }

    /**
     * 根据题型和知识点将试题分组
     * @param questionTypeMap
     * @param knowledges
     * @return
     */
    private LinkedHashMap<String,Set<Integer>> getQuestionLstByKonwledge(Map<String, List<Question>> questionTypeMap,String knowledges){
        LinkedHashMap<String,Set<Integer>> resMap=new LinkedHashMap<>();
        String[] split = knowledges.split(";");
        int length = split.length;
        for (Map.Entry<String, List<Question>> entry: questionTypeMap.entrySet()) {
            String key = entry.getKey();
            List<Question> questionList = entry.getValue();
            if(questionList.size()<=0)
                continue;
            if(length==1){
                Set<Integer> tempLst=new HashSet<>();
                List<Question> removeQuesLst=new ArrayList<>();
                for (Question question : questionList) {
                    String[] split1 = question.getKnowledgePoints().split(";");
                    List<String> stringList = Arrays.asList(split1);
                    if(stringList.contains(split[0])) {
                        tempLst.add(question.getQuestionId());
                        removeQuesLst.add(question);
                    }
                }
                for (Question question : removeQuesLst) {
                    questionList.remove(question);
                }
                resMap.put(key+"###"+knowledges,tempLst);
            }else{
                for (String s : split) {
                    Set<Integer> oneLst=new HashSet<>();
                    List<Question> removeQuesLst=new ArrayList<>();
                    for (Question question : questionList) {
                        String[] split1 = question.getKnowledgePoints().split(";");
                        List<String> stringList = Arrays.asList(split1);
                        if(stringList.contains(s)){
                            oneLst.add(question.getQuestionId());
                            removeQuesLst.add(question);
                        }
                    }
                    for (Question question : removeQuesLst) {
                        questionList.remove(question);
                    }
                    resMap.put(key+"###"+s,oneLst);
                }
            }
        }
        return resMap;
    }

    /**
     * 获取组卷结果试题
     * @param formatPerKonwledgeNum
     * @param questionLstByKonwledge
     * @return
     */
    private List<Integer> getQuesRes(LinkedHashMap<String,Integer> formatPerKonwledgeNum,LinkedHashMap<String,Set<Integer>> questionLstByKonwledge)throws Exception{
        List<Integer> integerList=new ArrayList<>();
        for (Map.Entry<String, Integer> entry: formatPerKonwledgeNum.entrySet()) {
            String key = entry.getKey();
            int value = entry.getValue().intValue();
            Set<Integer> integerList1 = questionLstByKonwledge.get(key);
            while (value>0){
                int size = integerList1.size();
                if(size<value) {
                    //从其他两个知识点里面去拿
                    Integer queFromOtherCollection = getQueFromOtherCollection(key, questionLstByKonwledge);
                    if(queFromOtherCollection.intValue()==-1)
                        throw new ServiceException("知识点对应的题不够或者不可用");
                    integerList.add(queFromOtherCollection);
                    value--;
                    continue;
                }
                int nextInt = new Random().nextInt(size);
                List<Integer> list = new ArrayList(integerList1);
                integerList.add(list.get(nextInt));
                integerList1.remove(list.get(nextInt));
                value--;
            }
        }
        return integerList;
    }

    private Integer getQueFromOtherCollection(String key,LinkedHashMap<String,Set<Integer>> questionLstByKonwledge){
        String[] split = key.split("###");
        for (Map.Entry<String, Set<Integer>> entry: questionLstByKonwledge.entrySet()){
            String key1 = entry.getKey();
            String[] split1 = key1.split("###");
            if(split[0].equals(split1[0])){
                Set<Integer> value = entry.getValue();
                if(value.size()>0) {
                    Integer integer = value.iterator().next();
                    value.remove(integer);
                    return integer;
                }
            }
        }
        return -1;
    }
}