package org.tron.justlend.justlendapiserver.service;

import org.tron.justlend.justlendapiserver.model.StrxAccountRecord;
import org.tron.justlend.justlendapiserver.core.Service;


public interface StrxAccountRecordService extends Service<StrxAccountRecord> {
  void upsert(StrxAccountRecord strxAccountRecord);
}
