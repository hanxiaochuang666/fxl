package com.by.blcu.course.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.by.blcu.core.aop.CheckToken;
import com.by.blcu.core.aop.CourseCheck;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.ServiceException;
import com.by.blcu.core.universal.BaseServiceImpl;
import com.by.blcu.core.universal.IBaseDao;
import com.by.blcu.core.utils.*;
import com.by.blcu.course.dao.ICatalogDao;
import com.by.blcu.course.dao.ICourseDao;
import com.by.blcu.course.dao.ICourseDetailDao;
import com.by.blcu.course.dto.Catalog;
import com.by.blcu.course.dto.Course;
import com.by.blcu.course.dto.CourseDetail;
import com.by.blcu.course.model.CatalogModel;
import com.by.blcu.course.model.KnowledgePointNode;
import com.by.blcu.course.model.KnowledgePointsModel;
import com.by.blcu.course.service.ICatalogService;
import com.by.blcu.course.service.ICourseService;
import com.by.blcu.course.service.courseCheck.CourseChangeCheckImpl;
import com.by.blcu.course.service.courseCheck.CourseCheckModel;
import com.by.blcu.resource.dao.IQuestionDao;
import com.by.blcu.resource.dao.IResourcesDao;
import com.by.blcu.resource.dao.IVideoInfoDao;
import com.by.blcu.resource.dto.Question;
import com.by.blcu.resource.dto.Resources;
import com.by.blcu.resource.dto.VideoInfo;
import com.by.blcu.resource.service.IResourcesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service("catalogService")
@CheckToken
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

    @Autowired
    private ICourseDetailDao courseDetailDao;

    @Autowired
    private IVideoInfoDao videoInfoDao;

    @Autowired
    private IResourcesDao resourcesDao;

    @Autowired
    private FastDFSClientWrapper clientWrapper;

    @Autowired
    private IQuestionDao questionDao;

    @Autowired
    private IResourcesService resourcesService;

    @Resource
    private ICourseService courseService;

    /**
     *  根据excel文件导入课程结构
     * @param file 文件
     * @param paraMap 参数列表
     * @return 返回导入后的课程结构
     * @throws Exception
     */
    @Override
    @Transactional
    public KnowledgePointNode importKnowledgePoints(MultipartFile file, Map<String, Object> paraMap) throws Exception {

        ImportParams importParams = new ImportParams();
        // 表头
        importParams.setHeadRows(1);
        importParams.setTitleRows(0);
        // 验证字段
        importParams.setNeedVerfiy(true);
        // 验证标题是否正确
        importParams.setImportFields(classFields.split(","));
        log.info("目录导入的标题：",classFields);
        Integer courseId;
        List<KnowledgePointsModel> successList = null;
        List<KnowledgePointsModel> failList = null;
        try {
            // 使用原生的工具类，可以返回成功条数，失败条数
            ExcelImportResult<KnowledgePointsModel> result = ExcelImportUtil.importExcelMore(file.getInputStream(), KnowledgePointsModel.class,
                    importParams);
            successList = result.getList();
            successList.removeIf(model -> StringUtils.isEmpty(model.getClassName()) && StringUtils.isEmpty(model.getChapter())
                    && StringUtils.isEmpty(model.getSection()));
            failList = result.getFailList();
            failList.removeIf(model -> StringUtils.isEmpty(model.getClassName()) && StringUtils.isEmpty(model.getChapter())
                    && StringUtils.isEmpty(model.getSection()));
            if (!StringUtils.isEmpty(failList) && failList.size() > 0) {
                StringBuilder orderStr = new StringBuilder();
                for (KnowledgePointsModel f : failList) {
                    orderStr.append(f.getOrder()).append(";");
                }
                throw new ServiceException("上传失败：问题数据序号为" + orderStr.toString() + "请检查导入文件！");
            }
        } catch (Exception e){
            log.info("导入异常",e);
            e.printStackTrace();
            try{
                if (e.getMessage().contains("值获取失败！")) {
                    throw new ServiceException("上传失败，请检查导入文件！（助记码只能填写大于0的整数！）");
                }else if(!StringUtils.isEmpty(e.getCause()) && e.getCause().getClass() == IllegalArgumentException.class){
                    throw new ServiceException("上传失败，请检查导入文件！（助记码只能填写大于0的整数！）");
                } else {
                    throw new ServiceException("上传失败，请检查导入文件！（只能使用模板提供的列名称，不能修改！）");
                }
            }catch (Exception e1){
                    throw new ServiceException("上传失败，请检查导入文件是否为空！（只能使用模板提供的列名称，不能修改！）");
            }

        }
        checkPoints(successList);
        courseId = insertPoints(successList, paraMap);
        return getKnowledgePoints(courseId);
    }

    // 校验是否有重复目录名称
    private void checkPoints(List<KnowledgePointsModel> successList){

        List<KnowledgePointsModel> chapters = new ArrayList<>();
        List<KnowledgePointsModel> sections = new ArrayList<>();
        List<KnowledgePointsModel> classes = new ArrayList<>();
        chapters = successList.stream().filter(s->!StringUtils.isEmpty(s.getChapter())).collect(Collectors.toList());
        sections = successList.stream().filter(s->!StringUtils.isEmpty(s.getSection())).collect(Collectors.toList());
        classes = successList.stream().filter(s->!StringUtils.isEmpty(s.getKnowledgePoints())).collect(Collectors.toList());

        List<KnowledgePointsModel> chapters1 = new ArrayList<>();
        List<KnowledgePointsModel> sections1 = new ArrayList<>();
        List<KnowledgePointsModel> classes1 = new ArrayList<>();

        // 比较是否数量一致，不一致就表示有重复的
        if(!CommonUtils.listIsEmptyOrNull(classes)){
            classes1 = classes.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                    new TreeSet<>(Comparator.comparing(KnowledgePointsModel::getKnowledgePoints))), ArrayList::new));
            if(classes.size() != classes1.size()){
                throw new ServiceException("【课时】名称不能重复，请检查文件内容！");
            }
        }else if(!CommonUtils.listIsEmptyOrNull(sections)){
            sections1 = sections.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                    new TreeSet<>(Comparator.comparing(KnowledgePointsModel::getSection))), ArrayList::new));
            if(sections.size() != sections1.size()){
                throw new ServiceException("【节】名称不能重复，请检查文件内容！");
            }
        }else if(!CommonUtils.listIsEmptyOrNull(chapters)){
            chapters1 = chapters.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                    new TreeSet<>(Comparator.comparing(KnowledgePointsModel::getChapter))), ArrayList::new));
            if(chapters.size() != chapters1.size()){
                throw new ServiceException("【章】名称不能重复，请检查文件内容！");
            }
        }
    }


    /**
     *
     * @param list 课程结果列表
     * @param map 入参包含课程id；
     * @return 返回课程id
     * @throws Exception
     */
    private Integer insertPoints(List<KnowledgePointsModel> list, Map<String, Object> map) throws Exception {

        log.info("课程目录导入数据====================" + list.toString());
        // 先查这个课程是否已经有知识点了，有了就删除重新导入
        Integer courseId = Integer.valueOf(String.valueOf(map.get("courseId")));
        Integer createUserId = Integer.valueOf(String.valueOf(map.get("createUserId")));
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("courseId", courseId);
        Course course = courseDao.selectByPrimaryKey(courseId);
        if (null == course) {
            throw new ServiceException("未查询到该课程！");
        }

        List<Catalog> catalogs = catalogDao.selectList(objectMap);
        if (!CommonUtils.listIsEmptyOrNull(catalogs)) {
            Map<String, Object> delMap = new HashMap<>();
            delMap.put("courseId", catalogs.get(0).getCourseId());
            catalogDao.deleteByParams(delMap);
            // 清空试题中的知识点
            catalogs.forEach(c -> {
                Map<String, Object> map1 = new HashMap<>();
                map1.put("knowledgePoints", c.getCatalogId());
                List<Question> questions = questionDao.selectListByPoints(map1);
                if (!CommonUtils.listIsEmptyOrNull(questions)) {
                    for (Question question : questions) {
                        question.setKnowledgePoints("");
                        questionDao.updateByPrimaryKey(question);
                    }
                }
            });
            // 删除关联关系
            Map<String, Object> pMap = new HashMap<>();
            pMap.put("courseId", catalogs.get(0).getCourseId());
            List<CourseDetail> details = courseDetailDao.selectList(pMap);
            if (!CommonUtils.listIsEmptyOrNull(details)) {
                for (CourseDetail d : details) {
                    // 删除课程详情
                    courseDetailDao.deleteByPrimaryKey(d.getCourseDetailId());
                    // 删除关联关系
                    deleteRelation(d.getResourcesId());
                    // 删除已购买学生学习进度
                    resourcesService.syncDeleteStudentResource(d.getCourseId(),d.getCourseDetailId() );
                }
            }
        }
        list.forEach(pointsModel -> {
            String chapterName = pointsModel.getChapter();
            String sectionName = pointsModel.getSection();
            String className = pointsModel.getKnowledgePoints();
            Integer parentId;
            // 1、插入一级目录
            if (!StringUtils.isEmpty(chapterName)) {
                parentId = insertPointsDetail(pointsModel.getChapter(), pointsModel.getChapterMnemonicCode(),
                        courseId, 0, pointsModel.getResourceName(), pointsModel.getResourceType(), createUserId);
                // 2、插入二级目录
                Integer sectionParentId;
                if (!StringUtils.isEmpty(sectionName)) {
                    sectionParentId = insertPointsDetail(pointsModel.getSection(), pointsModel.getSectionMnemonicCode(),
                            courseId, parentId, pointsModel.getResourceName(), pointsModel.getResourceType(), createUserId);
                    // 3、插入三级目录
                    if (!StringUtils.isEmpty(className)) {
                        insertPointsDetail(pointsModel.getKnowledgePoints(), pointsModel.getPointsMnemonicCode(),
                                courseId, sectionParentId, pointsModel.getResourceName(), pointsModel.getResourceType(), createUserId);
                    }
                }
            }
        });
        return courseId;
    }

    /**
     * 插入目录
     * @param name 目录名称
     * @param titleNumber 助记码
     * @param courseId 课程id
     * @param parentId 父节点id
     * @param resourceName 资源名称
     * @param resourceType 资源类型
     * @param createUserId 创建人id
     * @return 返回插入的目录id
     */
    private int insertPointsDetail(String name, Integer titleNumber, Integer courseId, Integer parentId,
                                   String resourceName, String resourceType, Integer createUserId) {
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
            detail.setStatus(1);
            detail.setCheckStatus(0);
            detail.setCreateTime(DateUtils.now());
            detail.setCreateUser(createUserId);
            detail.setUpdateTime(DateUtils.now());
            detail.setUpdateUser(createUserId);
            catalogDao.insertSelective(detail);
            // 判断资源类型自动匹配资源
            if (!StringUtils.isEmpty(resourceType)) {
                if ("V".equals(resourceType.toUpperCase())) {// 视频
                    Map<String, Object> pMap = new HashMap<>();
                    pMap.put("videoName", resourceName);
                    pMap.put("createUser", createUserId);
                    List<VideoInfo> videoInfos = videoInfoDao.selectList(pMap);
                    if (CommonUtils.listIsEmptyOrNull(videoInfos)) {
                        log.warn("根据视频名称【{}】未查询到视频资源！", resourceName);
                    } else {
                        insertVideoResource(videoInfos.get(0), detail.getCatalogId(), createUserId, courseId);
                    }
                }
            }
            return detail.getCatalogId();
        }
        return lists.get(0).getCatalogId();
    }

    /**
     * 根据课程id返回课程结构对象
     * @param courseId 课程id
     * @return 课程结构节点对象
     * @throws ServiceException
     */
    @Override
    public KnowledgePointNode getKnowledgePoints(int courseId) throws ServiceException {

        Map<String, Object> map = new HashMap<>();
        map.put("courseId", courseId);
        List<KnowledgePointNode> nodes = new ArrayList<>();
        Map<String, Object> paraMap = new HashMap<>();
        paraMap.put("_sort_line", "sort");
        paraMap.put("parentId", "0");
        paraMap.put("_order_", "ASC");
        paraMap.put("courseId", courseId);
        // 递归查询子节点
        nodes = getChildNodes(paraMap);
        KnowledgePointNode node = new KnowledgePointNode();
        node.setId(-1);
        node.setName("全部");
        node.setNodes(nodes);
        node.setSort(0);
        return node;
    }


    /**
     * 根据参数获递归取子节点
     * @param paraMap 入参包含课程结构id
     * @return 返回子节点列表
     */
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
                nodeTmp.setCatalogStatus(catalog.getStatus());
                paraMap.put("_sort_line", "sort");
                paraMap.put("parentId", catalog.getCatalogId());
                paraMap.put("_order_", "ASC");
                paraMap.put("courseId", catalog.getCourseId());
                List<KnowledgePointNode> list = getChildNodes(paraMap);
                if (!CommonUtils.listIsEmptyOrNull(list)) {
                    nodeTmp.setNodes(list);
                }
                nodes.add(nodeTmp);
            });
        }
        return nodes;
    }


    /**
     * 添加课程结构
     * @param catalogModel 课程结构对象
     * @param request 请求
     * @return 返回添加成功的课程结构id
     * @throws ServiceException
     */
    @Override
    @Transactional
    @CourseCheck
    public RetResult addKnowledgePoints(CatalogModel catalogModel, HttpServletRequest request, CourseCheckModel courseCheckModel) throws ServiceException {

        int userId = Integer.valueOf(request.getAttribute("userId").toString());
        Integer parentId = catalogModel.getParentId();
        String name = catalogModel.getName();
        Integer courseId = catalogModel.getCourseId();
        Map<String, Object> map = new HashMap<>();
        map.put("courseId", catalogModel.getCourseId());
        map.put("_sort_line", "sort");
        map.put("parentId", catalogModel.getParentId());
        map.put("_order_", "ASC");
        // 校验一下是否有重复的名称
        Map<String, Object> pMap = new HashMap<>();
        pMap.put("courseId", catalogModel.getCourseId());
        pMap.put("name", name.trim());
        List<Catalog> catalogs = catalogDao.selectList(pMap);
        if(!CommonUtils.listIsEmptyOrNull(catalogs)){
            throw new ServiceException("本课程下已经存在目录【"+name+"】,请更换名称！");
        }
        List<Catalog> lists = new ArrayList<>();
        lists = catalogDao.selectList(map);
        Catalog catalog = new Catalog();
        catalog.setParentId(parentId);
        catalog.setName(name);
        catalog.setCourseId(courseId);
        catalog.setUpdateUser(userId);
        catalog.setStatus(1);
        catalog.setCheckStatus(0);
        catalog.setUpdateTime(DateUtils.now());
        catalog.setCreateUser(userId);
        catalog.setCreateTime(DateUtils.now());
        if (!CommonUtils.listIsEmptyOrNull(lists)) {
            catalog.setSort(lists.get(lists.size() - 1).getSort() + 1);
        } else {
            catalog.setSort(1);
        }
        catalogDao.insertSelective(catalog);
        return RetResponse.makeOKRsp(catalog.getCatalogId());
    }

    /**
     *
     * @param pointDetailId 课程结构id
     * @param name 课程结构名称
     * @param request 请求
     * @return 返回结果
     * @throws ServiceException
     */
    @Override
    @CourseCheck
    public RetResult editKnowledgePoints(int pointDetailId, String name, HttpServletRequest request,CourseCheckModel courseCheckModel) throws ServiceException {

        int userId = Integer.valueOf(request.getAttribute("userId").toString());
        Catalog catalog = catalogDao.selectByPrimaryKey(pointDetailId);
        if(catalog != null){
            catalog.setUpdateTime(DateUtils.now());
            catalog.setUpdateUser(userId);
            catalog.setName(name);
            catalog.setCheckStatus(0);
            catalog.setCatalogId(pointDetailId);
            catalogDao.updateByPrimaryKeySelective(catalog);
        }else {
            throw new ServiceException("当前修改目录不存在！目录ID：" + pointDetailId);
        }
        return RetResponse.makeOKRsp(pointDetailId);
    }

    /**
     * 删除课程结构
     * @param catalogId 课程结构id
     * @return 删除结果
     * @throws Exception
     */
    @Override
    @Transactional
    public RetResult deleteKnowledgePoints(int catalogId) throws Exception {

        Catalog catalog = catalogDao.selectByPrimaryKey(catalogId);
        if (StringUtils.isEmpty(catalog)) {
            return RetResponse.makeOKRsp();
        }
        List<KnowledgePointNode> nodes = new ArrayList<>();
        Map<String, Object> paraMap = new HashMap<>();
        paraMap.put("_sort_line", "sort");
        paraMap.put("parentId", catalog.getParentId());
        paraMap.put("_order_", "ASC");
        paraMap.put("courseId", catalog.getCourseId());

        List<Catalog> lists = new ArrayList<>();
        lists = catalogDao.selectList(paraMap);
        // 清空试题中的知识点
        for (Catalog c : lists) {
            Map<String, Object> map1 = new HashMap<>();
            map1.put("knowledgePoints", c.getCatalogId());
            List<Question> questions = questionDao.selectListByPoints(map1);
            if (!CommonUtils.listIsEmptyOrNull(questions)) {
                for (Question question : questions) {
                    log.info("清空试题的知识点:" + question.toString());
                    question.setKnowledgePoints("");
                    questionDao.updateByPrimaryKey(question);
                }
            }
        }
        // 删除时，同级下其后面的助记码都要调整
        if (!CommonUtils.listIsEmptyOrNull(lists)) {
            lists.stream().filter(s -> s.getSort() > catalog.getSort() && s.getSort() > 1)
                    .forEach(detailPoint -> {
                        detailPoint.setSort(detailPoint.getSort() - 1);
                        catalogDao.updateByPrimaryKeySelective(detailPoint);
                    });
        }
        catalogDao.deleteByPrimaryKey(catalogId);
        // 删除关联
        Map<String, Object> pMap = new HashMap<>();
        pMap.put("catalogId", catalogId);
        List<CourseDetail> details = courseDetailDao.selectList(pMap);
        if (!CommonUtils.listIsEmptyOrNull(details)) {
            courseDetailDao.deleteByPrimaryKey(details.get(0).getCourseDetailId());
            deleteRelation(details.get(0).getResourcesId());
            resourcesService.syncDeleteStudentResource(details.get(0).getCourseId(), details.get(0).getCourseDetailId());
        }
        paraMap.put("parentId", catalogId);
        nodes = getChildNodes(paraMap);
        deleteNotes(nodes);
        return RetResponse.makeOKRsp();
    }

    /**
     * 整体保存课程结构
     * @param pointNodes 课程结构节点对象
     * @throws ServiceException
     */
    @Override
    @Transactional
    public void saveKnowledgePoints(List<KnowledgePointNode> pointNodes) throws ServiceException {

        if (!CommonUtils.listIsEmptyOrNull(pointNodes)) {
            for (KnowledgePointNode knowledgePointNode : pointNodes) {
                Catalog catalog = new Catalog();
                catalog.setSort(knowledgePointNode.getSort());
                catalog.setCatalogId(knowledgePointNode.getId());
                catalog.setName(knowledgePointNode.getName());
                catalogDao.updateByPrimaryKeySelective(catalog);
                List<KnowledgePointNode> nodes = knowledgePointNode.getNodes();
                if (!CommonUtils.listIsEmptyOrNull(nodes)) {
                    // 循环子节点进行递归保存
                    nodes.forEach(n -> {
                        List<KnowledgePointNode> knowledgePointNodes = new ArrayList<>();
                        knowledgePointNodes.add(n);
                        saveKnowledgePoints(knowledgePointNodes);
                    });
                }
            }
        }
    }

    /**
     * 删除子节点
     * @param nodes 课程结构节点
     * @throws Exception
     */
    private void deleteNotes(List<KnowledgePointNode> nodes) throws Exception {

        if (!CommonUtils.listIsEmptyOrNull(nodes)) {
            for (KnowledgePointNode node : nodes) {
                catalogDao.deleteByPrimaryKey(node.getId());
                // 删除关联
                Map<String, Object> pMap = new HashMap<>();
                pMap.put("catalogId", node.getId());
                List<CourseDetail> details = courseDetailDao.selectList(pMap);
                if (!CommonUtils.listIsEmptyOrNull(details)) {
                    courseDetailDao.deleteByPrimaryKey(details.get(0).getCourseDetailId());
                    deleteRelation(details.get(0).getResourcesId());
                }
                deleteNotes(node.getNodes());
            }
        }
    }

    /**
     * 关联视频信息
     * @param videoInfo 视频信息
     * @param catalogId 课程结构id
     * @param createUserId 创建人id
     * @param courseId 课程id
     */
    private void insertVideoResource(VideoInfo videoInfo, Integer catalogId, Integer createUserId, Integer courseId) {

        // 存课程关联表
        Map<String, Object> pMap = new HashMap<>();
        pMap.put("content", videoInfo.getVideoInfoId());
        List<Resources> resources = resourcesDao.selectList(pMap);
        if (!CommonUtils.listIsEmptyOrNull(resources)) {
            CourseDetail detail = new CourseDetail();
            detail.setCreateTime(DateUtils.now());
            detail.setCreateUser(createUserId);
            detail.setResourcesId(resources.get(0).getResourcesId());
            detail.setCatalogId(catalogId);
            detail.setCourseId(courseId);
            courseDetailDao.insertSelective(detail);
        }
    }

    /**
     * 删除目录的关联关系
     * @param resourceId 资源id
     * @throws Exception
     */
    private void deleteRelation(Integer resourceId) throws Exception {

        if (!StringUtils.isEmpty(resourceId)) {
            resourcesService.removeByResourceType(resourceId);

        }
    }

    // 目录的启用和禁用接口
    @Override
    @Transactional
    public RetResult changeStatus(int pointDetailId, Integer status, HttpServletRequest request) throws ServiceException {
        int userId = Integer.valueOf(request.getAttribute("userId").toString());
        Catalog c = catalogDao.selectByPrimaryKey(pointDetailId);

        Set<Integer> catalogIds = new HashSet();
        selectCatalogNode(pointDetailId, catalogIds);
        for(Integer catalogId : catalogIds){
            Catalog catalog = new Catalog();
            catalog.setUpdateTime(DateUtils.now());
            catalog.setUpdateUser(userId);
            catalog.setStatus(status);
            if(status.intValue() == 1) catalog.setCheckStatus(0);//重启启用 需要重新审核
            catalog.setCatalogId(catalogId);
            catalogDao.updateByPrimaryKeySelective(catalog);
        }
        if(status.intValue() == 1 && c != null) courseService.changeCourseStatus(c.getCourseId(), userId);
        return RetResponse.makeOKRsp(pointDetailId);
    }

    //递归查询返回 所有待更新catalogId
    @Override
    public void selectCatalogNode(Integer catalogId, Set<Integer> idSet){
        idSet.add(catalogId);
        List<Catalog> catalogs = catalogDao.selectList(MapUtils.initMap("parentId", catalogId));
        if(catalogs != null && catalogs.size() > 0){
            for(Catalog c : catalogs){
                selectCatalogNode(c.getCatalogId(), idSet);
            }
        }else{
            return;
        }
    }

    @Override
    public void resetCatalogCheckStatus(Integer catalogId) {
        Catalog temp = getDao().selectByPrimaryKey(catalogId);
        if(null==temp)
            return;
        Catalog catalog = new Catalog();
        catalog.setCatalogId(catalogId);
        catalog.setCheckStatus(0);
        getDao().updateByPrimaryKeySelective(catalog);
        Course course = new Course();
        course.setCourseId(temp.getCourseId());
        course.setStatus(0);
        courseDao.updateByPrimaryKeySelective(course);
    }

}