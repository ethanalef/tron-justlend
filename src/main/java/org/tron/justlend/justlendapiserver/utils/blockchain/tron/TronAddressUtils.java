package org.tron.justlend.justlendapiserver.utils.blockchain.tron;

import org.bouncycastle.util.encoders.Hex;
import org.tron.justlend.justlendapiserver.utils.blockchain.crypto.Base58;
import org.tron.justlend.justlendapiserver.utils.blockchain.crypto.Sha256Hash;

import java.util.Arrays;

public class TronAddressUtils {

  public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
  public static String ALL_ZERO_BASE58_ADDR = "T9yD14Nj9j7xAB4dbGeiX9h8unkKHxuWwb";
  public static byte addressPrefixByte = (byte) 0x41;   //41 + address;

  /**
   * Converts a Base58 Tron address to a hexadecimal string.
   * @param base58Address The Base58 Tron address string.
   * @return The hexadecimal representation of the address.
   */
  public static String base58ToHex(String base58Address) {
    try {
      byte[] decoded = Base58.decode(base58Address);
      // Typically, the first byte is the network prefix and the last 4 bytes are the checksum.
      // We need to remove these parts to get the raw address.
      byte[] rawAddress = Arrays.copyOfRange(decoded, 1, decoded.length - 4);
      return Hex.toHexString(rawAddress);
    } catch (IllegalArgumentException e) {
      return null;
    }
  }


  /**
   * Converts a hexadecimal string to a Base58 Tron address.
   * @param hexString Address in hexadecimal string with prefix 0x.
   * @return Base58 Tron address.
   */
  public static String hexToBase58(String hexString) {
    return encode58Check(convertToTronAddress(hexString));
  }

  /**
   * get bytes data from hex string data.
   */
  public static byte[] fromHexString(String data) {
    if (data == null) {
      return EMPTY_BYTE_ARRAY;
    }
    if (data.startsWith("0x")) {
      data = data.substring(2);
    }
    if (data.length() % 2 != 0) {
      data = "0" + data;
    }
    return Hex.decode(data);
  }

  public static byte[] convertToTronAddress(String hexString) {
    if (hexString.length() > 40) {
      hexString = hexString.substring(hexString.length() - 40);
    }
    byte[] ret = fromHexString(hexString);
    return convertToTronAddress(ret);
  }

  public static byte[] convertToTronAddress(byte[] address) {
    if (address.length == 20) {
      byte[] newAddress = new byte[21];
      byte[] temp = new byte[]{addressPrefixByte};
      System.arraycopy(temp, 0, newAddress, 0, temp.length);
      System.arraycopy(address, 0, newAddress, temp.length, address.length);
      address = newAddress;
    }
    return address;
  }

  public static String encode58Check(byte[] input) {
    byte[] hash0 = Sha256Hash.hash(input);
    byte[] hash1 = Sha256Hash.hash(hash0);
    byte[] inputCheck = new byte[input.length + 4];
    System.arraycopy(input, 0, inputCheck, 0, input.length);
    System.arraycopy(hash1, 0, inputCheck, input.length, 4);
    return Base58.encode(inputCheck);
  }
}
