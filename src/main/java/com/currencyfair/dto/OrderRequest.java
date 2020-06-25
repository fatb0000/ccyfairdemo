package com.currencyfair.dto;

import com.currencyfair.enu.Country;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;


@Data
@NoArgsConstructor
@ToString
public class OrderRequest {

    @Schema(example = "123456")
    @NotBlank
    private String userId;

    @Schema(example = "HKD")
    @NotBlank
    private String currencyFrom;

    @Schema(example = "USD")
    @NotBlank
    private String currencyTo;

    @Schema(example = "100.01", type = "double")
    @NotNull
    private Double amountSell;

    @Schema(example = "9900.02", type = "double")
    @NotNull
    private Double amountBuy;

    @Schema(example = "7.7754", type = "double")
    @NotNull
    private Double rate;

    @NotNull
    @Schema(example = "24-Jan-2020 15:30:05", pattern = "dd-MMM-yy HH:mm:ss", type = "string")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yy HH:mm:ss", locale = "en")
    private Date timePlaced;

    @NotNull
    private Country originatingCountry;

}
