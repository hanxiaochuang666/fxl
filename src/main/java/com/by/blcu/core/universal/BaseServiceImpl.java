package com.by.blcu.core.universal;

import java.util.List;
import java.util.Map;

public abstract class BaseServiceImpl implements IBaseService {


    protected abstract IBaseDao getDao();

    @Override
    public <T> long selectCount(Map<String,Object> map) {
        return getDao().selectCount(map);
    }

    @Override
    public <T> List<T> selectList(Map<String,Object> map) {
        return getDao().selectList(map);
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return getDao().deleteByPrimaryKey(id);
    }

    @Override
    public <T> int insertSelective(T record) {
        return getDao().insertSelective(record);
    }

    @Override
    public <T> T selectByPrimaryKey(Integer id) {
        return getDao().selectByPrimaryKey(id);
    }

    @Override
    public <T> int updateByPrimaryKeySelective(T record) {
        return getDao().updateByPrimaryKeySelective(record);
    }

    @Override
    public <T> int deleteByParams(Map<String, Object> map) {
        return getDao().deleteByParams(map);
    }
}
