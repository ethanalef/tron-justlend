package org.tron.justlend.justlendapiserver.utils.blockchain.eth;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.tron.justlend.justlendapiserver.config.jsonrpc.JsonRpcPool;
import org.tron.justlend.justlendapiserver.utils.web3j.Web3jQuery;
import org.tron.justlend.justlendapiserver.utils.web3j.Web3jQueryException;
import org.tron.justlend.justlendapiserver.utils.web3j.Web3jWrapper;
import org.tron.justlend.justlendapiserver.utils.web3j.dto.BlockDTO;
import org.tron.justlend.justlendapiserver.utils.web3j.dto.TransactionReceiptDTO;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;

import java.math.BigInteger;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction;

@Component
public class EthJsonRpc implements Web3jQuery {

  public final JsonRpcPool jsonRpcPool;

  public EthJsonRpc(@Qualifier("ethJsonRpcPool") JsonRpcPool jsonRpcPool) {
    this.jsonRpcPool = jsonRpcPool;
  }

  // TODO: cache 3 seconds
  public BigInteger getCurrentHeight() throws Web3jQueryException {
    Web3jWrapper web3jWrapper = jsonRpcPool.getWeb3j();
    try {
      EthBlockNumber ethBlockNumber = web3jWrapper.ethBlockNumber();
      return ethBlockNumber.getBlockNumber();
    } catch (Exception e) {
      String exceptionMsg = web3jWrapper.getIdentifier() + " getCurrentHeight exception";
      throw new Web3jQueryException(exceptionMsg, e);
    }
  }

  public boolean isContract(String address) throws Web3jQueryException {
    Web3jWrapper web3jWrapper = jsonRpcPool.getWeb3j();
    try {
      EthGetCode ethGetCode = web3jWrapper.ethGetCode(address, DefaultBlockParameterName.LATEST);
      String code = ethGetCode.getCode();

      if (code == null) {
        throw new Web3jQueryException(String.format("isContract failed, no code available for addr=%s", address));
      }

      return !"0x".equals(code);
    } catch (Exception e) {
      throw new Web3jQueryException(String.format("%s isContract failed addr=%s, err=%s",
        web3jWrapper.getIdentifier(), address, e.getMessage()));
    }
  }

  public EthFilter getEthFilter(BigInteger startBlockNum, BigInteger endBlockNum, List<String> topics, List<String> contracts) {
    EthFilter filter = new EthFilter(
      DefaultBlockParameter.valueOf(startBlockNum),
      DefaultBlockParameter.valueOf(endBlockNum),
      contracts);
    if (topics == null)   // seems not supported
      filter.addNullTopic();
    else if (topics.size() == 1)
      filter.addSingleTopic(topics.getFirst());
    else
      filter.addOptionalTopics(topics.toArray(new String[0]));
    return filter;
  }

  public EthLog getLog(EthFilter filter) throws Web3jQueryException {
    Web3jWrapper web3jWrapper = jsonRpcPool.getWeb3j();
    try {
      EthLog ethLog = web3jWrapper.ethGetLogs(filter);
      if (ethLog.hasError()) {
        String errorMessage = web3jWrapper.getIdentifier() + " "
                                + (ethLog.getError() == null ? "Unknown error" : ethLog.getError().getMessage());
        throw new Web3jQueryException(errorMessage);
      }
      return ethLog;
    } catch (Exception e) {
      String exceptionMsg = web3jWrapper.getIdentifier() + " ethGetLogs exception";
      throw new Web3jQueryException(exceptionMsg, e);
    }
  }

  public BlockDTO getBlock(BigInteger height) throws Web3jQueryException {
    Web3jWrapper web3jWrapper = jsonRpcPool.getWeb3j();
    try {
      EthBlock ethBlock = web3jWrapper.ethGetBlockByNumber(DefaultBlockParameter.valueOf(height));
      if (ethBlock.hasError()) {
        String errorMessage = web3jWrapper.getIdentifier() + " "
                                + (ethBlock.getError() == null ? "Unknown error" : ethBlock.getError().getMessage());
        throw new Web3jQueryException(errorMessage);
      }
      return BlockDTO.format(ethBlock);
    } catch (Exception e) {
      String exceptionMsg = web3jWrapper.getIdentifier() + " ethGetLogs exception";
      throw new Web3jQueryException(exceptionMsg, e);
    }
  }

  public Instant getBlockTime(BigInteger height) throws Web3jQueryException {
    BlockDTO blockDTO = getBlock(height);
    try {
      long epoch = Long.parseLong(blockDTO.getTimestamp());
      return Instant.ofEpochSecond(epoch);
    } catch (Exception e) {
      String exceptionMsg = "getBlockTime exception at " + height;
      throw new Web3jQueryException(exceptionMsg, e);
    }
  }

  public List<Log> getLogResult(EthFilter filter) throws Web3jQueryException {
    EthLog ethLog = getLog(filter);
    return ethLog.getLogs().stream().map(r -> (Log) r.get()).toList();
  }

  public SortedMap<BigInteger, LinkedHashSet<String>> getBlockTxnMap(EthFilter filter) throws Web3jQueryException {
    TreeMap<BigInteger, LinkedHashSet<String>> blocks = new TreeMap<>();
    for (Log log : getLogResult(filter)) {
      blocks.computeIfAbsent(log.getBlockNumber(), k -> new LinkedHashSet<>()).add(log.getTransactionHash());
    }
    return blocks;
  }

  public EthGetTransactionReceipt getTxnReceipt(String txnHash) throws Web3jQueryException {
    Web3jWrapper web3jWrapper = jsonRpcPool.getWeb3j();
    EthGetTransactionReceipt ethGetTransactionReceipt;
    try {
      ethGetTransactionReceipt = web3jWrapper.ethGetTransactionReceipt(txnHash);
    } catch (Exception e) {
      String exceptionMsg = web3jWrapper.getIdentifier() + " getTxnReceipt exception";
      throw new Web3jQueryException(exceptionMsg, e);
    }
    if (ethGetTransactionReceipt != null && ethGetTransactionReceipt.getResult() != null
          && !ethGetTransactionReceipt.hasError() && ethGetTransactionReceipt.getResult().isStatusOK()) {
      return ethGetTransactionReceipt;
    }
    throw new Web3jQueryException(web3jWrapper.getIdentifier() + " Null transaction receipt with hash " + txnHash);
  }

  public TransactionReceiptDTO getTransactionReceiptDTO(String txnHash) throws Web3jQueryException {
    EthGetTransactionReceipt ethGetTransactionReceipt = getTxnReceipt(txnHash);
    TransactionReceiptDTO transactionReceiptDTO = null;
    if (ethGetTransactionReceipt != null) {
      transactionReceiptDTO = TransactionReceiptDTO.format(ethGetTransactionReceipt.getResult());
    }
    return transactionReceiptDTO;
  }

  public EthTransaction getTransactionByHash(String txnHash) throws Web3jQueryException {
    Web3jWrapper web3jWrapper = jsonRpcPool.getWeb3j();
    EthTransaction ethTransaction;
    try {
      ethTransaction = web3jWrapper.ethGetTransactionByHash(txnHash);
    } catch (Exception e) {
      String exceptionMsg = web3jWrapper.getIdentifier() + " getTransactionByHash exception";
      throw new Web3jQueryException(exceptionMsg, e);
    }
    if (ethTransaction != null && !ethTransaction.hasError()) {
      return ethTransaction;
    }
    String errorMessage;
    if (ethTransaction != null) {
      errorMessage = String.format("%s getTransactionByHash error for %s %s",
        web3jWrapper.getIdentifier(), txnHash, ethTransaction.getError().getMessage());
    } else {
      errorMessage = web3jWrapper.getIdentifier() + " getTransactionByHash null for " + txnHash;
    }
    throw new Web3jQueryException(errorMessage);
  }

  public <T> T triggerConstantContract(String address, Function function, Class<T> returnType) throws Web3jQueryException {
    Web3jWrapper web3jWrapper = jsonRpcPool.getWeb3j();
    String data = FunctionEncoder.encode(function);
    Transaction transaction = createEthCallTransaction(null, address, data);
    EthCall ethCall;
    try {
      ethCall = web3jWrapper.ethCall(transaction);
    } catch (Exception e) {
      String exceptionMsg = web3jWrapper.getIdentifier() + " triggerConstantContract exception";
      throw new Web3jQueryException(exceptionMsg, e);
    }
    if (ethCall.hasError()) {
      String exceptionMsg = web3jWrapper.getIdentifier() + " triggerConstantContract error " + ethCall.getError().getMessage();
      throw new Web3jQueryException(exceptionMsg);
    }
    List<Type> result = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
    // https://docs.web3j.io/4.11.0/transactions/transactions_and_smart_contracts/
    if (CollectionUtils.isEmpty(result)) {
      String exceptionMsg = String.format("Invalid function call is made or a null result is obtained! address=%s function=%s",
        address, function.getName());
      throw new Web3jQueryException(exceptionMsg);
    }
    return returnType.cast(result.getFirst().getValue());
  }
}
