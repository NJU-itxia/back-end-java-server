package com.itxia.backend.controller.vo;

import lombok.Getter;
import lombok.var;

/**
 * 用于封装返回值
 * 无需改动
 *
 * @param <T>
 */
@Getter
public class WrapperResponse<T> {

    private boolean success;
    private T data;

    private WrapperResponse() {
    }

    /**
     * 包装返回对象
     *
     * @param value 被包装的对象
     * @param <T>   对象的类型
     * @return 包装后的对象
     */
    public static <T> WrapperResponse wrap(T value) {
        var response = new WrapperResponse();
        response.success = true;
        response.data = value;
        return response;
    }

    /**
     * 包装一个成功的返回信息
     *
     * @param <T> 实际上没有意义
     * @return 包装后的返回信息
     */
    public static <T> WrapperResponse wrapFail() {
        var response = new WrapperResponse();
        response.success = false;
        response.data = null;
        return response;
    }

    /**
     * 附带错误信息的失败
     * @param message 错误信息
     * @param <T> 不管
     * @return 包装后的返回信息
     */
    public static <T> WrapperResponse wrapFail(String message) {
        var response = new WrapperResponse();
        response.success = false;
        response.data = message;
        return response;
    }

    /**
     * 包装一个失败的返回信息
     *
     * @param <T> 实际上没有意义
     * @return 包装后的返回信息
     */
    public static <T> WrapperResponse wrapSuccess() {
        var response = new WrapperResponse();
        response.success = true;
        response.data = null;
        return response;
    }
}
