package com.tuandc.interview.aquariux_crypto_trading.comtroller;

import com.tuandc.interview.aquariux_crypto_trading.model.BaseResponse;
import com.tuandc.interview.aquariux_crypto_trading.model.StatusCode;
import com.tuandc.interview.aquariux_crypto_trading.model.User;
import com.tuandc.interview.aquariux_crypto_trading.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
