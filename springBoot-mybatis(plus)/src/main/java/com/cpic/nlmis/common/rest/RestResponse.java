package com.cpic.nlmis.common.rest;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: hk
 * @Description: rest包装结果
 * @Date: 2019/8/14 10:49
 */

@ApiModel(description = "返回结果")
@JsonPropertyOrder({"status","msg","data"})
public class RestResponse {

	@ApiModelProperty(value = "返回数据")
	private Object data;
	@ApiModelProperty(value = "返回码")
	private String status;
	@ApiModelProperty(value = "返回信息")
	private String msg;
	public RestResponse() {
		
	}
	

	private RestResponse(String status, String msg) {
		this.status=status;
		this.msg=msg;
	}

	private RestResponse(String status, String msg, Object data) {
		this.status=status;
		this.msg=msg;
		this.data = data;
	}
	
	public static RestResponse builder(String status,String msg,Object data) {
		return new RestResponse(status, msg,data);
	}
	public static RestResponse builder(String status,String msg) {
		return new RestResponse(status, msg);
	}
	public static RestResponse builder(Object data) {
		return new RestResponse("200", "成功",data);
	}
	public static RestResponse builder() {
		return new RestResponse("200", "成功");
	}

	
	
	
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getstatus() {
		return status;
	}

	public void setstatus(String status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
   
}
