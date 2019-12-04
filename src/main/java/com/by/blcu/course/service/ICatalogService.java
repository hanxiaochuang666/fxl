package com.by.blcu.course.service;

import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.ServiceException;
import com.by.blcu.core.universal.IBaseService;
import com.by.blcu.course.model.CatalogModel;
import com.by.blcu.course.model.KnowledgePointNode;
import com.by.blcu.course.service.courseCheck.CourseCheckModel;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ICatalogService extends IBaseService {

    KnowledgePointNode importKnowledgePoints(MultipartFile file, Map<String, Object> paraMap) throws Exception;

    KnowledgePointNode getKnowledgePoints(int courseId,Integer status) throws ServiceException;

    RetResult addKnowledgePoints(CatalogModel catalogModel, HttpServletRequest request, CourseCheckModel courseCheckModel) throws ServiceException;

    RetResult editKnowledgePoints(int pointDetailId,String name,HttpServletRequest request,CourseCheckModel courseCheckModel) throws ServiceException;

    RetResult changeStatus(int pointDetailId,Integer status,HttpServletRequest request) throws ServiceException;

    RetResult deleteKnowledgePoints(int pointDetailId) throws Exception;

    void saveKnowledgePoints(List<KnowledgePointNode> knowledgePointNode) throws ServiceException;

    void selectCatalogNode(Integer catalogId, Set<Integer> idSet);

    void resetCatalogCheckStatus(Integer catalogId);

}