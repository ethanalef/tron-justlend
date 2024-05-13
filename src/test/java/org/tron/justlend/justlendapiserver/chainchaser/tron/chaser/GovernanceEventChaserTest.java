package org.tron.justlend.justlendapiserver.chainchaser.tron.chaser;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.tron.justlend.justlendapiserver.JustLendApiServerApplicationTests;
import org.tron.justlend.justlendapiserver.chainchaser.dto.EventLog;

import java.math.BigInteger;
import java.util.List;

class GovernanceEventChaserTest  extends JustLendApiServerApplicationTests {
  @Autowired
  GovernanceEventChaser governanceEventChaser;
  @Test
  void test() {
    BigInteger from = new BigInteger("29491005");
    BigInteger to = new BigInteger("29491007");
    List<EventLog> eventLogs = governanceEventChaser.fetchEventLog(from, to);
  }
}
