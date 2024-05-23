package org.tron.justlend.justlendapiserver.task;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.tron.justlend.justlendapiserver.chainchaser.tron.chaser.GovernanceUserEventChaser;
import org.tron.justlend.justlendapiserver.chainchaser.tron.chaser.StrxStakeUserEventChaser;
import org.tron.justlend.justlendapiserver.config.AppProperties;

@Component
@RequiredArgsConstructor
public class ChaserTask {
  private final AppProperties appProperties;
  private final GovernanceUserEventChaser governanceUserEventChaser;
  private final StrxStakeUserEventChaser strxStakeUserEventChaser;

  @Scheduled(fixedDelayString = "${task.gov-account-record-chaser.interval}")
  public void governanceUserRecordTask() {
    if (appProperties.getGovAccountRecordChaserEnabled()) {
      governanceUserEventChaser.step();
    }
  }

  @Scheduled(fixedDelayString = "${task.strx-account-record-chaser.interval}")
  public void strxStakeUserRecordTask() {
    if (appProperties.getStrxAccountRecordChaserEnabled()) {
      strxStakeUserEventChaser.step();
    }
  }
}
