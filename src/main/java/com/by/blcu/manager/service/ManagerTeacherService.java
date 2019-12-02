package com.by.blcu.manager.service;

import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.universal.Service;
import com.by.blcu.manager.common.UserSessionHelper;
import com.by.blcu.manager.model.ManagerTeacher;
import com.by.blcu.manager.umodel.ManagerTeacherModel;
import com.by.blcu.manager.umodel.TeacherSearchModel;
import com.by.blcu.manager.umodel.TeacherShowModel;
import com.by.blcu.manager.umodel.TeacherTeamSearch;
import com.github.pagehelper.PageInfo;
import io.swagger.models.auth.In;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ManagerTeacherService extends Service<ManagerTeacher> {

    RetResult<Integer> addTeacher(ManagerTeacher paramManagerTeacher, UserSessionHelper helper);
    RetResult<Integer> updateTeacher(ManagerTeacher paramManagerTeacher, UserSessionHelper helper);
    RetResult<Integer> deleteByTeacherIdList(List<String> paramList, UserSessionHelper helper);
    RetResult<TeacherShowModel> selectTeacherByTeacherId(String paramString, UserSessionHelper helper);
    List<TeacherShowModel> selectTeacherList(TeacherSearchModel paramTeacherSearchModel, UserSessionHelper helper);
    Integer selectTeacherListCount(TeacherSearchModel paramTeacherSearchModel, UserSessionHelper helper);

    //region 李程用
    Map selectLectureNameByUserId(Set<String> paramSet);
    ManagerTeacher selectTeacherByTeacherIdInter(String paramString);
    //endregion

    Integer insertRecommendTeacherList(ManagerTeacherModel model, UserSessionHelper helper);
    Integer deleteRecommendTeacherList(ManagerTeacherModel model, UserSessionHelper helper);
    //根据教师信息获取教师列表
    RetResult selectByTeacherName(TeacherTeamSearch teacher, String orgCode, Integer page, Integer size);
}
