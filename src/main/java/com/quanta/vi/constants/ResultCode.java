package com.quanta.vi.constants;

/**
 * description:
 *
 * @author quanta
 * @date 2021/9/13
 */
public enum ResultCode {
    SUCCESS(0, "操作成功"),
    FAILED(400, "操作失败"),
    SERVER_ERROR(500, "服务器错误"),
    VALIDATE_FAILED(402, "参数检验失败"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    FORBIDDEN(403, "没有相关权限");

    private final int code;
    private final String msg;

    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
