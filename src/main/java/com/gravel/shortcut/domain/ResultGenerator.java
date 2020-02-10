package com.gravel.shortcut.domain;

/**
 * @ClassName ResultGenerator
 * @Description: 响应结果包装类
 * @Author gravel
 * @Date 2020/2/10
 * @Version V1.0
 **/
public class ResultGenerator {

    /**
     * 默认响应结果
     */
    private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";

    public static Result<String> genSuccessResult() {
        return new Result<String>()
                .setCode(ResultCode.SUCCESS)
                .setMessage(DEFAULT_SUCCESS_MESSAGE);
    }

    public static <T> Result<T> genSuccessResult(T data) {
        return new Result<T>()
                .setCode(ResultCode.SUCCESS)
                .setMessage(DEFAULT_SUCCESS_MESSAGE)
                .setData(data);
    }

    public static Result<String> genFailResult(String message) {
        return new Result<String>()
                .setCode(ResultCode.FAIL)
                .setMessage(message);
    }
}
