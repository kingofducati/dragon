package com.ximucredit.dragon.DO;

import java.io.Serializable;
import java.util.Date;

public class MemberDO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 625868238029926524L;
	
	private String memberId;
	private String userId;
	private String projectId;
	private Date gmtCreate;
	private Date gmtModify;
	
	private UserDO user;

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

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public UserDO getUser() {
		return user;
	}

	public void setUser(UserDO user) {
		this.user = user;
	}
}
