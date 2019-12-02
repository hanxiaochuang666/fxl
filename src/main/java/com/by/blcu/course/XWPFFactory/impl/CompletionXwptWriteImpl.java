package com.by.blcu.course.XWPFFactory.impl;

import com.by.blcu.core.utils.CommonUtils;
import com.by.blcu.core.utils.FastDFSClientWrapper;
import com.by.blcu.core.utils.HttpReqUtil;
import com.by.blcu.core.utils.SpringContextUtil;
import com.by.blcu.course.XWPFFactory.CustomXWPFDocument;
import com.by.blcu.course.XWPFFactory.Html2Text;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Set;

/**
 * @ClassName CompletionXwptWriteImpl
 * @Author 焦冬冬
 * 填空题(可复用到计算题)
 * @Date 2019/6/17 15:55
 **/
@Slf4j
public class CompletionXwptWriteImpl extends SingleChoiceXwptWriteImpl{

    //填空题没有选项的说法
    @Override
    protected void writeQuestionOpt(CustomXWPFDocument docxDocument, String questionOpt) {

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
        for (String s : imgStr) {
            run.addCarriageReturn();
            log.info("图片URL:"+s);
            //run.setText("题干图片路径:"+s);
            //try {
                //数据流读取不到
                //FastDFSClientWrapper dfsClient = SpringContextUtil.getBean(FastDFSClientWrapper.class);
                /*byte[] fileByte = dfsClient.getByteData(s);
                InputStream inputStream = dfsClient.downFile(s);*/
                InputStream inputStream = HttpReqUtil.getInputStream(s);
                if(null==inputStream)
                    continue;
                byte[] fileByte = HttpReqUtil.getbyteByInputStream(inputStream);
                BufferedImage image1 = ImageIO.read(new URL(s));
                //BufferedImage image1 = ImageIO.read(inputStream);
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
                docxDocument.createPicture(addPictureData,nextPicNameNumber, writeWidth, writeHeight, paragraph);
                inputStream.close();
                byteInputStream.close();
            /*}catch (Exception e){
                e.printStackTrace();
            }*/
        }
        //run.addCarriageReturn();
    }
}
