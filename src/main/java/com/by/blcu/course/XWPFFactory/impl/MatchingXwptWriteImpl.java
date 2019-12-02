package com.by.blcu.course.XWPFFactory.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.by.blcu.core.utils.StringUtils;
import com.by.blcu.course.XWPFFactory.CustomXWPFDocument;
import com.by.blcu.course.XWPFFactory.model.DocModel;
import com.by.blcu.course.XWPFFactory.model.DocQueModel;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * @ClassName MatchingXwptWriteImpl
 * @Author 焦冬冬
 * 配对题
 * @Date 2019/6/17 16:25
 **/
public class MatchingXwptWriteImpl extends SingleChoiceXwptWriteImpl{
    @Override
    public void writeObject(CustomXWPFDocument docxDocument, DocModel docModel, boolean isExportAnswer, boolean isExportReslove)throws Exception {
        //1.写入题类目
        writeTab(docxDocument,docModel);
        //2.写入综合题题干
        if(docModel.getIsScore()==1)
            docModel.setPerScore(-1);
        writeQuestionBody(docxDocument,docModel.getPerScore(),docModel.getNumber(),docModel.getSynthesisStr());
        //2.3写入选项
        writeQuestionOpt(docxDocument,docModel.getMatchOptStr());
        //3.写入配对题每个小题
        List<DocQueModel> questionLst = docModel.getQuestionLst();
        for (DocQueModel docQueModel : questionLst) {
            try {
            //2.1写入题干
            if(docModel.getIsScore()==1)
                docQueModel.setScore(-1);
            writeQuestionBody(docxDocument,docQueModel.getScore(),docQueModel.getNumber()-(docQueModel.getNumber()*2),URLDecoder.decode(docQueModel.getQuestionBody(), "UTF-8"));
            //2.4写入答案
            if(isExportAnswer && !StringUtils.isEmpty(docQueModel.getQuestionAnswer())) {
                writeQuestionAnswer(docxDocument, URLDecoder.decode(docQueModel.getQuestionAnswer(), "UTF-8"));
            }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        if(isExportReslove)
            writeQuestionReslove(docxDocument,docModel.getMatchReslove());
    }

    @Override
    protected void writeQuestionOpt(CustomXWPFDocument docxDocument, String questionOpt) throws Exception{
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
                run.addCarriageReturn();
            }
        }
    }
}
