package org.tron.justlend.justlendapiserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JToken extends Token {
  private String underlying;
  private Token collateral;
}
