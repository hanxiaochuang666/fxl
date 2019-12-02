package com.by.blcu.course.XWPFFactory.impl;

import com.by.blcu.core.utils.StringUtils;
import com.by.blcu.course.XWPFFactory.CustomXWPFDocument;
import com.by.blcu.course.XWPFFactory.model.DocModel;
import com.by.blcu.course.XWPFFactory.model.DocQueModel;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * @ClassName SynthesisXwptWriteImpl
 * @Author 焦冬冬
 * 综合题
 * @Date 2019/6/17 16:09
 **/
public class SynthesisXwptWriteImpl extends SingleChoiceXwptWriteImpl{
    @Override
    public void writeObject(CustomXWPFDocument docxDocument, DocModel docModel, boolean isExportAnswer, boolean isExportReslove)throws Exception {
        //1.写入题类目
        writeTab(docxDocument,docModel);
        //2.写入综合题题干
        if(docModel.getIsScore()==1)
            docModel.setPerScore(-1);
        writeQuestionBody(docxDocument,docModel.getPerScore(),docModel.getNumber(),docModel.getSynthesisStr());
        //3.写入综合题每个小题
        List<DocQueModel> questionLst = docModel.getQuestionLst();
        for (DocQueModel docQueModel : questionLst) {
            try {
                //2.1写入小题题干
                if(docModel.getIsScore()==1)
                    docQueModel.setScore(-1);
                writeQuestionBody(docxDocument, docQueModel.getScore(), docQueModel.getNumber()-(docQueModel.getNumber()*2), URLDecoder.decode(docQueModel.getQuestionBody(), "UTF-8"));
                //2.2写入小题选项
                if(!StringUtils.isEmpty(docQueModel.getQuestionOpt())) {
                    writeQuestionOpt(docxDocument, URLDecoder.decode(docQueModel.getQuestionOpt(), "UTF-8"));
                }
                //2.3写入小题答案
                if (isExportAnswer && !StringUtils.isEmpty(docQueModel.getQuestionAnswer()))
                    writeQuestionAnswer(docxDocument, URLDecoder.decode(docQueModel.getQuestionAnswer(),"UTF-8"));
                //2.4写入小题解析
                if (isExportReslove&& !StringUtils.isEmpty(docQueModel.getQuestionReslove()))
                    writeQuestionReslove(docxDocument, URLDecoder.decode(docQueModel.getQuestionReslove(),"UTF-8"));
            }catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }
        }
    }
}
