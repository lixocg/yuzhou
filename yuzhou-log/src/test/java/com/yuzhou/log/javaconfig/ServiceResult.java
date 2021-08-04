package com.yuzhou.log.javaconfig;

import java.io.Serializable;

public class ServiceResult<T> implements Serializable {
	private static final long serialVersionUID = -828357066742441358L;

	private boolean success = true;

	private T data;

	private String msgInfo;

	private String msgCode;

	public ServiceResult() {

	}

	public ServiceResult(T data) {
		this.data = data;
	}

	public ServiceResult(boolean success, String errCode, String errMessage) {
		this.success = success;
		this.msgCode = errCode;
		this.msgInfo = errMessage;
	}

	public static <T> ServiceResult<T> successResult(T t) {
		return new ServiceResult<T>(t);
	}

	public static <T> ServiceResult<T> failedResult(String errCode, String errMessage) {
		return new ServiceResult<T>(false, errCode, errMessage);
	}


	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}


	public boolean isSuccess() {
		return success;
	}

	public boolean getSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsgInfo() {
		return msgInfo;
	}

	public void setMsgInfo(String msgInfo) {
		this.msgInfo = msgInfo;
	}

	public String getMsgCode() {
		return msgCode;
	}

	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
	}
}