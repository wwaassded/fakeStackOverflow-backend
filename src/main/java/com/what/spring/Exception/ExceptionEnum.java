package com.what.spring.Exception;

public enum ExceptionEnum implements BaseErrorInfoInterface {

    //TODO 有待完善更多的情况
    SUCCESS("2000", "成功!"),
    BODY_NOT_MATCH("4000", "请求的数据格式不符!"),
    SIGNATURE_NOT_MATCH("4001", "请求的数字签名不匹配!"),
    NOT_FOUND("4004", "未找到该资源!"),
    INTERNAL_SERVER_ERROR("5000", "服务器内部错误!"),
    JSON_PARSE_ERROR("50001", "服务器内部解析JSON结构出错"),
    SERVER_BUSY("5003", "服务器正忙，请稍后再试!");

    private final String resultCode;

    private final String resultMsg;

    ExceptionEnum(String resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

    @Override
    public String getResultCode() {
        return resultCode;
    }

    @Override
    public String getResultMsg() {
        return resultMsg;
    }
}
