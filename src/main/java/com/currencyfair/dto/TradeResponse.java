package com.currencyfair.dto;

import com.currencyfair.enu.Country;
import com.currencyfair.enu.TradeStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class TradeResponse {
    @Schema(example = "t-2000002030")
    private String txnId;

    private TradeStatus status;
    @Schema(example = "123456")
    private String userId;

    @Schema(example = "HKD")
    private String currencyFrom;

    @Schema(example = "USD")
    private String currencyTo;

    @Schema(example = "100.01", type = "double")
    private Double amountSell;

    @Schema(example = "9900.02", type = "double")
    private Double amountBuy;

    @Schema(example = "7.7754", type = "double")
    private Double rate;

    @Schema(example = "24-Jan-2020 15:30:05", pattern = "dd-MMM-yy HH:mm:ss")
    private String timePlaced;

    private Country originatingCountry;
}
