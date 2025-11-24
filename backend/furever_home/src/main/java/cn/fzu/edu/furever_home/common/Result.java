package cn.fzu.edu.furever_home.common;

import lombok.Data;

@Data
public class Result<T> {
    private int code;
    private String message;
    private T data;

    public static <T> Result<T> build(int code, String msg, T data) {
        Result<T> r = new Result<>();
        r.code = code;
        r.message = msg;
        r.data = data;
        return r;
    }

    public static <T> Result<T> success() {
        return build(200, "操作成功", null);
    }

    public static <T> Result<T> success(T data) {
        return build(200, "操作成功", data);
    }

    public static <T> Result<T> success(String msg, T data) {
        return build(200, msg, data);
    }

    public static <T> Result<T> fail(String msg) {
        return build(500, msg, null);
    }

    public static <T> Result<T> fail(int code, String msg) {
        return build(code, msg, null);
    }
}