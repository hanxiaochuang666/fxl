package com.by.blcu.test;

import com.by.blcu.manager.common.FtpClientHelper;
import com.by.blcu.manager.common.RegexHelper;
import com.by.blcu.manager.common.ThymeleafHelper;
import com.by.blcu.manager.model.ManagerOrganization;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ThymeleafApplicationTests {

    @Resource
    private FtpClientHelper ftpClientHelper;
    @Resource
    private ThymeleafHelper thymeleafHelper;

    /**
     * 生成本地文件
     */
    @Test
    public void orgLocalTest() {
        //模板相对路径
        String prefix="static/org/";
        //模板名
        String template="organ_login";
        //模板后缀
        String suffix=".html";

        //本地根路径
        String path = "D:/tongshi/201911/非学历/管理平台/机构首页/2019fxlpt/2019fxlpt/student/";
        //本地生成文件全路径
        String target=path+template+".html";

        ManagerOrganization model=new ManagerOrganization();
        model.setOrgCode("1234");
        model.setOrganizationLogo("https://www.baidu.com/img/bd_logo1.png");
        model.setWebPic("https://desk-fd.zol-img.com.cn/t_s960x600c5/g5/M00/0B/09/ChMkJ13TYESIfD9TAAyL4p28fE4AAvTuQGJ2dIADIv6663.jpg");

        Map<String,Object> data=new HashMap<>();
        data.put("orgModel",model);

        try{
            thymeleafHelper.process(prefix,suffix,template,target,data);
        }
        catch (Exception ex){

        }
    }

    /**
     * 生成网络文件
     */
    @Test
    public void orgFtpTest() {
        //模板相对路径
        String prefix="static/org/";
        //模板名
        String template="organ_login";
        //模板后缀
        String suffix=".html";

        //本地根路径
        String path = "D:/tongshi/201911/非学历/管理平台/机构首页/2019fxlpt/2019fxlpt/student/";
        //本地生成文件全路径
        String targetLocal=path+template+".html";

        //FTP 相对路径
        String ftpPaht="2019fxlpt/student";
        String ftpName= template+".html";

        ManagerOrganization model=new ManagerOrganization();
        model.setOrgCode("1234");
        model.setOrganizationLogo("https://www.baidu.com/img/bd_logo1.png");
        model.setWebPic("https://desk-fd.zol-img.com.cn/t_s960x600c5/g5/M00/0B/09/ChMkJ13TYESIfD9TAAyL4p28fE4AAvTuQGJ2dIADIv6663.jpg");

        Map<String,Object> data=new HashMap<>();
        data.put("orgModel",model);

        //region 生成到网络

        try{
            //本地生成
            thymeleafHelper.process(prefix,suffix,template,targetLocal,data);
            //网络上传
            ftpClientHelper.uploadFile(ftpPaht,ftpName,targetLocal);
            System.out.println("ok");
        }
        catch (Exception ex){

        }

        //endregion
    }


    private static final String regexPassword = "^(?![^A-Za-z]+$)(?![^0-9]+$)[\\da-zA-Z]{6,12}$";

    @Test
    public void Ghc(){
        String password="123456";
        if(!RegexHelper.CheckPassword(password)){
           System.out.println("1");
        }
        password="abceedddd";
        if(!RegexHelper.CheckPassword(password)){
            System.out.println("2");
        }
        password="123adgcc";
        if(!RegexHelper.CheckPassword(password)){
            System.out.println("3");
        }
    }

    @Test
    public void downLoad(){
        String fileUrl="https://www.baidu.com/img/bd_logo1.png";
       try{
           downloadNet(fileUrl);
       }
       catch (Exception ex){

       }
    }

    public void downloadNet(String fileUrl) throws MalformedURLException {
        // 下载网络文件
        int bytesum = 0;
        int byteread = 0;

        URL url = new URL(fileUrl);

        try {
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();
            FileOutputStream fs = new FileOutputStream("d:/ghc/wo.png");

            byte[] buffer = new byte[1204];
            int length;
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;
                System.out.println(bytesum);
                fs.write(buffer, 0, byteread);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
