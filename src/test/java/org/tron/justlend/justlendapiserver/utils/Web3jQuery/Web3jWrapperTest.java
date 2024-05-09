package org.tron.justlend.justlendapiserver.utils.Web3jQuery;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.tron.justlend.justlendapiserver.JustLendApiServerApplicationTests;
import org.tron.justlend.justlendapiserver.config.jsonrpc.JsonRpcPool;
import org.tron.justlend.justlendapiserver.utils.blockchain.tron.TronAddressUtils;
import org.tron.justlend.justlendapiserver.utils.web3j.Web3jWrapper;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthLog;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class Web3jWrapperTest extends JustLendApiServerApplicationTests {
  @Qualifier("tronJsonRpcPool")
  @Autowired
  JsonRpcPool jsonRpcPool;

  @Test
  void test() throws ExecutionException, InterruptedException, TimeoutException {
    Web3jWrapper web3jWrapper = jsonRpcPool.getWeb3j();
    String address = TronAddressUtils.base58ToHex("TKRQdrYXgrFSBx6jjfqRv6vFdsDZA9iG4q");
    EthFilter filter = new EthFilter(DefaultBlockParameter.valueOf(BigInteger.valueOf(61444271)), DefaultBlockParameter.valueOf(BigInteger.valueOf(61447175)), address);
    List<EthLog.LogResult> results = web3jWrapper.ethGetLogs(filter).getLogs();
  }

  @Test
  void ethGetBlockByNumberTest() throws ExecutionException, InterruptedException, TimeoutException {
    Web3jWrapper web3jWrapper = jsonRpcPool.getWeb3j();
    EthBlock block = web3jWrapper.ethGetBlockByNumber(DefaultBlockParameter.valueOf(BigInteger.valueOf(46633933)));
    List<EthBlock.TransactionResult> txn = block.getBlock().getTransactions();
  }
}
