package com.school.uniform.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Response {
    private Integer code;//操作是否成功的代号
    private String msg;//异常执行的原因说明
    private Object data;//正常执行的响应数据

    public Response(WebResponseEnum webResponseEnum) {
        this.code = webResponseEnum.getStatus();
        this.msg = webResponseEnum.getMessage();
        this.data = null;
    }

    public Response(WebResponseEnum webResponseEnum, Object data) {
        this.code = webResponseEnum.getStatus();
        this.msg = webResponseEnum.getMessage();
        this.data = data;
    }

    public static Response error(Integer code,String message) {
        return new Response(code,message,null);
    }
    public static Object success(Object obj) {
        return new Response(200,"SUCCESS",obj);
    }
}
