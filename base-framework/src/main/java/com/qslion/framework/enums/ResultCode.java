package com.qslion.framework.enums;

import com.google.common.collect.Lists;
import com.qslion.framework.util.Localize;

/**
 * API 统一返回状态码,按照业务划分
 *
 * @author Gray.Z
 * @date 2018/9/20.
 */
public enum ResultCode {

    /**
     * 未知
     */
    UNKNOWN(-1, "result_unknown"),
    /**
     * 处理成功
     */
    SUCCESS(0, "result_success"),
    /**
     * 失败
     */
    FAIL(1, "result_fail"),

    /**
     * 参数错误：10000-19999
     */
    PARAMETER_ERROR(10000, "result_parameter_error"),
    PARAMETER_IS_INVALID(10001, "参数无效"),
    PARAMETER_IS_BLANK(10002, "参数为空"),
    PARAMETER_TYPE_BIND_ERROR(10003, "参数类型错误"),
    PARAMETER_NOT_COMPLETE(10004, "参数缺失"),

    /**
     * 用户错误：20001-29999
     */
    USER_NOT_LOGGED_IN(20001, "用户未登录"),
    USER_LOGIN_ERROR(20002, "login_username_or_password_error"),
    USER_ACCOUNT_FORBIDDEN(20003, "login_account_disabled"),
    USER_NOT_EXIST(20004, "user_not_exist"),
    USER_HAS_EXISTED(20005, "用户已存在"),
    USER_ACCOUNT_LOCKED(20006, "login_account_locked"),
    USER_ACCOUNT_FAILURE_LOCK(20007, "login_failure_lock"),

    /**
     * 业务错误：30001-39999
     */
    SPECIFIED_QUESTIONED_USER_NOT_EXIST(30001, "某业务出现问题"),

    /**
     * 系统错误：40001-49999
     */
    INTERNAL_SERVER_ERROR(40000, "result_internal_server_error"),
    INTERNAL_SYSTEM__ERROR(40001, "系统繁忙，请稍后重试"),

    /**
     * 数据错误：50001-599999
     */
    DATA_IS_NONE(50001, "数据未找到"),
    DATA_IS_WRONG(50002, "数据有误"),
    DATA_ALREADY_EXISTED(50003, "数据已存在"),

    /**
     * 接口错误：60000-69999
     */
    INTERFACE_NOT_FOUND(60000, "接口不存在"),
    INTERFACE_INNER_INVOKE_ERROR(60001, "内部系统接口调用异常"),
    INTERFACE_OUTER_INVOKE_ERROR(60002, "外部系统接口调用异常"),
    INTERFACE_FORBID_VISIT(60003, "该接口禁止访问"),
    INTERFACE_ADDRESS_INVALID(60004, "接口地址无效"),
    INTERFACE_REQUEST_TIMEOUT(60005, "接口请求超时"),
    INTERFACE_EXCEED_LOAD(60006, "接口负载过高"),

    /**
     * 权限错误：70001-79999
     */
    UNAUTHORIZED(70000, "result_unauthorized"),
    PERMISSION_NO_ACCESS(70001, "无访问权限"),;

    private final int code;

    private String msg;

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

    public String getDesc() {
        return Localize.getMessage(msg);
    }

    public String getDesc(Object... args) {
        return Localize.getMessage(msg, args);
    }

    public static ResultCode getByCode(int code) {
        return Lists.newArrayList(ResultCode.values()).stream().filter(t -> t.getCode() == code).findFirst()
            .orElse(ResultCode.UNKNOWN);
    }
}
