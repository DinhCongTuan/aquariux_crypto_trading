package com.tuandc.interview.aquariux_crypto_trading.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadParameterException extends CryptoTradingException {
    public BadParameterException()
    {
        super();
    }
    public BadParameterException(String message)
    {
        super(message);
    }
    public BadParameterException(Throwable throwable)
    {
        super(throwable);
    }
    public BadParameterException(String message, Throwable throwable)
    {
        super(message, throwable);
    }
}
