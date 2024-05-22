package org.tron.justlend.justlendapiserver.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token {
  private String symbol;
  private String address;
  private int decimals;
}
