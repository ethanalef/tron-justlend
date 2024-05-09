package org.tron.justlend.justlendapiserver.utils.blockchain.tron;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedMap;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.tron.justlend.justlendapiserver.JustLendApiServerApplicationTests;
import org.web3j.protocol.core.methods.request.EthFilter;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

class TronJsonRpcTest extends JustLendApiServerApplicationTests {

  @Autowired
  TronJsonRpc tronJsonRpc;

  EthFilter getFilter_TRANSFER() {
    /*
      Define a filter for test.
      Range from 46578305 to 46578352 inclusively.
      Filter TRANSFER events of USDT:
        46578305: bab5276a822fbb4baa84c26bb612d2b52447e2d3d2291de7692fecb062878719
        46578319: b0ae14dfcb0b84fe7045f1235f717854db8823bbbf3844be05f08f7925fc570c
        46578323: 7c0152c1878dac2ca6293a73fa3c2336c221fb6e750b96cdc27c054ce0846638
        46578349: 5ddba9b9801a1816b081bbcaa00fd5526f5df7daad01e0deebbd53fa10619b72
        46578352: f5c9f35e6a17364e183a33c3c823d6ef1b303dae76ccfd3b79ece6f47b726604
     */
    final String usdt = "TXYZopYRdj2D9XRtbG411XZZ3kM5VkAeBf";
    final String topic = "ddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef";
    BigInteger startBlockNum = new BigInteger("46578305");
    BigInteger endBlockNum = new BigInteger("46578352");
    List<String> contracts = Lists.newArrayList(usdt);
    List<String> topics = Lists.newArrayList(topic);
    EthFilter filter = tronJsonRpc.getEthFilter(startBlockNum, endBlockNum, topics, contracts);
    return filter;
  }

  Map<BigInteger, LinkedHashSet<String>> getBlockHash_TRANSFER() {
    return ImmutableSortedMap.<BigInteger, LinkedHashSet<String>>naturalOrder()
             .put(new BigInteger("46578305"), new LinkedHashSet<>(ImmutableSet.of(
               "bab5276a822fbb4baa84c26bb612d2b52447e2d3d2291de7692fecb062878719"
             )))
             .put(new BigInteger("46578319"), new LinkedHashSet<>(ImmutableSet.of(
               "b0ae14dfcb0b84fe7045f1235f717854db8823bbbf3844be05f08f7925fc570c"
             )))
             .put(new BigInteger("46578323"), new LinkedHashSet<>(ImmutableSet.of(
               "7c0152c1878dac2ca6293a73fa3c2336c221fb6e750b96cdc27c054ce0846638"
             )))
             .put(new BigInteger("46578349"), new LinkedHashSet<>(ImmutableSet.of(
               "5ddba9b9801a1816b081bbcaa00fd5526f5df7daad01e0deebbd53fa10619b72"
             )))
             .put(new BigInteger("46578352"), new LinkedHashSet<>(ImmutableSet.of(
               "f5c9f35e6a17364e183a33c3c823d6ef1b303dae76ccfd3b79ece6f47b726604"
             )))
             .build();
  }

  @Test
  void isContractTest() {
    /* main
    // TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t
    boolean isContract = web3jQuery.isContract("a614f803b6fd780986a42c78ec9c7f77e6ded13c");
    Assert.isTrue(isContract, "TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t is a contract!");

    // THPvaUhoh2Qn2y9THCZML3H815hhFhn5YC (normal account)
    isContract = web3jQuery.isContract("517591d35d313bf6a5e33098284502b045e2bc08");
    Assert.isTrue(!isContract, "THPvaUhoh2Qn2y9THCZML3H815hhFhn5YC is not a contract!");
     */

    /* Shasta
    // TG3XXyExBkPp9nzdajDZsozEu4BkaSJozs (USDT in Shasta)
    boolean isContract = web3jQuery.isContract("42a1e39aefa49290f2b3f9ed688d7cecf86cd6e0");
    Assert.isTrue(isContract, "TG3XXyExBkPp9nzdajDZsozEu4BkaSJozs is a contract!");

    // TSNEe5Tf4rnc9zPMNXfaTF5fZfHDDH8oyW (normal account)
    isContract = web3jQuery.isContract("b3dcf27c251da9363f1a4888257c16676cf54edf");
    Assert.isTrue(!isContract, "TSNEe5Tf4rnc9zPMNXfaTF5fZfHDDH8oyW is not a contract!");
    */

    // nile
    // TXYZopYRdj2D9XRtbG411XZZ3kM5VkAeBf
    boolean isContract = tronJsonRpc.isContract("TXYZopYRdj2D9XRtbG411XZZ3kM5VkAeBf");
    Assertions.assertTrue(isContract, "TXYZopYRdj2D9XRtbG411XZZ3kM5VkAeBf is a contract!");

    // TKGRE6oiU3rEzasue4MsB6sCXXSTx9BAe3 (normal account)
    isContract = tronJsonRpc.isContract("TKGRE6oiU3rEzasue4MsB6sCXXSTx9BAe3");
    Assertions.assertFalse(isContract, "TKGRE6oiU3rEzasue4MsB6sCXXSTx9BAe3 is not a contract!");
  }

  @Test
  void getLog_TRANSFER_Test() {
    // get filtered logs
    var logs = tronJsonRpc.getLog(getFilter_TRANSFER());
    Assertions.assertEquals(logs.getLogs().size(), getBlockHash_TRANSFER().size(), "Inconsistent size!");
  }

  @Test
  void getBlockTxnMap_TRANSFER_Test() {
    // get filtered Block+TxnHsh
    var map = tronJsonRpc.getBlockTxnMap(getFilter_TRANSFER());
    Assertions.assertEquals(map.size(), getBlockHash_TRANSFER().size(), "Inconsistent size!");
    Assertions.assertEquals(map.entrySet(), getBlockHash_TRANSFER().entrySet(), "Inconsistent elements!");
  }

  @Test
  void getTxnReceipt_RENT_Test() {
    // get filtered Block+TxnHsh
    var receipt = tronJsonRpc.getTxnReceipt("fd1dee209763467a4072eb0d82ab08a3f323c04eebde602a4387c7a54ccc1908");
    Date expireDate = Date.from(Instant.now().plus(30, ChronoUnit.MINUTES));
    long instant = Instant.now().toEpochMilli();
    long ts = Timestamp.valueOf(LocalDateTime.now()).getTime();
    long st = System.currentTimeMillis();
//    var receipt = tronJsonRpc.getTxnReceipt("4f4c906b2d1a2e0f3d09e1360b4ee0c1f5abe6a462b494c6b8242f9d6c079584");
  }

  @Test
  void getTxnById_TransferTrx_Test() {
    // get filtered Block+TxnHsh
    var receipt = tronJsonRpc.getTxnReceipt("76125b50de4975459dad7aa515ea63c34609aa5372b71fa104886dabe8fbc701");
//    var receipt = tronJsonRpc.getTransactionByHash("f30178478f6ccbb56f715cb24cf9543e984bffd9e63bf73b26261219e123164f");
  }

  @Test
  void getAllTxnOfAnAccount_Test() {
    // get filtered Block+TxnHsh
//    var receipt = tronJsonRpc.getTxnReceipt("fd1dee209763467a4072eb0d82ab08a3f323c04eebde602a4387c7a54ccc1908");
    var filter = tronJsonRpc.getEthFilter(new BigInteger("61437737"), new BigInteger("61447964"), null, List.of("TKRQdrYXgrFSBx6jjfqRv6vFdsDZA9iG4q"));
    var logs = tronJsonRpc.getLog(filter);
  }
}
