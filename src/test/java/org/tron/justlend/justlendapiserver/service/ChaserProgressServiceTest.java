package org.tron.justlend.justlendapiserver.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.tron.justlend.justlendapiserver.JustLendApiServerApplicationTests;
import org.tron.justlend.justlendapiserver.model.ChaserProgress;

import java.util.List;

class ChaserProgressServiceTest extends JustLendApiServerApplicationTests {
  @Autowired
  ChaserProgressService chaserProgressService;

  @Test
  void test() {
    List<ChaserProgress> list = chaserProgressService.findAll();
  }

  @Test
  void test2() {
    ChaserProgress a = chaserProgressService.findFirstBy("event", "governance");
    List<ChaserProgress> b = chaserProgressService.findByModel(ChaserProgress.builder().event("governance").build());
    List<ChaserProgress> c = chaserProgressService.findAll();
//    var result = Optional.ofNullable(progress).map(ChaserProgress::getProcessedHeight).orElse(BigInteger.ZERO);
  }
}
