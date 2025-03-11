package com.what.spring.pojo;

import com.alibaba.fastjson2.JSONObject;
import com.what.spring.Exception.BaseErrorInfoInterface;
import com.what.spring.Exception.ExceptionEnum;
import lombok.Data;

@Data
public class ResultResponse {
    private String resultCode;
    private String resultMsg;
    private Object result;

    public ResultResponse() {
    }

    public ResultResponse(BaseErrorInfoInterface baseErrorInfoInterface) {
        resultMsg = baseErrorInfoInterface.getResultMsg();
        resultCode = baseErrorInfoInterface.getResultCode();
    }

    public static ResultResponse success() {
        return success(null);
    }

    public static ResultResponse success(Object object) {
        ResultResponse response = new ResultResponse();
        response.setResultCode(ExceptionEnum.SUCCESS.getResultCode());
        response.setResultMsg(ExceptionEnum.SUCCESS.getResultMsg());
        response.setResult(object);
        return response;
    }

    public static ResultResponse error(BaseErrorInfoInterface baseErrorInfoInterface) {
        ResultResponse response = new ResultResponse();
        response.setResultCode(baseErrorInfoInterface.getResultCode());
        response.setResultMsg(baseErrorInfoInterface.getResultMsg());
        response.setResult(null);
        return response;
    }

    public static ResultResponse error(String resultCode, String resultMsg) {
        ResultResponse response = new ResultResponse();
        response.setResultCode(resultCode);
        response.setResultMsg(resultMsg);
        response.setResult(null);
        return response;
    }

    public static ResultResponse error(String resultMsg) {
        ResultResponse response = new ResultResponse();
        response.setResultCode("-1");
        response.setResultMsg(resultMsg);
        response.setResult(null);
        return response;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
