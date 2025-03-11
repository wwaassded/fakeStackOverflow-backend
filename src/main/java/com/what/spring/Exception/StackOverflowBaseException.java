package com.what.spring.Exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class StackOverflowBaseException extends RuntimeException {
    protected String resultCode;
    protected String resultMsg;

    public StackOverflowBaseException() {
        super();
    }

    public StackOverflowBaseException(BaseErrorInfoInterface baseErrorInfoInterface) {
        super(baseErrorInfoInterface.getResultCode());
        resultCode = baseErrorInfoInterface.getResultCode();
        resultMsg = baseErrorInfoInterface.getResultMsg();
    }

    public StackOverflowBaseException(BaseErrorInfoInterface baseErrorInfoInterface, Throwable cause) {
        super(baseErrorInfoInterface.getResultCode(), cause);
        resultCode = baseErrorInfoInterface.getResultCode();
        resultMsg = baseErrorInfoInterface.getResultMsg();
    }

    public StackOverflowBaseException(String resultMsg) {
        super(resultMsg);
        this.resultMsg = resultMsg;
    }

    public StackOverflowBaseException(String resultCode, String resultMsg) {
        super(resultCode);
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

    public StackOverflowBaseException(String resultCode, String resultMsg, Throwable cause) {
        super(resultCode, cause);
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }


    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
