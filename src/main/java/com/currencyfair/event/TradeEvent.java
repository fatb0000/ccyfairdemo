package com.currencyfair.event;

import com.currencyfair.dto.OrderRequest;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class TradeEvent extends ApplicationEvent {

    private OrderRequest orderRequest;
    private String txnId;

    public TradeEvent(Object object, String txnId, OrderRequest orderRequest) {
        super(object);
        this.txnId = txnId;
        this.orderRequest = orderRequest;

    }
}
