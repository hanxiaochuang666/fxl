package com.by.blcu.mall.service.impl;

import com.by.blcu.core.ret.ServiceException;
import com.by.blcu.core.universal.BaseServiceImpl;
import com.by.blcu.core.universal.IBaseDao;
import com.by.blcu.core.utils.DateUtils;
import com.by.blcu.core.utils.MapAndObjectUtils;
import com.by.blcu.core.utils.MapUtils;
import com.by.blcu.core.utils.StringUtils;
import com.by.blcu.mall.dao.CommodityInfoMapper;
import com.by.blcu.mall.model.CommodityLecturer;
import com.by.blcu.mall.model.File;
import com.by.blcu.mall.service.FileService;
import com.by.blcu.mall.vo.CommodityInfoFileVo;
import com.by.blcu.mall.vo.CommodityInfoVo;
import com.by.blcu.manager.model.ManagerTeacher;
import com.by.blcu.manager.service.ManagerTeacherService;
import com.by.blcu.resource.dao.IMyFavoriteDao;
import com.by.blcu.resource.dto.MyFavorite;
import com.by.blcu.mall.service.IMyFavoriteService;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("myFavoriteService")
@Slf4j
public class MyFavoriteServiceImpl extends BaseServiceImpl implements IMyFavoriteService {
    @Autowired
    private IMyFavoriteDao myFavoriteDao;

    @Resource
    private FileService fileService;

    @Resource
    private CommodityInfoMapper commodityInfoMapper;

    @Resource
    private ManagerTeacherService managerTeacherService;

    @Override
    protected IBaseDao getDao() {
        return this.myFavoriteDao;
    }

    @Override
    public int addMyFavorite(int userId, String commodityId) {

        //不可重复收藏
        Map<String, Object> param = MapUtils.initMap("userId", String.valueOf(userId));
        param.put("commodityId", commodityId);
        if (selectCount(param) > 0) {
            throw new ServiceException("不可重复收藏：" + String.valueOf(userId) + ":" + commodityId);
        }

        MyFavorite fav = new MyFavorite();
        fav.setUserId(userId);
        fav.setCommodityId(commodityId);
        fav.setCreateTime(DateUtils.now());
        try {
            return insertSelective(fav);
        }catch (Exception e){
            e.printStackTrace();
            log.error("收藏插入异常，异常信息："+e);
            throw new ServiceException("数据插入异常！");
        }
    }

    @Override
    public int deleteMyFavorite(Integer userId, String commodityId) {

        Map<String,Object> favMap = new HashMap<>();
        favMap.put("userId", String.valueOf(userId.intValue()));
        favMap.put("commodityId", commodityId);

        try {
            return deleteByParams(favMap);
        }catch (Exception e){
            e.printStackTrace();
            log.error("删除收藏异常，异常信息："+e);
            throw new ServiceException("数据插入异常,字段信息过长！(汉字最长不超过7000个，字母最长不超过50000个)");
        }
    }

    @Override
    public List<CommodityInfoFileVo> selectMyFavorite(Integer page, Integer size, Integer userid) {

        //get commdity id from my_favorite
        Map<String,Object> favMap = new HashMap<>();
        favMap.put("_sort_line","id");
        //favMap.put("_order_","asc");
        favMap.put("page", String.valueOf(page.intValue()));
        favMap.put("pageSize", String.valueOf(size.intValue()));
        favMap.put("userId", String.valueOf(userid.intValue()));
        PageHelper.startPage(page, size);
        List<String> commodityIdList = myFavoriteDao.selectCommdityNameListFromMyFavorite(favMap);

        //get commdities from commdity id list
        List<CommodityInfoVo> commodityInfoVos = commodityInfoMapper.selectFavoriteListByCommodityIdList(commodityIdList);
        if (commodityInfoVos != null && commodityInfoVos.size() > 0){
            List<CommodityInfoFileVo> commodityInfoFileVos = new ArrayList();
            for(CommodityInfoVo commodityInfoVo : commodityInfoVos){
                List<CommodityLecturer> commodityLecturerList = commodityInfoVo.getCommodityLecturerList();
                for(CommodityLecturer commodityLecturer: commodityLecturerList){
                    if(!StringUtils.isBlank(commodityLecturer.getLecturer())){
                        ManagerTeacher managerTeacher = managerTeacherService.selectTeacherByTeacherIdInter(commodityLecturer.getLecturer());
                        if(null != managerTeacher){
                            commodityLecturer.setLecturerName(managerTeacher.getTeacherName());
                        }
                    }
                }
                File file = fileService.selectByFileId(commodityInfoVo.getPicturePath());
                File file1 = fileService.selectByFileId(commodityInfoVo.getAuditionVideoPath());
                CommodityInfoFileVo commodityInfoFileVo = MapAndObjectUtils.ObjectClone(commodityInfoVo, CommodityInfoFileVo.class);
                commodityInfoFileVo.setCommodityLecturerList(commodityInfoVo.getCommodityLecturerList());
                if(file != null){
                    commodityInfoFileVo.setPicturePathView(file.getFilePath());
                }
                if(file1 != null){
                    commodityInfoFileVo.setAuditionVideoPathView(file1.getFileName());
                }
                commodityInfoFileVos.add(commodityInfoFileVo);
            }

            return commodityInfoFileVos;
        } else{
            throw new ServiceException( "没有对应的收藏商品！");
        }
    }
}