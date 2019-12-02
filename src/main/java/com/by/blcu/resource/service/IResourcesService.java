package com.by.blcu.resource.service;

import com.by.blcu.core.ret.ServiceException;
import com.by.blcu.core.universal.IBaseService;
import com.by.blcu.course.service.courseCheck.CourseCheckModel;
import com.by.blcu.resource.dto.Resources;
import com.by.blcu.resource.model.FileViewModel;
import com.by.blcu.resource.model.ResourcesViewModel;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

public interface IResourcesService extends IBaseService {
    /**
     * 判断资源是否被使用（关联）
     * @param contentId
     * @param type
     * @param minType
     * @param maxType
     * @return
     */
    boolean isUsedResources(Integer contentId,Integer type,Integer minType,Integer maxType);

    /**
     * 资源修改后同步其资源的checkStatus
     * @param contentId
     * @param type
     * @param minType
     * @param maxType
     */
    void syncResources(Integer contentId,Integer type,Integer minType,Integer maxType);

    /**
     * 判断资源是否上架
     * @param contentId
     * @param type
     * @param minType
     * @param maxType
     * @return
     */
    boolean isUsedMall(Integer contentId,Integer type,Integer minType,Integer maxType);

    /**
     * 新增/更新 目录下富文本
     * @param fileViewModel
     * @param request
     * @return
     * @throws Exception
     */
    Integer saveRichText(FileViewModel fileViewModel, HttpServletRequest request, CourseCheckModel courseCheckModel) throws Exception;

    /**
     * 根据资源类型 移除资源
     * @param resourcesId
     * @return
     * @throws Exception
     */
    boolean removeByResourceType(Integer resourcesId) throws Exception;

    /**
     * 根据目录ID获取资源详情
     * @param resourcesViewModel
     * @return
     * @throws Exception
     */
    boolean getResourcesByCatalogId(ResourcesViewModel resourcesViewModel, int userId) throws Exception;

    /**
     * 根据目录ID移除下属资源信息
     * @param resourcesViewModel
     * @return
     * @throws Exception
     */
    boolean delResourcesByCatalogId(ResourcesViewModel resourcesViewModel, int userId) throws Exception;


    /**
     * 同步课程下的resources
     * @param courseId
     * @param resourcesSet
     */
    void syncStudentResources(Integer courseId, Set<Integer> resourcesSet);


    /**
     * 重置资源审核状态
     * @param res
     */
    void resetResourcesCheckStatus(Integer res);

    /**
     * 删除资源时同步删除学生学习进度
      */
    void syncDeleteStudentResource(Integer courseId ,Integer courseDetailId) throws ServiceException;
}