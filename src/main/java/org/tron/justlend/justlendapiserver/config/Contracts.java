package org.tron.justlend.justlendapiserver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.tron.justlend.justlendapiserver.core.YamlPropertySourceFactory;

import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "contracts")
@PropertySource(value = "classpath:${spring.profiles.active}/contracts.yml", factory = YamlPropertySourceFactory.class)
public class Contracts {
  private Map<String, String> tron;
  private Map<String, String> eth;
}
