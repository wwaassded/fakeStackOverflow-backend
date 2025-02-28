package com.what.spring.pojo;

import lombok.Data;

@Data
public class Result {
    private Boolean isSuccessful;
    private String message;
    private Object troubleMaker;
    private String caseSite; // 标记发生问题的类的名称
}
