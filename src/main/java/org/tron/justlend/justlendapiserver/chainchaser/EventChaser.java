package org.tron.justlend.justlendapiserver.chainchaser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tron.justlend.justlendapiserver.chainchaser.dto.EventLog;
import org.tron.justlend.justlendapiserver.core.ServiceException;
import org.tron.justlend.justlendapiserver.model.ChaserProgress;
import org.tron.justlend.justlendapiserver.service.ChaserProgressService;
import org.tron.justlend.justlendapiserver.utils.web3j.Web3jQuery;
import org.tron.justlend.justlendapiserver.utils.web3j.dto.TransactionReceiptDTO;
import org.web3j.protocol.core.methods.request.EthFilter;

import java.math.BigInteger;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
public abstract class EventChaser {
  protected final Web3jQuery web3jQuery;
  private final ChaserProgressService chaserProgressService;

  protected List<String> topic;
  protected List<String> contract;

  protected String event;
  protected BigInteger range;
  protected BigInteger startHeight;

  protected Logger logger = LoggerFactory.getLogger(this.getClass());

  protected BigInteger getProcessedHeight() {
    ChaserProgress progress = chaserProgressService.findBy("event", event);
    return Optional.ofNullable(progress).map(ChaserProgress::getProcessedHeight).orElse(startHeight);
  }

  protected void updateProcessedHeight(BigInteger height) {
    chaserProgressService.upsert(event, height);
  }

  public void step() {
    BigInteger from = getProcessedHeight();
    step(from);
  }

  public void step(BigInteger from) {
    BigInteger currentHeight = web3jQuery.getCurrentHeight();
    if (currentHeight.compareTo(from) < 0) {
      String errorMessage = String.format("Current height %s not yet reach %s", currentHeight, from);
      throw new ServiceException(errorMessage);
    }
    BigInteger to = currentHeight.compareTo(from.add(range)) < 0 ? currentHeight : from.add(range);
    List<EventLog> eventLogs = fetchEventLog(from, to);

    processEventLog(eventLogs);
    updateProcessedHeight(to);
    log.info("{} Found {}", event, eventLogs.size());
  }

  public List<EventLog> fetchEventLog(BigInteger from, BigInteger to) {
    log.info("{} Fetch from {} to {}", event, from, to);

    // get height-txnHash
    EthFilter filter = web3jQuery.getEthFilter(from, to, topic, contract);
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
        .toList();

      // get event logs
      result.addAll(transactionLogDTOList.stream().flatMap(
        transactionReceiptDTO -> getEventLog(transactionReceiptDTO,  timestamp).stream()).toList());
    }

    return result.stream().filter(this::isTarget).toList();
  }

  protected void processEventLog(List<EventLog> eventLogs) {
    eventLogs.forEach(this::processEvent);
  }

  protected abstract void processEvent(EventLog eventLog);

  protected abstract List<EventLog> getEventLog(TransactionReceiptDTO transactionReceiptDTO, long timestamp);

  private boolean isTarget(EventLog eventLog) {
    boolean matchTopic = topic.isEmpty() || topic.contains(eventLog.topics().getFirst());
    boolean matchAddr = contract.isEmpty() || contract.contains(eventLog.contractAddress());
    return matchTopic && matchAddr;
  }
}