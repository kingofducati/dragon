package com.ximucredit.dragon.dao;

import java.util.List;

import com.ximucredit.dragon.DO.ProjectDO;

public interface ProjectMapper {
	public void deleteProject(String projectId);
	public ProjectDO findById(String projectId);
	public void insert(ProjectDO projectDO);
	public void updateByPrimaryKeySelective(ProjectDO projectDO);
	public List<ProjectDO> findByUserId(String userId);
	public List<ProjectDO> findAll();
}
