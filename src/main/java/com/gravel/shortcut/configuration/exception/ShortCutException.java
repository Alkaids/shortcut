package com.gravel.shortcut.configuration.exception;

/**
 * @ClassName ShortCutException
 * @Description: 业务异常--- 目前没有用到
 * @Author gravel
 * @Date 2020/2/10
 * @Version V1.0
 **/
public class ShortCutException extends RuntimeException {
    public ShortCutException() {
    }

    public ShortCutException(String message) {
        super(message);
    }

    public ShortCutException(String message, Throwable cause) {
        super(message, cause);
    }
}
