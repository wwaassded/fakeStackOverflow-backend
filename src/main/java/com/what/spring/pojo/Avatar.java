package com.what.spring.pojo;

import lombok.Data;

@Data
public class Avatar {
    private Integer id;
    private String name;
    private String uploadTime;
    private Integer uploadUserId;
}
