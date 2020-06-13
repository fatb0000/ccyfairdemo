package com.currencyfair.rest;

import com.currencyfair.dto.TradeResponse;
import com.currencyfair.service.TradeProcessor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private TradeProcessor tradeProcessor;

    @Autowired
    public AdminController(TradeProcessor tradeProcessor) {
        this.tradeProcessor = tradeProcessor;
    }

    @GetMapping("/trade/list")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "List all trade message", responses = {
            @ApiResponse(responseCode = "200", description = "List of trade message",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TradeResponse.class)))),
            @ApiResponse(responseCode = "401", description = "It cannot be access without login"),
            @ApiResponse(responseCode = "403", description = "The role has no permission to this API")})
    public ResponseEntity<?> list() {
        return ResponseEntity.ok(this.tradeProcessor.all());

    }

}
