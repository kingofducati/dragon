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
public class TaskGroupDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8376396765990378511L;

	private String taskGroupId;
	private String taskGroupName;
	private String parentTaskGroupId;
	private String taskGroupType;
	private Date gmtCreate;
	private Date gmtModify;
	
	private List<TaskGroupDO> childen;
	private List<TaskDO> tasks;
	private TaskGroupDO parentTaskGroupDO;
	
	public TaskGroupDO getParentTaskGroupDO() {
		return parentTaskGroupDO;
	}
	public void setParentTaskGroupDO(TaskGroupDO parentTaskGroupDO) {
		this.parentTaskGroupDO = parentTaskGroupDO;
	}
	public String getTaskGroupType() {
		return taskGroupType;
	}
	public void setTaskGroupType(String taskGroupType) {
		this.taskGroupType = taskGroupType;
	}
	public String getTaskGroupId() {
		return taskGroupId;
	}
	public void setTaskGroupId(String taskGroupId) {
		this.taskGroupId = taskGroupId;
	}
	public String getTaskGroupName() {
		return taskGroupName;
	}
	public void setTaskGroupName(String taskGroupName) {
		this.taskGroupName = taskGroupName;
	}
	public String getParentTaskGroupId() {
		return parentTaskGroupId;
	}
	public void setParentTaskGroupId(String parentTaskGroupId) {
		this.parentTaskGroupId = parentTaskGroupId;
	}
	public List<TaskGroupDO> getChilden() {
		return childen;
	}
	public void setChilden(List<TaskGroupDO> childen) {
		this.childen = childen;
	}
	public List<TaskDO> getTasks() {
		return tasks;
	}
	public void setTasks(List<TaskDO> tasks) {
		this.tasks = tasks;
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
	
	public boolean isRoot(){
		return this.parentTaskGroupId==null||this.parentTaskGroupId.length()==0;
	}
	
	public boolean isLeaf(){
		return this.childen==null;
	}
}
