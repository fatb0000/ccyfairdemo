package com.currencyfair.entity;

import com.currencyfair.enu.Country;
import com.currencyfair.enu.TradeStatus;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@ToString
public class Trade {

    @Id
    private String txnId;

    @Column
    private TradeStatus status;

    @Column
    private String userId;

    @Column
    private String currencyFrom;

    @Column
    private String currencyTo;

    @Column
    private Double amountSell;

    @Column
    private Double amountBuy;

    @Column
    private Double rate;

    @Column
    private Date timePlaced;

    @Column
    private Country originatingCountry;
}
