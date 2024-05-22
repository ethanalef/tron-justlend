package org.tron.justlend.justlendapiserver.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.tron.justlend.justlendapiserver.JustLendApiServerApplicationTests;
import org.tron.justlend.justlendapiserver.model.JToken;
import org.tron.justlend.justlendapiserver.model.Token;

class TokensTest extends JustLendApiServerApplicationTests {
  @Autowired
  Tokens tokens;

  @Test
  void test() {
    Token token = tokens.getTokenBySymbol("SUN");
    JToken jToken = tokens.getJTokenBySymbol("jTRX");
  }
}
