package com.by.blcu.course.dao;

import com.by.blcu.core.universal.IBaseDao;
import com.by.blcu.course.dto.AutomaticCheck;

import java.util.List;
import java.util.Map;

public interface IAutomaticCheckDao extends IBaseDao {

    List<AutomaticCheck> selectActiveObj(Map<String, Object> map);
}