package org.tron.justlend.justlendapiserver.task;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.tron.justlend.justlendapiserver.model.PriceCentreFeed;
import org.tron.justlend.justlendapiserver.service.PriceCentreFeedService;
import org.tron.justlend.justlendapiserver.service.TokenService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PriceCentreFeedTask {
  private final TokenService tokenService;
  private final PriceCentreFeedService priceCentreFeedService;

  @Scheduled(fixedDelay = 1000 * 60)
  public void updateTokenPriceTask() {
    List<String> allSymbols = tokenService.getAllSymbol();
    List<PriceCentreFeed> feeds = priceCentreFeedService.fetch(allSymbols);
    priceCentreFeedService.upsert(feeds);
  }
}
