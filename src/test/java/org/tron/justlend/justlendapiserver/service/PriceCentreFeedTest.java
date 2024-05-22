package org.tron.justlend.justlendapiserver.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.tron.justlend.justlendapiserver.JustLendApiServerApplicationTests;
import org.tron.justlend.justlendapiserver.model.PriceCentreFeed;

import java.util.List;

class PriceCentreFeedTest extends JustLendApiServerApplicationTests {
  @Autowired
  PriceCentreFeedService priceCentreFeedService;

  @Test
  void test() {
    List<PriceCentreFeed> result = priceCentreFeedService.fetch(List.of("USDT", "TRX", "USDJ", "stUSDT"));
    priceCentreFeedService.upsert(result);
  }
}
