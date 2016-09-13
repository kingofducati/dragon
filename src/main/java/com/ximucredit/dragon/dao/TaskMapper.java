package com.ximucredit.dragon.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ximucredit.dragon.DO.TaskDO;

public interface TaskMapper {
	public TaskDO findById(String taskId);
	public void insert(TaskDO taskDO);
	public void updateByPrimaryKey(TaskDO taskDO);
	public List<TaskDO> selectTaskByProjectId(String projectId);
	public List<TaskDO> selectByTaskGroupIdAndProjectId(@Param("taskGroupId")String taskGroupId,@Param("projectId")String projectId);
	public void deleteTask(String taskId);
	public void deleteTasks(String projectId);
}
