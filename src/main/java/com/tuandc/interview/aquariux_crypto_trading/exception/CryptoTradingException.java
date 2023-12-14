package com.tuandc.interview.aquariux_crypto_trading.exception;

import lombok.Data;

@Data
public class CryptoTradingException extends RuntimeException {

    private transient Object data;
    public CryptoTradingException(Object data) {
        super();
        this.data = data;
    }
    public CryptoTradingException() {
        super();
    }
    public CryptoTradingException(String message) {
        super(message);
    }
    public CryptoTradingException(Throwable throwable) {
        super(throwable);
    }
    public CryptoTradingException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
