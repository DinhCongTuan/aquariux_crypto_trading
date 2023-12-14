package com.tuandc.interview.aquariux_crypto_trading.comtroller;


import com.tuandc.interview.aquariux_crypto_trading.exception.BadParameterException;
import com.tuandc.interview.aquariux_crypto_trading.exception.NotFoundEntityException;
import com.tuandc.interview.aquariux_crypto_trading.model.BaseResponse;
import com.tuandc.interview.aquariux_crypto_trading.model.StatusCode;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Level;

@RestController
@Log
public class BaseController {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public BaseResponse<Object> handleException(Exception ex) {
        log.log(Level.SEVERE, ex.getMessage());
        return new BaseResponse<>(StatusCode.SERVER_ERROR, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(NotFoundEntityException.class)
    public BaseResponse<String> handleNotFoundEntityException(NotFoundEntityException ex) {
        return new BaseResponse<>(StatusCode.NOT_FOUND, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadParameterException.class)
    public BaseResponse<String> handleBadParameterException(BadParameterException ex) {
        return new BaseResponse<>(StatusCode.BAD_PARAMETER, ex.getMessage());
    }
}
