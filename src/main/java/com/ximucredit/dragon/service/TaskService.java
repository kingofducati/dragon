package com.ximucredit.dragon.service;

import java.util.List;

import com.ximucredit.dragon.DO.TaskDO;

public interface TaskService {
	public TaskDO findById(String taskId);
	public void updateTask(TaskDO taskDO);
	public void deleteTask(String taskId);
	public void deleteTasks(String projectId);
	public List<TaskDO> findByProjectID(String projectId);
}
