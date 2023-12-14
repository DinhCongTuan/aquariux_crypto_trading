package com.tuandc.interview.aquariux_crypto_trading.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class BaseResponse<T> {
    private String traceNo;
    private int    code;
    private String message;
    private String date;
    private T      data;

    public void setCode(StatusCode code) {
        this.code    = code.getCode();
        this.message = code.getMessage();
    }

    public void init() {
        this.traceNo = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSSSSS"));
        this.date    = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public BaseResponse(final StatusCode code) {
        init();
        this.setCode(code);
    }

    public BaseResponse(final StatusCode code, final T data) {
        init();
        this.setCode(code);
        this.data = data;
    }
}
