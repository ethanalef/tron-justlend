package org.tron.justlend.justlendapiserver.config.jsonrpc;

import org.apache.commons.lang3.StringUtils;
import org.tron.justlend.justlendapiserver.utils.web3j.Web3jWrapper;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


public interface JsonRpcPool {
  AtomicInteger index = new AtomicInteger(0);
  default List<Web3jWrapper> createWeb3jPool(List<Map<String, String>> nodes) {
    List<Web3jWrapper> pool = new ArrayList<>(nodes.size());
    nodes.forEach(node -> {
      HttpService httpService = new HttpService(node.get("node"));
      if (!StringUtils.isEmpty(node.get("header"))) {
        httpService.addHeader(node.get("header"), node.get("api-key"));
      }
      Web3j web3j = Web3j.build(httpService);
      Web3jWrapper web3jWrapper = new Web3jWrapper(web3j, node.get("id"));
      pool.add(web3jWrapper);
    });
    return pool;
  }

  List<Web3jWrapper> web3jPool();

  default Web3jWrapper getWeb3j() {
    int currentIndex = index.getAndIncrement() % web3jPool().size();
    return web3jPool().get(currentIndex);
  }
}
