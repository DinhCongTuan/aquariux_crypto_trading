package com.tuandc.interview.aquariux_crypto_trading.model;

import lombok.Getter;

@Getter
public enum StatusCode {
    SUCCESS(0, "Success"),
    BAD_PARAMETER(400, "Bad parameter"),
    NOT_FOUND(404,"Not found"),
    INVALID_PARAM(412, "Invalid param"),
    SERVER_ERROR(500, "Server error");


    private final int code;
    private final String message;

    StatusCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
