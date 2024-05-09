package org.tron.justlend.justlendapiserver.core;

import org.apache.ibatis.exceptions.TooManyResultsException;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;


public interface Service<T> {

  void save(T model);

  void save(List<T> models);

  void deleteById(Integer id);

  void update(T model);

  T findById(Integer id);

  T findBy(String fieldName, Object value) throws TooManyResultsException;

  T findFirstBy(String fieldName, Object value);

  List<T> findByCondition(Condition condition);

  T findFirstByCondition(Condition condition);

  List<T> findAll();

  List<T> findByModel(T model);

  void deleteBy(String fieldName, Object value);
}
