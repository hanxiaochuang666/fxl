package com.by.blcu.manager.common;

import com.by.blcu.core.utils.ChineseCharacterUtil;
import com.by.blcu.manager.model.ManagerOrganization;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 静态页面生成类(thymeleaf)
 */

@Service
public class ThymeleafHelper {

    @Resource
    private HttpHelper httpHelper;

    /**
     *
     * @param prefix    模板文件前缀，即模板文件所在的目录，相对于根目录。如：static/templates/
     * @param suffix    模板文件后缀，即文件类型。如：.html
     * @param template  模板文件名。不包含文件的前缀目录和后缀.扩展名
     * @param target    目标文件的绝对地址.eg:/data/my/2018121901/test/index.html
     * @param data      数据模型：传递给页面的数据
     * @throws IOException
     */
    public void process(String prefix,String suffix, String template, String target, Map<String, Object> data) throws IOException {
        try{
            //模板设置
            ClassLoaderTemplateResolver resolver=new ClassLoaderTemplateResolver();
            resolver.setPrefix(prefix);
            resolver.setSuffix(suffix);
            TemplateEngine templateEngine=new TemplateEngine();
            templateEngine.setTemplateResolver(resolver);
            //数据模型
            Context context=new Context();
            context.setVariables(data);
            //根据模板，生成文件
            FileWriter writer=new FileWriter(target);
            templateEngine.process(template,context,writer);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    //region 机构生成

    public boolean generatorOrgIdex(ManagerOrganization organization){
        if(organization==null){
            return false;
        }

        //模板相对路径
        String prefix="static/org/";
        //模板名
        String template="organ_login";
        //模板后缀
        String suffix=".html";

        //获取当前jar 的执行路径
        ApplicationHome home = new ApplicationHome(ThymeleafHelper.class);
        File jarFile = home.getSource();
        String parent = jarFile.getParent();
        System.out.println(parent);

        //机构目录
        String orgNameShot = ChineseCharacterUtil.getLowerCase(organization.getOrganizationName()+"_"+organization.getOrgCode(),false);
        //本地根路径
        String path = parent+ "/suborg/" +orgNameShot+"/";
        //本地生成文件全路径
        String targetIndex=path+"student/"+template+".html";

        Map<String,Object> data=new HashMap<>();

        try{
            //生成路径
            File imgPath = new File(path+"/img/");
            if(!imgPath.exists()){     //判断文件路径是否存在
                imgPath.mkdirs();              //不存在创建新的文件
            }
            File studentPath = new File(path+"/student/");
            if(!studentPath.exists()){     //判断文件路径是否存在
                studentPath.mkdirs();              //不存在创建新的文件
            }

            //下载机构logo
            if(!StringHelper.IsNullOrWhiteSpace(organization.getOrganizationLogo())){
                String fileName = organization.getOrganizationLogo();
                String newName = "logo."+ fileName.substring(fileName.lastIndexOf(".") + 1);
                String logAllName=path+"img/"+newName;;
                httpHelper.downloadNet(organization.getOrganizationLogo(),logAllName);
                organization.setOrganizationLogo("../img/"+newName);
            }
            //下载首页封面
            if(!StringHelper.IsNullOrWhiteSpace(organization.getWebPic())){
                String fileName = organization.getWebPic();
                String newName = "webpic."+fileName.substring(fileName.lastIndexOf(".") + 1);
                String logAllName=path+"img/"+newName;;
                httpHelper.downloadNet(organization.getWebPic(),logAllName);
                organization.setWebPic("../img/"+newName);
            }
        }
        catch (Exception ex){
            return false;
        }

        data.put("orgModel",organization);

        try{
            process(prefix,suffix,template,targetIndex,data);
        }
        catch (Exception ex){
            return false;
        }
        return true;
    }


    //endregion

}
