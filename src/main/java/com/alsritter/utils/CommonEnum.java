package com.alsritter.utils;

/**
 * 这里在这里自定响应码
 */
public enum CommonEnum implements BaseErrorInfoInterface{
    // 数据操作错误定义
    SUCCESS(200, "成功!"),
    CREATED(201, "创建成功!"),
    DELETED(204, "删除成功!"),
    BAD_REQUEST(400,"请求的数据格式不符!"),
    UNAUTHORIZED(401,"请求的数字签名不匹配!"),
    FORBIDDEN(403,"被禁止访问!"),
    NOT_FOUND(404, "请求的资源不存在!"),
    INTERNAL_SERVER_ERROR(500, "服务器内部错误!"),
    SERVER_BUSY(503,"服务器正忙，请稍后再试!")
    ;

    /** 响应码 */
    private final Integer resultCode;

    /** 响应描述 */
    private final String resultMsg;

    CommonEnum(Integer resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

    @Override
    public Integer getResultCode() {
        return resultCode;
    }

    @Override
    public String getResultMsg() {
        return resultMsg;
    }
}
