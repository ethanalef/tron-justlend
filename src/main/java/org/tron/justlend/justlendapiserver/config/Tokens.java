package org.tron.justlend.justlendapiserver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.tron.justlend.justlendapiserver.core.YamlPropertySourceFactory;
import org.tron.justlend.justlendapiserver.model.JToken;
import org.tron.justlend.justlendapiserver.model.Token;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "tokens")
@PropertySource(value = "classpath:${spring.profiles.active}/tokens.yml", factory = YamlPropertySourceFactory.class)
public class Tokens {
  private List<Token> list;
  private Map<String, Token> mapBySymbol = new HashMap<>();
  private Map<String, Token> mapByAddress = new HashMap<>();

  private List<JToken> jtokens;
  private Map<String, JToken> mapByJTokenSymbol = new HashMap<>();
  private Map<String, JToken> mapByJTokenAddress = new HashMap<>();

  public void setList(List<Token> list) {
    this.list = list;
    list.forEach(token -> {
      mapBySymbol.put(token.getSymbol(), token);
      mapByAddress.put(token.getAddress(), token);
    });

    jtokens.forEach(jtoken -> {
      Token collateral = mapByAddress.getOrDefault(jtoken.getUnderlying(), null);
      jtoken.setCollateral(collateral);
      mapByJTokenSymbol.put(jtoken.getSymbol(), jtoken);
      mapByJTokenAddress.put(jtoken.getAddress(), jtoken);
    });
  }

  public Token getTokenBySymbol(String symbol) {
    return mapBySymbol.getOrDefault(symbol, null);
  }

  public Token getTokenByAddress(String address) {
    return mapByAddress.getOrDefault(address, null);
  }

  public JToken getJTokenBySymbol(String symbol) {
    return mapByJTokenSymbol.getOrDefault(symbol, null);
  }

  public JToken getJTokenByAddress(String address) {
    return mapByJTokenAddress.getOrDefault(address, null);
  }
}
