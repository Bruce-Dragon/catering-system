package com.catering.handler;

import com.catering.constant.MessageConstant;
import com.catering.exception.BaseException;
import com.catering.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }
    /*
    *
    * 捕获SQLIntegrityConstraintViolationException异常，
    * SQLIntegrityConstraintViolationException:新增操作用户名重复的情况
    *
    * */
    @ExceptionHandler
    public Result exceptionHandler(SQLIntegrityConstraintViolationException exception){
        String message = exception.getMessage();
        if (message.contains("Duplicate entry")) {
            String[] msg = message.split(" ");
            String username = msg[2];
            String s = username + MessageConstant.ALREADY_EXISTS;
            return Result.error(s);
        }else {
            return Result.error(MessageConstant.UNKNOWN_ERROR);

        }
    }

}
