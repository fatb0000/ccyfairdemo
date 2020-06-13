package com.currencyfair.event;

import com.currencyfair.dto.OrderRequest;
import com.currencyfair.repo.TradeRepo;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TradeEventListenerHandlingTest {

    @MockBean
    TradeRepo tradeRepo;
    EnhancedRandom random;
    @Autowired
    private ApplicationEventPublisher publisher;
    @MockBean
    private TradeEventListener eventListener;

    @Test
    public void testTradeEventListenerProcess() {
        random = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build();
        OrderRequest request = random.nextObject(OrderRequest.class);
        publisher.publishEvent(new TradeEvent(this, "123", request));
        Mockito.verify(eventListener, Mockito.times(1)).process(Mockito.any(TradeEvent.class));
    }
}
