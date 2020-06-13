package com.currencyfair.rest;

import com.currencyfair.dto.OrderRequest;
import com.currencyfair.dto.OrderResponse;
import com.currencyfair.dto.TradeResponse;
import com.currencyfair.service.TradeProcessor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Validated
@RequestMapping("/trade")
public class TradeController {
    private TradeProcessor tradeProcessor;

    @Autowired
    public TradeController(TradeProcessor tradeProcessor) {
        this.tradeProcessor = tradeProcessor;
    }

    @PostMapping("/review")
    @Operation(summary = "Review trade message", responses = {
            @ApiResponse(responseCode = "200", description = "Trade Message",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    public ResponseEntity<?> review(@Valid @RequestBody OrderRequest request) {
        String txnId = this.tradeProcessor.generateTxnId();
        this.tradeProcessor.process(txnId, request);
        OrderResponse response = new OrderResponse();
        response.setTxnId(txnId);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/query")
    @Operation(summary = "query trade message by userId", responses = {
            @ApiResponse(responseCode = "200", description = "List of trade message",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TradeResponse.class)))),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    public ResponseEntity<?> query(@RequestParam(value = "userId") String userId) {
        return ResponseEntity.ok(this.tradeProcessor.listTradeByUserId(userId));

    }

    @GetMapping("/{txnId}")
    @Operation(summary = "find trade message by txnId", responses = {
            @ApiResponse(responseCode = "200", description = "trade message",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TradeResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    public ResponseEntity<?> get(@PathVariable("txnId") String txnId) {
        return ResponseEntity.ok(this.tradeProcessor.getTrade(txnId));
    }
}
