package com.by.blcu.manager.service.impl;

import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.universal.AbstractService;
import com.by.blcu.core.utils.ApplicationUtils;
import com.by.blcu.mall.service.CourseCategoryInfoService;
import com.by.blcu.mall.vo.CourseCategoryInfoVo;
import com.by.blcu.manager.common.ReflexHelper;
import com.by.blcu.manager.common.StringHelper;
import com.by.blcu.manager.common.UserSessionHelper;
import com.by.blcu.manager.dao.WebNewsCategoryMapper;
import com.by.blcu.manager.model.WebNewsCategory;
import com.by.blcu.manager.model.extend.NewsCategoryAmount;
import com.by.blcu.manager.model.extend.NewsCategoryTree;
import com.by.blcu.manager.model.sql.InputNewsCategory;
import com.by.blcu.manager.service.WebNewsCategoryService;
import com.by.blcu.manager.umodel.CategoryNewsModel;
import com.by.blcu.manager.umodel.NewsLeftCategoryItemModel;
import com.by.blcu.manager.umodel.NewsLeftCategoryModel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
* @Description: WebNewsCategoryService接口实现类
* @author 耿鹤闯
* @date 2019/10/28 17:13
*/
@Service
public class WebNewsCategoryServiceImpl extends AbstractService<WebNewsCategory> implements WebNewsCategoryService {

    @Resource
    private WebNewsCategoryMapper webNewsCategoryMapper;
    @Resource
    private CourseCategoryInfoService courseCategoryInfoService;

    public List<WebNewsCategory> findCategoryList(InputNewsCategory model, UserSessionHelper helper){
        //非本部机构检索
        if(!"1".equals(helper.getOrgType())){
            model.setOrgCode(helper.getOrgCode());
        }
       return webNewsCategoryMapper.findCategoryList(model);
    }
    public Integer findCategoryListCount(InputNewsCategory model, UserSessionHelper helper){
        return webNewsCategoryMapper.findCategoryListCount(model);
    }
    public RetResult<Integer> deleteCategoryByIdList(InputNewsCategory model, UserSessionHelper helper){
        if(model==null ||  StringHelper.IsNullOrEmpty(model.getCategoryIdList())){
            return RetResponse.makeErrRsp("[新闻Id列表]不能为空");
        }

        //检查分类下是否存在新闻
        InputNewsCategory checkModel =new InputNewsCategory();
        checkModel.setCategoryIdList(model.getCategoryIdList());
        Integer checkResult  =webNewsCategoryMapper.checkNewsR(checkModel);
        if(checkResult!=null &&  checkResult>0){
            return RetResponse.makeErrRsp("分类下有新闻");
        }

        Date dateTime =new Date();
        model.setModifyBy(helper.getUserName());
        model.setModifyTime(dateTime);
        Integer state = webNewsCategoryMapper.deleteCategoryByIdList(model);
        return RetResponse.makeOKRsp(state);
    }

    public RetResult<Integer> addCategory(WebNewsCategory model, UserSessionHelper helper){
        if(model==null || StringHelper.IsNullOrWhiteSpace(model.getCategoryName())){
            return RetResponse.makeErrRsp("[分类名称]不能为空");
        }
        //只一级分类
        model.setParentId("0");

//        if(StringHelper.IsNullOrWhiteSpace(model.getParentId())){
//            model.setParentId("0");
//        }
        //父类id是否存在
        InputNewsCategory search =new InputNewsCategory();
        search.setParentId(model.getParentId());
        List<WebNewsCategory> checkList = webNewsCategoryMapper.checkCategoryList(search);
//        boolean isParentExit=false;
//        if(!model.getParentId().equals("0")){
//            if(!StringHelper.IsNullOrEmpty(checkList)){
//               Optional<WebNewsCategory>  checkResult = checkList.stream().filter(t->t.getCategoryId().equals(model.getParentId())).findAny();
//                if(checkResult.isPresent()){
//                    isParentExit=true;
//                }
//            }
//        }
//        else{
//            isParentExit=true;
//        }
//        if(!isParentExit){
//            return RetResponse.makeErrRsp("[父类Id]不存在");
//        }

        //名称查重
        if(!StringHelper.IsNullOrEmpty(checkList)){
            Optional<WebNewsCategory>  checkName = checkList.stream().filter(t->t.getCategoryName().equals(model.getCategoryName())).findAny();
            if(checkName.isPresent()){
                return RetResponse.makeErrRsp("[分类名称]已存在");
            }
        }

        //排序
        Integer sort =1;
        if(!StringHelper.IsNullOrEmpty(checkList)){
            Optional<WebNewsCategory>  maxSortOpt = checkList.stream().max(Comparator.comparing(WebNewsCategory :: getSort));
            if(maxSortOpt.isPresent()){
                sort=maxSortOpt.get().getSort()+1;
            }
        }
        model.setSort(sort);

        if(StringHelper.IsNullOrZero(model.getStatus())){
            model.setStatus(1);
        }

        model.setCategoryId(ApplicationUtils.getUUID());
        model.setOrgCode(helper.getOrgCode());
        Date datetime =new Date();
        model.setCreateTime(datetime);
        model.setCreateBy(helper.getUserName());
        model.setModifyTime(datetime);
        model.setModifyBy(helper.getUserName());
        Integer state = webNewsCategoryMapper.insert(model);

        return RetResponse.makeOKRsp(state);
    }
    public RetResult<Integer> updateCategory(WebNewsCategory model, UserSessionHelper helper){
        if(model==null || StringHelper.IsNullOrWhiteSpace(model.getCategoryId())){
            return RetResponse.makeErrRsp("[新闻分类Id]不能为空");
        }
        //只一级分类
        model.setParentId("0");

        //父类id是否存在
        InputNewsCategory search =new InputNewsCategory();
        search.setParentId(model.getParentId());
        search.setCategoryId(model.getCategoryId());
        List<WebNewsCategory> checkList = webNewsCategoryMapper.checkCategoryList(search);
        if(StringHelper.IsNullOrEmpty(checkList)){
            return RetResponse.makeErrRsp("[新闻分类Id]不存在");
        }

//        boolean isParentExit=false;
//        if(!model.getParentId().equals("0")){
//            Optional<WebNewsCategory>  checkResult = checkList.stream().filter(t->t.getCategoryId().equals(model.getParentId())).findAny();
//            if(checkResult.isPresent()){
//                isParentExit=true;
//            }
//        }
//        else{
//            isParentExit=true;
//        }
//        if(!isParentExit){
//            return RetResponse.makeErrRsp("[父类Id]不存在");
//        }

        //名称查重
        if(!StringHelper.IsNullOrEmpty(checkList)){
            Optional<WebNewsCategory>  checkName = checkList.stream().filter(t->t.getCategoryName().equals(model.getCategoryName()) && !t.getCategoryId().equals(model.getCategoryId())).findAny();
            if(checkName.isPresent()){
                return RetResponse.makeErrRsp("[分类名称]已存在");
            }
        }
        model.setOrgCode(helper.getOrgCode());
        Date datetime =new Date();
        model.setCreateTime(null);
        model.setCreateBy(null);
        model.setModifyTime(datetime);
        model.setModifyBy(helper.getUserName());
        Integer state = webNewsCategoryMapper.updateByPrimaryKeySelective(model);

        return RetResponse.makeOKRsp(state);
    }

   //region 上下移动

    public RetResult<Integer>  updateUpSort(WebNewsCategory model, UserSessionHelper helper) {
        if(model==null || StringHelper.IsNullOrWhiteSpace(model.getCategoryId())){
            return RetResponse.makeErrRsp("[新闻分类Id]不能为空");
        }
        InputNewsCategory search=new InputNewsCategory();
        search.setCategoryId(model.getCategoryId());
        List<WebNewsCategory> thisLeveList =webNewsCategoryMapper.getCategoryThisLevel(search);
        if(StringHelper.IsNullOrEmpty(thisLeveList)){
            return RetResponse.makeErrRsp("[新闻分类Id]不存在");
        }
        //当前分类标识
        WebNewsCategory thisModel = thisLeveList.stream().filter(t->t.getCategoryId().equals(model.getCategoryId())).findFirst().get();
        Integer thisIndex = thisLeveList.indexOf(thisModel);
        if(thisIndex>0){
            //不是第一个
            WebNewsCategory preModel = thisLeveList.get(thisIndex-1);
            Integer preSort = preModel.getSort();
            Integer thisSort = thisModel.getSort();
            //更新前一条记录
            preModel.setSort(thisSort);
            webNewsCategoryMapper.updateByPrimaryKeySelective(preModel);
            //更新当前记录
            thisModel.setSort(preSort);
            webNewsCategoryMapper.updateByPrimaryKeySelective(thisModel);
            return RetResponse.makeOKRsp(2);
        }
        else{
            return RetResponse.makeErrRsp("已经置顶！");
        }
    }

    public RetResult<Integer>  updateDownSort(WebNewsCategory model, UserSessionHelper helper) {
        if(model==null || StringHelper.IsNullOrWhiteSpace(model.getCategoryId())){
            return RetResponse.makeErrRsp("[新闻分类Id]不能为空");
        }
        InputNewsCategory search=new InputNewsCategory();
        search.setCategoryId(model.getCategoryId());
        List<WebNewsCategory> thisLeveList =webNewsCategoryMapper.getCategoryThisLevel(search);
        if(StringHelper.IsNullOrEmpty(thisLeveList)){
            return RetResponse.makeErrRsp("[新闻分类Id]不存在");
        }
        //当前分类标识
        WebNewsCategory thisModel = thisLeveList.stream().filter(t->t.getCategoryId().equals(model.getCategoryId())).findFirst().get();
        Integer thisIndex = thisLeveList.indexOf(thisModel);
        if(thisIndex<thisLeveList.size()-1){
            //不是最后一个
            WebNewsCategory nextModel = thisLeveList.get(thisIndex+1);
            Integer nextSort = nextModel.getSort();
            Integer thisSort = thisModel.getSort();
            //更新后一条记录
            nextModel.setSort(thisSort);
            webNewsCategoryMapper.updateByPrimaryKeySelective(nextModel);
            //更新当前记录
            thisModel.setSort(nextSort);
            webNewsCategoryMapper.updateByPrimaryKeySelective(thisModel);
            return RetResponse.makeOKRsp(2);
        }
        else{
            return RetResponse.makeErrRsp("已经到底！");
        }
    }

    //endregion

    //region 操作树

    public List<NewsCategoryTree> selectNewsCategoryTree(InputNewsCategory model, UserSessionHelper helper){
        InputNewsCategory searchModel =new InputNewsCategory();
        List<WebNewsCategory> categoryListAll = webNewsCategoryMapper.findCategoryList(searchModel);
        if(categoryListAll==null || categoryListAll.isEmpty()){
            return null;
        }
        List<NewsCategoryTree> resultList =new ArrayList<NewsCategoryTree>();
        List<WebNewsCategory> firstList =null;
        if(StringHelper.IsNullOrWhiteSpace(model.getParentId())){
            firstList = categoryListAll.stream().filter(t->t.getParentId()==null || t.getParentId().equals("") || t.getParentId().equals("0")) .collect(Collectors.toList());
        }
        else{
            firstList = categoryListAll.stream().filter(t->t.getParentId().equals(model.getParentId())).collect(Collectors.toList());
        }

        if(firstList!=null && !firstList.isEmpty()){
            for(WebNewsCategory item : firstList){
                if(item!=null){
                    NewsCategoryTree treeItem = new NewsCategoryTree();
                    try{
                        ReflexHelper.Copy(item,treeItem);
                    }catch (Exception ex){

                    }
                    resultList.add(treeItem);
                }
            }
            for(NewsCategoryTree item:resultList) {
                item.setChildren(selectNewsCategoryTreeChildren(categoryListAll,item));
            }
        }
        return resultList;
    }

    private List<NewsCategoryTree> selectNewsCategoryTreeChildren(List<WebNewsCategory> categoryListAll, NewsCategoryTree parentModel){
        if(parentModel==null){
            return null;
        }
        List<NewsCategoryTree> resultList =new ArrayList<NewsCategoryTree>();
        List<WebNewsCategory> firstList = categoryListAll.stream().filter(t->t.getParentId()!=null && t.getParentId().equals(parentModel.getCategoryId())).collect(Collectors.toList());
        if(firstList!=null && !firstList.isEmpty()){
            for(WebNewsCategory item : firstList){
                if(item!=null){
                    NewsCategoryTree treeItem = new NewsCategoryTree();
                    try{
                        ReflexHelper.Copy(item,treeItem);
                    }catch (Exception ex){

                    }
                    resultList.add(treeItem);
                }
            }
            for(NewsCategoryTree item:resultList) {
                item.setChildren(selectNewsCategoryTreeChildren(categoryListAll,item));
            }
        }
        return resultList;
    }

    //endregion


    //region 左侧分类

    public  List<NewsLeftCategoryModel> getLeftCategory(InputNewsCategory model, UserSessionHelper helper){
        List<NewsLeftCategoryModel> result=new ArrayList<>();

        //获取分类
        List<CourseCategoryInfoVo> categoryList = courseCategoryInfoService.selectListRecursion();

        InputNewsCategory search=new InputNewsCategory();
        List<NewsCategoryAmount> newsList =webNewsCategoryMapper.getCategoryAmount(search);

        if(!StringHelper.IsNullOrEmpty(categoryList)){
            //一级分类
            categoryList.forEach(item->{
                //二级分类
                if(!StringHelper.IsNullOrEmpty(item.getChildren())){
                    List<CategoryNewsModel> resultList2=new ArrayList<>();
                    item.getChildren().forEach(item2->{
                        NewsLeftCategoryModel leftCategory =new NewsLeftCategoryModel();
                        leftCategory.setCcId1(item.getCcId());
                        leftCategory.setCcName1(item.getCcName());
                        leftCategory.setCcId2(item2.getCcId());
                        leftCategory.setCcName2(item2.getCcName());

                        if(!StringHelper.IsNullOrEmpty(newsList)){
                            List<NewsCategoryAmount> amountModelList = newsList.stream().filter(t->t.getCcId2().equals(item2.getCcId())).collect(Collectors.toList());
                            if(!StringHelper.IsNullOrEmpty(amountModelList)){
                                List<NewsLeftCategoryItemModel> categoryItemList =new ArrayList<>();
                                amountModelList.forEach(t->{
                                    NewsLeftCategoryItemModel tmpModel =new NewsLeftCategoryItemModel();
                                    tmpModel.setCategoryId(t.getCategoryId());
                                    tmpModel.setCategoryName(t.getCategoryName());
                                    tmpModel.setAmount(t.getAmount());
                                    categoryItemList.add(tmpModel);
                                });
                                leftCategory.setCategoryList(categoryItemList);
                            }
                        }
                        result.add(leftCategory);

                    });

                }
            });
        }

        return  result;
    }

    //endregion

}