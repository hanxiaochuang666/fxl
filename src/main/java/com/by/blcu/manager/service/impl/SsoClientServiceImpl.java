package com.by.blcu.manager.service.impl;

import com.by.blcu.manager.dao.SsoClientMapper;
import com.by.blcu.manager.model.SsoClient;
import com.by.blcu.manager.service.SsoClientService;
import com.by.blcu.core.universal.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* @Description: SsoClientService接口实现类
* @author 耿鹤闯
* @date 2019/08/05 14:37
*/
@Service
public class SsoClientServiceImpl extends AbstractService<SsoClient> implements SsoClientService {

    @Resource
    private SsoClientMapper ssoClientMapper;

}