package com.by.blcu.course.XWPFFactory.impl;

import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

/**
 * @ClassName CheckBoxXwptWriteImpl
 * @Author 焦冬冬
 * @Date 2019/6/17 15:11
 **/
public class CheckBoxXwptWriteImpl extends SingleChoiceXwptWriteImpl{

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
        /**
         * 多选题在存储答案时，使用#&&&#间隔
         */
        String[] split = answer.split("#&&&#");
        StringBuilder res=new StringBuilder();
        for (String s : split) {
            res.append(s);
            res.append(" ");
        }
        run.setText("答案: "+res.toString());
    }
}
