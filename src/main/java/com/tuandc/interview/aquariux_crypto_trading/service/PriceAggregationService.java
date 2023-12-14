package com.tuandc.interview.aquariux_crypto_trading.service;

import com.tuandc.interview.aquariux_crypto_trading.entity.PriceAggregationEntity;
import com.tuandc.interview.aquariux_crypto_trading.model.BinanceSymbol;
import com.tuandc.interview.aquariux_crypto_trading.model.HuobiData;
import com.tuandc.interview.aquariux_crypto_trading.model.HuobiSymbol;
import com.tuandc.interview.aquariux_crypto_trading.repository.PriceAggregationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PriceAggregationService {
    private static final String ETH_USDT = "ETHUSDT";
    private static final String BTC_USDT = "BTCUSDT";
    private static final int CURRENCY_PAIR_SIZE = 2;
    private final RestTemplate restTemplate;
    private final String binanceUrl;
    private final String houbiUrl;
    private final PriceAggregationRepository priceAggregationRepository;

    @Autowired
    public PriceAggregationService(PriceAggregationRepository priceAggregationRepository,
                                   RestTemplate restTemplate,
                                   @Value("${api.binance.url}") String binanceUrl,
                                   @Value("${api.huobi.url}") String houbiUrl) {

        this.priceAggregationRepository = priceAggregationRepository;
        this.restTemplate = restTemplate;
        this.binanceUrl = binanceUrl;
        this.houbiUrl = houbiUrl;
    }

    @Transactional
    public void processAndStoreAggregatedPrices() {

        Map<String, BinanceSymbol> binanceSymbols = retrievePriceFromBinance();
        Map<String, HuobiSymbol> huobiSymbols = retrievePriceFromHuobi();

        // store price for BTC
        BinanceSymbol binanceBTC = binanceSymbols.get(BTC_USDT);
        HuobiSymbol   huobiBTC   = huobiSymbols.get(BTC_USDT);
        storeAggregatedPrice(BTC_USDT, binanceBTC, huobiBTC);

        // store price for ETH
        BinanceSymbol binanceETH = binanceSymbols.get(ETH_USDT);
        HuobiSymbol   huobiETH   = huobiSymbols.get(ETH_USDT);
        storeAggregatedPrice(ETH_USDT, binanceETH, huobiETH);
    }

    private void storeAggregatedPrice(String currencyType, BinanceSymbol binanceSymbol, HuobiSymbol huobiSymbol) {
        // Determine the best bid and ask prices (you need to define the logic based on your business rules)
        BigDecimal bestBidPrice = determineBestBidPrice(binanceSymbol.getBidPrice(), huobiSymbol.getBidPrice());
        BigDecimal bestAskPrice = determineBestAskPrice(binanceSymbol.getAskPrice(), huobiSymbol.getAskPrice());
        // Create/update a new PriceAggregation entity
        PriceAggregationEntity priceAggregation = priceAggregationRepository
                .findByCurrencyType(currencyType)
                .orElse(new PriceAggregationEntity());
        priceAggregation.setBidPrice(bestBidPrice);
        priceAggregation.setAskPrice(bestAskPrice);
        priceAggregation.setCurrencyType(currencyType);
        priceAggregation.setTimestamp(LocalDateTime.now());

        // create/update the entity to the database
        priceAggregationRepository.save(priceAggregation);
    }

    private Map<String, BinanceSymbol> retrievePriceFromBinance() {
        ResponseEntity<List<BinanceSymbol>> binanceResponse = restTemplate.exchange(
                binanceUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});
        if (binanceResponse.getStatusCode().is2xxSuccessful()) {
            Map<String, BinanceSymbol> symbols = new HashMap<>(CURRENCY_PAIR_SIZE);
            for (BinanceSymbol symbol : Objects.requireNonNull(binanceResponse.getBody())) {
                if (ETH_USDT.equalsIgnoreCase(symbol.getSymbol())
                        || BTC_USDT.equalsIgnoreCase(symbol.getSymbol())) {

                    symbols.put(symbol.getSymbol().toUpperCase(), symbol);
                }
            }
            return symbols;
        }

        return  Map.of();
    }

    private Map<String, HuobiSymbol> retrievePriceFromHuobi() {
        ResponseEntity<HuobiData> huobiResponse = restTemplate.getForEntity(houbiUrl, HuobiData.class);
        if (huobiResponse.getStatusCode().is2xxSuccessful()) {
            Map<String, HuobiSymbol> symbols = new HashMap<>(CURRENCY_PAIR_SIZE);
            for (HuobiSymbol symbol : Objects.requireNonNull(Objects.requireNonNull(huobiResponse.getBody()).getData())) {
                if (ETH_USDT.equalsIgnoreCase(symbol.getSymbol())
                        || BTC_USDT.equalsIgnoreCase(symbol.getSymbol())) {

                    symbols.put(symbol.getSymbol().toUpperCase(), symbol);
                }
            }
            return symbols;
        }
        return Map.of();
    }

    private BigDecimal determineBestBidPrice(BigDecimal binanceBidPrice, BigDecimal huobiBidPrice) {
        // just return the higher bid price
        return binanceBidPrice.compareTo(huobiBidPrice) >= 0 ? binanceBidPrice : huobiBidPrice;
    }

    private BigDecimal determineBestAskPrice(BigDecimal binanceAskPrice, BigDecimal huobiAskPrice) {
        // Implement your logic to determine the best ask price
        // just return the lower ask price
        return binanceAskPrice.compareTo(huobiAskPrice) <= 0 ? binanceAskPrice : huobiAskPrice;
    }
}
