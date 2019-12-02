package com.by.blcu.manager.dao;

import com.by.blcu.core.universal.Dao;
import com.by.blcu.manager.model.ManagerTeacher;
import com.by.blcu.manager.umodel.ManagerTeacherModel;

import java.util.List;
import java.util.Map;

public interface ManagerTeacherMapper extends Dao<ManagerTeacher> {
    //基本操作
    List<ManagerTeacher> findTeacherEquAnd(Map paramMap);
    List<ManagerTeacher> findTeacherList(Map paramMap);
    List<ManagerTeacher> findTeacherListWithOrg(Map paramMap);
    Integer findTeacherListCount(Map paramMap);
    Integer deleteTeacherById(Map paramMap);

    //推荐教师操作
    Integer insertRecommendTeacherList(ManagerTeacherModel model);
    Integer deleteRecommendTeacherList(ManagerTeacherModel model);
}
