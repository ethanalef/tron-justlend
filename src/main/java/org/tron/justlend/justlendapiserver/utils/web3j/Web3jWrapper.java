package org.tron.justlend.justlendapiserver.utils.web3j;

import lombok.Getter;
import org.springframework.retry.annotation.Retryable;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Web3jWrapper {
  private final Web3j web3j;
  @Getter
  private final String identifier;
  private static final long TIMEOUT = 30; // Timeout in seconds

  public Web3jWrapper(Web3j web3j, String identifier) {
    this.web3j = web3j;
    this.identifier = identifier;
  }

  @Retryable(retryFor = { TimeoutException.class, ExecutionException.class, InterruptedException.class })
  public EthBlockNumber ethBlockNumber() throws ExecutionException, InterruptedException, TimeoutException {
    CompletableFuture<EthBlockNumber> future = web3j.ethBlockNumber().sendAsync();
    return future.get(TIMEOUT, TimeUnit.SECONDS);
  }

  @Retryable(retryFor = { TimeoutException.class, ExecutionException.class, InterruptedException.class })
  public EthGetCode ethGetCode(String address, DefaultBlockParameter defaultBlockParameter) throws ExecutionException, InterruptedException, TimeoutException {
    CompletableFuture<EthGetCode> future = web3j.ethGetCode(address, defaultBlockParameter).sendAsync();
    return future.get(TIMEOUT, TimeUnit.SECONDS);
  }

  @Retryable(retryFor = { TimeoutException.class, ExecutionException.class, InterruptedException.class })
  public EthLog ethGetLogs(EthFilter filter) throws ExecutionException, InterruptedException, TimeoutException {
    CompletableFuture<EthLog> future = web3j.ethGetLogs(filter).sendAsync();
    return future.get(TIMEOUT, TimeUnit.SECONDS);
  }

  @Retryable(retryFor = { TimeoutException.class, ExecutionException.class, InterruptedException.class })
  public EthGetTransactionReceipt ethGetTransactionReceipt(String transactionHash) throws ExecutionException, InterruptedException, TimeoutException {
    CompletableFuture<EthGetTransactionReceipt> future = web3j.ethGetTransactionReceipt(transactionHash).sendAsync();
    return future.get(TIMEOUT, TimeUnit.SECONDS);
  }

  @Retryable(retryFor = { TimeoutException.class, ExecutionException.class, InterruptedException.class })
  public EthTransaction ethGetTransactionByHash(String transactionHash) throws ExecutionException, InterruptedException, TimeoutException {
    CompletableFuture<EthTransaction> future = web3j.ethGetTransactionByHash(transactionHash).sendAsync();
    return future.get(TIMEOUT, TimeUnit.SECONDS);
  }

  @Retryable(retryFor = { TimeoutException.class, ExecutionException.class, InterruptedException.class })
  public EthBlock ethGetBlockByNumber(DefaultBlockParameter defaultBlockParameter) throws ExecutionException, InterruptedException, TimeoutException {
    CompletableFuture<EthBlock> future = web3j.ethGetBlockByNumber(defaultBlockParameter, false).sendAsync();
    return future.get(TIMEOUT, TimeUnit.SECONDS);
  }
}
