package com.by.blcu.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import com.artofsolving.jodconverter.openoffice.converter.StreamOpenOfficeDocumentConverter;
import com.by.blcu.core.ret.ServiceException;
import lombok.extern.slf4j.Slf4j;

/**
 * 利用jodconverter(基于OpenOffice服务)将文件(*.doc、*.docx、*.xls、*.ppt)转化为html格式或者pdf格式，
 * 使用前请检查OpenOffice服务是否已经开启, OpenOffice进程名称：soffice.exe | soffice.bin
 */
@Slf4j
public class Office2HtmlOrPdfUtil {


    private static Office2HtmlOrPdfUtil office2HtmlOrPdfUtil;
    /** * 获取Doc2HtmlUtil实例 */
    public static synchronized Office2HtmlOrPdfUtil getDoc2HtmlUtilInstance() {
        if (office2HtmlOrPdfUtil == null) {
            office2HtmlOrPdfUtil = new Office2HtmlOrPdfUtil();
        }
        return office2HtmlOrPdfUtil;
    }
    /*** 转换文件成pdf */
    public String file2pdf(InputStream fromFileInputStream, String toFilePath,String fileName,String type,String host,Integer port) throws IOException {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timesuffix = sdf.format(date);
        String docFileName = null;
        String htmFileName = null;
        if(".doc".equals(type)){
            docFileName = "doc_" + timesuffix + ".doc";
            htmFileName = "doc_" + timesuffix + ".pdf";
        }else if(".docx".equals(type)){
            docFileName = "docx_" + timesuffix + ".docx";
            htmFileName = "docx_" + timesuffix + ".pdf";
        }else if(".xls".equals(type)){
            docFileName = "xls_" + timesuffix + ".xls";
            htmFileName = "xls_" + timesuffix + ".pdf";
        }else if(".xlsx".equals(type)){
            docFileName = "xlsx_" + timesuffix + ".xlsx";
            htmFileName = "xlsx_" + timesuffix + ".pdf";
        }else if(".ppt".equals(type)){
            docFileName = "ppt_" + timesuffix + ".ppt";
            htmFileName = "ppt_" + timesuffix + ".pdf";
        }else if(".pptx".equals(type)){
            docFileName = "pptx_" + timesuffix + ".pptx";
            htmFileName = "pptx_" + timesuffix + ".pdf";
        }else{
            return null;
        }
        String htmlFilePath = toFilePath + File.separatorChar + fileName+"_"+htmFileName;
        String docFilePath = toFilePath + File.separatorChar + fileName+"_"+docFileName;
        File htmlOutputFile = new File(htmlFilePath);
        File docInputFile = new File(docFilePath);
        if (htmlOutputFile.exists())
            htmlOutputFile.delete();
        htmlOutputFile.createNewFile();
        if (docInputFile.exists())
            docInputFile.delete();
        docInputFile.createNewFile();
        /*** 由fromFileInputStream构建输入文件  */
        try {
            OutputStream os = new FileOutputStream(docInputFile);
            int bytesRead = 0;
            byte[] buffer = new byte[1024 * 8];
            while ((bytesRead = fromFileInputStream.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            fromFileInputStream.close();
        } catch (IOException e) {
        }
        // 连接服务
        OpenOfficeConnection connection = new SocketOpenOfficeConnection(host,port);
        try {
            connection.connect();
            log.info("》》》》》》》》》》》》》》》》》》》》》》》》》》》》》连接openoffice服务成功！");
        } catch (ConnectException e) {
            e.printStackTrace();
            htmlOutputFile.delete();
            docInputFile.delete();
            log.error("》》》》》》》》》》》》》》》》》》》》》》》》》》》》》文件转换出错，请检查OpenOffice服务是否启动。");
            throw new ServiceException("》》》》》》》》》》》》》》》》》》》》》》》》》》》》》文件转换出错，请检查OpenOffice服务是否启动。");
        }
        // convert 转换
//        DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
        DocumentConverter converter = new StreamOpenOfficeDocumentConverter(connection);
        converter.convert(docInputFile, htmlOutputFile);
        log.info("openoffice的pdf转换完成！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！");
        connection.disconnect();
        // 转换完之后删除word文件
        docInputFile.delete();
        return htmlFilePath;
    }

    /**文件转换成Html*/
    public String file2Html (InputStream fromFileInputStream, String toFilePath,String type,String fileName,String host,Integer port) throws IOException{
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timesuffix = sdf.format(date);
        String docFileName = null;
        String htmFileName = null;
        if("doc".equals(type)){
            docFileName = timesuffix.concat(".doc");
            htmFileName = timesuffix.concat(".html");
        }else if("docx".equals(type)){
            docFileName = timesuffix.concat(".docx");
            htmFileName = timesuffix.concat(".html");
        }else if("xls".equals(type)){
            docFileName = timesuffix.concat(".xls");
            htmFileName = timesuffix.concat(".html");
        }else if("xlsx".equals(type)){
            docFileName = timesuffix.concat(".xlsx");
            htmFileName = timesuffix.concat(".html");
        }else if("ppt".equals(type)){
            docFileName = timesuffix.concat(".ppt");
            htmFileName = timesuffix.concat(".html");
        }else if("pptx".equals(type)){
            docFileName = timesuffix.concat(".pptx");
            htmFileName = timesuffix.concat(".html");
        }else if("txt".equals(type)){
            docFileName = timesuffix.concat(".txt");
            htmFileName = timesuffix.concat(".html");
        }else if("pdf".equals(type)){
            docFileName = timesuffix.concat(".pdf");
            htmFileName = timesuffix.concat(".html");
        }else{
            return null;
        }
        String htmlFilePath = toFilePath + File.separatorChar + fileName+"_"+htmFileName;
        String docFilePath = toFilePath + File.separatorChar + fileName+"_"+docFileName;
        File htmlOutputFile = new File(htmlFilePath);
        File docInputFile = new File(docFilePath);
        if (htmlOutputFile.exists()){
            htmlOutputFile.delete();
        }
        htmlOutputFile.createNewFile();
        docInputFile.createNewFile();
        /**
         * 由fromFileInputStream构建输入文件
         */
        int bytesRead = 0;
        byte[] buffer = new byte[1024 * 8];
        OutputStream os = new FileOutputStream(docInputFile);
        while ((bytesRead = fromFileInputStream.read(buffer)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.close();
        fromFileInputStream.close();
        OpenOfficeConnection connection = new SocketOpenOfficeConnection(host,port);
        try {
            connection.connect();
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>连接openoffice服务成功！");
        } catch (ConnectException e) {
            e.printStackTrace();
            htmlOutputFile.delete();
            docInputFile.delete();
            log.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>文件转换出错，请检查OpenOffice服务是否启动。");
            throw new ServiceException(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>文件转换出错，请检查OpenOffice服务是否启动。");
        }
        // convert
        DocumentConverter converter = new StreamOpenOfficeDocumentConverter(connection);
        converter.convert(docInputFile, htmlOutputFile);
        connection.disconnect();
        // 转换完之后删除word文件
        docInputFile.delete();
        return htmlFilePath;
    }
    /*public static void main(String[] args) throws IOException {
        Office2HtmlOrPdfUtil coc2HtmlUtil = getDoc2HtmlUtilInstance ();
        File file = null;
        FileInputStream fileInputStream = null;
        file = new File("D:\\logs\\网络教育学院2019年6月网络安全值班时间及工作安排.doc");
        fileInputStream = new FileInputStream(file);
        String fileName = coc2HtmlUtil.file2pdf(fileInputStream, "D:\\logs","网络教育学院2019年6月网络安全值班时间及工作安排",".doc","192.168.15.154",8100);
        System.out.println(fileName);
    }*/

}