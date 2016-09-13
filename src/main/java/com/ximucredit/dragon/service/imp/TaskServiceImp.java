package com.ximucredit.dragon.service.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ximucredit.dragon.DO.TaskDO;
import com.ximucredit.dragon.dao.TaskMapper;
import com.ximucredit.dragon.service.TaskService;

@Service("taskService")
public class TaskServiceImp implements TaskService {
	@Autowired
	private TaskMapper taskMapper;

	public TaskDO findById(String taskId) {
		return taskMapper.findById(taskId);
	}

	public void updateTask(TaskDO taskDO) {
		TaskDO t=findById(taskDO.getTaskId());
		if(t==null){
			taskMapper.insert(taskDO);
		}else{
			taskMapper.updateByPrimaryKey(taskDO);
		}
	}

	public List<TaskDO> findByProjectID(String projectId) {
		return taskMapper.selectTaskByProjectId(projectId);
	}

	public void deleteTask(String taskId) {
		taskMapper.deleteTask(taskId);
	}
	
	public void deleteTasks(String projectId) {
		taskMapper.deleteTasks(projectId);
	}
}
