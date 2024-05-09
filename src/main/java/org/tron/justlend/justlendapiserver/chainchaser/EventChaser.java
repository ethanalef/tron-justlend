package org.tron.justlend.justlendapiserver.chainchaser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tron.justlend.justlendapiserver.chainchaser.dto.EventLog;
import org.tron.justlend.justlendapiserver.utils.web3j.Web3jQuery;
import org.tron.justlend.justlendapiserver.utils.web3j.dto.TransactionReceiptDTO;
import org.web3j.protocol.core.methods.request.EthFilter;

import java.math.BigInteger;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
public abstract class EventChaser {
  protected final Web3jQuery web3jQuery;
  protected List<String> topics;
  protected List<String> contracts;

  protected String event;
  protected int range;
  protected boolean isEnabled;

  public List<EventLog> fetchEventLog(BigInteger from, BigInteger to) {
    // get height-txnHash
    EthFilter filter = web3jQuery.getEthFilter(from, to, topics, contracts);
    SortedMap<BigInteger, LinkedHashSet<String>> blockTxnMap = web3jQuery.getBlockTxnMap(filter);
    if (blockTxnMap.isEmpty()) {
      return Collections.emptyList();
    }

    List<EventLog> result = new ArrayList<>();
    for (var entry : blockTxnMap.entrySet()) {
      // get block info with timestamp
      long timestamp = web3jQuery.getBlockTime(entry.getKey());

      // get transaction logs
      List<TransactionReceiptDTO> transactionLogDTOList = entry.getValue().parallelStream()
        .map(web3jQuery::getTransactionReceiptDTO)
        .filter(Objects::nonNull)
//        .peek(this::filterLog)
        .toList();

      // get event logs
      result.addAll(transactionLogDTOList.stream().flatMap(
        transactionReceiptDTO -> getEventLog(transactionReceiptDTO,  timestamp).stream()).toList());
    }

    return result.stream().filter(this::isTarget).toList();
  }

  protected abstract List<EventLog> getEventLog(TransactionReceiptDTO transactionReceiptDTO, long timestamp);

  private boolean isTarget(EventLog eventLog) {
    boolean matchTopic = topics.isEmpty() || topics.contains(eventLog.topics().getFirst());
    boolean matchAddr = contracts.isEmpty() || contracts.contains(eventLog.contractAddress());
    return matchTopic && matchAddr;
  }
}