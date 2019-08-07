package com.by.blcu.course.XWPFFactory;

import com.by.blcu.course.XWPFFactory.model.DocModel;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public abstract class XwptAbstractWrite {

    /**
     * 写一级标题
     * @param docModel
     */
    protected abstract void writeTab(XWPFDocument docxDocument, DocModel docModel);

    /**
     * 写题干
     * @param score
     * @param questionBody
     */
    protected abstract void writeQuestionBody(XWPFDocument docxDocument, int score, int number, String questionBody);

    /**
     * 写选项
     * @param questionOpt
     */
    protected abstract void writeQuestionOpt(XWPFDocument docxDocument, String questionOpt);

    /**
     * 写答案
     * @param answer
     */
    protected abstract void writeQuestionAnswer(XWPFDocument docxDocument, String answer);

    /**
     * 写解析
     * @param reslove
     */
    protected abstract void writeQuestionReslove(XWPFDocument docxDocument, String reslove);
}
