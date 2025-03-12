package com.what.spring.Exception;

public class IOExceptionReadingFromRequestBody extends StackOverflowBaseException {
    public IOExceptionReadingFromRequestBody() {
        super();
    }

    public IOExceptionReadingFromRequestBody(String resultCode, String resultMsg) {
        super(resultCode, resultMsg);
    }

    public IOExceptionReadingFromRequestBody(String resultCode, String resultMsg, Throwable cause) {
        super(resultCode, resultMsg, cause);
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