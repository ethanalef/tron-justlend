package org.tron.justlend.justlendapiserver.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.tron.justlend.justlendapiserver.core.Mapper;
import org.tron.justlend.justlendapiserver.model.StrxAccountRecord;

public interface StrxAccountRecordMapper extends Mapper<StrxAccountRecord> {
  @Insert("INSERT INTO strx_account_record (id, user_address, op_type, amount, usd, tx_id, log_index, event_time) values (" +
            "#{strxAccountRecord.id}, #{strxAccountRecord.userAddress}, #{strxAccountRecord.opType}, #{strxAccountRecord.amount}" +
            ", #{strxAccountRecord.usd}, #{strxAccountRecord.txId}, #{strxAccountRecord.logIndex}, #{strxAccountRecord.eventTime})" +
            " ON DUPLICATE KEY UPDATE user_address = #{strxAccountRecord.userAddress}, op_type = #{strxAccountRecord.opType}" +
            ", amount = #{strxAccountRecord.amount}, event_time = #{strxAccountRecord.eventTime}")
  Integer upsert(@Param("strxAccountRecord") StrxAccountRecord strxAccountRecord);
}