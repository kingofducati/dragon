package com.ximucredit.dragon.service;

import java.util.Date;
import java.util.List;

import com.ximucredit.dragon.DO.ProjectBugDO;

public interface ProjectBugService {
	public ProjectBugDO findById(String bugId);
	public List<ProjectBugDO> listByProjectId(String projectId);
	public void saveBug(
			String bugId,
			String projectId,
			String taskId,
			String content,
			String creatorId,
			String ownerId,
			String dueTime,
			int priority,
			int state,
			String remark,
			Date gmtCreate,
			Date gmtModify);
	public void saveBug(ProjectBugDO bugDO);
	public void delete(String bugId);
	public void deleteBugs(String projectId);
}
