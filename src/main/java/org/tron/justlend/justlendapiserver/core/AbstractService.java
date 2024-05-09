package org.tron.justlend.justlendapiserver.core;


import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Condition;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class AbstractService<T> implements Service<T> {

  private final Class<T> modelClass;
  @Autowired
  protected Mapper<T> mapper;

  protected AbstractService() {
    ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
    modelClass = (Class<T>) pt.getActualTypeArguments()[0];
  }

  public void save(T model) {
    mapper.insertSelective(model);
  }

  public void save(List<T> models) {
    if (models != null && models.size() > 0) {
      mapper.insertList(models);
    }
  }

  public void deleteById(Integer id) {
    mapper.deleteByPrimaryKey(id);
  }

  public void update(T model) {
    mapper.updateByPrimaryKeySelective(model);
  }

  public void update(List<T> models) {
    for (T model : models) {
      mapper.updateByPrimaryKeySelective(model);
    }

  }

  public T findById(Integer id) {
    return mapper.selectByPrimaryKey(id);
  }

  @Override
  public T findBy(String fieldName, Object value) throws TooManyResultsException {
    try {
      T model = modelClass.newInstance();
      Field field = modelClass.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(model, value);
      return mapper.selectOne(model);
    } catch (ReflectiveOperationException e) {
      throw new ServiceException(e.getMessage(), e);
    }
  }

  @Override
  public T findFirstBy(String fieldName, Object value) {
    try {
      T model = modelClass.newInstance();
      Field field = modelClass.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(model, value);
      List<T> res = mapper.select(model);
      if (res != null && res.size() > 0) {
        return res.get(0);
      }

    } catch (ReflectiveOperationException e) {
      throw new ServiceException(e.getMessage(), e);
    }
    return null;
  }


  public List<T> findByCondition(Condition condition) {
    return mapper.selectByCondition(condition);
  }

  @Override
  public T findFirstByCondition(Condition condition) {
    List<T> res = mapper.selectByCondition(condition);
    if (res != null && res.size() > 0) {
      return res.get(0);
    }
    return null;
  }

  public List<T> findByModel(T model) {
    return mapper.select(model);
  }

  public List<T> findAll() {
    return mapper.selectAll();
  }

  public void deleteBy(String fieldName, Object value) {
    try {
      T model = modelClass.newInstance();
      Field field = modelClass.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(model, value);
      mapper.delete(model);
    } catch (ReflectiveOperationException e) {
      throw new ServiceException(e.getMessage(), e);
    }
  }
}
