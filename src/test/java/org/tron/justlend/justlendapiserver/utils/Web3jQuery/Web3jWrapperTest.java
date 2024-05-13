package org.tron.justlend.justlendapiserver.utils.Web3jQuery;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.tron.justlend.justlendapiserver.JustLendApiServerApplicationTests;
import org.tron.justlend.justlendapiserver.config.Tokens;
import org.tron.justlend.justlendapiserver.config.jsonrpc.JsonRpcPool;
import org.tron.justlend.justlendapiserver.model.Token;
import org.tron.justlend.justlendapiserver.utils.blockchain.tron.TronAddressUtils;
import org.tron.justlend.justlendapiserver.utils.web3j.ConstantFunction;
import org.tron.justlend.justlendapiserver.utils.web3j.Web3jWrapper;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthLog;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction;

public class Web3jWrapperTest extends JustLendApiServerApplicationTests {
  @Qualifier("tronJsonRpcPool")
  @Autowired
  JsonRpcPool jsonRpcPool;
  @Autowired
  Tokens tokens;

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

  @Test
  void ethCallTest() throws ExecutionException, InterruptedException, TimeoutException {
    Web3jWrapper web3jWrapper = jsonRpcPool.getWeb3j();
    Token token = tokens.getTronTokenBySymbol("WJST");
    Function function = ConstantFunction.name();
    String data = FunctionEncoder.encode(function);
    Transaction transaction = createEthCallTransaction(null, TronAddressUtils.base58ToHex(token.address()), data);
    EthCall ethCall = web3jWrapper.ethCall(transaction);
    List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
    String result = results.getFirst().getValue().toString(); //  Wrapped JST

    function = ConstantFunction.decimals();
    data = FunctionEncoder.encode(function);
    transaction = createEthCallTransaction(null, TronAddressUtils.base58ToHex(token.address()), data);
    ethCall = web3jWrapper.ethCall(transaction);
    results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
    result = results.getFirst().getValue().toString();
  }
}
