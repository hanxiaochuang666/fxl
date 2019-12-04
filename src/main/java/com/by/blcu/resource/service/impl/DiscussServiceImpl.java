package com.by.blcu.resource.service.impl;

import com.by.blcu.core.filter.SensitiveWordsFilter;
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
import com.by.blcu.manager.model.SsoUser;
import com.by.blcu.manager.service.SsoUserService;
import com.by.blcu.resource.dao.IDiscussDao;
import com.by.blcu.resource.dao.IResourcesDao;
import com.by.blcu.resource.dto.Discuss;
import com.by.blcu.resource.dto.Resources;
import com.by.blcu.resource.model.*;
import com.by.blcu.resource.service.IDiscussService;
import com.by.blcu.resource.service.IResourcesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service("discussService")
public class DiscussServiceImpl extends BaseServiceImpl implements IDiscussService {
    @Autowired
    private IDiscussDao discussDao;
    @Autowired
    private IResourcesDao resourcesDao;
    @Autowired
    private ICourseDao courseDao;
    @Autowired
    private ICatalogDao catalogDao;
    @Autowired
    private ICourseDetailDao courseDetailDao;
    @Autowired
    private IResourcesService resourcesService;
    @Autowired
    private SsoUserService ssoUserService;
    @Resource
    SensitiveWordsFilter sensitiveWordsFilter;

    @Override
    protected IBaseDao getDao() {
        return this.discussDao;
    }

    // 目录下的保存更新讨论
    @Override
    @Transactional
    public DiscussModel saveDiscuss(DiscussModel model, HttpServletRequest httpServletRequest) throws Exception {

        checkCreatRule(model);
        Date date = DateUtils.now();
        Integer userId = Integer.valueOf(httpServletRequest.getAttribute("userId").toString());
        model.setCreateTime(date);
        model.setCreateUserId(userId);
        model.setModelType(1);
        if(StringUtils.isEmpty(model.getResourceId())){
            // 当前目录是否已关联资源 存在则更新，否则新建
            CourseDetail courseDetail = null;
            Map<String, Object> detailParam = MapUtils.initMap("catalogId", model.getCatalogId());
            detailParam.put("courseId",model.getCourseId());
            List<CourseDetail> details = courseDetailDao.selectList(detailParam);
            if(details != null && details.size() > 0){
                courseDetail = details.get(0);
                // 移除之前挂载资源
                resourcesService.removeByResourceType(courseDetail.getResourcesId());
            }

            // 资源不存在，创建资源
            Resources resources = insertResources(model, date, userId);

            // 创建课程详情 关联目录
            if(courseDetail != null){
                courseDetail.setResourcesId(resources.getResourcesId());
                courseDetail.setModelType(1);//默认目录
                courseDetail.setCreateTime(date);
                courseDetail.setCreateUser(userId);
                courseDetailDao.updateByPrimaryKeySelective(courseDetail);
            }else{
                insertCourseDetail(model, date, userId, resources);
            }
            model.setResourceId(resources.getResourcesId());
        }else{
            //5.当前目录是否已关联资源
            Map<String, Object> detailParam = MapUtils.initMap("catalogId", model.getCatalogId());
            detailParam.put("courseId",model.getCourseId());
            List<CourseDetail> details = courseDetailDao.selectList(detailParam);
            if(details != null && details.size() > 0){
                CourseDetail courseDetail = details.get(0);
                if(!model.getResourceId().equals(courseDetail.getResourcesId())){
                    //移除之前挂载资源
                    resourcesService.removeByResourceType(courseDetail.getResourcesId());
                    //创建资源
                    Resources resources = insertResources(model, date, userId);

                    model.setResourceId(resources.getResourcesId());
                }else{
                    //6.资源存在，更新资源
                    Map param = MapUtils.initMap("resourcesId",model.getResourceId());
                    List<Resources> resourcesList = resourcesDao.selectList(param);
                    if(resourcesList != null && resourcesList.size() > 0) {
                        Resources resource = resourcesList.get(0);
                        model.setResourceId(resource.getResourcesId());
                        if(resource.getType().intValue() != ResourceTypeEnum.DISCUSS.getTypeCode().intValue()){
                            //类型不一致则创建
                            Resources resources = insertResources(model, date, userId);
                            courseDetail.setResourcesId(resources.getResourcesId());
                            model.setResourceId(resources.getResourcesId());
                        }else {
                            //类型一致则更新
                            resource.setContent(model.getDiscussContent());
                            resource.setTitle(model.getDiscussTitle());
                            resource.setUpdateUser(userId);
                            resource.setUpdateTime(date);
                            resource.setCheckStatus(0);
                            resourcesDao.updateByPrimaryKey(resource);
                            courseDetail.setResourcesId(resource.getResourcesId());
                        }
                    }
                }

                //更新关联 course_detail 表信息
                courseDetailDao.updateByPrimaryKey(courseDetail);
            }else{
                //目录未关联资源
                Resources resources = insertResources(model, date, userId);
                insertCourseDetail(model, date, userId, resources);
                model.setResourceId(resources.getResourcesId());
            }
        }
        return model;
    }

    @Override
    @Transactional
    public DiscussModel addDiscuss(DiscussModel model,HttpServletRequest httpServletRequest) throws Exception {

        checkCreatRule(model);

        Date date = DateUtils.now();
        Integer userId = Integer.valueOf(httpServletRequest.getAttribute("userId").toString());
        // 存入资源表
        Resources resources = insertResources(model, date, userId);
        // 存入课程详情表
        model.setCatalogId(0);
        model.setModelType(2);
        insertCourseDetail(model, date, userId, resources);
        model.setResourceId(resources.getResourcesId());
        model.setCreateTime(date);
        model.setCreateUserId(userId);

        return model;
    }

    private void insertCourseDetail(DiscussModel model, Date date, Integer userId, Resources resources) {

        CourseDetail detail = new CourseDetail();
        detail.setResourcesId(resources.getResourcesId());
        detail.setCatalogId(model.getCatalogId());
        detail.setCourseId(model.getCourseId());
        detail.setCreateTime(date);
        detail.setCreateUser(userId);
        detail.setModelType(model.getModelType());
        if(!StringUtils.isEmpty(model.getCatalogId())){
            detail.setCatalogId(model.getCatalogId());
        }else{
            detail.setCatalogId(0);
        }
        courseDetailDao.insertSelective(detail);
    }

    private Resources insertResources(DiscussModel model, Date date, Integer userId) {

        Resources resources = new Resources();
        resources.setContent(model.getDiscussContent());
        resources.setTitle(model.getDiscussTitle());
        resources.setCreateUser(userId);
        resources.setCreayeTime(date);
        resources.setUpdateTime(date);
        resources.setUpdateUser(userId);
        resources.setType(ResourceTypeEnum.DISCUSS.getTypeCode());
        resources.setCheckStatus(0);
        resourcesDao.insertSelective(resources);
        return resources;
    }

    @Override
    public ReplyInfoModel addReply(ReplyInfoModel model, HttpServletRequest httpServletRequest) throws ServiceException {

        Integer createUserId = Integer.valueOf(httpServletRequest.getAttribute("userId").toString());
        String createUserName = StringUtils.obj2Str(httpServletRequest.getAttribute("username"));
        Date date = DateUtils.now();

        // 规则校验
        checkReplyRule(model);
        if (StringUtils.isEmpty(model.getParentDiscussId())){
            throw new ServiceException("被回复者主键【parentDiscussId】不能为空！");
        }
        Resources resources = resourcesDao.selectByPrimaryKey(model.getResourceId());
        Discuss discuss = new Discuss();
        Discuss parentDiscuss = null;
        if(0 != model.getParentDiscussId()){
            parentDiscuss = selectByPrimaryKey(model.getParentDiscussId());
            if(null == parentDiscuss){
                throw new ServiceException("未查询到被回复内容【"+model.getParentDiscussId()+"】");
            }else{
                discuss.setParentUserId(parentDiscuss.getCreateUser());
            }
        }else{
            discuss.setParentUserId(resources.getCreateUser());
        }
        discuss.setCreateUser(createUserId);
        discuss.setCreateTime(date);
        discuss.setParentId(model.getParentDiscussId());
        discuss.setStudentId(createUserId);
        discuss.setContent(model.getReplyContent());
        discuss.setUpdateTime(date);
        discuss.setUpdateUser(createUserId);
        discuss.setResourceId(model.getResourceId());
        insertSelective(discuss);

        SsoUser createUser = ssoUserService.getUserByIdInter(createUserId);
        model.setDiscussId(discuss.getDiscussId());
        model.setCreateUserName(createUserName);
        model.setCreateUserHeadUrl(createUser.getHeaderUrl());
        model.setCreateUserId(createUserId);

        SsoUser replyUser = ssoUserService.getUserByIdInter(resources.getCreateUser());
        model.setReplyUserId(resources.getCreateUser());
        model.setReplyUserName(replyUser.getUserName());
        model.setReplyUserHeadUrl(replyUser.getHeaderUrl());
        model.setCreateTime(date);
        return model;
    }

    private void checkReplyRule(ReplyInfoModel model){

        if (StringUtils.isEmpty(model.getResourceId())){
            throw new ServiceException("本条讨论主题resourceId不能为空");
        }else{
            Resources resources = resourcesDao.selectByPrimaryKey(model.getResourceId());
            if(null == resources){
                throw new ServiceException("根据resourceId未查询到讨论主题！");
            }
        }
        if (StringUtils.isEmpty(model.getReplyContent()) || StringUtils.isEmpty(model.getReplyContent().trim())){
            throw new ServiceException("回复内容【replyContent】不能为空！");
        }
        if (StringUtils.isEmpty(model.getReplyUserId()) ){
            throw new ServiceException("被回复者【replyUserId】不能为空！");
        }

    }

    @Override
    public DiscussModel editDiscuss(DiscussModel model, HttpServletRequest httpServletRequest) throws Exception {

        checkSensitiveWorlds(model.getDiscussTitle(),model.getDiscussContent());
        Integer userId = Integer.valueOf(httpServletRequest.getAttribute("userId").toString());
        if(StringUtils.isEmpty(model.getResourceId())){
            throw new ServiceException("讨论主题【resourceId】不能为空！");
        }
        Resources resources = resourcesDao.selectByPrimaryKey(model.getResourceId());
        if (null == resources){
            throw new ServiceException("未查询到当前讨论主题！");
        }
        if(userId.equals(resources.getCreateUser())){
            resources.setTitle(model.getDiscussTitle());
            resources.setContent(model.getDiscussContent());
            resources.setUpdateTime(DateUtils.now());
            resources.setCheckStatus(0);
            resourcesDao.updateByPrimaryKeySelective(resources);
        }else{
            throw new ServiceException("只能修改自己的讨论！");
        }
        return model;
    }

    @Override
    public ReplyInfoModel editReply(ReplyInfoModel model, HttpServletRequest httpServletRequest) throws Exception {

        checkSensitiveWorlds("",model.getReplyContent());
        Integer userId = Integer.valueOf(httpServletRequest.getAttribute("userId").toString());
        // 规则校验
        checkReplyRule(model);
        if(StringUtils.isEmpty(model.getDiscussId())){
            throw new ServiceException("回复主键discussId必传！");
        }
        Discuss discuss = selectByPrimaryKey(model.getDiscussId());
        if(null == discuss){
            throw new ServiceException("根据主键discussId未查询到回复内容！");
        }

        if(userId.equals(discuss.getCreateUser())){
            discuss.setContent(model.getReplyContent());
            discuss.setUpdateTime(DateUtils.now());
            discuss.setUpdateUser(userId);
            updateByPrimaryKeySelective(discuss);
        }else{
            throw new ServiceException("只能修改自己的回复内容！");
        }

        return model;
    }

    @Override
    @Transactional
    public void deleteDiscuss(Integer resourceId, HttpServletRequest httpServletRequest) throws ServiceException {

        if(StringUtils.isEmpty(resourceId)){
            throw new ServiceException("讨论主题resourceId不可空！");
        }

        Integer userId = Integer.valueOf(httpServletRequest.getAttribute("userId").toString());
        String userName = StringUtils.obj2Str(httpServletRequest.getAttribute("username"));
        Resources resources = resourcesDao.selectByPrimaryKey(resourceId);
        if (null != resources) {
            if (userId.equals(resources.getCreateUser())) {
                removeDiscuss(resourceId);
            }else{
                // 判断用户角色
                if (ssoUserService.isTeacher(userName)) {
                    // 老师可以删除自己的以及学生的讨论
                    removeDiscuss(resourceId);
                } else {
                    // 学生只能删除自己的讨论
                    throw new ServiceException("当前登录用户与讨论创建者不一致，只能删除自己的讨论！");
                }
            }
        } else {
            throw new ServiceException("未查询到讨论主题！");
        }
    }


    @Override
    public void deleteReply(Integer discussId, HttpServletRequest httpServletRequest) throws ServiceException {

        Integer userId = Integer.valueOf(httpServletRequest.getAttribute("userId").toString());
        String userName = StringUtils.obj2Str(httpServletRequest.getAttribute("username"));

        List<ReplyNode> nodes = new ArrayList<>();
        Map<String, Object> paraMap = new HashMap<>();
        paraMap.put("_sort_line", "create_time");
        paraMap.put("_order_", "ASC");
        paraMap.put("discussId", discussId);
        nodes = getChildNodes(paraMap);
        Discuss discuss = selectByPrimaryKey(discussId);
        if(null == discuss){
            throw new ServiceException("未查询到讨论id");
        }
        if (userId.equals(discuss.getCreateUser())) {
            deleteChildNodes(nodes);
        }else{
            // 判断用户角色
            if (ssoUserService.isTeacher(userName)) {
                // 老师可以删除自己的以及学生的回复内容
                deleteChildNodes(nodes);
            } else {
                // 学生只能删除自己的回复内容
                throw new ServiceException("当前登录用户与回复的创建者不一致，只能删除自己的回复内容！");
            }
        }

    }

    // 遍历查询子节点
    private List<ReplyNode> getChildNodes(Map<String,Object> map){

        List<ReplyNode> nodes = new ArrayList<>();
        List<Discuss> discusses = selectList(map);
        if(!CommonUtils.listIsEmptyOrNull(discusses)){
            for (Discuss discuss : discusses) {
                ReplyNode node = new ReplyNode();
                node.setDiscussId(discuss.getDiscussId());
                map.put("parentId",discuss.getDiscussId());
                map.put("discussId","");
                List<ReplyNode> childNodes = getChildNodes(map);
                if(!CommonUtils.listIsEmptyOrNull(childNodes)){
                    node.setChilds(childNodes);
                }
                nodes.add(node);
            }
        }
        return nodes;
    }

    // 遍历删除子节点
    private void deleteChildNodes(List<ReplyNode> nodes){

        if(!CommonUtils.listIsEmptyOrNull(nodes)){
            for (ReplyNode node : nodes) {
                deleteByPrimaryKey(node.getDiscussId());
                deleteChildNodes(node.getChilds());
            }
        }

    }

    private void removeDiscuss(Integer resourceId) {
        Map<String, Object> map = MapUtils.initMap("resourceId", resourceId);
        // 删除所有回复
        deleteByParams(map);
        // 删除讨论标题
        resourcesDao.deleteByPrimaryKey(resourceId);
        List<CourseDetail> details = courseDetailDao.selectList(MapUtils.initMap("resourcesId", resourceId));
        if(!CommonUtils.listIsEmptyOrNull(details)){
            for (CourseDetail detail : details) {
                // 删除资源与课程的关联关系
                courseDetailDao.deleteByPrimaryKey(detail.getCourseDetailId());
                // 删除已购买学生的学习进度
                resourcesService.syncDeleteStudentResource(detail.getCourseId(), detail.getCourseDetailId());
            }

        }
    }

    // 规则校验
    private void checkCreatRule(DiscussModel model) throws Exception{

        if(StringUtils.isEmpty(model.getCourseId())){
            throw new ServiceException("课程id必传！");
        }
        if(StringUtils.isEmpty(model.getDiscussContent())){
            throw new ServiceException("讨论内容不能为空！");
        }
        if(StringUtils.isEmpty(model.getDiscussTitle())){
            throw new ServiceException("讨论标题不能为空！");
        }

        Course course = courseDao.selectByPrimaryKey(model.getCourseId());
        if(null == course){
           throw new ServiceException("课程不存在！");
        }
        if(!StringUtils.isEmpty(model.getCatalogId()) && 0 != model.getCatalogId()){
            Map<String,Object> map = MapUtils.initMap("courseId",model.getCourseId());
            map.put("catalogId",model.getCatalogId());
            List<Catalog> catalogs = catalogDao.selectList(map);
            if(CommonUtils.listIsEmptyOrNull(catalogs)){
                throw new ServiceException("课程下不存在该目录!");
            }
        }
        checkSensitiveWorlds(model.getDiscussTitle(),model.getDiscussContent());
    }

    @Override
    public RetResult getDiscussList(QryDiscussModel model, HttpServletRequest httpServletRequest) throws ServiceException {

        Integer pageIndex = StringUtils.isEmpty(model.getPageIndex())?1:model.getPageIndex();
        Integer pageSize = StringUtils.isEmpty(model.getPageSize())?10:model.getPageSize();
        Integer userId = Integer.valueOf(httpServletRequest.getAttribute("userId").toString());
        String userName = StringUtils.obj2Str(httpServletRequest.getAttribute("username"));

        Map<String,Object> paraMap = new HashMap<>();
        paraMap.put("courseId", model.getCourseId());
        paraMap.put("createUser", userId);
        paraMap.put("type", ResourceTypeEnum.DISCUSS.getTypeCode());
        paraMap.put("title", model.getTitle());
        if(1 == model.getType()){
            // 只查自己的
            paraMap.put("createUser", userId);
        }else{
            // 所有人的
            paraMap.put("createUser", "");
        }
        List<DiscussModel> list = discussDao.queryDiscussList(paraMap);
        if (CommonUtils.listIsEmptyOrNull(list)){
            return RetResponse.makeRsp(null, 0);
        }
        SubListUtil<DiscussModel> subList = new SubListUtil<>(pageIndex, pageSize, list);
        List<DiscussModel> modelList = subList.getList();
        Map<String,Object> pMap = new HashMap<>();
        for (DiscussModel discussModel : modelList) {
            pMap.put("resourceId", discussModel.getResourceId());
            discussModel.setReplyCount(selectCount(pMap));
            discussModel.setNowUserId(userId);
            SsoUser createUser = ssoUserService.getUserByIdInter(discussModel.getCreateUserId());
            if(null != createUser){
                discussModel.setCreateUserName(createUser.getUserName());
                discussModel.setUserHeadUrl(createUser.getHeaderUrl());
                setDiscussEditAndDeletePower(userId, userName, discussModel);
            }
        }

        return RetResponse.makeRsp(modelList, subList.getTotal());
    }

    private void setDiscussEditAndDeletePower(Integer userId, String userName, DiscussModel discussModel) {
        if(userId.equals(discussModel.getCreateUserId())){
            // 是自己创建的就可以编辑和删除
            discussModel.setCanEdit(1);
            discussModel.setCanDelete(1);
        }else{
            // 不是自己建的不能编辑
            discussModel.setCanEdit(0);
            if(ssoUserService.isTeacher(userName)){
                // 是教师的话可以删除
                discussModel.setCanDelete(1);
            }else{
                // 不是教师不能删除别人创建的讨论
                discussModel.setCanDelete(0);
            }
        }
    }

    @Override
    public RetResult getReplyList(Integer resourceId, HttpServletRequest httpServletRequest) throws ServiceException {

        Integer userId = Integer.valueOf(httpServletRequest.getAttribute("userId").toString());
        String userName = StringUtils.obj2Str(httpServletRequest.getAttribute("username"));

        Resources resources = resourcesDao.selectByPrimaryKey(resourceId);
        if(null == resources){
            throw new ServiceException("根据resourceId未查询到回复主题！");
        }
        DiscussModel discussModel = new DiscussModel();
        discussModel.setResourceId(resources.getResourcesId());
        discussModel.setCreateUserId(resources.getCreateUser());
        discussModel.setCreateTime(resources.getCreayeTime());
        discussModel.setDiscussContent(resources.getContent());
        discussModel.setDiscussTitle(resources.getTitle());
        discussModel.setReplyCount(selectCount(MapUtils.initMap("resourceId", resourceId)));

        List<CourseDetail> courseDetails = courseDetailDao.selectList(MapUtils.initMap("resourceId", resources.getResourcesId()));
        if (CommonUtils.listIsEmptyOrNull(courseDetails)){
            throw new ServiceException("根据资源未查询到与课程关联关系！");
        }
        discussModel.setCatalogId(courseDetails.get(0).getCatalogId());
        discussModel.setCourseId(courseDetails.get(0).getCourseId());
        SsoUser user = ssoUserService.getUserByIdInter(resources.getCreateUser());
        discussModel.setCreateUserName(user.getUserName());
        discussModel.setUserHeadUrl(user.getHeaderUrl());
        discussModel.setNowUserId(userId);
        setDiscussEditAndDeletePower(userId, userName, discussModel);
        List<ReplyInfoModel> replyList = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("parentId", "0");// 先查询第一层的回复
        map.put("resourceId",resourceId );
        List<Discuss> list = selectList(map);
        if(!CommonUtils.listIsEmptyOrNull(list)){
            for (Discuss discuss : list) {
                SsoUser createUser = ssoUserService.getUserByIdInter(discuss.getCreateUser());
                SsoUser parentUser = ssoUserService.getUserByIdInter(discuss.getParentUserId());
                // 第一层回复
                ReplyInfoModel model = new ReplyInfoModel();
                model.setCreateUserId(discuss.getCreateUser());
                model.setCreateTime(discuss.getCreateTime());
                model.setResourceId(discuss.getResourceId());
                model.setCreateUserHeadUrl(createUser.getHeaderUrl());
                model.setCreateUserName(createUser.getUserName());
                model.setReplyUserHeadUrl(parentUser.getHeaderUrl());
                model.setReplyUserName(parentUser.getUserName());
                model.setReplyContent(discuss.getContent());
                model.setDiscussId(discuss.getDiscussId());
                model.setParentDiscussId(discuss.getParentId());
                model.setReplyUserId(discuss.getParentUserId());
                model.setNowUserId(userId);
                setEditAndDeletePower(userId, userName, model);
                // 第二~第N层的回复
                List<ReplyInfoModel> replyInfoModels = new ArrayList<>();
                getChildReply(replyInfoModels, discuss,userId,userName);
                replyInfoModels.sort(Comparator.comparing(ReplyInfoModel::getCreateTime).reversed());
                model.setChildList(replyInfoModels);
                replyList.add(model);
            }
            discussModel.setReplyList(replyList);
            return RetResponse.makeOKRsp(discussModel);
        }else{
            discussModel.setReplyList(new ArrayList<>());
            return RetResponse.makeOKRsp(discussModel);

        }
    }

    private void setEditAndDeletePower(Integer userId, String userName, ReplyInfoModel model) {
        if(userId.equals(model.getCreateUserId())){
            // 是自己创建的就可以编辑和删除
            model.setCanEdit(1);
            model.setCanDelete(1);
        }else{
            // 不是自己建的不能编辑
            model.setCanEdit(0);
            if(ssoUserService.isTeacher(userName)){
                // 是教师的话可以删除
                model.setCanDelete(1);
            }else{
                // 不是教师不能删除别人创建的讨论
                model.setCanDelete(0);
            }
        }
    }

    private List<ReplyInfoModel> getChildReply(List<ReplyInfoModel> replyInfoModels,Discuss dis,Integer nowUserId,String nowUserName){

        List<Discuss> discussList = selectList(MapUtils.initMap("parentId",dis.getDiscussId()));
        if(CommonUtils.listIsEmptyOrNull(discussList)){
            return replyInfoModels;
        }else{
            for (Discuss discuss : discussList) {
                SsoUser createUser = ssoUserService.getUserByIdInter(discuss.getCreateUser());
                SsoUser parentUser = ssoUserService.getUserByIdInter(discuss.getParentUserId());

                ReplyInfoModel model = new ReplyInfoModel();
                model.setCreateUserId(discuss.getCreateUser());
                model.setCreateTime(discuss.getCreateTime());
                model.setResourceId(discuss.getResourceId());
                model.setCreateUserHeadUrl(createUser.getHeaderUrl());
                model.setCreateUserName(createUser.getUserName());
                model.setReplyUserHeadUrl(parentUser.getHeaderUrl());
                model.setReplyUserName(parentUser.getUserName());
                model.setReplyContent(discuss.getContent());
                model.setDiscussId(discuss.getDiscussId());
                model.setParentDiscussId(discuss.getParentId());
                model.setReplyUserId(discuss.getParentUserId());
                model.setNowUserId(nowUserId);
                setEditAndDeletePower(nowUserId, nowUserName, model);
                replyInfoModels.add(model);
                getChildReply(replyInfoModels, discuss,nowUserId,nowUserName);
            }

        }
        return replyInfoModels;
    }

    private void checkSensitiveWorlds(String title, String content) throws Exception {
        if (!StringUtils.isEmpty(title) && sensitiveWordsFilter.hasSensitiveWord(title)) {
            throw new ServiceException("标题中含有敏感词汇！");
        }
        if (!StringUtils.isEmpty(content) && sensitiveWordsFilter.hasSensitiveWord(content)) {
            throw new ServiceException("内容中含有敏感词汇！");
        }
    }
}