package org.tron.justlend.justlendapiserver.chainchaser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.tron.justlend.justlendapiserver.JustLendApiServerApplicationTests;

class EventSignatureTest extends JustLendApiServerApplicationTests {
  
  @Test
  void getSignatureHashTest() {
    String hash = EventSignature.TRANSFER.getSignatureHash();
    Assertions.assertEquals("ddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef", hash, "Signature hash is not correct!");

    hash = EventSignature.JUSTLENDGOV_PROPOSALCREATE.getSignatureHash();
    Assertions.assertEquals("7d84a6263ae0d98d3329bd7b46bb4e8d6f98cd35a7adb45c274c8b7fd5ebd5e0", hash, "Signature hash is not correct!");
  }
}
