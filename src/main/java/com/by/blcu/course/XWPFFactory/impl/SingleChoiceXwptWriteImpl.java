package com.by.blcu.course.XWPFFactory.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.by.blcu.core.utils.StringUtils;
import com.by.blcu.course.XWPFFactory.IXwptWrite;
import com.by.blcu.course.XWPFFactory.XwptAbstractWrite;
import com.by.blcu.course.XWPFFactory.model.DocModel;
import com.by.blcu.course.XWPFFactory.model.DocQueModel;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.util.List;

/**
 * @ClassName SingleChoiceXwptWriteImpl
 * @Author 焦冬冬
 * @Date 2019/6/17 13:12
 **/
public class SingleChoiceXwptWriteImpl extends XwptAbstractWrite implements IXwptWrite {
    @Override
    public void writeObject(XWPFDocument docxDocument, DocModel docModel, boolean isExportAnswer, boolean isExportReslove) {
        //1.写入题类目
        writeTab(docxDocument,docModel);
        //2.写入每个题
        List<DocQueModel> questionLst = docModel.getQuestionLst();
        for (DocQueModel docQueModel : questionLst) {
            //2.1写入题干
            writeQuestionBody(docxDocument,docQueModel.getScore(),docQueModel.getNumber(),docQueModel.getQuestionBody());
            //2.2写入选项
            writeQuestionOpt(docxDocument,docQueModel.getQuestionOpt());
            //2.3写入答案
            if(isExportAnswer)
                writeQuestionAnswer(docxDocument,docQueModel.getQuestionAnswer());
            //2.4写入解析
            if(isExportReslove)
                writeQuestionReslove(docxDocument,docQueModel.getQuestionReslove());
        }
    }

    @Override
    protected void writeTab(XWPFDocument docxDocument, DocModel docModel) {
        XWPFParagraph p1 = docxDocument.createParagraph();
        XWPFRun run = p1.createRun();
        p1.setStyle(" 标题 8");
        run.setFontSize(20);
        if(docModel.getSort()>=5 && StringUtils.isEmpty(docModel.getQuestionTypeName()))
            return;
        switch (docModel.getSort()){
            case 1:
                run.setText("一、");
                break;
            case 2:
                run.setText("二、");
                break;
            case 3:
                run.setText("三、");
                break;
            case 4:
                run.setText("四、");
                break;
            case 5:
                run.setText("五、");
                break;
            case 6:
                run.setText("六、");
                break;
            case 7:
                run.setText("七、");
                break;
            case 8:
                run.setText("八、");
                break;
            case 9:
                run.setText("九、");
                break;
        }
        run.setText(docModel.getQuestionTypeName());
        run.setText("("+docModel.getTotalScore()+"分)");
    }

    @Override
    protected void writeQuestionBody(XWPFDocument docxDocument, int score, int number, String questionBody) {
        XWPFParagraph paragraph = docxDocument.createParagraph();
        paragraph.setVerticalAlignment(TextAlignment.BOTTOM);
        XWPFRun run = paragraph.createRun();
        run.setFontSize(16);
        paragraph.setStyle(" 正文 ");
        run.addTab();
        if(number>0)
            run.setText(number+"、");
        run.setText(questionBody);
        run.setText("("+score+"分)");
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
            run.addTab();
            run.setText(option);
            if(--size >0) {
                run.addBreak();
            }
        }
    }

    @Override
    protected void writeQuestionAnswer(XWPFDocument docxDocument, String answer) {
        XWPFParagraph paragraph = docxDocument.createParagraph();
        paragraph.setVerticalAlignment(TextAlignment.BOTTOM);
        XWPFRun run = paragraph.createRun();
        //加粗
        run.setBold(true);
        run.setFontSize(14);
        run.addTab();
        run.addTab();
        run.setText("答案: "+answer);
    }

    @Override
    protected void writeQuestionReslove(XWPFDocument docxDocument, String reslove) {
        XWPFParagraph paragraph = docxDocument.createParagraph();
        paragraph.setVerticalAlignment(TextAlignment.BOTTOM);
        XWPFRun run = paragraph.createRun();
        run.setFontSize(14);
        run.addTab();
        run.addTab();
        run.setColor("FF0000");
        run.setText("解析:");
        //run.setColor("BED4F1");
        run.addBreak();
        XWPFRun run1 = paragraph.createRun();
        run1.setFontSize(14);
        run1.addTab();
        run1.addTab();
        run1.addTab();
        run1.setText(reslove);
    }
}
