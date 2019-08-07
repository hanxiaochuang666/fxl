package com.by.blcu.course.XWPFFactory;

import com.by.blcu.course.XWPFFactory.model.DocModel;
import lombok.Data;
import org.apache.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFStyle;
import org.apache.poi.xwpf.usermodel.XWPFStyles;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName XwptAbstractFactory
 * @Author 焦冬冬
 * @Date 2019/6/14 17:16
 **/
@Data
public class XwptFactory {


    protected static Logger logger = Logger.getLogger(XwptFactory.class);
    private XWPFDocument docxDocument;
    private List<DocModel> docModelLst;
    private List<IXwptWrite> xwptWriteLst;
    /**
     * 是否导出答案
     */
    private boolean isExportAnswer=true;
    /**
     * 是否导出解析
     */
    private boolean isExportReslove=true;
    /**
     * word名称(全路径)
     */
    private String docName;

    /**
     * 构造方法
     */
    public XwptFactory(){
        createDocxDocument();
    }
    /**
     * 创建文档对象
     */
    public  void createDocxDocument(){
        docxDocument=new XWPFDocument();
        docModelLst=new ArrayList<>();
        xwptWriteLst=new ArrayList<>();
        addCustomHeadingStyle(docxDocument," 标题 3", 3);
        addCustomHeadingStyle(docxDocument," 标题 8", 8);
    }

    /**
     * 保存文件
     */
    private  void  saveWord(){

        try {
            FileOutputStream out = new FileOutputStream(docName);
            docxDocument.write(out);
            out.close();
        }catch (Exception e){
            logger.info("保存文件"+docName+"失败");
        }finally {
            try {
                docxDocument.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addCustomHeadingStyle(XWPFDocument docxDocument, String strStyleId, int headingLevel) {

        CTStyle ctStyle = CTStyle.Factory.newInstance();
        ctStyle.setStyleId(strStyleId);

        CTString styleName = CTString.Factory.newInstance();
        styleName.setVal(strStyleId);
        ctStyle.setName(styleName);

        CTDecimalNumber indentNumber = CTDecimalNumber.Factory.newInstance();
        indentNumber.setVal(BigInteger.valueOf(headingLevel)); // lower number > style is more prominent in the formats bar
        ctStyle.setUiPriority(indentNumber);

        CTOnOff onoffnull = CTOnOff.Factory.newInstance();
        ctStyle.setUnhideWhenUsed(onoffnull); // style shows up in the formats bar
        ctStyle.setQFormat(onoffnull); // style defines a heading of the given level
        CTPPr ppr = CTPPr.Factory.newInstance();
        ppr.setOutlineLvl(indentNumber);
        ctStyle.setPPr(ppr);

        XWPFStyle style = new XWPFStyle(ctStyle); // is a null op if already defined
        XWPFStyles styles = docxDocument.createStyles();

        style.setType(STStyleType.PARAGRAPH);
        styles.addStyle(style);

    }


    /**
     * 写入整个word
     */
    public  void writeObject(){
        for(int i=0;i<xwptWriteLst.size();i++){
            IXwptWrite iXwptWrite = xwptWriteLst.get(i);
            iXwptWrite.writeObject(docxDocument,docModelLst.get(i),isExportAnswer,isExportReslove);
        }
        saveWord();
    }

    public void addDocModel(DocModel docModel){
        docModelLst.add(docModel);
    }
    public void clearDocModel(){
        docModelLst.clear();
    }

    public void removeDocModel(DocModel docModel){
        docModelLst.remove(docModel);
    }

    public void addXwptWrite(IXwptWrite xwptWrite){
        xwptWriteLst.add(xwptWrite);
    }

    public void clearXwptWrite(){
        xwptWriteLst.clear();
    }

    public void removeXwptWrite(IXwptWrite xwptWrite){
        xwptWriteLst.remove(xwptWrite);
    }
}
