package org.tron.justlend.justlendapiserver.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tron.justlend.justlendapiserver.core.AbstractService;
import org.tron.justlend.justlendapiserver.dao.LendVoteRecordMapper;
import org.tron.justlend.justlendapiserver.model.LendVoteRecord;
import org.tron.justlend.justlendapiserver.service.LendVoteRecordService;

@RequiredArgsConstructor
@Service
@Transactional
public class LendVoteRecordServiceImpl extends AbstractService<LendVoteRecord> implements LendVoteRecordService {
  private final LendVoteRecordMapper lendVoteRecordMapper;

  @Override
  public void upsert(LendVoteRecord lendVoteRecord) {
    lendVoteRecordMapper.upsert(lendVoteRecord);
  }
}
