package org.tron.justlend.justlendapiserver.service;

import org.tron.justlend.justlendapiserver.core.Service;
import org.tron.justlend.justlendapiserver.model.PriceCentreFeed;

import java.math.BigDecimal;
import java.util.List;


public interface PriceCentreFeedService extends Service<PriceCentreFeed> {

  void upsert(List<PriceCentreFeed> priceCentreFeed);

  List<PriceCentreFeed> fetch(List<String> symbol);
  BigDecimal getUsd(String sybmol);
}
