package org.tron.justlend.justlendapiserver.chainchaser.dto;

import lombok.Builder;

import java.math.BigInteger;
import java.util.List;

@Builder
public record EventLog(
  long timestamp,
  int logIndex,
  String transactionId,
  String contractAddress,
  String from,
  String to,
  BigInteger blockNumber,
  String data,
  List<String> topics
) {
  public String getData(int index) {
    String raw = data;
    if (raw.startsWith("0x"))
      raw = raw.substring("0x".length());
    if (raw.length() >= (index + 1) * 64 )
      return raw.substring(index * 64, index * 64 + 64);
    return "";
  }

  public String getLogIndex() {
    return transactionId + "_" + logIndex;
  }
}
