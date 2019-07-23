package com.yingzi.myLearning.constant;

/**
 * @Author: hk
 * @Description: 部门分配的返回码以及对应提示信息
 * @Date: 2019/1/23 9:53
 */
public enum ReturnCodeEnum {
    PARAM_MISSING("40001","参数缺失"),
    PARAM_ERROR("40002","参数有误"),
    PARAM_UN_LOGIN("40003","用户没登陆"),
    FILE_UPLOAD_FAIL("41001","文件上传失败"),
    FILE_UPLOAD_EMPTY("41002","上传的文件为空"),
    TOKEN_EMPTY("10005","token为空"),
    TOKEN_ERROR("10006","解析token出错"),
    TOKEN_DISABLE("10007","token失效"),
    OTHOR_ERROR("500","服务器异常了啦啦啦"),
    TOKEN_NULL("10005","authToken不能为空"),
	TOKEN_WRONG("10006","解析authToken出错"),
	TOKEN_INVALID("10007","authToken失效，请重新调用登录接口");

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
