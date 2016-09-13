/**
 * 
 */
package com.ximucredit.teambition;

import java.util.Date;


/**
 * 任务
 * @author dux.fangl
 *
 */
public class Task{
	private String taskID;
	private String projectID;
	private String creatorID;
	private String executorID;
	private String tasklistID;
	private String stageID;
	private boolean isDone;
	private int priority;
	private String note;
	private Date accomplished;
	private String content;
	private Date created;
	private Date updated;
	private Date startDate;
	private Date endDate;
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Date getAccomplished() {
		return accomplished;
	}
	public void setAccomplished(Date accomplished) {
		this.accomplished = accomplished;
	}
	public String getTaskID() {
		return taskID;
	}
	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}
	public String getProjectID() {
		return projectID;
	}
	public void setProjectID(String projectID) {
		this.projectID = projectID;
	}
	public String getCreatorID() {
		return creatorID;
	}
	public void setCreatorID(String creatorID) {
		this.creatorID = creatorID;
	}
	public String getExecutorID() {
		return executorID;
	}
	public void setExecutorID(String executorID) {
		this.executorID = executorID;
	}
	public String getTasklistID() {
		return tasklistID;
	}
	public void setTasklistID(String tasklistID) {
		this.tasklistID = tasklistID;
	}
	public String getStageID() {
		return stageID;
	}
	public void setStageID(String stageID) {
		this.stageID = stageID;
	}
	public boolean isDone() {
		return isDone;
	}
	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getUpdated() {
		return updated;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
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
	
	
}
