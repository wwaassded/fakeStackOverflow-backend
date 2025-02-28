package com.what.spring.Exception;

public class StringEmptyOrNull extends BaseException {

    public StringEmptyOrNull() {
    }

    public StringEmptyOrNull(String message) {
        super(message);
    }

    public StringEmptyOrNull(String message, Throwable cause) {
        super(message, cause);
    }

    public StringEmptyOrNull(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public StringEmptyOrNull(Throwable cause) {
        super(cause);
    }


}
