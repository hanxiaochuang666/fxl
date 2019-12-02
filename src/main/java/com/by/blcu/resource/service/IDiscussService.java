package com.by.blcu.resource.service;

import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.ServiceException;
import com.by.blcu.core.universal.IBaseService;
import com.by.blcu.resource.model.DiscussModel;
import com.by.blcu.resource.model.QryDiscussModel;
import com.by.blcu.resource.model.ReplyInfoModel;

import javax.servlet.http.HttpServletRequest;

public interface IDiscussService extends IBaseService {

    // 目录下的保存,更新讨论
    DiscussModel saveDiscuss(DiscussModel model,HttpServletRequest httpServletRequest) throws Exception;
    // 教师和学生在模块下创建讨论
    DiscussModel addDiscuss(DiscussModel model,HttpServletRequest httpServletRequest) throws Exception;
    // 添加回复
    ReplyInfoModel addReply(ReplyInfoModel model, HttpServletRequest httpServletRequest) throws ServiceException;
    // 编辑讨论
    DiscussModel editDiscuss(DiscussModel model,HttpServletRequest httpServletRequest) throws Exception;
    // 编辑回复
    ReplyInfoModel editReply(ReplyInfoModel model,HttpServletRequest httpServletRequest) throws Exception;
    // 删除讨论
    void deleteDiscuss(Integer resourceId,HttpServletRequest httpServletRequest) throws ServiceException;
    // 删除回复内容
    void deleteReply(Integer discussId,HttpServletRequest httpServletRequest) throws ServiceException;
    // 查询主题列表
    RetResult getDiscussList(QryDiscussModel model, HttpServletRequest httpServletRequest) throws ServiceException;
    // 查询主题下面的回复列表
    RetResult getReplyList(Integer resourceId, HttpServletRequest httpServletRequest) throws ServiceException;


}