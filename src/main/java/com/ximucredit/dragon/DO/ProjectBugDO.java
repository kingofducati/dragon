package com.ximucredit.dragon.DO;

import java.io.Serializable;
import java.util.Date;

import com.ximucredit.dragon.util.Utils;

public class ProjectBugDO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5196985757680812819L;
	
	private String bugId;
	private String projectId;
	private String taskId;
	private String bugContent;
	private String creatorId;
	private String ownerId;
	private String dueTime;
	private int priority;
	private int state;//1=已完成，2=进行中，3=暂缓，4=停止
	private String chatId;
	private String remark;
	private Date gmtCreate;
	private Date gmtModify;
	
	public String getChatId() {
		return chatId;
	}
	public void setChatId(String chatId) {
		this.chatId = chatId;
	}
	
	public String getBugId() {
		return bugId;
	}
	public void setBugId(String bugId) {
		this.bugId = bugId;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getBugContent() {
		return bugContent;
	}
	public void setBugContent(String bugContent) {
		this.bugContent = bugContent;
	}
	public String getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}
	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	public String getDueTime() {
		return dueTime;
	}
	public void setDueTime(String dueTime) {
		this.dueTime = dueTime;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
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
	public double getProgressPercentage() {
		if(this.state==1){
			return 1;
		}else{
			Date d=Utils.paeserDate(dueTime);
			if(d!=null&&this.gmtCreate!=null){
				return Utils.proccess(gmtCreate, d);
			}
		}
		return 0;
	}
	
}
