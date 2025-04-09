package com.what.spring.Exception;

public class CannotFindUser extends StackOverflowBaseException {
    public CannotFindUser() {
        super();
    }

    public CannotFindUser(String resultCode, String resultMsg) {
        super(resultCode, resultMsg);
    }

    public CannotFindUser(BaseErrorInfoInterface baseErrorInfoInterface) {
        super(baseErrorInfoInterface);
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
