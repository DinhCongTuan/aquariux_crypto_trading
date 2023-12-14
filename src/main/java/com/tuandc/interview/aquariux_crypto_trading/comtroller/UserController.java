package com.tuandc.interview.aquariux_crypto_trading.comtroller;

import com.tuandc.interview.aquariux_crypto_trading.model.BaseResponse;
import com.tuandc.interview.aquariux_crypto_trading.model.StatusCode;
import com.tuandc.interview.aquariux_crypto_trading.model.Transaction;
import com.tuandc.interview.aquariux_crypto_trading.model.User;
import com.tuandc.interview.aquariux_crypto_trading.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created"),
            @ApiResponse(responseCode = "400", description = "Invalid input")})
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse<User> createUser(
            @Parameter(description = "User details to create") @RequestParam("username") String userName) {
        // Your logic to create a user
        User user =  userService.createUser(userName);
       return new BaseResponse<>(StatusCode.SUCCESS, user);
    }

    @GetMapping("/{userId}/wallet-balance")
    @Operation(summary = "Get user's cryptocurrency wallet balance")
    public BaseResponse<User> getUserWalletBalance(
            @PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return new BaseResponse<>(StatusCode.SUCCESS, user);
    }

    @GetMapping("/{userId}/trading-history")
    @Operation(summary = "Get user's trading history")
    public BaseResponse<Map<String, Object>> getUserTradingHistory(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Transaction> paging = userService.getUserTradingHistory(userId, page, size);
        paging.getTotalPages();

        Map<String, Object> data = new HashMap<>();
        data.put("total_records", paging.getTotalElements());
        data.put("total_pages", paging.getTotalPages());
        data.put("content", paging.getContent());
        return new BaseResponse<>(StatusCode.SUCCESS, data);
    }

}
