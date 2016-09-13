package com.ximucredit.dragon.DO;

import java.io.Serializable;
import java.util.Date;

public class LoginDO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9200805968883484749L;
	
	private String userId;
	private String code;
	private String accessToken;
	private Date loginTime;
	private String wechatCode;
	private String deviceId;
	private String remark;
	private Date gmtCreate;
	private Date gmtModify;
	
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getWechatCode() {
		return wechatCode;
	}
	public void setWechatCode(String wechatCode) {
		this.wechatCode = wechatCode;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public Date getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getGmtCreate() {
		return gmtCreate;
	}
	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}
	public Date getGmtModify() {
		return gmtModify;
	}
	public void setGmtModify(Date gmtModify) {
		this.gmtModify = gmtModify;
	}
	
}
