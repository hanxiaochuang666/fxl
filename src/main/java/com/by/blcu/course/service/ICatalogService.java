package com.by.blcu.course.service;

import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.ServiceException;
import com.by.blcu.core.universal.IBaseService;
import com.by.blcu.course.model.KnowledgePointNode;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ICatalogService extends IBaseService {

    List<KnowledgePointNode> importKnowledgePoints(MultipartFile file, Map<String, Object> paraMap) throws ServiceException;

    List<KnowledgePointNode> getKnowledgePoints(int courseId) throws ServiceException;

    RetResult addKnowledgePoints(int courseId, int parentId,String name) throws ServiceException;

    RetResult editKnowledgePoints(int pointDetailId,String name) throws ServiceException;

    RetResult deleteKnowledgePoints(int pointDetailId) throws ServiceException;

}