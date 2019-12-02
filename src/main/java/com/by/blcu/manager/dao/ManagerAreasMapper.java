package com.by.blcu.manager.dao;

import com.by.blcu.core.universal.Dao;
import com.by.blcu.manager.model.ManagerAreas;
import com.by.blcu.manager.umodel.AreaModel;

import java.util.List;

public interface ManagerAreasMapper extends Dao<ManagerAreas> {
    List<AreaModel> findAccountList(AreaModel search);
}