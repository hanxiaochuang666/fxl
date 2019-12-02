package com.by.blcu.core.universal;

import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.ServiceException;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Condition;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * @Description: 基于通用MyBatis Mapper插件的Service接口的实现
 * @author 
 */
public abstract class AbstractService<T> implements Service<T> {

   @Autowired
   protected Dao<T> dao;

   private Class<T> modelClass; // 当前泛型真实类型的Class

   @SuppressWarnings("unchecked")
   public AbstractService() {
      ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
      modelClass = (Class<T>) pt.getActualTypeArguments()[0];
   }

   @Override
   public Integer insert(T model) {
      return dao.insertSelective(model);
   }

   @Override
   public Integer deleteById(String id) {
      return dao.deleteByPrimaryKey(id);
   }

   @Override
   public Integer deleteByIds(String ids) {
      return dao.deleteByIds(ids);
   }

   @Override
   public Integer update(T model) {
      return dao.updateByPrimaryKeySelective(model);
   }

   @Override
   public T selectById(String id) {
      return dao.selectByPrimaryKey(id);
   }

   @Override
   public T selectBy(String fieldName, Object value) throws TooManyResultsException {
      try {
         T model = modelClass.newInstance();
         Field field = modelClass.getDeclaredField(fieldName);
         field.setAccessible(true);
         field.set(model, value);
         return dao.selectOne(model);
      } catch (ReflectiveOperationException e) {
         throw new ServiceException(e.getMessage(), e);
      }
   }

   @Override
   public List<T> selectListBy(String fieldName, Object value)  {
      try {
         T model = modelClass.newInstance();
         Field field = modelClass.getDeclaredField(fieldName);
         field.setAccessible(true);
         field.set(model, value);
         return dao.select(model);
      } catch (ReflectiveOperationException e) {
         throw new ServiceException(e.getMessage(), e);
      }
   }

   @Override
   public List<T> selectListByIds(String ids) {
      return dao.selectByIds(ids);
   }

   @Override
   public List<T> selectByCondition(Condition condition) {
      return dao.selectByCondition(condition);
   }

   @Override
   public List<T> selectAll() {
      return dao.selectAll();
   }

   @Override
   public List<T> select(T record){
      return dao.select(record);
   }

   @Override
   public T selectOne(T recoed){
      return dao.selectOne(recoed);
   }
}