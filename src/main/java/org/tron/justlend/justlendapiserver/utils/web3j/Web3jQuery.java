package org.tron.justlend.justlendapiserver.utils.web3j;

import org.tron.justlend.justlendapiserver.utils.web3j.dto.BlockDTO;
import org.tron.justlend.justlendapiserver.utils.web3j.dto.TransactionReceiptDTO;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.core.methods.response.Log;

import java.math.BigInteger;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.SortedMap;


public interface Web3jQuery {
  // Chain query
  BigInteger getCurrentHeight();
  boolean isContract(String address);
  EthFilter getEthFilter(BigInteger startBlockNum, BigInteger endBlockNum, List<String> topics, List<String> contracts);
  EthLog getLog(EthFilter filter);
  BlockDTO getBlock(BigInteger height);
  long getBlockTime(BigInteger height);
  List<Log> getLogResult(EthFilter filter);
  SortedMap<BigInteger, LinkedHashSet<String>> getBlockTxnMap(EthFilter filter);
  TransactionReceiptDTO getTransactionReceiptDTO(String txnHash);
  EthTransaction getTransactionByHash(String txnHash);
}
