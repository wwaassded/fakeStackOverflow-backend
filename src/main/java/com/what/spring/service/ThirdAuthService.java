package com.what.spring.service;

import com.what.spring.Exception.StringEmptyOrNull;
import com.what.spring.pojo.Result;
import org.springframework.dao.DataAccessException;

public interface ThirdAuthService {
    Result thirdAuthHandle(String thirdAuthCode) throws StringEmptyOrNull, DataAccessException;
}
