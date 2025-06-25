package org.example.ware.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter          //因为是枚举，所以只调用就行
public enum Code {
    LOGIN_ERROR(400, "用户名密码错误"),
    BAD_REQUEST(400, "请求错误"),
    UNAUTHORIZED(401, "未登录"),
    TOKEN_EXPIRED(403, "过期请重新登录"),
    FORBIDDEN(403, "无权限"),
    NAME_ERROR(400, "用户名已存在"),
    PARAM_ERROR(400,"用户名密码邮箱不能为空"),
    NAMEPASS_ERROR(400,"用户名密码不能为空"),
    USER_NOT_EXIST_ERROR(400, "用户名不存在"),
    PASSAGE_ERROR(400,"密码错误！"),
    PASSAGE_REQUEST(400,"新密码需至少8位，包含字母和数字"),
    EMAIL_EXIST_ERROR(400,"邮箱已经存在");



    private final int code;//业务码
    private final String message;//异常信息

    Code(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
