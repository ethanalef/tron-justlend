package org.tron.justlend.justlendapiserver.utils.web3j.dto;

import com.alibaba.fastjson2.JSON;
import lombok.Data;
import org.web3j.protocol.core.methods.response.EthBlock;

import java.util.ArrayList;
import java.util.List;

@Data
public class BlockDTO {
  private String timestamp;
  private List<String> transactions = new ArrayList<>();

  @Override
  public String toString() {
    return JSON.toJSONString(this);
  }

  public static BlockDTO format(EthBlock ethBlock) {
    String str = JSON.toJSONString(ethBlock.getResult());
    return JSON.parseObject(str, BlockDTO.class);
  }
}
