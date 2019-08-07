package com.by.blcu.course.XWPFFactory.impl;

import com.by.blcu.course.XWPFFactory.model.DocModel;
import com.by.blcu.course.XWPFFactory.model.DocQueModel;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.util.List;

/**
 * @ClassName CheckWordXwptWriteImpl
 * @Author 焦冬冬
 * @Date 2019/6/17 16:46
 **/
public class CheckWordXwptWriteImpl extends MatchingXwptWriteImpl{

    @Override
    public void writeObject(XWPFDocument docxDocument, DocModel docModel, boolean isExportAnswer, boolean isExportReslove) {
        //1.写入题类目
        writeTab(docxDocument,docModel);
        //2.写入综合题题干
        writeQuestionBody(docxDocument,docModel.getTotalScore(),-1,docModel.getSynthesisStr());
        //2.3写入选项
        writeQuestionOpt(docxDocument,docModel.getMatchOptStr());
        //3.写入综合题每个小题
        List<DocQueModel> questionLst = docModel.getQuestionLst();
        for (DocQueModel docQueModel : questionLst) {
            //3.1写入答案
            if(isExportAnswer)
                writeQuestionAnswer(docxDocument,docQueModel.getQuestionAnswer());
            //3.2写入解析
            if(isExportReslove)
                writeQuestionReslove(docxDocument,docQueModel.getQuestionReslove());
        }
    }
}
