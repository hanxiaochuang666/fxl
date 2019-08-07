package com.by.blcu.resource.controller;

import com.alibaba.fastjson.JSONObject;
import com.by.blcu.resource.model.QueryQuestionModel;
import com.by.blcu.resource.model.QuestionModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class QuestionControllerTest {


    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();//建议使用这种
    }

    @Test
    public void insertQuestionTest() throws Exception{

        String danXuan = "{\"categoryOne\":11,\"categoryTwo\":22,\"courseId\":123,\"difficultyLevel\":1,\"knowledgePoints\":\"1081;1082;1983\",\"orgId\":123123,\"questionAnswer\":\"北京\",\"questionBody\":\"我国的首都是下列哪个？\",\"questionOpt\":\"[{'option':'北京'},{'option':'上海'},{'option':'广州'},{'option':'深圳'}]\",\"questionResolve\":\"中国首都是北京，上海是直辖市\",\"questionSound\":\"123we323re223432\",\"questionType\":\"danXuan\"}";
        String duoXuan = "{\"categoryOne\":11,\"categoryTwo\":22,\"courseId\":123,\"difficultyLevel\":1,\"knowledgePoints\":\"1081;1983\",\"orgId\":123123,\"questionAnswer\":\"太原#&&&#南京\",\"questionBody\":\"下列哪些不是直辖市？\",\"questionOpt\":\"[{'option':'北京'},{'option':'上海'},{'option':'太原'},{'option':'南京'}]\",\"questionResolve\":\"中国共有4个直辖市：北京市、上海市、天津市、重庆市\",\"questionSound\":\"123we323re223432\",\"questionType\":\"duoXuan\"}";
        String panDuan = "{\"categoryOne\":11,\"categoryTwo\":22,\"courseId\":123,\"difficultyLevel\":1,\"knowledgePoints\":\"1081;1983\",\"orgId\":123123,\"questionAnswer\":\"不是\",\"questionBody\":\"中国是发达国家吗？\",\"questionOpt\":\"[{'option':'是'},{'option':'不是'}]\",\"questionResolve\":\"中国是最大的发展中国家，还没有步入发达国家的行列\",\"questionSound\":\"3124345455635\",\"questionType\":\"panDuan\"}";
        String tianKong = "{\"categoryOne\":11,\"categoryTwo\":22,\"courseId\":123,\"difficultyLevel\":1,\"knowledgePoints\":\"1081;1983\",\"orgId\":123123,\"questionAnswer\":\"北京#&&&#河南省\",\"questionBody\":\"中国的首都是（），中国人口最大的省份是（）？\",\"questionResolve\":\"中国是最大的发展中国家，还没有步入发达国家的行列\",\"questionSound\":\"3124345455635\",\"questionType\":\"tianKong\"}";
        String zongHeTi = "{\"categoryOne\":11,\"categoryTwo\":22,\"courseId\":123,\"difficultyLevel\":1,\"knowledgePoints\":\"1081;1983\",\"modelList\":[{\"categoryOne\":11,\"categoryTwo\":22,\"courseId\":123,\"difficultyLevel\":1,\"knowledgePoints\":\"1081;1983\",\"orgId\":123123,\"questionAnswer\":\"北京\",\"questionBody\":\"我国的首都是下列哪个？\",\"questionOpt\":\"[{'option':'北京'},{'option':'上海'},{'option':'广州'},{'option':'深圳'}]\",\"questionResolve\":\"无\",\"questionSound\":\"3124345455635\",\"questionType\":\"danXuan\"},{\"categoryOne\":11,\"categoryTwo\":22,\"courseId\":123,\"difficultyLevel\":1,\"knowledgePoints\":\"1081;1983\",\"orgId\":123123,\"questionAnswer\":\"太原#&&&#南京\",\"questionBody\":\"下列哪些不是直辖市？\",\"questionOpt\":\"[{'option':'北京'},{'option':'上海'},{'option':'太原'},{'option':'南京'}]\",\"questionResolve\":\"中国共有4个直辖市：北京市、上海市、天津市、重庆市\",\"questionSound\":\"3124345455635\",\"questionType\":\"duoXuan\"}],\"orgId\":123123,\"questionBody\":\"综合题的材料内容巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉\",\"questionSound\":\"3124345455635\",\"questionType\":\"zongHe\"}";
        String xuanCiTianKong = "{\"categoryOne\":11,\"categoryTwo\":22,\"courseId\":123,\"difficultyLevel\":1,\"knowledgePoints\":\"1081;1983\",\"orgId\":123123,\"questionAnswer\":\"1.B#&&&#2.G#&&&#3.C#&&&#4.F\",\"questionBody\":\"选词填空巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉\",\"questionOpt\":\"[{'option':' A:I’ll buy online only when I have to.'},{'option':' B:You can enjoy steep discounts with most online sellers.'},{'option':'C:I shift between buying on or off line depending on what suits me.'},{'option':'D:Shopping is not only a necessary task but a sociable pleasure.'}]\",\"questionResolve\":\"解析解析解析解析解析解析解析解析解析解析解析解析解析解析解析\",\"questionSound\":\"3124345455635\",\"questionType\":\"xuanCi\"}";
        String peiDui = "{\"categoryOne\":11,\"categoryTwo\":22,\"courseId\":123,\"difficultyLevel\":1,\"knowledgePoints\":\"1081\",\"modelList\":[{\"categoryOne\":11,\"categoryTwo\":22,\"courseId\":123,\"difficultyLevel\":1,\"knowledgePoints\":\"1081;1983\",\"orgId\":123123,\"questionAnswer\":\"A\",\"questionBody\":\"第一小题应该选？\",\"questionResolve\":\"配对题解析\",\"questionSound\":\"3124345455635\",\"questionType\":\"danXuan\"},{\"categoryOne\":11,\"categoryTwo\":22,\"courseId\":123,\"difficultyLevel\":1,\"knowledgePoints\":\"1081;1983\",\"orgId\":123123,\"questionAnswer\":\"B\",\"questionBody\":\"第二个小题应该选？\",\"questionResolve\":\"配对题解析\",\"questionSound\":\"3124345455635\",\"questionType\":\"danXuan\"}],\"orgId\":123123,\"questionAnswer\":\"[{'option':' A:I’ll buy online only when I have to.'},{'option':' B:You can enjoy steep discounts with most online sellers.'},{'option':'C:I shift between buying on or off line depending on what suits me.'},{'option':'D:Shopping is not only a necessary task but a sociable pleasure.'}]\",\"questionBody\":\"配对题题干巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉\",\"questionResolve\":\"解析解析解析解析解析解析解析解析解析解析解析解析解析解析解析\",\"questionSound\":\"3124345455635\",\"questionType\":\"peiDui\"}";
        String wenDa = "{\"categoryOne\":111,\"categoryTwo\":22222,\"courseId\":123123,\"difficultyLevel\":1,\"knowledgePoints\":\"1081\",\"orgId\":123123,\"questionAnswer\":\"中国共产党在社会主义初级阶段的基本路线，亦称“中国共产党建设有中国特色的社会主义的基本路线”。从1987年10月中共第十三次全国代表大会开始，在邓小平建设中国特色社会主义理论指导下逐步形成：“领导和团结全国各族人民，以经济建设为中心，坚持四项基本原则，坚持改革开放，自力更生，艰苦创业，为把我国建设成为富强、民主、文明、和谐、美丽的社会主义现代化强国而奋斗。”\",\"questionBody\":\"中国社会主义初级阶段的基本路线？\",\"questionResolve\":\"解析解析解析解析解析解析解析解析解析解析解析解析解析解析解析\",\"questionSound\":\"3124345455635\",\"questionType\":\"wenDa\"}";
        String jiSuan = "{\"categoryOne\":111,\"categoryTwo\":22222,\"courseId\":123123,\"difficultyLevel\":1,\"knowledgePoints\":\"1081\",\"orgId\":123123,\"questionAnswer\":\"15\",\"questionBody\":\"1+2(3+4)=？\",\"questionResolve\":\"先乘除后加减\",\"questionSound\":\"3124345455635\",\"questionType\":\"jiSuan\"}";
        MvcResult mvcResult =mockMvc.perform(MockMvcRequestBuilders.post("/question/insertQuestion")
                .header("token","AItgJBAEsTI6FdGtTh266zZeHJ9FYGhc86s6Bsli0LXPdTWEIfmBeuzNdgOdiB+pxsRy0t5dUO/MkPG1ZcWlMwr+0txkG/+lW85dQEsgDch/LydR7NZxRXkNgcVNiuvNR6UVpODqtymcssBWfss+olMVqMuVwgcBsIy33BKXomE6zXFQ+ToaV9b4HL3xKWnEmIaiskU2knWev/StJF5W0vxf1a3AcNry0hDWkdRUH7wdgkRTBDLn+eoUslWDnjBEmABbNO6AZFxoONcfN7IY3/HhSIBswITMEZICNObivsOca9usv/eZoIvUdYxolxnRllY/Tdgg4MfWALsPm1L3yk/ALsluSujr4Yj90aqdSjgFVUqt0HIW2nIXBN9NWakj//ObQycTQztxpHwcNj8iyuCCmqSXaR6JIn1QU2Q81LIzyres/CO6H18Qm0qpkoWK42FG9HOolzu8qUWQuaYzEr4cmQA0ajv9WJlkfzWAvWDxYqDvlGyxORGnMGhXAItO")
//                .content(JSONObject.toJSONString(getModel()))
                .content(wenDa)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        Assert.assertEquals(200, status);
        System.out.println("返回参数:" + content);
    }

    QuestionModel getPeiDuiModel(){
        QuestionModel model = new QuestionModel();
        model.setCategoryOne(11);
        model.setCategoryTwo(22);
        model.setCourseId(123);
        model.setDifficultyLevel(1);
        model.setKnowledgePoints("1081");
        model.setOrgId(123123);
        model.setQuestionBody("配对题题干巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉");
//        model.setQuestionOpt("[{'option':' A:I’ll buy online only when I have to.'},{'option':' B:You can enjoy steep discounts with most online sellers.'},{'option':'C:I shift between buying on or off line depending on what suits me.'},{'option':'D:Shopping is not only a necessary task but a sociable pleasure.'}]");
        model.setQuestionAnswer("[{'option':' A:I’ll buy online only when I have to.'},{'option':' B:You can enjoy steep discounts with most online sellers.'},{'option':'C:I shift between buying on or off line depending on what suits me.'},{'option':'D:Shopping is not only a necessary task but a sociable pleasure.'}]");
        model.setQuestionResolve("解析解析解析解析解析解析解析解析解析解析解析解析解析解析解析");
        model.setQuestionSound("3124345455635");
        model.setQuestionType("peiDui");


        List<QuestionModel> models = new ArrayList<>();
        QuestionModel model1 = new QuestionModel();
        model1.setCategoryOne(11);
        model1.setCategoryTwo(22);
        model1.setCourseId(123);
        model1.setDifficultyLevel(1);
        model1.setKnowledgePoints("1081;1983");
        model1.setOrgId(123123);
        model1.setQuestionBody("第一小题应该选？");
//        model1.setQuestionOpt("[{'option':'北京'},{'option':'上海'},{'option':'广州'},{'option':'深圳'}]");
        model1.setQuestionAnswer("A");
        model1.setQuestionResolve("配对题解析");
        model1.setQuestionSound("3124345455635");
        model1.setQuestionType("danXuan");
        models.add(model1);

        QuestionModel model2 = new QuestionModel();
        model2.setCategoryOne(11);
        model2.setCategoryTwo(22);
        model2.setCourseId(123);
        model2.setDifficultyLevel(1);
        model2.setKnowledgePoints("1081;1983");
        model2.setOrgId(123123);
        model2.setQuestionBody("第二个小题应该选？");
//        model2.setQuestionOpt("[{'option':'北京'},{'option':'上海'},{'option':'太原'},{'option':'南京'}]");
        model2.setQuestionAnswer("B");
        model2.setQuestionResolve("配对题解析");
        model2.setQuestionSound("3124345455635");
        model2.setQuestionType("danXuan");
        models.add(model2);

        model.setModelList(models);
        return model;
    }

    QuestionModel getModel(){
        QuestionModel model = new QuestionModel();
        model.setCategoryOne(111);
        model.setCategoryTwo(22222);
        model.setCourseId(123123);
        model.setDifficultyLevel(1);
        model.setKnowledgePoints("1081");
        model.setOrgId(123123);
        model.setQuestionBody("中国社会主义初级阶段的基本路线？");
//        model.setQuestionOpt("[{'option':' A:I’ll buy online only when I have to.'},{'option':' B:You can enjoy steep discounts with most online sellers.'},{'option':'C:I shift between buying on or off line depending on what suits me.'},{'option':'D:Shopping is not only a necessary task but a sociable pleasure.'}]");
        model.setQuestionAnswer("中国共产党在社会主义初级阶段的基本路线，亦称“中国共产党建设有中国特色的社会主义的基本路线”。从1987年10月中共第十三次全国代表大会开始，在邓小平建设中国特色社会主义理论指导下逐步形成：“领导和团结全国各族人民，以经济建设为中心，坚持四项基本原则，坚持改革开放，自力更生，艰苦创业，为把我国建设成为富强、民主、文明、和谐、美丽的社会主义现代化强国而奋斗。”");
        model.setQuestionResolve("解析解析解析解析解析解析解析解析解析解析解析解析解析解析解析");
        model.setQuestionSound("3124345455635");
        model.setQuestionType("wenDa");

        return model;
    }

    @Test
    public void updateQuestionTest() throws Exception{
        QuestionModel model = new QuestionModel();
        model.setQuestionId(1);
        model.setQuestionResolve("测试试题修改");
        MvcResult mvcResult =mockMvc.perform(MockMvcRequestBuilders.post("/question/editQuestion")
                .header("token","AItgJBAEsTI6FdGtTh266zZeHJ9FYGhc86s6Bsli0LXPdTWEIfmBeuzNdgOdiB+pxsRy0t5dUO/MkPG1ZcWlMwr+0txkG/+lW85dQEsgDch/LydR7NZxRXkNgcVNiuvNR6UVpODqtymcssBWfss+olMVqMuVwgcBsIy33BKXomE6zXFQ+ToaV9b4HL3xKWnEmIaiskU2knWev/StJF5W0vxf1a3AcNry0hDWkdRUH7wdgkRTBDLn+eoUslWDnjBEmABbNO6AZFxoONcfN7IY3/HhSIBswITMEZICNObivsOca9usv/eZoIvUdYxolxnRllY/Tdgg4MfWALsPm1L3yk/ALsluSujr4Yj90aqdSjgFVUqt0HIW2nIXBN9NWakj//ObQycTQztxpHwcNj8iyuCCmqSXaR6JIn1QU2Q81LIzyres/CO6H18Qm0qpkoWK42FG9HOolzu8qUWQuaYzEr4cmQA0ajv9WJlkfzWAvWDxYqDvlGyxORGnMGhXAItO")
                .content(JSONObject.toJSONString(model))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        Assert.assertEquals(200, status);
        System.out.println("返回参数:" + content);
    }

    @Test
    public void deleteQuestionTest() throws Exception {
        MvcResult mvcResult =mockMvc.perform(MockMvcRequestBuilders.delete("/question/deleteQuestion")
                .header("token","AItgJBAEsTI6FdGtTh266zZeHJ9FYGhc86s6Bsli0LXPdTWEIfmBeuzNdgOdiB+pxsRy0t5dUO/MkPG1ZcWlMwr+0txkG/+lW85dQEsgDch/LydR7NZxRXkNgcVNiuvNR6UVpODqtymcssBWfss+olMVqMuVwgcBsIy33BKXomE6zXFQ+ToaV9b4HL3xKWnEmIaiskU2knWev/StJF5W0vxf1a3AcNry0hDWkdRUH7wdgkRTBDLn+eoUslWDnjBEmABbNO6AZFxoONcfN7IY3/HhSIBswITMEZICNObivsOca9usv/eZoIvUdYxolxnRllY/Tdgg4MfWALsPm1L3yk/ALsluSujr4Yj90aqdSjgFVUqt0HIW2nIXBN9NWakj//ObQycTQztxpHwcNj8iyuCCmqSXaR6JIn1QU2Q81LIzyres/CO6H18Qm0qpkoWK42FG9HOolzu8qUWQuaYzEr4cmQA0ajv9WJlkfzWAvWDxYqDvlGyxORGnMGhXAItO")
                .param("questionId","1")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        Assert.assertEquals(200, status);
        System.out.println("返回参数:" + content);
    }

    @Test
    public void selectQuestionListCountTest() throws Exception {
        MvcResult mvcResult =mockMvc.perform(MockMvcRequestBuilders.get("/question/selectQuestionListCount")
                .header("token","AItgJBAEsTI6FdGtTh266zZeHJ9FYGhc86s6Bsli0LXPdTWEIfmBeuzNdgOdiB+pxsRy0t5dUO/MkPG1ZcWlMwr+0txkG/+lW85dQEsgDch/LydR7NZxRXkNgcVNiuvNR6UVpODqtymcssBWfss+olMVqMuVwgcBsIy33BKXomE6zXFQ+ToaV9b4HL3xKWnEmIaiskU2knWev/StJF5W0vxf1a3AcNry0hDWkdRUH7wdgkRTBDLn+eoUslWDnjBEmABbNO6AZFxoONcfN7IY3/HhSIBswITMEZICNObivsOca9usv/eZoIvUdYxolxnRllY/Tdgg4MfWALsPm1L3yk/ALsluSujr4Yj90aqdSjgFVUqt0HIW2nIXBN9NWakj//ObQycTQztxpHwcNj8iyuCCmqSXaR6JIn1QU2Q81LIzyres/CO6H18Qm0qpkoWK42FG9HOolzu8qUWQuaYzEr4cmQA0ajv9WJlkfzWAvWDxYqDvlGyxORGnMGhXAItO")
                .param("categoryOne","11")
                .param("categoryTwo","22")
                .param("courseId","123")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        Assert.assertEquals(200, status);
        System.out.println("返回参数:" + content);
    }

    @Test
    public void selectQuestionListTest() throws Exception {
        QueryQuestionModel model = new QueryQuestionModel();
        model.setCategoryOne(11);
        model.setCategoryTwo(22);
        model.setCourseId(123);
        model.setDifficultyLevel(-1);
        model.setKnowledgePoints("-1");
        model.setQuestionType("danXuan");
        model.setQuestionBody("中国");
        MvcResult mvcResult =mockMvc.perform(MockMvcRequestBuilders.get("/question/selectQuestionList")
                .header("token","AItgJBAEsTI6FdGtTh266zZeHJ9FYGhc86s6Bsli0LXPdTWEIfmBeuzNdgOdiB+pxsRy0t5dUO/MkPG1ZcWlMwr+0txkG/+lW85dQEsgDch/LydR7NZxRXkNgcVNiuvNR6UVpODqtymcssBWfss+olMVqMuVwgcBsIy33BKXomE6zXFQ+ToaV9b4HL3xKWnEmIaiskU2knWev/StJF5W0vxf1a3AcNry0hDWkdRUH7wdgkRTBDLn+eoUslWDnjBEmABbNO6AZFxoONcfN7IY3/HhSIBswITMEZICNObivsOca9usv/eZoIvUdYxolxnRllY/Tdgg4MfWALsPm1L3yk/ALsluSujr4Yj90aqdSjgFVUqt0HIW2nIXBN9NWakj//ObQycTQztxpHwcNj8iyuCCmqSXaR6JIn1QU2Q81LIzyres/CO6H18Qm0qpkoWK42FG9HOolzu8qUWQuaYzEr4cmQA0ajv9WJlkfzWAvWDxYqDvlGyxORGnMGhXAItO")
                .content(JSONObject.toJSONString(model))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        Assert.assertEquals(200, status);
        System.out.println("返回参数:" + content);
    }

    @Test
    public void importQuestionTest() throws Exception {
        File file = new File("D:\\workspace\\QuestionBank\\（Excel版）试题导入模板.xlsx");
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "（Excel版）试题导入模板.xlsx", MediaType.TEXT_PLAIN_VALUE,
                new BufferedInputStream(new FileInputStream(file)));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.fileUpload("/question/importQuestion")
                .file(mockMultipartFile)
//                .header("token","AItgJBAEsTI6FdGtTh266zZeHJ9FYGhc86s6Bsli0LXPdTWEIfmBeuzNdgOdiB+pxsRy0t5dUO/MkPG1ZcWlMwr+0txkG/+lW85dQEsgDch/LydR7NZxRXkNgcVNiuvNR6UVpODqtymcssBWfss+olMVqMuVwgcBsIy33BKXomE6zXFQ+ToaV9b4HL3xKWnEmIaiskU2knWev/StJF5W0vxf1a3AcNry0hDWkdRUH7wdgkRTBDLn+eoUslWDnjBEmABbNO6AZFxoONcfN7IY3/HhSIBswITMEZICNObivsOca9usv/eZoIvUdYxolxnRllY/Tdgg4MfWALsPm1L3yk/ALsluSujr4Yj90aqdSjgFVUqt0HIW2nIXBN9NWakj//ObQycTQztxpHwcNj8iyuCCmqSXaR6JIn1QU2Q81LIzyres/CO6H18Qm0qpkoWK42FG9HOolzu8qUWQuaYzEr4cmQA0ajv9WJlkfzWAvWDxYqDvlGyxORGnMGhXAItO")
                .param("categoryOne","11")
                .param("categoryTwo","12")
                .param("courseId","1")
                .param("orgId","111111")
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        Assert.assertEquals(200, mvcResult.getResponse().getStatus());
    }
}