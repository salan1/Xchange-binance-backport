package org.knowm.xchange.binance.dto.marketdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import java8.util.function.BiConsumer;
import java8.util.stream.StreamSupport;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public final class BinanceOrderbook {

    public final long lastUpdateId;
    public final SortedMap<BigDecimal, BigDecimal> bids;
    public final SortedMap<BigDecimal, BigDecimal> asks;

    public BinanceOrderbook(
            @JsonProperty("lastUpdateId") long lastUpdateId,
            @JsonProperty("bids") List<Object[]> bidsJson,
            @JsonProperty("asks") List<Object[]> asksJson) {
        this.lastUpdateId = lastUpdateId;
        BiConsumer<Object[], Map<BigDecimal, BigDecimal>> entryProcessor =
                (obj, col) -> {
                    BigDecimal price = new BigDecimal(obj[0].toString());
                    BigDecimal qty = new BigDecimal(obj[1].toString());
                    col.put(price, qty);
                };

        TreeMap<BigDecimal, BigDecimal> bids = new TreeMap<>((k1, k2) -> -k1.compareTo(k2));
        TreeMap<BigDecimal, BigDecimal> asks = new TreeMap<>();

        StreamSupport.stream(bidsJson).forEach(e -> entryProcessor.accept(e, bids));
        StreamSupport.stream(asksJson).forEach(e -> entryProcessor.accept(e, asks));

        this.bids = Collections.unmodifiableSortedMap(bids);
        this.asks = Collections.unmodifiableSortedMap(asks);
    }
}
