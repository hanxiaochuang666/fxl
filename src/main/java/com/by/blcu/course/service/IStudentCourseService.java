package com.by.blcu.course.service;

import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.ServiceException;
import com.by.blcu.course.model.CatalogAndResourceModel;
import com.by.blcu.course.model.PreLiveInfoModel;
import com.by.blcu.resource.model.ResourcesViewModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IStudentCourseService {

    RetResult selectStudentCourse(Integer type, HttpServletRequest request,Integer pageSize,Integer pageIndex) throws ServiceException;

    Map<String,Object> selectStudySchedule(Integer courseId,Integer studentId) throws ServiceException;

    ResourcesViewModel keepStudy(Integer courseId, HttpServletRequest request) throws Exception;

    ResourcesViewModel beginStudy(String type,Integer catalogId, HttpServletRequest request) throws Exception;

    List<CatalogAndResourceModel> selectCourseCatalog(Integer status,Integer courseId,String userType,HttpServletRequest request) throws ServiceException;

    List<PreLiveInfoModel> selectPreLiveInfo(Integer courseId) throws ServiceException;

    void filePreview(String fileUrl,String fileName,HttpServletRequest httpServletRequest, HttpServletResponse response) throws Exception;
}
