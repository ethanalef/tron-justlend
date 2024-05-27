package org.tron.justlend.justlendapiserver.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.tron.justlend.justlendapiserver.config.AppProperties;
import org.tron.justlend.justlendapiserver.core.AbstractService;
import org.tron.justlend.justlendapiserver.core.ServiceException;
import org.tron.justlend.justlendapiserver.dao.PriceCentreFeedMapper;
import org.tron.justlend.justlendapiserver.model.PriceCentreFeed;
import org.tron.justlend.justlendapiserver.service.PriceCentreFeedService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
public class PriceCentreFeedServiceImpl extends AbstractService<PriceCentreFeed> implements PriceCentreFeedService {
  private final PriceCentreFeedMapper priceCentreFeedMapper;
  private final AppProperties appProperties;
  private RestClient restClient;

  public PriceCentreFeedServiceImpl(PriceCentreFeedMapper priceCentreFeedMapper, AppProperties appProperties) {
    this.priceCentreFeedMapper = priceCentreFeedMapper;
    this.appProperties = appProperties;
    this.restClient = RestClient.builder()
                          .baseUrl(appProperties.getPriceCentreUri())
                          .build();
  }

  @Override
  public void upsert(List<PriceCentreFeed> priceCentreFeed) {
    priceCentreFeedMapper.upsertBatch(priceCentreFeed);
  }

  @Override
  @Retryable(retryFor = { ResourceAccessException.class, ServiceException.class })
  public List<PriceCentreFeed> fetch(List<String> symbols) {
    String symbolsJoined = String.join(",", symbols);
    ResponseEntity<String> response = restClient.get()
                                        .uri(uriBuilder -> uriBuilder
                                                             .queryParam("symbol", symbolsJoined)
                                                             .queryParam("convert", "USD")
                                                             .build())
                                        .header("Content-Type", "application/json")
                                        .retrieve()
                                        .toEntity(String.class);

    if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
      throw new ServiceException("Failed to get price for " + symbols);
    }

    return parseResponse(response.getBody());
  }

  // cache
  @Override
  public BigDecimal getUsd(String symbol) {
    PriceCentreFeed result = priceCentreFeedMapper.selectOne(PriceCentreFeed.builder().symbol(symbol.toUpperCase()).build());
    if (result != null) {
      return result.getUsd();
    }
    List<PriceCentreFeed> results = fetch(List.of(symbol.toUpperCase()));
    priceCentreFeedMapper.upsertBatch(results);
    return getUsd(symbol);
  }

  private List<PriceCentreFeed> parseResponse(String json) {
    List<PriceCentreFeed> feeds = new ArrayList<>();
    try {
      JSONObject root = JSON.parseObject(json);
      JSONObject data = root.getJSONObject("data");
      for (String symbol : data.keySet()) {
        JSONObject details = data.getJSONObject(symbol);
        JSONObject quote = details.getJSONObject("quote").getJSONObject("USD");
        if (quote == null) {
          log.debug("cannot get price for {}", symbol);
          continue;
        }
        PriceCentreFeed feed = new PriceCentreFeed();
        feed.setSymbol(symbol);
        feed.setUsd(new BigDecimal(quote.getString("price")));
        feeds.add(feed);
      }
    } catch (Exception e) {
      throw new ServiceException("Error parsing JSON response", e);
    }
    return feeds;
  }
}
