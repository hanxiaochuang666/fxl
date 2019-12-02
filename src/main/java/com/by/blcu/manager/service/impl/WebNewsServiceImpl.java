package com.by.blcu.manager.service.impl;

import com.alibaba.fastjson.JSON;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.utils.ApplicationUtils;
import com.by.blcu.mall.service.CourseCategoryInfoService;
import com.by.blcu.mall.vo.CourseCategoryInfoVo;
import com.by.blcu.manager.common.ManagerHelper;
import com.by.blcu.manager.common.StringHelper;
import com.by.blcu.manager.common.UserSessionHelper;
import com.by.blcu.manager.dao.WebNewsMapper;
import com.by.blcu.manager.model.WebNews;
import com.by.blcu.manager.model.sql.InputNews;
import com.by.blcu.manager.service.WebNewsService;
import com.by.blcu.core.universal.AbstractService;
import com.by.blcu.manager.umodel.CategoryNewsModel;
import com.by.blcu.manager.umodel.NewsTitleModel;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;
import springfox.documentation.spring.web.json.Json;

import javax.annotation.Resource;
import javax.xml.bind.util.JAXBSource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 耿鹤闯
 * @Description: WebNewsService接口实现类
 * @date 2019/10/28 17:13
 */
@Service
public class WebNewsServiceImpl extends AbstractService<WebNews> implements WebNewsService {

    @Resource
    private WebNewsMapper webNewsMapper;
    @Resource
    private CourseCategoryInfoService courseCategoryInfoService;

    public List<WebNews> findNewsList(InputNews model, UserSessionHelper helper) {
        if(!helper.getOrgType().equals(ManagerHelper.OrgType)){
            model.setOrgCode(helper.getOrgCode());
        }
        List<WebNews> result = webNewsMapper.findNewsList(model);
        if(!StringHelper.IsNullOrEmpty(result)){
            result.forEach(item->{
                item.setContent(HtmlUtils.htmlUnescape(item.getContent()));
            });
        }
        return result;
    }

    public List<WebNews> findNewsListPage(InputNews model, UserSessionHelper helper) {
        if(!helper.getOrgType().equals(ManagerHelper.OrgType)){
            model.setOrgCode(helper.getOrgCode());
        }
        List<WebNews> result = webNewsMapper.findNewsListPage(model);
        if(!StringHelper.IsNullOrEmpty(result)){
            result.forEach(item->{
                item.setContent(HtmlUtils.htmlUnescape(item.getContent()));
            });
        }
        return result;
    }

    public Integer findNewsListCount(InputNews model, UserSessionHelper helper) {
        if(!helper.getOrgType().equals(ManagerHelper.OrgType)){
            model.setOrgCode(helper.getOrgCode());
        }
        return webNewsMapper.findNewsListCount(model);
    }

    public RetResult<Integer> deleteNewsByIdList(InputNews model, UserSessionHelper helper) {
        if(model==null ||  StringHelper.IsNullOrEmpty(model.getNewsIdList())){
            return RetResponse.makeErrRsp("[新闻Id列表]不能为空");
        }
        Date dateTime =new Date();
        model.setModifyBy(helper.getUserName());
        model.setModifyTime(dateTime);
        if(!helper.getOrgType().equals(ManagerHelper.OrgType)){
            model.setOrgCode(helper.getOrgCode());
        }
        Integer state = webNewsMapper.deleteNewsByIdList(model);
        return RetResponse.makeOKRsp(state);
    }

    public RetResult<Integer> addNews(WebNews model, UserSessionHelper helper) {
        if(model==null || StringHelper.IsNullOrWhiteSpace(model.getTitle()) || StringHelper.IsNullOrWhiteSpace(model.getContent())
                || StringHelper.IsNullOrWhiteSpace(model.getCategoryId()) ){
            return RetResponse.makeErrRsp("[标题，内容，新闻类别]不能为空");
        }
        if(StringHelper.IsNullOrZero(model.getSort())){
            model.setSort(0);
        }
        if(!helper.getOrgType().equals(ManagerHelper.OrgType)){
            model.setOrgCode(helper.getOrgCode());
        }
        model.setContent(HtmlUtils.htmlEscape(model.getContent(), "UTF-8"));
        model.setNewsId(ApplicationUtils.getUUID());
        Date datetime =new Date();
        model.setCreateTime(datetime);
        model.setCreateBy(helper.getUserName());
        model.setModifyTime(datetime);
        model.setModifyBy(helper.getUserName());

        Integer state =webNewsMapper.insert(model);
        return RetResponse.makeOKRsp(state);
    }

    public RetResult<Integer> updateNews(WebNews model, UserSessionHelper helper) {
        if(model==null || StringHelper.IsNullOrWhiteSpace(model.getNewsId())){
            return RetResponse.makeErrRsp("[新闻Id]不能为空");
        }
        InputNews checkModel =new InputNews();
        checkModel.setNewsId(model.getNewsId());
        if(!helper.getOrgType().equals(ManagerHelper.OrgType)){
            checkModel.setOrgCode(helper.getOrgCode());
        }
        Integer exit = webNewsMapper.findNewsListCount(checkModel);

        if(exit<1){
            return RetResponse.makeErrRsp("新闻不存在");
        }
        if(!StringHelper.IsNullOrWhiteSpace(model.getContent())){
            model.setContent(HtmlUtils.htmlEscape(model.getContent(), "UTF-8"));
        }

        Date datetime =new Date();
        model.setOrgCode(null);
        model.setCreateTime(null);
        model.setCreateBy(null);
        model.setModifyTime(datetime);
        model.setModifyBy(helper.getUserName());
        Integer state = webNewsMapper.updateByPrimaryKeySelective(model);

        return RetResponse.makeOKRsp(state);
    }

    public  List<CategoryNewsModel> getNewsIndex(WebNews model, UserSessionHelper helper){
        List<CategoryNewsModel> result=new ArrayList<>();
        Integer pageSize =10; //每个分类获取前几条

        //获取分类
        List<CourseCategoryInfoVo> categoryList = courseCategoryInfoService.selectListRecursion();

        //TODO 各类新闻获取前几条
        InputNews search=new InputNews();
        if(!helper.getOrgType().equals(ManagerHelper.OrgType)){
            search.setOrgCode(helper.getOrgCode());
        }
        List<NewsTitleModel> newsList =webNewsMapper.getNewsTitleList(search);

        if(!StringHelper.IsNullOrEmpty(categoryList)){
            //一级分类
            categoryList.forEach(item->{
                //分类信息
                CategoryNewsModel resultCategory = new CategoryNewsModel();
                resultCategory.setCcId(item.getCcId());
                resultCategory.setCcName(item.getCcName());
                resultCategory.setLevel(item.getLevel());
                resultCategory.setParentId(item.getParentId());
                //新闻内容
                List<NewsTitleModel> tmpList = newsList.stream().filter(t->t.getCcId1()!=null&& t.getCcId2()==null && t.getCcId1().equals(item.getCcId())).collect(Collectors.toList());
                if(!StringHelper.IsNullOrEmpty(tmpList)){
                    resultCategory.setNewsList(tmpList.stream().limit(pageSize).collect(Collectors.toList()));
                }
                else{
                    resultCategory.setNewsList(null);
                }

                //二级分类
                if(!StringHelper.IsNullOrEmpty(item.getChildren())){
                    List<CategoryNewsModel> resultList2=new ArrayList<>();
                    item.getChildren().forEach(item2->{
                        CategoryNewsModel resultCategory2 = new CategoryNewsModel();
                        resultCategory2.setCcId(item2.getCcId());
                        resultCategory2.setCcName(item2.getCcName());
                        resultCategory2.setLevel(item2.getLevel());
                        resultCategory2.setParentId(item2.getParentId());
                        //新闻内容
                        List<NewsTitleModel> tmpList2 = newsList.stream().filter(t->t.getCcId2()!=null && t.getCcId2().equals(item2.getCcId())).collect(Collectors.toList());
                        if(!StringHelper.IsNullOrEmpty(tmpList2)){
                            resultCategory2.setNewsList(tmpList2.stream().limit(pageSize).collect(Collectors.toList()));
                        }else{
                            resultCategory2.setNewsList(null);
                        }
                        resultList2.add(resultCategory2);
                    });
                    resultCategory.setChildren(resultList2);
                }

                //添加
                result.add(resultCategory);
            });
        }

        return  result;
    }

    @Async
    public Integer updateClicks(InputNews search, UserSessionHelper helper){
        if(search==null || StringHelper.IsNullOrWhiteSpace(search.getNewsId()) || StringHelper.IsNullOrZero(search.getClicks())){
           return 0;
        }
        InputNews checkModel =new InputNews();
        checkModel.setNewsId(search.getNewsId());
        checkModel.setClicks(search.getClicks());
        if(!helper.getOrgType().equals(ManagerHelper.OrgType)){
            checkModel.setOrgCode(helper.getOrgCode());
        }
        Integer result = webNewsMapper.updateClicks(checkModel);
        return result;
    }

}