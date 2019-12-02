package com.by.blcu.course.service;

import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.universal.IBaseService;
import com.by.blcu.course.dto.AutomaticCheck;
import com.by.blcu.course.model.CheckPassModel;
import com.by.blcu.course.model.CourseCheckQueryModel;
import com.by.blcu.course.model.CourseCommitCheck;
import com.by.blcu.mall.model.File;
import com.by.blcu.resource.dto.Resources;

import java.util.List;
import java.util.Map;

public interface IAutomaticCheckService extends IBaseService {
    /**
     * 提交审核
     * @param courseId
     * @return
     * @throws Exception
     */
    RetResult commitCheck(int courseId)throws Exception;

    /**
     * 审核请求后的数据库同步
     * @param automaticCheckList
     * @return
     */
    RetResult SyncAutomaticCheck(List<AutomaticCheck> automaticCheckList, Map<String,File> fileMap,
                                 Map<String,Resources> resourcesMap, Map<String,Resources> videoResourcesMap, int courseId);

    /**
     * 审核回调
     * @param checksum
     * @param content
     */
    boolean checkCallBack(String checksum, String content);

    /**
     * 审核一键通过
     * @param courseId
     * @return
     */
    RetResult checkPassAll(int courseId,int userId);

    /**
     * 审核提交
     * @param courseCommitCheck
     * @return
     */
    RetResult checkCommit(CourseCommitCheck courseCommitCheck,int userId);

    /**
     * 资源审核状态切换
     * 只能看到目录和资源的数据
     * @return
     */
    RetResult checkPass(CheckPassModel checkPassModel);

    /**
     * 审核类表查询
     * @param courseCheckQueryModel
     * @return
     */
    RetResult selectList(CourseCheckQueryModel courseCheckQueryModel)throws Exception;
}