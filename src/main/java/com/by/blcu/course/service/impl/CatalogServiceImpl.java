package com.by.blcu.course.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.ServiceException;
import com.by.blcu.core.universal.BaseServiceImpl;
import com.by.blcu.core.universal.IBaseDao;
import com.by.blcu.core.utils.CommonUtils;
import com.by.blcu.course.dao.ICatalogDao;
import com.by.blcu.course.dao.ICourseDao;
import com.by.blcu.course.dto.Catalog;
import com.by.blcu.course.dto.Course;
import com.by.blcu.course.model.KnowledgePointNode;
import com.by.blcu.course.model.KnowledgePointsModel;
import com.by.blcu.course.service.ICatalogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("catalogService")
@Slf4j
public class CatalogServiceImpl extends BaseServiceImpl implements ICatalogService {

    @Override
    protected IBaseDao getDao() {
        return this.catalogDao;
    }

    @Value("${excel.classFields}")
    private String classFields;

    @Autowired
    private ICatalogDao catalogDao;

    @Autowired
    private ICourseDao courseDao;

    @Override
    @Transactional
    public List<KnowledgePointNode> importKnowledgePoints(MultipartFile file, Map<String, Object> paraMap) throws ServiceException {

        ImportParams importParams = new ImportParams();
        importParams.setHeadRows(1);// 表头
        importParams.setTitleRows(0);
        // 验证字段
        importParams.setNeedVerfiy(true);
        // 验证标题是否正确
        importParams.setImportFields(classFields.split(","));
        Integer courseId;
        try {
            // 使用原生的，可以返回成功条数，失败条数
            ExcelImportResult<KnowledgePointsModel> result = ExcelImportUtil.importExcelMore(file.getInputStream(), KnowledgePointsModel.class,
                    importParams);
            List<KnowledgePointsModel> successList = result.getList();
            List<KnowledgePointsModel> failList = result.getFailList();
            if (!StringUtils.isEmpty(failList) && failList.size() > 0) {
                throw new ServiceException("上传失败：" + failList.size() + "条，请检查excel内容是否规范！");
            }
            courseId = insertPoints(successList, paraMap);
            // 使用工具
//            List<KnowledgePointsModel> results = ExcelUtiles.importExcel(file,0,1,KnowledgePointsModel.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("上传出错"+e.getMessage());
        }
        return getKnowledgePoints(courseId);
    }


    private Integer insertPoints(List<KnowledgePointsModel> list, Map<String, Object> map) {
        log.info("课程目录导入数据====================" + list.toString());
        // 先查这个课程是否已经有知识点了，有了就删除重新导入
        Integer courseId = Integer.valueOf(String.valueOf(map.get("courseId")));
        Map<String,Object> objectMap = new HashMap<>();
        objectMap.put("courseId", map.get("courseId"));
        List<Course> courses = courseDao.selectList(objectMap);
        if (CommonUtils.listIsEmptyOrNull(courses)) {
            throw new ServiceException( "未查询到该课程！");
        }

        List<Catalog> catalogs = catalogDao.selectList(objectMap);
        if (!CommonUtils.listIsEmptyOrNull(catalogs)) {
            if (!CommonUtils.listIsEmptyOrNull(catalogs)) {
                Map<String, Object> delMap = new HashMap<>();
                delMap.put("catalogId", catalogs.get(0).getCatalogId());
                catalogDao.deleteByParams(delMap);
            }
        }
        list.forEach(pointsModel -> {
            String chapterName = pointsModel.getChapter();
            String sectionName = pointsModel.getSection();
            String className = pointsModel.getKnowledgePoints();
            Integer parentId;
            // 1、一级目录
            if (!StringUtils.isEmpty(chapterName)) {
                parentId = insertPointsDetail(pointsModel.getChapter(), pointsModel.getChapterMnemonicCode(),
                        courseId, 0);
                // 2、二级目录
                Integer sectionParentId;
                if (!StringUtils.isEmpty(sectionName)) {
                    sectionParentId = insertPointsDetail(pointsModel.getSection(), pointsModel.getSectionMnemonicCode(),
                            courseId, parentId);
                    // 3、三级目录
                    if (!StringUtils.isEmpty(className)) {
                        insertPointsDetail(pointsModel.getKnowledgePoints(), pointsModel.getPointsMnemonicCode(),
                                courseId, sectionParentId);
                    }
                }
            }
        });
        return courseId;
    }

    private int insertPointsDetail(String name, Integer titleNumber, Integer courseId, Integer parentId) {
        Catalog detail = new Catalog();
        detail.setCourseId(courseId);
        Map<String, Object> paraMap = new HashMap<>();
        paraMap.put("name", name);
        paraMap.put("courseId", courseId);
        List<Catalog> lists = catalogDao.selectList(paraMap);
        if (CommonUtils.listIsEmptyOrNull(lists)) {
            detail.setName(name);
            detail.setParentId(parentId);
            detail.setSort(titleNumber);
            detail.setStatus((byte) 1);
            catalogDao.insertSelective(detail);
            return detail.getCatalogId();
        }
        return lists.get(0).getCatalogId();
    }

    @Override
    public List<KnowledgePointNode> getKnowledgePoints(int courseId) throws ServiceException {

        Map<String, Object> map = new HashMap<>();
        map.put("courseId", courseId);
        List<KnowledgePointNode> nodes = new ArrayList<>();
        Map<String, Object> paraMap = new HashMap<>();
        paraMap.put("_sort_line", "sort");
        paraMap.put("parentId", "0");
        paraMap.put("_order_", "ASC");
        paraMap.put("courseId", courseId);
        nodes = getChildNodes(paraMap);
        return nodes;
    }


    private List<KnowledgePointNode> getChildNodes(Map<String, Object> paraMap) {

        List<KnowledgePointNode> nodes = new ArrayList<>();
        List<Catalog> lists = new ArrayList<>();
        lists = catalogDao.selectList(paraMap);
        if (!CommonUtils.listIsEmptyOrNull(lists)) {
            lists.forEach(catalog -> {
                KnowledgePointNode nodeTmp = new KnowledgePointNode();
                nodeTmp.setId(catalog.getCatalogId());
                nodeTmp.setName(catalog.getName());
                nodeTmp.setSort(catalog.getSort());
                paraMap.put("_sort_line", "sort");
                paraMap.put("parentId", catalog.getCatalogId());
                paraMap.put("_order_", "ASC");
                paraMap.put("courseId", catalog.getCourseId());
                nodeTmp.setNodes(getChildNodes(paraMap));
                nodes.add(nodeTmp);
            });
        }
        return nodes;
    }


    @Override
    @Transactional
    public RetResult addKnowledgePoints(int courseId, int parentId, String name) throws ServiceException {

        Map<String, Object> map = new HashMap<>();
        map.put("courseId", courseId);
        map.put("_sort_line", "sort");
        map.put("parentId", parentId);
        map.put("_order_", "ASC");
        List<Catalog> lists = new ArrayList<>();
        lists = catalogDao.selectList(map);
        Catalog catalog = new Catalog();
        catalog.setParentId(parentId);
        catalog.setName(name);
        catalog.setCourseId(courseId);
        if (!CommonUtils.listIsEmptyOrNull(lists)) {
            catalog.setSort(lists.get(lists.size() - 1).getSort() + 1);
        } else {
            catalog.setSort(1);
        }
        catalogDao.insertSelective(catalog);
        return RetResponse.makeOKRsp();
    }

    @Override
    public RetResult editKnowledgePoints(int pointDetailId, String name) throws ServiceException {

        Catalog catalog = new Catalog();
        catalog.setName(name);
        catalog.setCatalogId(pointDetailId);
        catalogDao.updateByPrimaryKeySelective(catalog);
        return RetResponse.makeOKRsp();
    }

    @Override
    @Transactional
    public RetResult deleteKnowledgePoints(int pointDetailId) throws ServiceException {

        Catalog catalog = catalogDao.selectByPrimaryKey(pointDetailId);
        if (StringUtils.isEmpty(catalog)) {
            return RetResponse.makeOKRsp();
        }
        List<KnowledgePointNode> nodes = new ArrayList<>();
        Map<String, Object> paraMap = new HashMap<>();
        paraMap.put("_sort_line", "sort");
        paraMap.put("parentId", catalog.getParentId());
        paraMap.put("_order_", "ASC");
        paraMap.put("courseId", catalog.getCourseId());

        // 删除时，同级下其后面的助记码都要调整
        List<Catalog> lists = new ArrayList<>();
        lists = catalogDao.selectList(paraMap);
        if (!CommonUtils.listIsEmptyOrNull(lists)) {
            lists.stream().filter(s -> s.getSort() > catalog.getSort() && s.getSort() > 1)
                    .forEach(detailPoint -> {
                        detailPoint.setSort(detailPoint.getSort() - 1);
                        catalogDao.updateByPrimaryKeySelective(detailPoint);
                    });
        }
        catalogDao.deleteByPrimaryKey(pointDetailId);
        paraMap.put("parentId", pointDetailId);
        nodes = getChildNodes(paraMap);
        deleteNotes(nodes);
        return RetResponse.makeOKRsp();
    }

    private void deleteNotes(List<KnowledgePointNode> nodes) {

        if (!CommonUtils.listIsEmptyOrNull(nodes)) {
            nodes.forEach(node -> {
                catalogDao.deleteByPrimaryKey(node.getId());
                deleteNotes(node.getNodes());
            });
        }
    }

}