package com.currencyfair.event;

import com.currencyfair.dto.OrderRequest;
import com.currencyfair.entity.Trade;
import com.currencyfair.enu.TradeStatus;
import com.currencyfair.repo.TradeRepo;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
@Log4j2
public class TradeEventListenerTest {

    @Mock
    TradeRepo tradeRepo;
    EnhancedRandom random;
    @InjectMocks
    private TradeEventListener eventListener;

    @Before
    public void setup() {
        random = EnhancedRandomBuilder.aNewEnhancedRandom();
    }

    @Test
    public void testTradeEventListenerProcessAndTradeStatusIsConfirm() {
        String txnId = "123";
        OrderRequest request = random.nextObject(OrderRequest.class);
        TradeEvent tradeEvent = new TradeEvent(this, txnId, request);
        Trade trade = random.nextObject(Trade.class);
        trade.setStatus(TradeStatus.PROCESSING);
        Mockito.when(tradeRepo.findById(txnId)).thenReturn(Optional.of(trade));
        Mockito.when(tradeRepo.save(Mockito.any(Trade.class))).thenAnswer(i -> i.getArguments()[0]);

        eventListener.process(tradeEvent);
        Mockito.verify(tradeRepo, Mockito.times(1)).findById(txnId);
        Mockito.verify(tradeRepo, Mockito.times(1)).save(trade);
        Assert.assertEquals(TradeStatus.CONFIRMED, trade.getStatus());
    }
}
