package org.tron.justlend.justlendapiserver.service;

import org.tron.justlend.justlendapiserver.model.ChaserProgress;
import org.tron.justlend.justlendapiserver.core.Service;

import java.math.BigInteger;


public interface ChaserProgressService extends Service<ChaserProgress> {
  void upsert(String event, BigInteger processedHeight);
}
