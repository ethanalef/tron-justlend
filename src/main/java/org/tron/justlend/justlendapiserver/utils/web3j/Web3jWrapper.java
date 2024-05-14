package org.tron.justlend.justlendapiserver.utils.web3j;

import lombok.Getter;
import org.springframework.retry.annotation.Retryable;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;

import java.io.IOException;

public class Web3jWrapper {
  private final Web3j web3j;
  @Getter
  private final String identifier;

  public Web3jWrapper(Web3j web3j, String identifier) {
    this.web3j = web3j;
    this.identifier = identifier;
  }

  @Retryable(retryFor = { IOException.class })
  public EthBlockNumber ethBlockNumber() throws IOException {
    return web3j.ethBlockNumber().send();
  }

  @Retryable(retryFor = { IOException.class })
  public EthGetCode ethGetCode(String address, DefaultBlockParameter defaultBlockParameter) throws IOException {
    return web3j.ethGetCode(address, defaultBlockParameter).send();
  }

  @Retryable(retryFor = { IOException.class })
  public EthLog ethGetLogs(EthFilter filter) throws IOException {
    return web3j.ethGetLogs(filter).send();
  }

  @Retryable(retryFor = { IOException.class })
  public EthGetTransactionReceipt ethGetTransactionReceipt(String transactionHash) throws IOException {
    return web3j.ethGetTransactionReceipt(transactionHash).send();
  }

  @Retryable(retryFor = { IOException.class })
  public EthTransaction ethGetTransactionByHash(String transactionHash) throws IOException {
    return web3j.ethGetTransactionByHash(transactionHash).send();
  }

  @Retryable(retryFor = { IOException.class })
  public EthBlock ethGetBlockByNumber(DefaultBlockParameter defaultBlockParameter) throws IOException {
    return web3j.ethGetBlockByNumber(defaultBlockParameter, false).send();
  }

  @Retryable(retryFor = { IOException.class })
  public EthCall ethCall(Transaction transaction) throws IOException {
    return web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
  }
}
