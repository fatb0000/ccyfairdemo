package com.currencyfair.service;

import com.currencyfair.dto.OrderRequest;
import com.currencyfair.dto.TradeResponse;

import java.util.List;

public interface TradeProcessor {
    String generateTxnId();

    void process(String txnId, OrderRequest orderRequest);

    TradeResponse getTrade(String id);

    List<TradeResponse> all();

    List<TradeResponse> listTradeByUserId(String userId);
}
