package com.by.blcu.mall.service.impl;

import com.by.blcu.core.utils.MapAndObjectUtils;
import com.by.blcu.mall.dao.CourseCategoryInfoMapper;
import com.by.blcu.mall.service.CourseCategoryInfoService;
import com.by.blcu.mall.model.CourseCategoryInfo;
import com.by.blcu.core.universal.AbstractService;
import com.by.blcu.mall.vo.CourseCategoryInfoVo;
import org.jsoup.internal.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
* @Description: CourseCategoryInfoService接口实现类
* @author 李程
* @date 2019/07/25 15:20
*/
@Service
public class CourseCategoryInfoServiceImpl extends AbstractService<CourseCategoryInfo> implements CourseCategoryInfoService {

    @Resource
    private CourseCategoryInfoMapper courseCategoryInfoMapper;

    @Resource
    private CourseCategoryInfoService courseCategoryInfoService;

    @Override
    public Integer updateUpSort(String ccId) {
        CourseCategoryInfo ccInfo = courseCategoryInfoService.selectById(ccId);
        //查询上一条记录
        CourseCategoryInfo ccInfo1 = courseCategoryInfoMapper.moveUp(ccInfo.getCcSort(),ccInfo.getParentId());
        //最上面的不能上移
        if(null==ccInfo1){return 0;}
        //交换position的值
        int temp=ccInfo.getCcSort();
        ccInfo.setCcSort(ccInfo1.getCcSort());
        ccInfo1.setCcSort(temp);
        //更新到数据库中
        courseCategoryInfoService.update(ccInfo);
        courseCategoryInfoService.update(ccInfo1);
        return 1;
    }

    @Override
    public Integer updateDownSort(String ccId) {
        CourseCategoryInfo ccInfo = courseCategoryInfoService.selectById(ccId);
        //查询下一条记录
        CourseCategoryInfo ccInfo1 = courseCategoryInfoMapper.moveDown(ccInfo.getCcSort(),ccInfo.getParentId());
        //最下面的不能下移
        if(null==ccInfo1){return 0;}
        //交换position的值
        int temp=ccInfo.getCcSort();
        ccInfo.setCcSort(ccInfo1.getCcSort());
        ccInfo1.setCcSort(temp);
        //更新到数据库中
        courseCategoryInfoService.update(ccInfo);
        courseCategoryInfoService.update(ccInfo1);
        return 1;
    }

    @Override
    public Integer selectMaxSortByParentId(String parentId) {
        return courseCategoryInfoMapper.selectMaxSortByParentId(parentId);
    }

    @Override
    public List<CourseCategoryInfo> selectByParentId(String parentId) {
        return courseCategoryInfoMapper.selectByParentId(parentId);
    }

    @Override
    public List<CourseCategoryInfoVo> selectListRecursion() {
        List<CourseCategoryInfo> list = courseCategoryInfoService.selectAllBy();
        List<CourseCategoryInfoVo> listVo = new ArrayList<CourseCategoryInfoVo>();
        for(CourseCategoryInfo courseCategoryInfo : list){
            CourseCategoryInfoVo courseCategoryInfoVo = MapAndObjectUtils.ObjectClone(courseCategoryInfo,CourseCategoryInfoVo.class);
            listVo.add(courseCategoryInfoVo);
        }
        return CourseCategoryInfoServiceImpl.listGetStree(listVo);
    }

    @Override
    public Integer deleteByCcId(String ccId) {
        List<CourseCategoryInfo> groups=new ArrayList<CourseCategoryInfo>();  //必须New一个对象。
        groups=courseCategoryInfoService.selectByParentId(ccId);               //找出子节点
        for(CourseCategoryInfo group:groups) {
            deleteByCcId(group.getCcId());
        }
        return courseCategoryInfoService.updateCcStatusByCcId(ccId);
    }

    @Override
    public Integer updateNameByCcId(String ccId,String ccName) {
        return courseCategoryInfoMapper.updateNameByCcId(ccId,ccName);
    }

    @Override
    public List<CourseCategoryInfo> selectAllBy() {
        return courseCategoryInfoMapper.selectAllBy();
    }

    @Override
    public Integer updateCcStatusByCcId(String ccId) {
        return courseCategoryInfoMapper.updateCcStatusByCcId(ccId);
    }

    private static List<CourseCategoryInfoVo> listGetStree(List<CourseCategoryInfoVo> list) {
        List<CourseCategoryInfoVo> treeList = new ArrayList<CourseCategoryInfoVo>();
        for (CourseCategoryInfoVo tree : list) {
            //找到根
            if ("0".equals(tree.getParentId())) {
                treeList.add(tree);
            }
            //找到子
            for (CourseCategoryInfoVo treeNode : list) {
                if (treeNode.getParentId().equals(tree.getCcId())) {
                    if (tree.getChildren() == null) {
                        tree.setChildren(new ArrayList<CourseCategoryInfoVo>());
                    }
                    tree.getChildren().add(treeNode);
                }
            }
        }
        return treeList;
    }

}