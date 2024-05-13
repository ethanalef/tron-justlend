package org.tron.justlend.justlendapiserver.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.tron.justlend.justlendapiserver.core.Mapper;
import org.tron.justlend.justlendapiserver.model.LendVoteRecord;

public interface LendVoteRecordMapper extends Mapper<LendVoteRecord> {
  @Insert("INSERT INTO lend_vote_record (id, user_address, op_type, proposal_id, amount, tx_id, log_index, block_timestamp) values (" +
            "#{lendVoteRecord.id}, #{lendVoteRecord.userAddress}, #{lendVoteRecord.opType}, #{lendVoteRecord.proposalId}, #{lendVoteRecord.amount}" +
            ", #{lendVoteRecord.txId}, #{lendVoteRecord.logIndex}, #{lendVoteRecord.blockTimestamp})" +
            " ON DUPLICATE KEY UPDATE user_address = #{lendVoteRecord.userAddress}, op_type = #{lendVoteRecord.opType}" +
            ", proposal_id = #{lendVoteRecord.proposalId}, amount = #{lendVoteRecord.amount}, block_timestamp = #{lendVoteRecord.blockTimestamp}")
  Integer upsert(@Param("lendVoteRecord") LendVoteRecord lendVoteRecord);
}