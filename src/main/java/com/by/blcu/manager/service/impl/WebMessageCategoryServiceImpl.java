package com.by.blcu.manager.service.impl;

import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.universal.AbstractService;
import com.by.blcu.core.utils.ApplicationUtils;
import com.by.blcu.manager.common.ReflexHelper;
import com.by.blcu.manager.common.StringHelper;
import com.by.blcu.manager.common.UserSessionHelper;
import com.by.blcu.manager.dao.WebMessageCategoryMapper;
import com.by.blcu.manager.model.WebMessage;
import com.by.blcu.manager.model.WebMessageCategory;
import com.by.blcu.manager.model.WebMessageCategory;
import com.by.blcu.manager.model.WebNewsCategory;
import com.by.blcu.manager.model.extend.NewsCategoryTree;
import com.by.blcu.manager.model.sql.InputMessage;
import com.by.blcu.manager.model.sql.InputMessageCategory;
import com.by.blcu.manager.model.sql.InputMessageCategory;
import com.by.blcu.manager.model.sql.InputNewsCategory;
import com.by.blcu.manager.service.WebMessageCategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
* @Description: WebMessageCategoryService接口实现类
* @author 耿鹤闯
* @date 2019/09/19 10:19
*/
@Service
public class WebMessageCategoryServiceImpl extends AbstractService<WebMessageCategory> implements WebMessageCategoryService {

    @Resource
    private WebMessageCategoryMapper webMessageCategoryMapper;

    public List<WebMessageCategory> findCategoryList(InputMessageCategory model, UserSessionHelper helper){
        return webMessageCategoryMapper.findCategoryList(model);
    }
    public Integer findCategoryListCount(InputMessageCategory model, UserSessionHelper helper){
        return webMessageCategoryMapper.findCategoryListCount(model);
    }
    public RetResult<Integer> deleteCategoryByIdList(InputMessageCategory model, UserSessionHelper helper){
        if(model==null ||  StringHelper.IsNullOrEmpty(model.getCategoryIdList())){
            return RetResponse.makeErrRsp("[消息分类Id]不能为空");
        }

        //检查分类下是否存在新闻
        InputMessageCategory checkModel =new InputMessageCategory();
        checkModel.setCategoryIdList(model.getCategoryIdList());
        Integer checkResult  =webMessageCategoryMapper.checkMessageR(checkModel);
        if(checkResult!=null &&  checkResult>0){
            return RetResponse.makeErrRsp("分类下有消息");
        }

        Date dateTime =new Date();
        model.setModifyBy(helper.getUserName());
        model.setModifyTime(dateTime);
        Integer state =  webMessageCategoryMapper.deleteCategoryByIdList(model);
        return RetResponse.makeOKRsp(state);
    }

    public RetResult<Integer> addCategory(WebMessageCategory model, UserSessionHelper helper){
        if(model==null || StringHelper.IsNullOrWhiteSpace(model.getCategoryName())){
            return RetResponse.makeErrRsp("[分类名称]不能为空");
        }

        model.setParentId("0");

        //父类id是否存在
        InputMessageCategory search =new InputMessageCategory();
        search.setParentId(model.getParentId());
        List<WebMessageCategory> checkList = webMessageCategoryMapper.checkCategoryList(search);

        //名称查重
        if(!StringHelper.IsNullOrEmpty(checkList)){
            Optional<WebMessageCategory> checkName = checkList.stream().filter(t->t.getCategoryName().equals(model.getCategoryName())).findAny();
            if(checkName.isPresent()){
                return RetResponse.makeErrRsp("[分类名称]已存在");
            }
        }

        //排序
        Integer sort =1;
        if(!StringHelper.IsNullOrEmpty(checkList)){
            Optional<WebMessageCategory>  maxSortOpt = checkList.stream().max((p1, p2) -> p1.getSort().compareTo(p2.getSort()));
            if(maxSortOpt.isPresent()){
                sort=maxSortOpt.get().getSort()+1;
            }
        }
        model.setSort(sort);

        if(StringHelper.IsNullOrZero(model.getStatus())){
            model.setStatus(1);
        }
        model.setCategoryId(ApplicationUtils.getUUID());

        model.setClassLayer(1);
        Date datetime =new Date();
        model.setCreateTime(datetime);
        model.setCreateBy(helper.getUserName());
        model.setModifyTime(datetime);
        model.setModifyBy(helper.getUserName());
        Integer state = webMessageCategoryMapper.insert(model);

        return RetResponse.makeOKRsp(state);
    }

    public RetResult<Integer> updateCategory(WebMessageCategory model, UserSessionHelper helper){
        if(model==null || StringHelper.IsNullOrWhiteSpace(model.getCategoryId())){
            return RetResponse.makeErrRsp("[消息分类Id]不能为空");
        }
        //只一级分类
        model.setParentId("0");

        InputMessageCategory checkModel =new InputMessageCategory();
        checkModel.setParentId(model.getParentId());
        checkModel.setCategoryId(model.getCategoryId());
        List<WebMessageCategory> checkList = webMessageCategoryMapper.checkCategoryList(checkModel);
        if(StringHelper.IsNullOrEmpty(checkList)){
            return RetResponse.makeErrRsp("[消息分类Id]不存在");
        }

        //名称查重
        if(!StringHelper.IsNullOrEmpty(checkList)){
            Optional<WebMessageCategory>  checkName = checkList.stream().filter(t->t.getCategoryName().equals(model.getCategoryName()) && !t.getCategoryId().equals(model.getCategoryId())).findAny();
            if(checkName.isPresent()){
                return RetResponse.makeErrRsp("[分类名称]已存在");
            }
        }

        Date datetime =new Date();
        model.setCreateTime(null);
        model.setCreateBy(null);
        model.setModifyTime(datetime);
        model.setModifyBy(helper.getUserName());
        Integer state = webMessageCategoryMapper.updateByPrimaryKeySelective(model);

        return RetResponse.makeOKRsp(state);
    }
    public WebMessageCategory selecCategoryById(String categoryId,UserSessionHelper helper){
        if(StringHelper.IsNullOrWhiteSpace(categoryId)){
            return null;
        }
        InputMessageCategory checkModel =new InputMessageCategory();
        checkModel.setCategoryId(categoryId);
        List<WebMessageCategory> list = webMessageCategoryMapper.findCategoryList(checkModel);
        if(StringHelper.IsNullOrEmpty(list)){
            return null;
        }
        return list.get(0);
    }

    //region 上下移动

    public RetResult<Integer>  updateUpSort(WebMessageCategory model, UserSessionHelper helper) {
        if(model==null || StringHelper.IsNullOrWhiteSpace(model.getCategoryId())){
            return RetResponse.makeErrRsp("[消息分类Id]不能为空");
        }
        InputMessageCategory search=new InputMessageCategory();
        search.setCategoryId(model.getCategoryId());
        List<WebMessageCategory> thisLeveList =webMessageCategoryMapper.getCategoryThisLevel(search);
        if(StringHelper.IsNullOrEmpty(thisLeveList)){
            return RetResponse.makeErrRsp("[消息分类Id]不存在");
        }
        //当前分类标识
        WebMessageCategory thisModel = thisLeveList.stream().filter(t->t.getCategoryId().equals(model.getCategoryId())).findFirst().get();
        Integer thisIndex = thisLeveList.indexOf(thisModel);
        if(thisIndex>0){
            //不是第一个
            WebMessageCategory preModel = thisLeveList.get(thisIndex-1);
            Integer preSort = preModel.getSort();
            Integer thisSort = thisModel.getSort();
            //更新前一条记录
            preModel.setSort(thisSort);
            webMessageCategoryMapper.updateByPrimaryKeySelective(preModel);
            //更新当前记录
            thisModel.setSort(preSort);
            webMessageCategoryMapper.updateByPrimaryKeySelective(thisModel);
            return RetResponse.makeOKRsp(2);
        }
        else{
            return RetResponse.makeErrRsp("已经置顶！");
        }
    }

    public RetResult<Integer>  updateDownSort(WebMessageCategory model, UserSessionHelper helper) {
        if(model==null || StringHelper.IsNullOrWhiteSpace(model.getCategoryId())){
            return RetResponse.makeErrRsp("[消息分类Id]不能为空");
        }
        InputMessageCategory search=new InputMessageCategory();
        search.setCategoryId(model.getCategoryId());
        List<WebMessageCategory> thisLeveList =webMessageCategoryMapper.getCategoryThisLevel(search);
        if(StringHelper.IsNullOrEmpty(thisLeveList)){
            return RetResponse.makeErrRsp("[消息分类Id]不存在");
        }
        //当前分类标识
        WebMessageCategory thisModel = thisLeveList.stream().filter(t->t.getCategoryId().equals(model.getCategoryId())).findFirst().get();
        Integer thisIndex = thisLeveList.indexOf(thisModel);
        if(thisIndex<thisLeveList.size()-1){
            //不是最后一个
            WebMessageCategory nextModel = thisLeveList.get(thisIndex+1);
            Integer nextSort = nextModel.getSort();
            Integer thisSort = thisModel.getSort();
            //更新后一条记录
            nextModel.setSort(thisSort);
            webMessageCategoryMapper.updateByPrimaryKeySelective(nextModel);
            //更新当前记录
            thisModel.setSort(nextSort);
            webMessageCategoryMapper.updateByPrimaryKeySelective(thisModel);
            return RetResponse.makeOKRsp(2);
        }
        else{
            return RetResponse.makeErrRsp("已经到底！");
        }
    }

    //endregion


}