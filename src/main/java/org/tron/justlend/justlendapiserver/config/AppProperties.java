package org.tron.justlend.justlendapiserver.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class AppProperties {
  @Value("${uri.price-centre}")
  private String priceCentreUri;
}
