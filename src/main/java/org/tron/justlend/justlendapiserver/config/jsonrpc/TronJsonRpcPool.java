package org.tron.justlend.justlendapiserver.config.jsonrpc;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tron.justlend.justlendapiserver.utils.web3j.Web3jWrapper;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class TronJsonRpcPool implements JsonRpcPool {
  private final JsonRpcNodes jsonRpcNodes;

  @Bean(name = "tronWeb3jPool")
  public List<Web3jWrapper> web3jPool() {
    return createWeb3jPool(jsonRpcNodes.getTron());
  }
}
