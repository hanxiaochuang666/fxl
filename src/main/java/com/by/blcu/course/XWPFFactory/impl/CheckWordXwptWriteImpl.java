package com.by.blcu.course.XWPFFactory.impl;

import com.by.blcu.core.utils.*;
import com.by.blcu.course.XWPFFactory.CustomXWPFDocument;
import com.by.blcu.course.XWPFFactory.Html2Text;
import com.by.blcu.course.XWPFFactory.model.DocModel;
import com.by.blcu.course.XWPFFactory.model.DocQueModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Set;

/**
 * @ClassName CheckWordXwptWriteImpl
 * @Author 焦冬冬
 * @Date 2019/6/17 16:46
 **/
@Slf4j
public class CheckWordXwptWriteImpl extends MatchingXwptWriteImpl{

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
        //3.写入综合题每个小题
        List<DocQueModel> questionLst = docModel.getQuestionLst();
        for (DocQueModel docQueModel : questionLst) {
            //3.1写入答案
            if(isExportAnswer&& !StringUtils.isEmpty(docQueModel.getQuestionAnswer())) {
                writeQuestionAnswer(docxDocument, docQueModel.getQuestionAnswer());
            }
        }
        //4.2写入解析
        if(isExportReslove)
            writeQuestionReslove(docxDocument,docModel.getMatchReslove());
    }

    @Override
    protected void writeQuestionBody(CustomXWPFDocument docxDocument, int score, int number, String questionBody)throws Exception {
        XWPFParagraph paragraph = docxDocument.createParagraph();
        paragraph.setVerticalAlignment(TextAlignment.BOTTOM);
        XWPFRun run = paragraph.createRun();
        run.setFontSize(16);
        paragraph.setStyle(" 正文 ");
        run.addTab();
        if(number>0)
            run.setText(number+"、");
        Set<String> imgStr = CommonUtils.getImgStr(questionBody);
        questionBody=questionBody.replaceAll("<input([\\s\\S]*?)>","(_)");
        String ques="</html>"+questionBody+"</html>";
        Reader in=new StringReader(ques);
        Html2Text parser = new Html2Text();
        try {
            parser.parse(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("questionBody:"+parser.getText());
        String text = parser.getText();
        run.setText(text);
        if(score>=0)
            run.setText("("+score+"分)");
        if(!imgStr.isEmpty()) {
            run.addCarriageReturn();
            for (String s : imgStr) {
                log.info("图片URL:" + s);
                //run.setText("题干图片路径:"+s);
                //try {
                //数据流读取不到
                //FastDFSClientWrapper dfsClient = SpringContextUtil.getBean(FastDFSClientWrapper.class);
                /*byte[] fileByte = dfsClient.getByteData(s);
                InputStream inputStream = dfsClient.downFile(s);*/
                InputStream inputStream = HttpReqUtil.getInputStream(s);
                if (null == inputStream)
                    continue;
                byte[] fileByte = HttpReqUtil.getbyteByInputStream(inputStream);
                //BufferedImage image1 = ImageIO.read(inputStream);
                BufferedImage image1 = ImageIO.read(new URL(s));
                int width = image1.getWidth();
                int height = image1.getHeight();
                int writeWidth=450;
                int writeHeight=writeWidth*height/width;
                if(width<=450){
                    writeWidth=width;
                    writeHeight=height;
                }
                ByteArrayInputStream byteInputStream = new ByteArrayInputStream(fileByte);
                //添加图片
                String prefix = s.substring(s
                        .lastIndexOf(".") + 1);
                int format = 0;
                if (prefix.equals("jpg") || prefix.equals(".jpeg")) {
                    format = XWPFDocument.PICTURE_TYPE_JPEG;
                } else if (prefix.equals("gif")) {
                    format = XWPFDocument.PICTURE_TYPE_GIF;
                } else if (prefix.equals("bmp")) {
                    format = XWPFDocument.PICTURE_TYPE_BMP;
                } else if (prefix.equals("png")) {
                    format = XWPFDocument.PICTURE_TYPE_PNG;
                } else if (prefix.equals("pict")) {
                    format = XWPFDocument.PICTURE_TYPE_PICT;
                } else if (prefix.equals("wmf")) {
                    format = XWPFDocument.PICTURE_TYPE_WMF;
                } else if (prefix.equals("wpg")) {
                    format = XWPFDocument.PICTURE_TYPE_WPG;
                }
                //图片大小、位置
                String addPictureData = docxDocument.addPictureData(byteInputStream, format);
                int nextPicNameNumber = docxDocument.getNextPicNameNumber(format);
                docxDocument.createPicture(addPictureData, nextPicNameNumber, writeWidth, writeHeight, paragraph);
                inputStream.close();
                byteInputStream.close();
            /*}catch (Exception e){
                e.printStackTrace();
            }*/
            }
        }
    }
}
