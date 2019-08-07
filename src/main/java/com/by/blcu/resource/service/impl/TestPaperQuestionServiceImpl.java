package com.by.blcu.resource.service.impl;

import com.by.blcu.core.ret.RetCode;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.ServiceException;
import com.by.blcu.core.universal.BaseServiceImpl;
import com.by.blcu.core.universal.IBaseDao;
import com.by.blcu.core.utils.FastDFSClientWrapper;
import com.by.blcu.core.utils.MapAndObjectUtils;
import com.by.blcu.core.utils.MapUtils;
import com.by.blcu.core.utils.StringUtils;
import com.by.blcu.course.XWPFFactory.XwptFactory;
import com.by.blcu.course.XWPFFactory.impl.*;
import com.by.blcu.course.XWPFFactory.model.DocModel;
import com.by.blcu.course.XWPFFactory.model.DocQueModel;
import com.by.blcu.resource.dao.*;
import com.by.blcu.resource.dto.*;
import com.by.blcu.resource.model.QuestionTypeCountModel;
import com.by.blcu.resource.model.TestPaperQuestionResModel;
import com.by.blcu.resource.service.ITestPaperQuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
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
    private FastDFSClientWrapper dfsClient;

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
            if(que!=null) {
                stringObjectMap.put("questionBody", que.getQuestionBody());
                stringObjectMap.put("questionSound", que.getQuestionSound());
                stringObjectMap.put("questionOpt", que.getQuestionOpt());
                if(null!=isNeedAnswer &&isNeedAnswer>0) {
                    stringObjectMap.put("questionAnswer", que.getQuestionAnswer());
                    stringObjectMap.put("questionResolve", que.getQuestionResolve());
                }
                stringObjectMap.put("questionType", que.getQuestionType());
                if (lastQuestionType != que.getQuestionType()) {
                    lastQuestionType = que.getQuestionType();
                    QuestionType questionType = questionTypeDao.selectByPrimaryKey(lastQuestionType);
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
                objLst.add(stringObjectMap);
            }
        }
        //2.试题信息分组打包
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

    @Override
    @Transactional(rollbackFor=ServiceException.class)
    public RetResult saveTestPaperQuestion(List<TestPaperQuestion> testPaperQuestionLst) throws Exception {
        //1.检测试卷组成是否符合规范
        List<Integer> idLst=new ArrayList<>();
        int testPaperId=-1;
        for (TestPaperQuestion testPaperQuestion : testPaperQuestionLst) {
            idLst.add(testPaperQuestion.getQuestionId());
            if(null!=testPaperQuestion.getTestPagerId()&&testPaperId<0)
                testPaperId=testPaperQuestion.getTestPagerId().intValue();
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
                QuestionType questionType = questionTypeDao.selectByPrimaryKey(format.getQuestionType());
                log.info(questionType.getName()+"数量与试卷设置数目不匹配");
                return RetResponse.makeRsp(RetCode.BUSINESS_ERROR.code,questionType.getName()+"数量与试卷设置数目不匹配");
            }
        }
        if(commonNum!=list.size()||commonNum!=questionTypeCountModels.size()){
            log.info("试题数量与试卷设置数目不匹配");
            return RetResponse.makeRsp(RetCode.BUSINESS_ERROR.code,"试题数量与试卷设置数目不匹配");
        }
        //2.删除原来的试题
        testPaperQuestionDao.deleteByParams(initMap);
        //3.插入新的试题
        for (TestPaperQuestion testPaperQuestion : testPaperQuestionLst) {
            testPaperQuestion.setTestPaperQuestionId(null);
            testPaperQuestionDao.insertSelective(testPaperQuestion);

        }
        log.info("试卷id为"+testPaperId+"的试题保存成功");
        //4.生成新的试卷word
        try {
            String newWord = createNewWord(testPaperId);
            TestPaper testPaper1=new TestPaper();
            testPaper1.setExportPath(newWord);
            testPaper1.setExportTime(new Date());
            testPaper1.setTestPaperId(testPaperId);
            testPaperDao.updateByPrimaryKeySelective(testPaper1);
        } catch (Exception e) {
            throw new ServiceException("创建试卷word失败");
        }
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
        LinkedHashMap<String, Integer> formatPerKonwledgeNum = getFormatPerKonwledgeNum(testPaperFormatLst, knowledges);
        log.info("试卷试题知识点分配:"+formatPerKonwledgeNum.toString());
        LinkedHashMap<String, Set<Integer>> questionLstByKonwledge = getQuestionLstByKonwledge(questionTypeMap, knowledges);
        log.info("可用试题按照试题类型和知识点分组:"+questionLstByKonwledge.toString());
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
        //生成对应新的试卷word
        try {
            String relPath = this.createNewWord(testPaperId);
            TestPaper testPaper1=new TestPaper();
            testPaper1.setExportPath(relPath);
            testPaper1.setExportTime(new Date());
            testPaper1.setTestPaperId(testPaper.getTestPaperId());
            testPaperDao.updateByPrimaryKeySelective(testPaper1);
        } catch (Exception e) {
            throw new ServiceException("创建试卷word失败");
        }

        return  RetResponse.makeOKRsp();
    }

    @Override
    public String createNewWord(int testPaperId) throws Exception {
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
        //写入试卷标题
        xwptFactory.addDocModel(docModel);
        xwptFactory.addXwptWrite(new HeaderXwptWriteImpl());
        //1.按类型及sort打包数据;LinkHashMap<String,LinkHashSet<Question>>  string 是问题类型code
        List<TestPaperQuestionResModel> testPaperQuestionResModels = this.queryTestPaper(testPaperId,1);
        //2.按不同题型分别写入;
        int i=0;
        for (TestPaperQuestionResModel testPaperQuestionResModel : testPaperQuestionResModels) {
            switch(testPaperQuestionResModel.getQuestionTypeName().trim()){
                case "单选题":
                    DocModel danxuanti = danxuanti(testPaperQuestionResModel,"单选题",++i);
                    xwptFactory.addDocModel(danxuanti);
                    xwptFactory.addXwptWrite(new SingleChoiceXwptWriteImpl());
                    break;
                case "多选题":
                    DocModel danxuanti1 = danxuanti(testPaperQuestionResModel, "多选题",++i);
                    xwptFactory.addDocModel(danxuanti1);
                    xwptFactory.addXwptWrite(new CheckBoxXwptWriteImpl());
                    break;
                case "判断题":
                    DocModel panduan = danxuanti(testPaperQuestionResModel, "判断题", ++i);
                    xwptFactory.addDocModel(panduan);
                    xwptFactory.addXwptWrite(new SingleChoiceXwptWriteImpl());
                    break;
                case "填空题":
                    DocModel tiankong = danxuanti(testPaperQuestionResModel, "填空题", ++i);
                    xwptFactory.addDocModel(tiankong);
                    xwptFactory.addXwptWrite(new CompletionXwptWriteImpl());
                    break;
                case "综合题":
                    List<DocModel> zonghe = zonghe(testPaperQuestionResModel, "综合题", ++i);
                    for (DocModel model : zonghe) {
                        xwptFactory.addDocModel(model);
                        xwptFactory.addXwptWrite(new SynthesisXwptWriteImpl());
                    }
                    break;
                case "配对题":
                    List<DocModel> peidui = peidui(testPaperQuestionResModel, "配对题", ++i);
                    for (DocModel model : peidui) {
                        xwptFactory.addDocModel(model);
                        xwptFactory.addXwptWrite(new MatchingXwptWriteImpl());
                    }
                    break;
                case "选词填空":
                    List<DocModel> xuancitiankong = xuancitiankong(testPaperQuestionResModel, "选词填空", ++i);
                    for (DocModel model : xuancitiankong) {
                        xwptFactory.addDocModel(model);
                        xwptFactory.addXwptWrite(new CheckWordXwptWriteImpl());
                    }
                    break;
                case "计算题":
                    DocModel tiankong1= danxuanti(testPaperQuestionResModel, "计算题", ++i);
                    xwptFactory.addDocModel(tiankong1);
                    xwptFactory.addXwptWrite(new CompletionXwptWriteImpl());
                    break;
                case "问答题":
                    DocModel weida = danxuanti(testPaperQuestionResModel, "问答题", ++i);
                    xwptFactory.addDocModel(weida);
                    xwptFactory.addXwptWrite(new EssayXwptWriteImpl());
                    break;
            }
        }
        //是否导出答案，默认导出
        //xwptFactory.setExportAnswer(false);
        //是否导出解析，默认导出
        //xwptFactory.setExportReslove(false);
        xwptFactory.writeObject();
        //将file存储到fastDfs
        resPath= dfsClient.localUploadFile(file, testPaper.getName() + ".docx");
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
            docModel.setSort(sort);
            docModel.setSynthesisStr(stringObjectMap.get("questionBody").toString());
            docModel.setMatchOptStr(stringObjectMap.get("questionOpt").toString());
            List<DocQueModel> questionLst7=new ArrayList<>();
            DocQueModel docQueModel7=new DocQueModel();
            docQueModel7.setNumber(1);
            docQueModel7.setQuestionAnswer(stringObjectMap.get("questionAnswer").toString());
            docQueModel7.setScore(testPaperQuestionResModel.getScore());
            docQueModel7.setQuestionReslove(stringObjectMap.get("questionResolve").toString());
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
            docModel.setSort(sort);
            docModel.setSynthesisStr(stringObjectMap.get("questionBody").toString());
            docModel.setMatchOptStr(stringObjectMap.get("questionAnswer").toString());
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
                docQueModel.setQuestionReslove(stringObjectMap.get("questionResolve").toString());
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
            docModel1.setSort(sort);
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
                            res.append(" ");
                        }
                        docQueModel5.setQuestionAnswer(res.toString());
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
                        res.append(" ");
                    }
                    docQueModel.setQuestionAnswer(res.toString());
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
     * @param knowledges
     * @return
     */
    private LinkedHashMap<String,Integer> getFormatPerKonwledgeNum(List<TestPaperFormat> testPaperFormatLst,String knowledges){
        LinkedHashMap<String,Integer> resMap =new LinkedHashMap<>();
        String[] split = knowledges.split(";");
        int length = split.length;
        for (TestPaperFormat testPaperFormat : testPaperFormatLst) {
            int questionNum = testPaperFormat.getQuestionNum().intValue();
            if(length==1)
                resMap.put(testPaperFormat.getQuestionType()+"###"+knowledges,questionNum);
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
                    else
                        k=j+new Random().nextInt(i);
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
            if(length==1){
                Set<Integer> tempLst=new HashSet<>();
                for (Question question : questionList) {
                    String[] split1 = question.getKnowledgePoints().split(";");
                    List<String> stringList = Arrays.asList(split1);
                    if(stringList.contains(split[0])) {
                        tempLst.add(question.getQuestionId());
                    }
                }
                resMap.put(key+"###"+knowledges,tempLst);
            }else{
                for (String s : split) {
                    Set<Integer> oneLst=new HashSet<>();
                    for (Question question : questionList) {
                        String[] split1 = question.getKnowledgePoints().split(";");
                        List<String> stringList = Arrays.asList(split1);
                        if(stringList.contains(s)){
                            oneLst.add(question.getQuestionId());
                        }
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