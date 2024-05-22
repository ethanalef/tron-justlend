package org.tron.justlend.justlendapiserver.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.tron.justlend.justlendapiserver.JustLendApiServerApplicationTests;
import org.tron.justlend.justlendapiserver.config.Tokens;

class TokenServiceTest extends JustLendApiServerApplicationTests {
  @Autowired
  TokenService tokenService;
  @Autowired
  Tokens tokens;

  @Test
  void test() {
  }
}
