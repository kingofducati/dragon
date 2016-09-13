/**
 * 
 */
package com.ximucredit.dragon.service.imp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ximucredit.dragon.DO.MemberDO;
import com.ximucredit.dragon.DO.ProjectBugDO;
import com.ximucredit.dragon.DO.ProjectDO;
import com.ximucredit.dragon.DO.TaskDO;
import com.ximucredit.dragon.DO.TaskGroupDO;
import com.ximucredit.dragon.DO.UserDO;
import com.ximucredit.dragon.dao.ProjectMapper;
import com.ximucredit.dragon.service.ProjectService;

/**
 * @author dux.fangl
 *
 */
@Service("projectService")
public class ProjectServiceImp implements ProjectService {
	@Autowired
	private ProjectMapper projectMapper;//注入dao
	@Autowired
	private Cache cache;
	
	public void setCache(Cache cache) {
		this.cache = cache;
	}
	
	//memory data model ----------start
	@SuppressWarnings("unchecked")
	public List<ProjectDO> getCachedProject(String userId) {
		if(cache!=null){
			Element mem=cache.get("allprojects");
			if(mem!=null){
				return (List<ProjectDO>)mem.getObjectValue();
			}
		}
		return null;
	}

	public void cachedProject(String userId, List<ProjectDO> project) {
		if(cache!=null){
			Element mem=cache.get("allprojects");
			if(mem!=null){
				cache.remove("allprojects");
			}
			
			mem=new Element("allprojects", project);
			cache.put(mem);
		}
	}
	
	public void removeCachedProject(){
		if(cache!=null){
			cache.remove("allprojects");
		}
	}
	
	public ProjectDO getCachedProjectByID(String userId, String projectId) {
		List<ProjectDO> list=getCachedProject(userId);
		if(list!=null){
			return getProject(projectId, list);
		}
		return null;
	}

	public void addNewProjectToCached(String userId, ProjectDO project) {
		ProjectDO p=getCachedProjectByID(userId, project.getProjectId());
		List<ProjectDO> list=getCachedProject(userId);
		
		if(p!=null){
			list.remove(p);
		}
		
		list.add(project);
	}

	public TaskDO findTaskInCached(String userId, String projectId,String taskId) {
		List<ProjectDO> list=getCachedProject(userId);
		if(list!=null){
			ProjectDO project=getProject(projectId,list);
			if(project!=null){
				return getTask(project,taskId);
			}
		}
		return null;
	}

	private TaskDO getTask(ProjectDO project, String taskId) {
		List<TaskDO> tasks=project.getTasks();
		if(tasks!=null&&taskId!=null){
			for(TaskDO task:tasks){
				if(task.getTaskId().equals(taskId)){
					return task;
				}
			}
		}
		return null;
	}

	private ProjectDO getProject(String projectId, List<ProjectDO> list) {
		if(list!=null&&projectId!=null){
			for(ProjectDO project:list){
				if(project.getProjectId().equals(projectId)){
					return project;
				}
			}
		}
		return null;
	}

	public ProjectBugDO findBugInCached(String userId, String projectId,String bugId) {
		List<ProjectDO> list=getCachedProject(userId);
		if(list!=null){
			ProjectDO project=getProject(projectId,list);
			if(project!=null){
				return getBug(project,bugId);
			}
		}
		return null;
	}

	private ProjectBugDO getBug(ProjectDO project, String bugId) {
		List<ProjectBugDO> bugs=project.getBugs();
		if(bugs!=null&&bugId!=null){
			for(ProjectBugDO bug:bugs){
				if(bug.getBugId().equals(bugId)){
					return bug;
				}
			}
		}
		return null;
	}

	public List<TaskDO> findTasksInCached(String userId, String projectId) {
		List<ProjectDO> list=getCachedProject(userId);
		if(list!=null){
			ProjectDO project=getProject(projectId,list);
			if(project!=null){
				return project.getTasks();
			}
		}
		return null;
	}

	public List<ProjectBugDO> findBugsInCachedByProject(String userId,
			String projectId) {
		List<ProjectDO> list=getCachedProject(userId);
		if(list!=null){
			ProjectDO project=getProject(projectId,list);
			if(project!=null){
				return project.getBugs();
			}
		}
		return null;
	}

	public List<ProjectBugDO> findBugsInCachedByTask(String userId,
			String projectId,String taskId) {
		List<ProjectDO> list=getCachedProject(userId);
		if(list!=null){
			ProjectDO project=getProject(projectId,list);
			if(project!=null){
				return getBugsByTaskId(project.getBugs(),taskId);
			}
		}
		return null;
	}

	private List<ProjectBugDO> getBugsByTaskId(List<ProjectBugDO> bugs,
			String taskId) {
		if(bugs!=null&&taskId!=null){
			List<ProjectBugDO> list=new ArrayList<ProjectBugDO>();
			for(ProjectBugDO bug:bugs){
				if(bug.getTaskId().equals(taskId)){
					list.add(bug);
				}
			}
			
			return list;
		}
		return null;
	}

	public void addNewTaskToCached(String userId, TaskDO task) {
		List<ProjectDO> list=getCachedProject(userId);
		if(list!=null){
			ProjectDO project=getProject(task.getProjectId(),list);
			if(project!=null){
				TaskDO t=getTask(project, task.getTaskId());
				if(t==null){
					List<TaskDO> tasks=project.getTasks();
					if(tasks==null){
						tasks=new ArrayList<TaskDO>();
						project.setTasks(tasks);
					}
				}else{
					project.getTasks().remove(t);
				}
				
				project.getTasks().add(task);
			}
		}
	}

	public void addNewBugToCached(String userId, ProjectBugDO bug) {
		List<ProjectDO> list=getCachedProject(userId);
		if(list!=null){
			ProjectDO project=getProject(bug.getProjectId(),list);
			if(project!=null){
				ProjectBugDO b=getBug(project, bug.getBugId());
				if(b==null){
					List<ProjectBugDO> bugs=project.getBugs();
					if(bugs==null){
						bugs=new ArrayList<ProjectBugDO>();
						project.setBugs(bugs);
					}
				}else{
					project.getBugs().remove(b);
				}
				
				project.getBugs().add(bug);
			}
		}
	}

	public void removeTaskFromCached(String userId, String projectId,
			String taskId) {
		List<ProjectDO> list=getCachedProject(userId);
		if(list!=null){
			ProjectDO project=getProject(projectId,list);
			if(project!=null){
				TaskDO t=getTask(project, taskId);
				if(t!=null){
					project.getTasks().remove(t);
				}
			}
		}
		
	}

	public void removeBugFromCached(String userId, String projectId,
			String bugId) {
		List<ProjectDO> list=getCachedProject(userId);
		if(list!=null){
			ProjectDO project=getProject(projectId,list);
			if(project!=null){
				ProjectBugDO b=getBug(project, bugId);
				if(b!=null){
					project.getBugs().remove(b);
				}
			}
		}
	}

	public void removeProjectFromCached(String userId, String projectId) {
		List<ProjectDO> list=getCachedProject(userId);
		if(list!=null){
			ProjectDO project=getProject(projectId,list);
			if(project!=null){
				list.remove(project);
			}
		}
	}

	public void addNewMemberToProject(String userId, String projectId,
			MemberDO member) {
		List<ProjectDO> list=getCachedProject(userId);
		if(list!=null){
			ProjectDO project=getProject(projectId,list);
			if(project!=null){
				MemberDO m=getMember(project,member.getMemberId());
				if(m==null){
					List<MemberDO> mems=project.getMembers();
					if(mems==null){
						mems=new ArrayList<MemberDO>();
						project.setMembers(mems);
					}
				}else{
					project.getMembers().remove(m);
				}
				
				project.getMembers().add(member);
			}
		}
	}

	private MemberDO getMember(ProjectDO project, String memberId) {
		List<MemberDO> mems=project.getMembers();
		if(mems!=null&&memberId!=null){
			for(MemberDO mem:mems){
				if(mem.getMemberId().equals(memberId)){
					return mem;
				}
			}
		}
		return null;
	}

	public void removeMemberFromProject(String userId, String projectId,
			String memberId) {
		List<ProjectDO> list=getCachedProject(userId);
		if(list!=null){
			ProjectDO project=getProject(projectId,list);
			if(project!=null){
				MemberDO m=getMember(project,memberId);
				if(m!=null){
					project.getMembers().remove(m);
				}
			}
		}
	}

	public List<MemberDO> findMembersInCached(String userId, String projectId) {
		List<ProjectDO> list=getCachedProject(userId);
		if(list!=null){
			ProjectDO project=getProject(projectId,list);
			if(project!=null){
				return project.getMembers();
			}
		}
		return null;
	}

	public MemberDO findMemberInCached(String userId, String projectId,
			String memberId) {
		List<ProjectDO> list=getCachedProject(userId);
		if(list!=null){
			ProjectDO project=getProject(projectId,list);
			if(project!=null){
				return getMember(project, memberId);
			}
		}
		return null;
	}
	
	public MemberDO findMemberByUserId(String userId,String projectId,String uId) {
		List<ProjectDO> list=getCachedProject(userId);
		if(list!=null){
			ProjectDO project=getProject(projectId,list);
			if(project!=null){
				List<MemberDO> mems=project.getMembers();
				if(mems!=null){
					for(MemberDO mem:mems){
						if(mem.getUserId().equals(uId)){
							return mem;
						}
					}
				}
			}
		}
		return null;
	}
	
	public UserDO findUserByUserId(String userId,String projectId) {
		List<ProjectDO> list=getCachedProject(userId);
		if(list!=null){
			ProjectDO project=getProject(projectId,list);
			if(project!=null){
				List<MemberDO> mems=project.getMembers();
				if(mems!=null){
					for(MemberDO mem:mems){
						if(mem.getUserId().equals(userId)){
							return mem.getUser();
						}
					}
				}
			}
		}
		return null;
	}
	
	public void addUsersToCache(List<UserDO> users) {
		Element em=this.cache.get("users");
		if(em!=null){
			this.cache.remove("users");
		}
		em=new Element("users", users);
		this.cache.put(em);
	}
	
	public void addUserToCache(UserDO user) {
		UserDO u=findUser(user.getUserId());
		if(u!=null){
			removeUserFromCache(user.getUserId());
		}
		List<UserDO> users=listUsers();
		users.add(user);
	}
	
	@SuppressWarnings("unchecked")
	public List<UserDO> listUsers() {
		Element em=this.cache.get("users");
		if(em!=null){
			return (List<UserDO>)em.getObjectValue();
		}
		return null;
	}
	
	public UserDO findUser(String userId) {
		List<UserDO> users=listUsers();
			
		for(UserDO user:users){
			if(user.getUserId().equals(userId)){
				return user;
			}
		}
		return null;
	}
	
	public List<UserDO> listUsers(String userId,String projectId) {
		ProjectDO project=getCachedProjectByID(userId, projectId);
		if(project!=null&&project.getMembers()!=null){
			List<UserDO> users=new ArrayList<UserDO>();
			List<MemberDO> mems=project.getMembers();
			for(MemberDO mem:mems){
				if(mem.getUser()!=null){
					users.add(mem.getUser());
				}
			}
			
			return users;
		}
		return null;
	}
	
	public void removeUserFromCache(String userId) {
		List<UserDO> users=listUsers();
			
		UserDO user=findUser(userId);
		users.remove(user);
	}
	
	public void cachedTaskGroupTree(Map<String, List<TaskGroupDO>> trees) {
		if(trees!=null&&trees.size()>0){
			Iterator<Entry<String, List<TaskGroupDO>>> its=trees.entrySet().iterator();
			while(its.hasNext()){
				Entry<String, List<TaskGroupDO>> en=its.next();
				
				Element em=this.cache.get(en.getKey());
				if(em!=null){
					this.cache.remove(en.getKey());
				}
				
				em=new Element(en.getKey(), en.getValue());
				this.cache.put(em);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<TaskGroupDO> getRoot(String type) {
		Element em=this.cache.get(type);
		if(em!=null){
			return (List<TaskGroupDO>)em.getObjectValue();
		}
		return null;
	}
	
	public List<TaskGroupDO> getChildren(TaskGroupDO parent) {
		if(parent.getChilden()!=null){
			return parent.getChilden();
		}else if(parent.getTaskGroupId()!=null&&parent.getTaskGroupType()!=null){
			
		}
		return null;
	}
	
	public TaskGroupDO getNode(String type,String taskGroupId) {
		List<TaskGroupDO> roots=getRoot(type);
		return findNode(roots,taskGroupId);
	}
	
	private TaskGroupDO findNode(List<TaskGroupDO> roots, String taskGroupId) {
		if(roots!=null&&roots.size()>0){
			for(TaskGroupDO tg:roots){
				if(tg.getTaskGroupId().equals(taskGroupId)){
					return tg;
				}else{
					return findNode(tg.getChilden(), taskGroupId);
				}
			}
		}
		
		return null;
	}
	
	public void removeTaskGroup(String type, String taskGroupId) {
		TaskGroupDO rn=getNode(type, taskGroupId);
		TaskGroupDO parent=rn.getParentTaskGroupDO();
		if(parent!=null){
			List<TaskGroupDO> children=parent.getChilden();
			children.remove(rn);
		}else{
			List<TaskGroupDO> roots=getRoot(type);
			roots.remove(rn);
		}
	}
	
	public List<TaskDO> getTasksFromGroup(String taskGroupId,String userId,String projectId) {
		List<TaskDO> tasks= findTasksInCached(userId, projectId);
		if(tasks!=null){
			List<TaskDO> list=new ArrayList<TaskDO>();
			for(TaskDO task:tasks){
				if(task.getTaskGroupId()!=null&&task.getTaskGroupId().equals(taskGroupId)){
					list.add(task);
				}
			}
			
			return list;
		}
		return null;
	}

	//memory data model ----------start
	

	public void insertProject(ProjectDO projectDO){
		projectMapper.insert(projectDO);
	}
	
	public ProjectDO getProjectExtendByID(String projectID) {
		return projectMapper.findById(projectID);
	}
	
	public void updateProjectExtend(String projectId, String projectCode,String projectName,String projectOwnerId,String projectGroup,String projectZijin,String deployTime,
			String nowTaskID, String remark) {
		ProjectDO p=new ProjectDO();
		p.setProjectId(projectId);
		p.setProjectName(projectName);
		p.setProjectOwnerId(projectOwnerId);
		p.setProjectCode(projectCode);
		p.setProjectGroup(projectGroup);
		p.setProjectZijin(projectZijin);
		p.setDeployTime(deployTime);
		p.setNowTaskID(nowTaskID);
		p.setRemark(remark);
		
		update(p);
	}
	
	public boolean update(ProjectDO project) {
		ProjectDO p=projectMapper.findById(project.getProjectId());
		boolean isNew=false;
		if(p==null){
			isNew=true;
		}
		
		if(isNew){
			if(project.getProjectId()!=null&&project.getProjectCode()!=null&&project.getProjectName()!=null){
				projectMapper.insert(project);
			}else{
				return false;
			}
		}else{
			projectMapper.updateByPrimaryKeySelective(project);
		}
		
		return true;
	}
	
	public List<ProjectDO> loadProjectByUserID(String userID) {
		return projectMapper.findAll();
	}
	
	public boolean deleteProject(String projectId) {
		projectMapper.deleteProject(projectId);
		return true;
	}
}
