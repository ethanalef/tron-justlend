package org.tron.justlend.justlendapiserver.chainchaser.tron.chaser;

import com.google.common.collect.Lists;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.tron.justlend.justlendapiserver.chainchaser.Event;
import org.tron.justlend.justlendapiserver.chainchaser.dto.EventLog;
import org.tron.justlend.justlendapiserver.chainchaser.tron.TronEventChaser;
import org.tron.justlend.justlendapiserver.service.ChaserProgressService;
import org.tron.justlend.justlendapiserver.utils.blockchain.tron.TronJsonRpc;

import java.math.BigInteger;

import static org.tron.justlend.justlendapiserver.chainchaser.EventSignature.*;

@Component
public class GovernanceEventChaser extends TronEventChaser {

  protected GovernanceEventChaser(TronJsonRpc tronJsonRpc, ChaserProgressService chaserProgressService) {
    super(tronJsonRpc, chaserProgressService);
  }

  @PostConstruct
  void postConstruct() {
    topic = Lists.newArrayList(
      JUSTLENDGOV_USER_VOTE.getSignature(),
      JUSTLENDGOV_BRAVO_USER_VOTECAST.getSignature(),
      JUSTLENDGOV_USER_WITHDRAW.getSignature(),
      JUSTLENDGOV_PROPOSALCREATE.getSignature(),
      JUSTLENDGOV_PROPOSALSNAPSHOT.getSignature(),
      JUSTLENDGOV_BRAVO_PROPOSALSNAPSHOT.getSignature(),
      JUSTLENDGOV_CANCELED.getSignature(),
      JUSTLENDGOV_QUEUED.getSignature(),
      JUSTLENDGOV_EXECUTED.getSignature()
    );
    contract = Lists.newArrayList("TXv12di94JPMUsndKD9pkmaSTyeBtcMUWF");
    event = Event.GOVERNANCE;
    range = BigInteger.valueOf(30000);
    startHeight = BigInteger.valueOf(30000);
  }

  @Override
  protected void processEvent(EventLog eventLog) {

  }


}
