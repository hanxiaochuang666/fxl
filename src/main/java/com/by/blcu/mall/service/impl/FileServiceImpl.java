package com.by.blcu.mall.service.impl;

import com.by.blcu.mall.dao.FileMapper;
import com.by.blcu.mall.service.FileService;
import com.by.blcu.mall.model.File;
import com.by.blcu.core.universal.AbstractService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @Description: FileService接口实现类
* @author 李程
* @date 2019/07/30 13:51
*/
@Service
public class FileServiceImpl extends AbstractService<File> implements FileService {

    @Resource
    private FileMapper fileMapper;

}