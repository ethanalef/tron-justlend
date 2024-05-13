package org.tron.justlend.justlendapiserver.service;

import org.tron.justlend.justlendapiserver.model.LendVoteRecord;
import org.tron.justlend.justlendapiserver.core.Service;


public interface LendVoteRecordService extends Service<LendVoteRecord> {
  void upsert(LendVoteRecord lendVoteRecord);
}
