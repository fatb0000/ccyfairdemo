package com.currencyfair.service;

import com.currencyfair.dto.TradeResponse;
import com.currencyfair.entity.Trade;
import com.currencyfair.repo.TradeRepo;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RunWith(MockitoJUnitRunner.class)
public class TradeProcessorImplTest {
    @Mock
    TradeRepo tradeRepo;

    @InjectMocks
    TradeProcessorImpl tradeProcessor;
    EnhancedRandom random;

    @Before
    public void setup() {
        random = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build();
    }

    @Test
    public void testGenerateTxnId() {
        String txnId = tradeProcessor.generateTxnId();
        Assert.assertNotNull(txnId);
    }

    @Test
    public void testListAllTrades() {
        List<Trade> trades = random.objects(Trade.class, 3).collect(Collectors.toList());
        Mockito.when(tradeRepo.findAll()).thenReturn(trades);
        List<TradeResponse> response = tradeProcessor.all();
        Assert.assertEquals(3, response.size());
    }

    @Test
    public void testListTradeByUserId() {
        List<Trade> trades = random.objects(Trade.class, 3).collect(Collectors.toList());
        Mockito.when(tradeRepo.findByUserId("123")).thenReturn(trades);
        List<TradeResponse> response = tradeProcessor.listTradeByUserId("123");
        Assert.assertEquals(3, response.size());
    }

    @Test
    public void testGetTradeByTxnId() {
        Optional<Trade> trades = random.objects(Trade.class, 1).findFirst();
        Mockito.when(tradeRepo.findById("123")).thenReturn(trades);
        TradeResponse response = tradeProcessor.getTrade("123");
        Assert.assertEquals(trades.get().getUserId(), response.getUserId());
        Assert.assertEquals(trades.get().getAmountBuy(), response.getAmountBuy());
        Assert.assertEquals(trades.get().getAmountSell(), response.getAmountSell());
        Assert.assertEquals(trades.get().getRate(), response.getRate());
        Assert.assertEquals(trades.get().getCurrencyFrom(), response.getCurrencyFrom());
        Assert.assertEquals(trades.get().getCurrencyTo(), response.getCurrencyTo());
        Assert.assertEquals(trades.get().getOriginatingCountry(), response.getOriginatingCountry());
        Assert.assertEquals(trades.get().getStatus(), response.getStatus());
        Assert.assertEquals(trades.get().getTimePlaced(), response.getTimePlaced());
        Assert.assertEquals(trades.get().getTxnId(), response.getTxnId());
    }
}
