package org.tron.justlend.justlendapiserver.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tron.justlend.justlendapiserver.config.Tokens;
import org.tron.justlend.justlendapiserver.model.Token;
import org.tron.justlend.justlendapiserver.service.PriceCentreFeedService;
import org.tron.justlend.justlendapiserver.service.TokenService;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
  private final Tokens tokens;
  private final PriceCentreFeedService priceCentreFeedService;

  @Override
  public List<String> getAllSymbol() {
      return Stream.concat(tokens.getList().stream(), tokens.getJtokens().stream()).map(Token::getSymbol).toList();
  }

  @Override
  public BigDecimal getAmount(Token token, BigInteger rawAmount) {
    return new BigDecimal(rawAmount).movePointLeft(token.getDecimals());
  }

  @Override
  public BigDecimal getUsd(Token token, BigDecimal amount) {
    BigDecimal tokenUsd = priceCentreFeedService.getUsd(token.getSymbol());
    return tokenUsd.multiply(amount, MathContext.UNLIMITED);
  }

  @Override
  public BigDecimal getUsd(Token token, BigInteger rawAmount) {
    BigDecimal amount = new BigDecimal(rawAmount).movePointLeft(token.getDecimals());
    return getUsd(token, amount);
  }
}
