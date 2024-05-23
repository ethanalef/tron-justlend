package org.tron.justlend.justlendapiserver.service.impl;

import lombok.RequiredArgsConstructor;
import org.tron.justlend.justlendapiserver.dao.ChaserProgressMapper;
import org.tron.justlend.justlendapiserver.model.ChaserProgress;
import org.tron.justlend.justlendapiserver.service.ChaserProgressService;
import org.tron.justlend.justlendapiserver.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

@RequiredArgsConstructor
@Service
@Transactional
public class ChaserProgressServiceImpl extends AbstractService<ChaserProgress> implements ChaserProgressService {
  private final ChaserProgressMapper chaserProgressMapper;

  @Override
  public void upsert(String event, BigInteger processedHeight) {
    chaserProgressMapper.upsert(ChaserProgress.builder().event(event).processedHeight(processedHeight).build());
  }
}
