package com.by.blcu.course.XWPFFactory.impl;

import com.by.blcu.course.XWPFFactory.model.DocModel;
import com.by.blcu.course.XWPFFactory.model.DocQueModel;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.util.List;

/**
 * @ClassName SynthesisXwptWriteImpl
 * @Author 焦冬冬
 * 综合题
 * @Date 2019/6/17 16:09
 **/
public class SynthesisXwptWriteImpl extends SingleChoiceXwptWriteImpl{
    @Override
    public void writeObject(XWPFDocument docxDocument, DocModel docModel, boolean isExportAnswer, boolean isExportReslove) {
        //1.写入题类目
        writeTab(docxDocument,docModel);
        //2.写入综合题题干
        writeQuestionBody(docxDocument,docModel.getTotalScore(),-1,docModel.getSynthesisStr());
        //3.写入综合题每个小题
        List<DocQueModel> questionLst = docModel.getQuestionLst();
        for (DocQueModel docQueModel : questionLst) {
            //2.1写入小题题干
            writeQuestionBody(docxDocument,docQueModel.getScore(),docQueModel.getNumber(),docQueModel.getQuestionBody());
            //2.2写入小题选项
            writeQuestionOpt(docxDocument,docQueModel.getQuestionOpt());
            //2.3写入小题答案
            if(isExportAnswer)
                writeQuestionAnswer(docxDocument,docQueModel.getQuestionAnswer());
            //2.4写入小题解析
            if(isExportReslove)
                writeQuestionReslove(docxDocument,docQueModel.getQuestionReslove());
        }
    }
}
