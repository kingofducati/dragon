package com.ximucredit.dragon.dao;

import java.util.List;

import com.ximucredit.dragon.DO.TaskGroupDO;

public interface TaskGroupMapper {
	public List<TaskGroupDO> findRootByType(String taskGroupType);
	public List<TaskGroupDO> selectTaskGroupsByParent(String parentTaskGroupId);
	
	public void insert(TaskGroupDO taskGroupDO);
	public void updateByPrimaryKey(TaskGroupDO taskGroupDO);
	
	public void deleteTaskGroupById(String taskGroupId);
	public void deleteTaskGroupsByType(String taskGroupType);

}
