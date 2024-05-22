package org.tron.justlend.justlendapiserver.dao;

import org.tron.justlend.justlendapiserver.core.Mapper;
import org.tron.justlend.justlendapiserver.model.PriceCentreFeed;

import java.util.List;

public interface PriceCentreFeedMapper extends Mapper<PriceCentreFeed> {

  void upsertBatch(List<PriceCentreFeed> feeds);
}