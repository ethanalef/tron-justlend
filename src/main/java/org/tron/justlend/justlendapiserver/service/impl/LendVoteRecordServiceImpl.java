package org.tron.justlend.justlendapiserver.service.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tron.justlend.justlendapiserver.core.AbstractService;
import org.tron.justlend.justlendapiserver.dao.LendVoteRecordMapper;
import org.tron.justlend.justlendapiserver.model.LendVoteRecord;
import org.tron.justlend.justlendapiserver.service.LendVoteRecordService;

@Slf4j
@Service
@Transactional
public class LendVoteRecordServiceImpl extends AbstractService<LendVoteRecord> implements LendVoteRecordService {
  @Resource
  private LendVoteRecordMapper lendVoteRecordMapper;

  @Override
  public void upsert(LendVoteRecord lendVoteRecord) {
    lendVoteRecordMapper.upsert(lendVoteRecord);
  }
}
