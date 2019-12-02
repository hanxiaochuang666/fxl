package com.by.blcu.course.XWPFFactory.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.by.blcu.core.ret.ServiceException;
import com.by.blcu.core.utils.*;
import com.by.blcu.course.XWPFFactory.CustomXWPFDocument;
import com.by.blcu.course.XWPFFactory.Html2Text;
import com.by.blcu.course.XWPFFactory.IXwptWrite;
import com.by.blcu.course.XWPFFactory.XwptAbstractWrite;
import com.by.blcu.course.XWPFFactory.model.DocModel;
import com.by.blcu.course.XWPFFactory.model.DocQueModel;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Set;

/**
 * @ClassName SingleChoiceXwptWriteImpl
 * @Author 焦冬冬
 * @Date 2019/6/17 13:12
 **/
@Slf4j
public class SingleChoiceXwptWriteImpl extends XwptAbstractWrite implements IXwptWrite {

    @Override
    public void writeObject(CustomXWPFDocument docxDocument, DocModel docModel, boolean isExportAnswer, boolean isExportReslove)throws Exception {
        //1.写入题类目
        writeTab(docxDocument,docModel);
        //2.写入每个题
        List<DocQueModel> questionLst = docModel.getQuestionLst();
        for (DocQueModel docQueModel : questionLst) {
            //2.1写入题干
            if(docModel.getIsScore()==1)
                docQueModel.setScore(-1);
            writeQuestionBody(docxDocument,docQueModel.getScore(),docQueModel.getNumber(),docQueModel.getQuestionBody());
            //2.2写入选项
            writeQuestionOpt(docxDocument,docQueModel.getQuestionOpt());
            //2.3写入答案
            if(isExportAnswer)
                writeQuestionAnswer(docxDocument,docQueModel.getQuestionAnswer());
            //2.4写入解析
            if(isExportReslove)
                writeQuestionReslove(docxDocument,docQueModel.getQuestionReslove());
        }
    }

    @Override
    protected void writeTab(CustomXWPFDocument docxDocument, DocModel docModel) {
        if(StringUtils.isEmpty(docModel.getQuestionTypeName()))
            return;
        XWPFParagraph p1 = docxDocument.createParagraph();
        XWPFRun run = p1.createRun();
        p1.setStyle(" 标题 8");
        run.setFontSize(20);
        switch (docModel.getSort()){
            case 1:
                run.setText("一、");
                break;
            case 2:
                run.setText("二、");
                break;
            case 3:
                run.setText("三、");
                break;
            case 4:
                run.setText("四、");
                break;
            case 5:
                run.setText("五、");
                break;
            case 6:
                run.setText("六、");
                break;
            case 7:
                run.setText("七、");
                break;
            case 8:
                run.setText("八、");
                break;
            case 9:
                run.setText("九、");
                break;
        }
        run.setText(docModel.getQuestionTypeName());
        if(docModel.getIsScore()==0&&docModel.getTotalScore()>0) {
            run.setText("(" + docModel.getTotalScore() + "分)");
        }
    }

    @Override
    protected void writeQuestionBody(CustomXWPFDocument docxDocument, int score, int number, String questionBody) throws Exception{
        XWPFParagraph paragraph = docxDocument.createParagraph();
        paragraph.setVerticalAlignment(TextAlignment.BOTTOM);
        XWPFRun run = paragraph.createRun();
        run.setFontSize(16);
        paragraph.setStyle(" 正文 ");
        run.addTab();
        if(number>0)
            run.setText(number+"、");
        else{
            number=Math.abs(number);
            run.setText("("+number+")");
        }
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
        run.setText(parser.getText());
        if(score>=0)
            run.setText("("+score+"分)");
        if(!imgStr.isEmpty()) {
            run.addCarriageReturn();
            for (String s : imgStr) {
                log.info("图片URL:" + s);
                //run.setText("题干图片路径:"+s);
                    //数据流读取不到
                    //FastDFSClientWrapper dfsClient = SpringContextUtil.getBean(FastDFSClientWrapper.class);
                    InputStream inputStream = HttpReqUtil.getInputStream(s);
                    if(null==inputStream)
                        continue;
                    byte[] fileByte = HttpReqUtil.getbyteByInputStream(inputStream);
                    /*byte[] fileByte = dfsClient.getByteData(s);
                    InputStream inputStream = dfsClient.downFile(s);*/
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
                    docxDocument.createPicture(addPictureData,nextPicNameNumber, writeWidth, writeHeight, paragraph);
                    inputStream.close();
                    byteInputStream.close();
            }
        }
        //run.addCarriageReturn();
    }

    @Override
    protected void writeQuestionOpt(CustomXWPFDocument docxDocument, String questionOpt)throws Exception {
        XWPFParagraph paragraph = docxDocument.createParagraph();
        paragraph.setVerticalAlignment(TextAlignment.BOTTOM);
        XWPFRun run = paragraph.createRun();
        run.setFontSize(14);
        JSONArray objects = JSONArray.parseArray(questionOpt);
        int size = objects.size();
        for (Object object : objects) {
            JSONObject JSONObject=(com.alibaba.fastjson.JSONObject)object;
            String option = JSONObject.getString("option");
            run.addTab();
            run.addTab();
            run.setText(option);
            if(--size >0) {
                run.addCarriageReturn();
            }
        }
    }

    @Override
    protected void writeQuestionAnswer(CustomXWPFDocument docxDocument, String answer)throws Exception {
        XWPFParagraph paragraph = docxDocument.createParagraph();
        paragraph.setVerticalAlignment(TextAlignment.BOTTOM);
        XWPFRun run = paragraph.createRun();
        //加粗
        run.setBold(true);
        run.setFontSize(14);
        run.addTab();
        run.addTab();
        log.info("questionBody["+answer);
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
        log.info("questionanswer:"+parser.getText());
        run.setText("答案: "+parser.getText());
        if(!imgStr.isEmpty()) {
            run.addCarriageReturn();
            for (String s : imgStr) {
                log.info("图片URL:" + s);
                //run.setText("答案图片路径:"+s);
                //数据流读取不到
                //FastDFSClientWrapper dfsClient = SpringContextUtil.getBean(FastDFSClientWrapper.class);
                /*byte[] fileByte = dfsClient.getByteData(s);
                InputStream inputStream = dfsClient.downFile(s);*/
                InputStream inputStream = HttpReqUtil.getInputStream(s);
                if(null==inputStream)
                    continue;
                byte[] fileByte = HttpReqUtil.getbyteByInputStream(inputStream);
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
                docxDocument.createPicture(addPictureData,nextPicNameNumber, writeWidth, writeHeight, paragraph);
                inputStream.close();
                byteInputStream.close();
            }
        }
        //run.addCarriageReturn();
    }

    @Override
    protected void writeQuestionReslove(CustomXWPFDocument docxDocument, String reslove)throws Exception {
        XWPFParagraph paragraph = docxDocument.createParagraph();
        paragraph.setVerticalAlignment(TextAlignment.BOTTOM);
        XWPFRun run = paragraph.createRun();
        run.setFontSize(14);
        run.addTab();
        run.addTab();
        run.setColor("FF0000");
        run.setText("解析:");
        //run.setColor("BED4F1");
        run.addCarriageReturn();
        XWPFRun run1 = paragraph.createRun();
        run1.setFontSize(14);
        run1.addTab();
        run1.addTab();
        run1.addTab();
        Set<String> imgStr = CommonUtils.getImgStr(reslove);
        String ques="</html>"+reslove+"</html>";
        Reader in=new StringReader(ques);
        Html2Text parser = new Html2Text();
        try {
            parser.parse(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("questionReslove:"+parser.getText());
        run1.setText(parser.getText());
        if(!imgStr.isEmpty()) {
            run1.addCarriageReturn();
            for (String s : imgStr) {
                log.info("图片URL:" + s);
                //run.setText("解析图片路径:"+s);
                //数据流读取不到
                //FastDFSClientWrapper dfsClient = SpringContextUtil.getBean(FastDFSClientWrapper.class);
                /*byte[] fileByte = dfsClient.getByteData(s);
                InputStream inputStream = dfsClient.downFile(s);*/
                InputStream inputStream = HttpReqUtil.getInputStream(s);
                if(null==inputStream)
                    continue;
                byte[] fileByte = HttpReqUtil.getbyteByInputStream(inputStream);
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
                docxDocument.createPicture(addPictureData,nextPicNameNumber, writeWidth, writeHeight, paragraph);
                inputStream.close();
                byteInputStream.close();
            }
        }
    }
}
