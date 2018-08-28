package org.knowm.xchange.bitfinex.v2;

import java.util.List;

import java8.util.stream.Collectors;
import java8.util.stream.RefStreams;
import org.junit.Assert;
import org.junit.Test;
import org.knowm.xchange.currency.CurrencyPair;

public class BitfinexAdaptersTest {

    @Test
    public void adaptCurrencyPairsToTickersParam() {
        List<CurrencyPair> currencyPairs =
                RefStreams.of(CurrencyPair.BTC_USD, CurrencyPair.ETH_USD, CurrencyPair.ETH_BTC)
                        .collect(Collectors.toList());
        String formattedPairs = BitfinexAdapters.adaptCurrencyPairsToTickersParam(currencyPairs);
        Assert.assertEquals("tBTCUSD,tETHUSD,tETHBTC", formattedPairs);
    }
}
