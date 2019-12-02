package com.by.blcu.course.XWPFFactory;

import com.by.blcu.course.XWPFFactory.model.DocModel;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public abstract class XwptAbstractWrite {

    /**
     * 写一级标题
     * @param docModel
     */
    protected abstract void writeTab(CustomXWPFDocument docxDocument, DocModel docModel)throws Exception;

    /**
     * 写题干
     * @param score
     * @param questionBody
     */
    protected abstract void writeQuestionBody(CustomXWPFDocument docxDocument, int score, int number, String questionBody)throws Exception;

    /**
     * 写选项
     * @param questionOpt
     */
    protected abstract void writeQuestionOpt(CustomXWPFDocument docxDocument, String questionOpt)throws Exception;

    /**
     * 写答案
     * @param answer
     */
    protected abstract void writeQuestionAnswer(CustomXWPFDocument docxDocument, String answer)throws Exception;

    /**
     * 写解析
     * @param reslove
     */
    protected abstract void writeQuestionReslove(CustomXWPFDocument docxDocument, String reslove)throws Exception;
}
