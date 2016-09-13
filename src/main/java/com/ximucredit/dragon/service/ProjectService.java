/**
 * 
 */
package com.ximucredit.dragon.service;

import java.util.List;
import java.util.Map;

import com.ximucredit.dragon.DO.MemberDO;
import com.ximucredit.dragon.DO.ProjectBugDO;
import com.ximucredit.dragon.DO.ProjectDO;
import com.ximucredit.dragon.DO.TaskDO;
import com.ximucredit.dragon.DO.TaskGroupDO;
import com.ximucredit.dragon.DO.UserDO;

/**
 * @author dux.fangl
 *
 */
public interface ProjectService {
	//memory data model ----------start
	public List<ProjectDO> getCachedProject(String userId);
	public void cachedProject(String userId,List<ProjectDO> projects);
	public void removeCachedProject();
	public ProjectDO getCachedProjectByID(String userId,String projectId);
	public void addNewProjectToCached(String userId,ProjectDO project);
	public TaskDO findTaskInCached(String userId,String projectId,String taskId);
	public ProjectBugDO findBugInCached(String userId,String projectId,String bugId);
	public List<TaskDO> findTasksInCached(String userId,String projectId);
	public List<ProjectBugDO> findBugsInCachedByProject(String userId,String projectId);
	public List<ProjectBugDO> findBugsInCachedByTask(String userId,String projectId,String taskId);
	public void addNewTaskToCached(String userId,TaskDO task);
	public void addNewBugToCached(String userId,ProjectBugDO bug);
	public void removeTaskFromCached(String userId,String projectId,String taskId);
	public void removeBugFromCached(String userId,String projectId,String bugId);
	public void removeProjectFromCached(String userId,String projectId);
	public void addNewMemberToProject(String userId,String projectId,MemberDO member);
	public void removeMemberFromProject(String userId,String projectId,String memberId);
	public List<MemberDO> findMembersInCached(String userId,String projectId);
	public MemberDO findMemberInCached(String userId,String projectId,String memberId);
	public MemberDO findMemberByUserId(String userId,String projectId,String uId);
	public UserDO findUserByUserId(String userId,String projectId);
	
	public void addUsersToCache(List<UserDO> users);
	public UserDO findUser(String userId);
	public List<UserDO> listUsers();
	public List<UserDO> listUsers(String userId,String projectId);
	public void addUserToCache(UserDO user);
	public void removeUserFromCache(String userId);
	
	public void cachedTaskGroupTree(Map<String, List<TaskGroupDO>> trees);
	public List<TaskGroupDO> getRoot(String type);
	public List<TaskGroupDO> getChildren(TaskGroupDO parent);
	public TaskGroupDO getNode(String type,String taskGroupId);
	public void removeTaskGroup(String type,String taskGroupId);
	public List<TaskDO> getTasksFromGroup(String taskGroupId,String userId,String projectId);
	
	//memory data model ----------end
	
	public void insertProject(ProjectDO projectDO);
	public boolean deleteProject(String projectId);
	public ProjectDO getProjectExtendByID(String projectID);
	public boolean update(ProjectDO projectDO);
	public void updateProjectExtend(String projectID,String projectCode,String projectName,String projectOwnerId,String projectGroup,String projectZijin,String deployTime,String nowTaskID,String remark);
	public List<ProjectDO> loadProjectByUserID(String userID);
}
