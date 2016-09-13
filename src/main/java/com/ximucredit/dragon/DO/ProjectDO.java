/**
 * 
 */
package com.ximucredit.dragon.DO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author dux.fangl
 *
 */
public class ProjectDO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2672906548580331653L;
	
	private String projectId;
	private String projectName;
	private String projectCode;
	private String projectOwnerId;
	private String projectGroup;
	private String projectZijin;
	private String projectType;
	private String deployTime;
	private String nowTaskID;
	private String remark;
	private Date startTime;
	private Date endTime;
	private boolean finished;
	private String chatRoomId;
	private Date gmtCreate;
	private Date gmtModify;
	
	private List<TaskDO> tasks;
	private List<MemberDO> members;
	private List<ProjectBugDO> bugs;
	
	public String getChatRoomId() {
		return chatRoomId;
	}
	public void setChatRoomId(String chatRoomId) {
		this.chatRoomId = chatRoomId;
	}
	public String getProjectType() {
		return projectType;
	}
	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}
	public List<ProjectBugDO> getBugs() {
		return bugs;
	}
	public void setBugs(List<ProjectBugDO> bugs) {
		this.bugs = bugs;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public boolean isFinished() {
		return finished;
	}
	public void setFinished(boolean finished) {
		this.finished = finished;
	}
	public String getProjectZijin() {
		return projectZijin;
	}
	public void setProjectZijin(String projectZijin) {
		this.projectZijin = projectZijin;
	}
	public List<MemberDO> getMembers() {
		return members;
	}
	public void setMembers(List<MemberDO> members) {
		this.members = members;
	}
	public List<TaskDO> getTasks() {
		return tasks;
	}
	public void setTasks(List<TaskDO> tasks) {
		this.tasks = tasks;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getProjectCode() {
		return projectCode;
	}
	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
	public String getProjectOwnerId() {
		return projectOwnerId;
	}
	public void setProjectOwnerId(String projectOwnerId) {
		this.projectOwnerId = projectOwnerId;
	}
	public String getProjectGroup() {
		return projectGroup;
	}
	public void setProjectGroup(String projectGroup) {
		this.projectGroup = projectGroup;
	}
	public String getDeployTime() {
		return deployTime;
	}
	public void setDeployTime(String deployTime) {
		this.deployTime = deployTime;
	}
	public String getNowTaskID() {
		return nowTaskID;
	}
	public void setNowTaskID(String nowTaskID) {
		this.nowTaskID = nowTaskID;
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
	
	public String getTaskByTaskID(String nowTaskID) {
		if(tasks!=null){
			for(TaskDO t:tasks){
				if(nowTaskID.equals(t.getTaskId())){
					return t.getContent();
				}
			}
		}
		return null;
	}
	
	public double getProgressPercentage(){
		double taskper=getTaskPercentage();
		double bugper=getBugPercentage();
		
		return taskper*0.9+bugper*0.1;
	}
	private double getBugPercentage() {
		if(this.bugs!=null&&this.bugs.size()>0){
			double bugper=0;
			for(ProjectBugDO bug:bugs){
				bugper+=bug.getProgressPercentage();
			}
			
			return bugper/bugs.size();
		}
		return 0;
	}
	private double getTaskPercentage() {
		if(this.tasks!=null&&this.tasks.size()>0){
			double taskper=0;
			for(TaskDO task:tasks){
				taskper+=task.getProgressPercentage();
			}
			return taskper/tasks.size();
		}
		return 0;
	}
	
}
