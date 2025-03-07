package com.what.spring.pojo;

import lombok.Data;

@Data
public class Result {
    private Object object;
    private Boolean isSuccessful;
    private String message;
    private String troubleMaker;
    private String caseSite; // 标记发生问题的类的名称

    public void fillFailedResult(Object o, Class<?> troubleMaker, String message) {
        this.object = o;
        this.isSuccessful = false;
        this.message = message;
        this.troubleMaker = troubleMaker.getName();
    }
}
