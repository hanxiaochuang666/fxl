package com.by.blcu.mall.service;

import com.by.blcu.mall.model.UserInfo;
import com.by.blcu.core.universal.Service;
import com.github.pagehelper.PageInfo;

/**
 * @author 
 */
public interface UserInfoService extends Service<UserInfo>{

    UserInfo selectById(Integer id);

    PageInfo<UserInfo> selectAll(Integer page,Integer size);
}