package com.ximucredit.dragon.service;

import java.util.List;
import java.util.Map;

import com.ximucredit.dragon.DO.TaskGroupDO;

public interface TaskGroupService {
	public List<TaskGroupDO> getRoot(String type);
	public void getTree(TaskGroupDO root);
	
	public void deleteTaskGroup(String taskGroupId);
	public void deleteGroupTree(String type);
	
	public void insertTaskGroup(TaskGroupDO taskGroupDO);
	public void updateTaskGroup(TaskGroupDO taskGroupDO);
	
	public Map<String,List<TaskGroupDO>> loadTaskGroupTree(List<String> types);
}
