package org.tron.justlend.justlendapiserver.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class AppProperties {
  @Value("${uri.price-centre}")
  private String priceCentreUri;

  @Value("${task.price-centre-feed.enabled}")
  private Boolean priceCentreFeedEnabled;
  @Value("${task.strx-account-record-chaser.enabled}")
  private Boolean strxAccountRecordChaserEnabled;
  @Value("${task.strx-account-record-chaser.range}")
  private Long strxAccountRecordChaserRange;
  @Value("${task.strx-account-record-chaser.start}")
  private Long strxAccountRecordChaserStart;
  @Value("${task.gov-account-record-chaser.enabled}")
  private Boolean govAccountRecordChaserEnabled;
  @Value("${task.gov-account-record-chaser.range}")
  private Long govAccountRecordChaserRange;
  @Value("${task.gov-account-record-chaser.start}")
  private Long govAccountRecordChaserStart;
}
