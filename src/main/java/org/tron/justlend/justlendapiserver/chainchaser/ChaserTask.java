package org.tron.justlend.justlendapiserver.chainchaser;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.tron.justlend.justlendapiserver.chainchaser.tron.chaser.GovernanceUserEventChaser;

@Component
@RequiredArgsConstructor
public class ChaserTask {
  private final GovernanceUserEventChaser governanceUserEventChaser;

  @Scheduled(fixedDelay = 3000)
  public void userRecordTask() {
    governanceUserEventChaser.step();
  }
}
