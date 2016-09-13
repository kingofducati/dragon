package com.ximucredit.dragon.service.imp;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ximucredit.dragon.DO.ProjectBugDO;
import com.ximucredit.dragon.dao.ProjectBugMapper;
import com.ximucredit.dragon.service.ProjectBugService;

@Service("bugService")
public class ProjectBugServiceImp implements ProjectBugService {
	@Autowired
	private ProjectBugMapper projectBugMapper;

	public ProjectBugDO findById(String bugId) {
		return projectBugMapper.findById(bugId);
	}
	public List<ProjectBugDO> listByProjectId(String projectId) {
		return projectBugMapper.findByProject(projectId);
	}

	public void saveBug(String bugId,String projectId, String taskId, String content,
			String creatorId, String ownerId, String dueTime, int priority,
			int state, String remark, Date gmtCreate, Date gmtModify) {
		ProjectBugDO bug=new ProjectBugDO();
		bug.setBugContent(content);
		bug.setBugId(bugId);
		bug.setCreatorId(creatorId);
		bug.setDueTime(dueTime);
		bug.setGmtCreate(gmtCreate);
		bug.setGmtModify(gmtModify);
		bug.setOwnerId(ownerId);
		bug.setPriority(priority);
		bug.setProjectId(projectId);
		bug.setRemark(remark);
		bug.setState(state);
		bug.setTaskId(taskId);
		
		saveBug(bug);
	}

	public void saveBug(ProjectBugDO bug) {
		boolean isNew=false;
		if(bug.getBugId()==null||bug.getBugId().length()<=0){
			isNew=true;
			bug.setBugId(UUID.randomUUID().toString());
		}
		
		if(isNew){
			projectBugMapper.insert(bug);
		}else{
			bug.setGmtCreate(null);
			projectBugMapper.update(bug);
		}
		
	}

	public void delete(String bugId) {
		projectBugMapper.deleteById(bugId);
	}
	
	public void deleteBugs(String projectId) {
		// TODO Auto-generated method stub
		
	}

}
