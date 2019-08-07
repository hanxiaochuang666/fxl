package com.by.blcu.course.XWPFFactory.impl;

import com.by.blcu.course.XWPFFactory.IXwptWrite;
import com.by.blcu.course.XWPFFactory.XwptAbstractWrite;
import com.by.blcu.course.XWPFFactory.model.DocModel;
import org.apache.poi.xwpf.usermodel.*;

/**
 * @ClassName HeaderXwptWriteImpl
 * @Author 焦冬冬
 * @Date 2019/6/17 10:25
 **/
public class HeaderXwptWriteImpl extends XwptAbstractWrite implements IXwptWrite {

    @Override
    public void writeObject(XWPFDocument docxDocument, DocModel docModel, boolean isExportAnswer, boolean isExportReslove) {
        this.writeTab(docxDocument,docModel);
    }

    @Override
    protected void writeTab(XWPFDocument docxDocument, DocModel docModel) {
        XWPFParagraph p1 = docxDocument.createParagraph();
        XWPFRun run = p1.createRun();
        p1.setAlignment(ParagraphAlignment.CENTER);
        p1.setBorderBottom(Borders.DOUBLE);
        p1.setBorderTop(Borders.DOUBLE);

        p1.setBorderRight(Borders.DOUBLE);
        p1.setBorderLeft(Borders.DOUBLE);
        p1.setBorderBetween(Borders.SINGLE);

        p1.setVerticalAlignment(TextAlignment.TOP);
        p1.setStyle(" 标题 3");
        run.setFontSize(25);
        run.setText(docModel.getQuestionTypeName());
        if(docModel.getTotalScore()>0){
            XWPFParagraph p2 = docxDocument.createParagraph();
            XWPFRun p2Run = p2.createRun();
            p2.setAlignment(ParagraphAlignment.CENTER);
            p2.setStyle(" 正文 ");
            p2Run.setFontSize(20);
            p2Run.setText("总分:"+docModel.getTotalScore()+"分");
            p2Run.addBreak();
        }
        run.setBold(true);
    }

    @Override
    protected void writeQuestionBody(XWPFDocument docxDocument, int score, int number, String questionBody) {

    }

    @Override
    protected void writeQuestionOpt(XWPFDocument docxDocument, String questionOpt) {

    }

    @Override
    protected void writeQuestionAnswer(XWPFDocument docxDocument, String answer) {

    }

    @Override
    protected void writeQuestionReslove(XWPFDocument docxDocument, String reslove) {

    }
}
