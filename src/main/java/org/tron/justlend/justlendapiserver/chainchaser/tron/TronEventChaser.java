package org.tron.justlend.justlendapiserver.chainchaser.tron;

import org.tron.justlend.justlendapiserver.chainchaser.EventChaser;
import org.tron.justlend.justlendapiserver.chainchaser.dto.EventLog;
import org.tron.justlend.justlendapiserver.service.ChaserProgressService;
import org.tron.justlend.justlendapiserver.utils.blockchain.tron.TronAddressUtils;
import org.tron.justlend.justlendapiserver.utils.blockchain.tron.TronJsonRpc;
import org.tron.justlend.justlendapiserver.utils.web3j.dto.TransactionReceiptDTO;

import java.time.Instant;
import java.util.List;

public abstract class TronEventChaser extends EventChaser {
  protected TronEventChaser(TronJsonRpc tronJsonRpc, ChaserProgressService chaserProgressService) {
    super(tronJsonRpc, chaserProgressService);
  }

  @Override
  protected List<EventLog> getEventLog(TransactionReceiptDTO transactionReceiptDTO, Instant timestamp) {
    return transactionReceiptDTO.getLogs().stream().map(
      log -> EventLog.builder()
               .contractAddress(TronAddressUtils.hexToBase58(log.getAddress()))  // hex to base58 address
               .topics(log.getTopics().stream().map(l -> l.substring(2)).toList())  // trim 0x
               .data(log.getData().substring(2))
               .blockNumber(transactionReceiptDTO.getNumberDecode())
               .from(TronAddressUtils.hexToBase58(transactionReceiptDTO.getFrom()))
               .to(TronAddressUtils.hexToBase58(transactionReceiptDTO.getTo()))
               .transactionId(log.getTransactionHash().substring(2))
               .logIndex(Integer.parseInt(log.getLogIndex()))
               .timestamp(timestamp)
               .build()).toList();
  }
}
