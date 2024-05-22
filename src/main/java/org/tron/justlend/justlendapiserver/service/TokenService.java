package org.tron.justlend.justlendapiserver.service;

import org.tron.justlend.justlendapiserver.model.Token;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public interface TokenService {
  List<String> getAllSymbol();
  BigDecimal getUsd(Token token, BigInteger rawAmount);
  BigDecimal getAmount(Token token, BigInteger rawAmount);
  BigDecimal getUsd(Token token, BigDecimal amount);
}
