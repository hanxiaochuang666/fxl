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
 * @ClassName CheckBoxXwptWriteImpl
 * @Author 焦冬冬
 * @Date 2019/6/17 15:11
 **/
@Slf4j
public class CheckBoxXwptWriteImpl extends SingleChoiceXwptWriteImpl{

    @Override
    protected void writeQuestionAnswer(CustomXWPFDocument docxDocument, String answer) throws Exception{
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
        Set<String> imgStr = CommonUtils.getImgStr(answer);
        String ques="</html>"+answer+"</html>";
        Reader in=new StringReader(ques);
        Html2Text parser = new Html2Text();
        try {
            parser.parse(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String text = parser.getText();
        log.info("questionanswer:"+text);
        run.setText("答案: "+text);
        if(!imgStr.isEmpty()) {
            run.addCarriageReturn();
            for (String s : imgStr) {
                log.info("图片URL:" + s);
                //run.setText("答案图片路径:"+s);
                //try {
                    //数据流读取不到
                    FastDFSClientWrapper dfsClient = SpringContextUtil.getBean(FastDFSClientWrapper.class);
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
               /* } catch (Exception e) {
                    e.printStackTrace();
                }*/
            }
        }
    }
}
