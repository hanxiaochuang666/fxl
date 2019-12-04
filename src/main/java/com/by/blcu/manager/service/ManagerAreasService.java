package com.by.blcu.manager.service;

import com.by.blcu.core.universal.Service;
import com.by.blcu.manager.common.UserSessionHelper;
import com.by.blcu.manager.model.ManagerAreas;
import com.by.blcu.manager.model.extend.AreaTree;

import java.util.List;

/**
* @Description: ManagerAreasService接口
* @author 耿鹤闯
* @date 2019/09/05 11:07
*/
public interface ManagerAreasService extends Service<ManagerAreas> {
    List<ManagerAreas> selectList();
    List<ManagerAreas> getProvince(String pcode);
    List<ManagerAreas> getCity(String pcode, String ccode);
    List<ManagerAreas> getArea(String ccode, String acode);
    //获取省市区，树结构
    List<AreaTree> selectAreaTree(UserSessionHelper helper);
}