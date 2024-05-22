package org.tron.justlend.justlendapiserver.service.impl;

import lombok.RequiredArgsConstructor;
import org.tron.justlend.justlendapiserver.dao.StrxAccountRecordMapper;
import org.tron.justlend.justlendapiserver.model.StrxAccountRecord;
import org.tron.justlend.justlendapiserver.service.StrxAccountRecordService;
import org.tron.justlend.justlendapiserver.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class StrxAccountRecordServiceImpl extends AbstractService<StrxAccountRecord> implements StrxAccountRecordService {
  private final StrxAccountRecordMapper strxAccountRecordMapper;

  @Override
  public void upsert(StrxAccountRecord strxAccountRecord) {
    strxAccountRecordMapper.upsert(strxAccountRecord);
  }
}
