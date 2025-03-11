package com.what.spring.Exception;

import com.what.spring.pojo.ResultResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = StackOverflowBaseException.class)
    @ResponseBody
    public ResultResponse stackOverflowExceptionHandler(HttpServletRequest request, StackOverflowBaseException exception) {
        LOG.error("业务逻辑错误{}", exception.getResultMsg());
        return ResultResponse.error(exception.getResultCode(), exception.getResultMsg());
    }

    @ExceptionHandler(value = NullPointerException.class)
    @ResponseBody
    public ResultResponse exceptionHandler(HttpServletRequest request, NullPointerException exception) {
        LOG.error("空指针错误", exception);
        return ResultResponse.error(ExceptionEnum.BODY_NOT_MATCH);
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultResponse exceptionHandler(HttpServletRequest request, Exception exception) {
        LOG.error("未知的错误发生:", exception);
        return ResultResponse.error(ExceptionEnum.INTERNAL_SERVER_ERROR);
    }
}
