package org.tron.justlend.justlendapiserver.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.tron.justlend.justlendapiserver.core.Mapper;
import org.tron.justlend.justlendapiserver.model.ChaserProgress;

public interface ChaserProgressMapper extends Mapper<ChaserProgress> {
  @Insert("INSERT INTO chaser_progress (id, event, processed_height) VALUES (#{chaserProgress.id}, #{chaserProgress.event}, #{chaserProgress.processedHeight}) " +
            "ON DUPLICATE KEY UPDATE processed_height = #{chaserProgress.processedHeight}")
  void upsert(@Param("chaserProgress") ChaserProgress chaserProgress);
}