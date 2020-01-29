package com.gravel.shortcut.entity;

import lombok.Data;

/**
 * @ClassName Response
 * @Description: 返回实体
 * @Author gravel
 * @Date 2020/1/29
 * @Version V1.0
 **/

@Data
public class Response<T> {
    /**
     * 是否请求成功
     * 成功：true 失败：false
     */
    private boolean success;

    /**
     * 返回数据
     */
    private T data;

    public Response(){
        this.success = true;
    }

    public Response(T data){
        this.success = true;
        this.data = data;
    }

    public Response(boolean success,T data){
        this.success = success;
        this.data = data;
    }


    /**
     * 请求是否成功
     * @return
     */
    public boolean isSuccess(){
        return this.success;
    }

}
