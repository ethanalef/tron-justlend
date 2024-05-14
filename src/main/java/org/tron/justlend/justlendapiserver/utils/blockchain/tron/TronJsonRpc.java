package org.tron.justlend.justlendapiserver.utils.blockchain.tron;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.tron.justlend.justlendapiserver.config.jsonrpc.JsonRpcPool;
import org.tron.justlend.justlendapiserver.utils.blockchain.eth.EthJsonRpc;
import org.tron.justlend.justlendapiserver.utils.web3j.Web3jQuery;
import org.tron.justlend.justlendapiserver.utils.web3j.Web3jQueryException;
import org.web3j.abi.datatypes.Function;
import org.web3j.protocol.core.methods.request.EthFilter;

import java.math.BigInteger;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Component
public class TronJsonRpc extends EthJsonRpc implements Web3jQuery {
  public TronJsonRpc(@Qualifier("tronJsonRpcPool") JsonRpcPool jsonRpcPool) {
    super(jsonRpcPool);
  }

  @Override
  public boolean isContract(String address) {
    String hexAddress = TronAddressUtils.base58ToHex(address);
    return super.isContract(hexAddress);
  }

  @Override
  public EthFilter getEthFilter(BigInteger startBlockNum, BigInteger endBlockNum, List<String> topics, List<String> base58Contracts) {
    List<String> contracts = base58Contracts.stream().map(TronAddressUtils::base58ToHex).toList();
    return super.getEthFilter(startBlockNum, endBlockNum, topics, contracts);
  }

  @Override
  public SortedMap<BigInteger, LinkedHashSet<String>> getBlockTxnMap(EthFilter filter) throws Web3jQueryException {
    SortedMap<BigInteger, LinkedHashSet<String>> blockTxnHashMap = super.getBlockTxnMap(filter);

    // Create a new sorted map to hold the updated values
    SortedMap<BigInteger, LinkedHashSet<String>> updatedBlockTxnHashMap = new TreeMap<>();

    // Iterate over the entries of the original map
    blockTxnHashMap.forEach((blockNumber, txnHashes) -> {
      // Stream the set of transaction hashes, remove '0x' prefix, and collect back to a LinkedHashSet
      LinkedHashSet<String> updatedTxnHashes = txnHashes.stream()
                                                 .map(hash -> hash.substring(2))
                                                 .collect(Collectors.toCollection(LinkedHashSet::new));
      // Put the updated set into the new map
      updatedBlockTxnHashMap.put(blockNumber, updatedTxnHashes);
    });

    return updatedBlockTxnHashMap;
  }

  /**
   * Triggers a read-only smart contract function on the Tron blockchain.
   * @param address The Base58 Tron address of the contract.
   * @param function The smart contract function to call.
   * @return The result of the function call.
   * @throws Web3jQueryException if there is an error during the call.
   */
  public <T> T triggerConstantContract(String address, Function function, Class<T> returnType) throws Web3jQueryException {
    String hexAddress = TronAddressUtils.base58ToHex(address);
    return super.triggerConstantContract(hexAddress, function, returnType);
  }
}
