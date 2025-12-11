package com.miku.result;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 后端统一返回结果
 * @param <T>
 */
@Data
public class Result<T> implements Serializable {

    private Integer code; //编码：200成功，500和其它数字为失败
    private String msg; //错误信息
    private T data; //数据

    public static <T> Result<T> success() {
        Result<T> result = new Result<T>();
        result.code = 200;
        result.data = (T) new ArrayList<>();
        result.msg = "请求成功";
        return result;
    }

    public static <T> Result<T> success(T object) {
        Result<T> result = new Result<T>();
        result.code = 200;
        result.data = object == null ? (T) new ArrayList<>() : object;
        result.msg = "请求成功";
        return result;
    }

    public static <T> Result<T> error(String msg) {
        Result result = new Result();
        result.code = 500;
        result.data = new ArrayList<>();
        result.msg = msg;
        return result;
    }

}
