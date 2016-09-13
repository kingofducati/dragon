package com.ximucredit.dragon.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ximucredit.dragon.DO.ProjectBugDO;

public interface ProjectBugMapper {
	public ProjectBugDO findById(String bugId);
	public List<ProjectBugDO> findByProject(@Param("projectId")String projectId);
	public void insert(ProjectBugDO projectBugDO);
	public void update(ProjectBugDO projectBugDO);
	public void deleteById(String bugId);
	public void deleteBugs(String projectId);
}
