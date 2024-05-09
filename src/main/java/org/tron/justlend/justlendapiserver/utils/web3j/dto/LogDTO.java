package org.tron.justlend.justlendapiserver.utils.web3j.dto;

import lombok.Data;
import org.web3j.utils.Numeric;

import java.util.ArrayList;
import java.util.List;

@Data
public class LogDTO {
  private String address;
  private List<String> topics = new ArrayList<>();
  private String data;
  private String blockNumber;
  private String transactionHash;
  private String transactionIndex;
  private String blockHash;
  private String logIndex;
  private boolean removed;

  public String getDecodeLogIndex() {
    return Numeric.decodeQuantity(logIndex).toString();
  }
}
