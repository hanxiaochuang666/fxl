package com.by.blcu.course.XWPFFactory.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.by.blcu.course.XWPFFactory.model.DocModel;
import com.by.blcu.course.XWPFFactory.model.DocQueModel;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.util.List;

/**
 * @ClassName MatchingXwptWriteImpl
 * @Author 焦冬冬
 * 配对题
 * @Date 2019/6/17 16:25
 **/
public class MatchingXwptWriteImpl extends SingleChoiceXwptWriteImpl{
    @Override
    public void writeObject(XWPFDocument docxDocument, DocModel docModel, boolean isExportAnswer, boolean isExportReslove) {
        //1.写入题类目
        writeTab(docxDocument,docModel);
        //2.写入综合题题干
        writeQuestionBody(docxDocument,docModel.getTotalScore(),-1,docModel.getSynthesisStr());
        //2.3写入选项
        writeQuestionOpt(docxDocument,docModel.getMatchOptStr());
        //3.写入配对题每个小题
        List<DocQueModel> questionLst = docModel.getQuestionLst();
        for (DocQueModel docQueModel : questionLst) {
            //2.1写入题干
            writeQuestionBody(docxDocument,docQueModel.getScore(),docQueModel.getNumber(),docQueModel.getQuestionBody());
            //2.4写入答案
            if(isExportAnswer)
                writeQuestionAnswer(docxDocument,docQueModel.getQuestionAnswer());
            //2.5写入解析
            if(isExportReslove)
                writeQuestionReslove(docxDocument,docQueModel.getQuestionReslove());
        }
    }

    @Override
    protected void writeQuestionOpt(XWPFDocument docxDocument, String questionOpt) {
        XWPFParagraph paragraph = docxDocument.createParagraph();
        paragraph.setVerticalAlignment(TextAlignment.BOTTOM);
        XWPFRun run = paragraph.createRun();
        run.setFontSize(14);
        JSONArray objects = JSONArray.parseArray(questionOpt);
        int size = objects.size();
        for (Object object : objects) {
            JSONObject JSONObject=(com.alibaba.fastjson.JSONObject)object;
            String option = JSONObject.getString("option");
            run.addTab();
            run.setText(option);
            if(--size >0) {
                run.addBreak();
            }
        }
    }
}
