package org.tron.justlend.justlendapiserver.chainchaser;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.tron.justlend.justlendapiserver.chainchaser.tron.chaser.GovernanceUserEventChaser;
import org.tron.justlend.justlendapiserver.chainchaser.tron.chaser.StrxStakeUserEventChaser;

@Component
@RequiredArgsConstructor
public class ChaserTask {
  private final GovernanceUserEventChaser governanceUserEventChaser;
  private final StrxStakeUserEventChaser strxStakeUserEventChaser;

  @Scheduled(fixedDelay = 4000)
  public void governanceUserRecordTask() {
    governanceUserEventChaser.step();
  }

  @Scheduled(fixedDelay = 4000)
  public void strxStakeUserRecordTask() {
    strxStakeUserEventChaser.step();
  }
}
