package org.tron.justlend.justlendapiserver.utils.blockchain.crypto;

import lombok.extern.slf4j.Slf4j;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

@Slf4j
public class SolTypeConvert {

  public static String getSolStrFromDataSlot(String src, int slot) {
    try {
      return src.substring(64 * slot, 64 * (slot + 1));

    } catch (Exception e) {
      log.error("parse error, source :{}", src);
      return "";
    }
  }
  
  public static String long2SolHex(Long value){
    return Numeric.toHexStringNoPrefixZeroPadded(BigInteger.valueOf(value), 64);
  }

  public static Long solHex2Long(String solHex) {
    return solHex2BigInt(solHex).longValue();
  }

  public static BigInteger solHex2BigInt(String solHex) {
    if (solHex.startsWith("0x") || solHex.startsWith("0X")) {
      solHex = solHex.substring(2);
    }
    return new BigInteger(solHex, 16);
  }

  public static Boolean solHex2Bool(String solHex) {
    return (BigInteger.ONE.compareTo(solHex2BigInt(solHex)) == 0);
  }
}
