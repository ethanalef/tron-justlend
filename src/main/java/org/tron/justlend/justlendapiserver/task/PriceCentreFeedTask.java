package org.tron.justlend.justlendapiserver.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.tron.justlend.justlendapiserver.model.PriceCentreFeed;
import org.tron.justlend.justlendapiserver.service.PriceCentreFeedService;
import org.tron.justlend.justlendapiserver.service.TokenService;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PriceCentreFeedTask {
  private final TokenService tokenService;
  private final PriceCentreFeedService priceCentreFeedService;

  @Scheduled(fixedDelayString = "${schedule.price-centre-feed}")
  public void updateTokenPriceTask() {
    // TODO: use @Timed for measuring cost
    long t = System.currentTimeMillis();
    List<String> allSymbols = tokenService.getAllSymbol();
    List<PriceCentreFeed> feeds = priceCentreFeedService.fetch(allSymbols);
    priceCentreFeedService.upsert(feeds);
    log.info("Updated {} tokens price in {} ms", feeds.size(), System.currentTimeMillis() - t);
  }
}
