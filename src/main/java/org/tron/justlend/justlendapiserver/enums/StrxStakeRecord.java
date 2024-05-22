package org.tron.justlend.justlendapiserver.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StrxStakeRecord {
  DEPOSIT(1),             // 质押 sTRX ------------ +xxx sTRX
  WITHDRAW(2),            // 解锁 sTRX ------------ -xxx sTRX
  UNFREEZE(3),
  CLAIM(4),               // 提取 ----------------- +xxx TRX
  TRANSFER_OUT(5),        // sTRX 转出 ------------ -xxx sTRX
  TRANSFER_IN(6);         // sTRX 转入 ------------ +xxx sTRX

  public final int type;
}
