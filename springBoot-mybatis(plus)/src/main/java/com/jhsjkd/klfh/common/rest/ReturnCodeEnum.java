package com.dfkj.nlmis.common.rest;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: hk
 * @Description:
 * @Date: 2019/8/14 11:09
 */
public enum ReturnCodeEnum {
    SUCCESS("1","请求成功"),
    PARAM_MISSING("4001","参数缺失"),
    PARAM_ERROR("4002","参数有误"),
    PARAM_UN_LOGIN("4003","用户没登陆"),
    OTHOR_ERROR("500","服务器异常");
    private String code;
    private String msg;
    ReturnCodeEnum(String code,String msg){
        this.code=code;
        this.msg=msg;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }


}
