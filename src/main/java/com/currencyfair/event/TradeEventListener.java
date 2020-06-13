package com.currencyfair.event;

import com.currencyfair.entity.Trade;
import com.currencyfair.enu.TradeStatus;
import com.currencyfair.repo.TradeRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Log4j2
@Component
public class TradeEventListener {
    private TradeRepo tradeRepo;

    public TradeEventListener(TradeRepo tradeRepo) {
        this.tradeRepo = tradeRepo;
    }

    @EventListener
    @Async
    public void process(TradeEvent tradeEvent) {
        log.info("[txnId: {}] Trade is processing ...", tradeEvent.getTxnId());
        try {
            update(tradeEvent);
            Thread.sleep(3000);
            log.info("[txnId: {}] Trade is confirmed ...", tradeEvent.getTxnId());
        } catch (InterruptedException e) {
            log.error("[txnId: {}] Trade is rejected ...", e);
            e.printStackTrace();
        }

    }

    private void update(TradeEvent tradeEvent) {
        Optional<Trade> trade = tradeRepo.findById(tradeEvent.getTxnId());
        trade.ifPresent(e -> {
            e.setStatus(TradeStatus.CONFIRMED);
            tradeRepo.save(e);
        });
    }
}
