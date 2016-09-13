package com.ximucredit.dragon.DO;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import com.ximucredit.dragon.util.Utils;

public class TaskDO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1355992977128195986L;
	
	private String taskId;
	private String creatorId;
	private String executorId;
	private String projectId;
	private boolean done;
	private int priority;
	private Date dueDate;//计划完成时间
	private Date startDate;//实际开始时间
	private Date endDate;//实际完成时间
	private String note;
	private String content;
	private Date accomplished;
	private String taskGroupId;
	private Date gmtCreate;
	private Date gmtModify;
	
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getTaskGroupId() {
		return taskGroupId;
	}
	public void setTaskGroupId(String taskGroupId) {
		this.taskGroupId = taskGroupId;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}
	public String getExecutorId() {
		return executorId;
	}
	public void setExecutorId(String executorId) {
		this.executorId = executorId;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public boolean isDone() {
		return done;
	}
	public void setDone(boolean done) {
		this.done = done;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getAccomplished() {
		return accomplished;
	}
	public void setAccomplished(Date accomplished) {
		this.accomplished = accomplished;
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
	
	public TaskDO clone(String userId,String projectId){
		TaskDO task=new TaskDO();
		task.setAccomplished(accomplished);
		task.setContent(content);
		task.setCreatorId(userId);
		task.setDone(isDone());
		task.setDueDate(dueDate);
		task.setExecutorId(executorId);
		task.setNote(note);
		task.setPriority(priority);
		task.setProjectId(projectId);
		task.setTaskId(UUID.randomUUID().toString().replaceAll("-", ""));
		task.setStartDate(startDate);
		task.setEndDate(endDate);
		task.setTaskGroupId(taskGroupId);
		
		return task;
	}
	public double getProgressPercentage() {
		if(isDone()){
			return 1;
		}else{
			if(this.startDate!=null&&this.endDate!=null){
				return Utils.proccess(startDate, endDate);
			}else if(this.gmtCreate!=null&&this.dueDate!=null){
				return Utils.proccess(gmtCreate, dueDate);
			}
		}
		return 0;
	}
}
