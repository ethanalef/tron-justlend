package org.tron.justlend.justlendapiserver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.tron.justlend.justlendapiserver.core.YamlPropertySourceFactory;
import org.tron.justlend.justlendapiserver.model.Token;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "tokens")
@PropertySource(value = "classpath:${spring.profiles.active}/tokens.yml", factory = YamlPropertySourceFactory.class)
public class Tokens {
  private List<Token> tron;
  private List<Token> eth;

  public Token getTronTokenByAddress(String address) {
    return tron.stream().filter(t -> t.address().equals(address)).findFirst().orElse(null);
  }

  public Token getTronTokenBySymbol(String symbol) {
    return tron.stream().filter(t -> t.symbol().equals(symbol)).findFirst().orElse(null);
  }
}
