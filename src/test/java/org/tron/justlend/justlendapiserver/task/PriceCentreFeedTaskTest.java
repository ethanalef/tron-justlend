package org.tron.justlend.justlendapiserver.task;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.tron.justlend.justlendapiserver.JustLendApiServerApplicationTests;

class PriceCentreFeedTaskTest extends JustLendApiServerApplicationTests {
  @Autowired
  PriceCentreFeedTask priceCentreFeedTask;

  @Test
  void test() {
    priceCentreFeedTask.updateTokenPriceTask();
  }
}
