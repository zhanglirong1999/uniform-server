package com.school.uniform.model.dto;

public enum WebResponseEnum {
    /**
     *
     */
    commonErr(-1, "服务器处理错误"),
    paramsError(400, "请求参数错误"),
    //    tokenExpired(),
//    tokenNull(),
    tokenError(401, "请先登录，获取token");

    private Integer status;
    private String message;

    WebResponseEnum(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
