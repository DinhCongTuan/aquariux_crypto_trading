package com.tuandc.interview.aquariux_crypto_trading.comtroller;

import com.tuandc.interview.aquariux_crypto_trading.model.BaseResponse;
import com.tuandc.interview.aquariux_crypto_trading.model.StatusCode;
import com.tuandc.interview.aquariux_crypto_trading.model.TradeRequest;
import com.tuandc.interview.aquariux_crypto_trading.service.TradeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trade")
public class TradeController extends BaseController  {

    private final TradeService tradeService;

    @Autowired
    public TradeController(TradeService tradeService) {
        this.tradeService = tradeService;
    }


    @Operation(summary = "Trade based on the latest aggregated price")
    @ApiResponse(responseCode = "200", description = "Successfully executed the trade")
    @ApiResponse(responseCode = "400", description = "Invalid trade request")
    @PostMapping("/execute")
    public BaseResponse<String> executeTrade(@RequestBody TradeRequest tradeRequest) {
        tradeService.executeTrade(tradeRequest);
        return new BaseResponse<>(StatusCode.SUCCESS, "Trade executed successfully");
    }
}
