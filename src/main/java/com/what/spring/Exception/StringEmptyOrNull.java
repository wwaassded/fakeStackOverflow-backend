package com.what.spring.Exception;

public class StringEmptyOrNull extends StackOverflowBaseException {
    public StringEmptyOrNull() {
        super();
    }

    public StringEmptyOrNull(String resultCode, String resultMsg) {
        super(resultCode, resultMsg);
    }

    @Override
    public Throwable fillInStackTrace() {
        return super.fillInStackTrace();
    }

    @Override
    public String getResultCode() {
        return super.getResultCode();
    }

    @Override
    public String getResultMsg() {
        return super.getResultMsg();
    }
}
