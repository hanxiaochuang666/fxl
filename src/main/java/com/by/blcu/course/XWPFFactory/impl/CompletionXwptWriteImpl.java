package com.by.blcu.course.XWPFFactory.impl;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

/**
 * @ClassName CompletionXwptWriteImpl
 * @Author 焦冬冬
 * 填空题(可复用到计算题)
 * @Date 2019/6/17 15:55
 **/
public class CompletionXwptWriteImpl extends SingleChoiceXwptWriteImpl{

    //填空题没有选项的说法
    @Override
    protected void writeQuestionOpt(XWPFDocument docxDocument, String questionOpt) {

    }
}
