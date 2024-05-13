package org.tron.justlend.justlendapiserver.utils.web3j;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;

import java.util.List;

public class ConstantFunction {
  public static Function symbol() {
    return new Function(
      "symbol",
      List.of(),
      List.of(new TypeReference<org.web3j.abi.datatypes.Utf8String>(){}));
  }

  public static Function decimals() {
    return new Function(
      "decimals",
      List.of(),
      List.of(new TypeReference<org.web3j.abi.datatypes.Uint>(){}));
  }

  public static Function name() {
    return new Function(
      "name",
      List.of(),
      List.of(new TypeReference<org.web3j.abi.datatypes.Utf8String>(){}));
  }

  public static Function totalSupply() {
    return new Function(
      "totalSupply",
      List.of(),
      List.of(new TypeReference<org.web3j.abi.datatypes.Uint>(){}));
  }

  public static Function balanceOf(String account) {
    Address address = new Address(account);
    return new Function(
      "balanceOf",
      List.of(address),
      List.of(new TypeReference<org.web3j.abi.datatypes.Uint>(){}));
  }
}
