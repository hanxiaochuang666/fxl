package com.by.blcu.mall.dao;

import com.by.blcu.core.universal.Dao;
import com.by.blcu.mall.model.UserInfo;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * @author 
 */
public interface UserInfoMapper extends Dao<UserInfo>{

    UserInfo selectById(@Param("id") Integer id);
    
    List<UserInfo> selectAll();
}