package com.currencyfair.service;

import com.currencyfair.dto.OrderRequest;
import com.currencyfair.dto.TradeResponse;
import com.currencyfair.entity.Trade;
import com.currencyfair.enu.TradeStatus;
import com.currencyfair.event.TradeEvent;
import com.currencyfair.repo.TradeRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
public class TradeProcessorImpl implements TradeProcessor, ApplicationEventPublisherAware {

    private static DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MMM-dd HH:mm:ss").withLocale(Locale.ENGLISH);
    private TradeRepo tradeRepo;
    private ApplicationEventPublisher applicationEventPublisher;

    public TradeProcessorImpl(TradeRepo tradeRepo) {
        this.tradeRepo = tradeRepo;
    }

    @Override
    public String generateTxnId() {
        String txnId = "t-" + System.currentTimeMillis();
        return txnId;

    }

    @Override
    public void process(String txnId, OrderRequest orderRequest) {
        this.save(txnId, orderRequest, TradeStatus.PROCESSING);
        this.applicationEventPublisher.publishEvent(new TradeEvent(this, txnId, orderRequest));
    }

    @Override
    public TradeResponse getTrade(String txnId) {
        Optional<Trade> trade = tradeRepo.findById(txnId);
        return trade.isPresent() ? toTradeResponse(trade.get()) : null;
    }

    @Override
    public List<TradeResponse> all() {
        List<Trade> tradeList = tradeRepo.findAll();
        return tradeList != null ? tradeList.stream().map(this::toTradeResponse).collect(Collectors.toList()) : null;
    }

    @Override
    public List<TradeResponse> listTradeByUserId(String userId) {
        List<Trade> tradeList = tradeRepo.findByUserId(userId);
        return tradeList != null ? tradeList.stream().map(this::toTradeResponse).collect(Collectors.toList()) : null;
    }

    private TradeResponse toTradeResponse(Trade trade) {
        return TradeResponse.builder().txnId(trade.getTxnId())
                .amountBuy(trade.getAmountBuy())
                .amountSell(trade.getAmountSell())
                .currencyFrom(trade.getCurrencyFrom())
                .currencyTo(trade.getCurrencyTo())
                .originatingCountry(trade.getOriginatingCountry())
                .rate(trade.getRate())
                .timePlaced(df.format(trade.getTimePlaced().toInstant().atZone(ZoneId.systemDefault())))
                .userId(trade.getUserId())
                .status(trade.getStatus()).build();
    }

    private void save(String txnId, OrderRequest orderRequest, TradeStatus tradeStatus) {
        Trade trade = new Trade();
        trade.setTxnId(txnId);
        trade.setAmountBuy(orderRequest.getAmountBuy());
        trade.setAmountSell(orderRequest.getAmountSell());
        trade.setCurrencyFrom(orderRequest.getCurrencyFrom());
        trade.setCurrencyTo(orderRequest.getCurrencyTo());
        trade.setOriginatingCountry(orderRequest.getOriginatingCountry());
        trade.setRate(orderRequest.getRate());
        trade.setTimePlaced(orderRequest.getTimePlaced());
        trade.setUserId(orderRequest.getUserId());
        trade.setStatus(tradeStatus);
        tradeRepo.save(trade);
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}