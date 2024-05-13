package org.tron.justlend.justlendapiserver.dao;

import org.apache.ibatis.annotations.Insert;
import org.tron.justlend.justlendapiserver.core.Mapper;
import org.tron.justlend.justlendapiserver.model.ChaserProgress;

import java.math.BigInteger;

public interface ChaserProgressMapper extends Mapper<ChaserProgress> {
  @Insert("INSERT INTO chaser_progress (event, processed_height) VALUES (#{event}, #{processedHeight}) ON DUPLICATE KEY UPDATE processed_height = #{processedHeight}")
  void upsert(String event, BigInteger processedHeight);
}