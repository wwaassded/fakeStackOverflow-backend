package com.what.spring.Exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.dozermapper.core.MappingException;
import com.what.spring.pojo.ResultResponse;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@SuppressWarnings("LoggingSimilarMessage")
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = StringEmptyOrNull.class)
    @ResponseBody
    public ResultResponse StringEmptyOrNull(HttpServletRequest request, StringEmptyOrNull exception) {
        LOG.error("业务逻辑错误{}", exception.getResultMsg());
        return ResultResponse.error(exception.getResultCode(), exception.getResultMsg());
    }

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

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public ResultResponse exceptionHandler(HttpServletRequest request, MethodArgumentNotValidException exception) {
        ResultResponse response = new ResultResponse();
        response.setResultCode(ExceptionEnum.BODY_NOT_MATCH.getResultCode());
        StringBuilder stringBuilder = new StringBuilder();
        exception.getBindingResult().getAllErrors().forEach(e -> {
            stringBuilder.append(e.getDefaultMessage());
        });
        response.setResultMsg(stringBuilder.toString());
        return response;
    }


    @ExceptionHandler(value = JsonProcessingException.class)
    @ResponseBody
    public ResultResponse exceptionHandler(HttpServletRequest request, JsonProcessingException exception) {
        LOG.error("jackson解析json结构错误", exception);
        return ResultResponse.error(ExceptionEnum.JSON_PARSE_ERROR);
    }

    @ExceptionHandler(value = MappingException.class)
    @ResponseBody
    public ResultResponse exceptionHandler(HttpServletRequest request, MappingException exception) {
        LOG.error("dozer mapper 映射出现问题", exception);
        return ResultResponse.error(ExceptionEnum.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = MessagingException.class)
    @ResponseBody
    public ResultResponse exceptionHandler(MessagingException exception) {
        LOG.error("邮件发送问题 ", exception);
        return ResultResponse.error(ExceptionEnum.EMAIL_SENDING_ERROR.getResultCode(), ExceptionEnum.EMAIL_SENDING_ERROR.getResultMsg() + exception.getMessage());
    }

    @ExceptionHandler(value = NoResourceFoundException.class)
    @ResponseBody
    public ResultResponse exceptionHandler(NoResourceFoundException exception) {
        LOG.error("无法找到静态资源: {} {} ", exception.getResourcePath(), exception.getHttpMethod(), exception);
        return ResultResponse.error(ExceptionEnum.CANOT_FIND_STATIC_PROPERTIES);
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultResponse exceptionHandler(HttpServletRequest request, Exception exception) {
        LOG.error("未知的错误发生:", exception);
        return ResultResponse.error(ExceptionEnum.INTERNAL_SERVER_ERROR);
    }
}
