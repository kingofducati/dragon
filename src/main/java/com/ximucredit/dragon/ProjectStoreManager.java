package com.ximucredit.dragon;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.ximucredit.dragon.DO.MemberDO;
import com.ximucredit.dragon.DO.ProjectBugDO;
import com.ximucredit.dragon.DO.ProjectDO;
import com.ximucredit.dragon.DO.TaskDO;
import com.ximucredit.dragon.DO.TaskGroupDO;
import com.ximucredit.dragon.DO.UserDO;
import com.ximucredit.dragon.service.LoginService;
import com.ximucredit.dragon.service.MailService;
import com.ximucredit.dragon.service.MemberService;
import com.ximucredit.dragon.service.ProjectBugService;
import com.ximucredit.dragon.service.ProjectService;
import com.ximucredit.dragon.service.TaskGroupService;
import com.ximucredit.dragon.service.TaskService;
import com.ximucredit.dragon.service.UserService;
import com.ximucredit.dragon.service.QYMessageService;
import com.ximucredit.dragon.util.Utils;
import com.ximucredit.teambition.Project;

public class ProjectStoreManager implements InitializingBean{
	@Autowired
	private ProjectService projectService;
	@Autowired
	private LoginService loginService;
	@Autowired
	private UserService userService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private ProjectBugService bugService;
	@Autowired
	private QYMessageService qyMessageService;
	@Autowired
	private TaskGroupService taskGroupService;
	@Autowired
	private MailService mailService;
	
	private List<String> projectTypes;
	private Map<String,String> taskDefs;
	private Map<String, String> groupDefs;
	
	public ProjectStoreManager() {
		
	}
	
	private void addMemberToProject(ProjectDO newProject, UserDO user) {
		MemberDO mem=new MemberDO();
		
		mem.setUser(user);
		mem.setMemberId(UUID.randomUUID().toString().replaceAll("-", ""));
		mem.setProjectId(newProject.getProjectId());
		mem.setUserId(user.getUserId());
		
		List<MemberDO> mems=newProject.getMembers();
		if(mems==null){
			mems=new ArrayList<MemberDO>();
		}
		
		mems.add(mem);
		newProject.setMembers(mems);
		
		memberService.updateMember(mem);
	}
	
	public void addNewProject(String userId,ProjectDO project){
		this.projectService.addNewProjectToCached(userId, project);
	}

	public void afterPropertiesSet() throws Exception {
		if(this.groupDefs==null){
	        iniGroupDefsData();
		}
		
		if(this.projectTypes==null){
			iniProjectTypes();
		}
		
		if(this.taskDefs==null){
			iniTaskDefsData();
		}
	}

	public void cachedProject(String userId, List<ProjectDO> projects) {
		this.projectService.cachedProject(userId, projects);
	}
	
	public List<UserDO> cachedUsers() {
		List<UserDO> users=userService.listAllUsers();
		if(users==null||users.size()==0){
			users=new ArrayList<UserDO>();
		}
		
		projectService.addUsersToCache(users);
		return users;
	}
	
	public void cloneProject(String userId,ProjectDO oldProject, ProjectDO newProject) {
		if(oldProject.getTasks()!=null&&oldProject.getTasks().size()>0){
			List<TaskDO> tasks=oldProject.getTasks();
			List<TaskDO> newTasks=newProject.getTasks();
			for(TaskDO task:tasks){
				TaskDO t=task.clone(userId, newProject.getProjectId());
				taskService.updateTask(t);
				
				if(newTasks==null){
					newTasks=new ArrayList<TaskDO>();
				}
				
				newTasks.add(t);
			}
			
			newProject.setTasks(newTasks);
		}
		
		UserDO user=projectService.findUserByUserId(userId, oldProject.getProjectId());
		
		if(user!=null){
			addMemberToProject(newProject,user);
		}
	}
	
	public boolean createBugChatRoom(ProjectDO project,ProjectBugDO bug) {
		UserDO user=projectService.findUserByUserId(bug.getCreatorId(), project.getProjectId());
		String ownerId=user.getEmail();
		String name="【"+project.getProjectName()+"项目】问题跟进："+bug.getBugContent();
		List<MemberDO> ms=project.getMembers();
		List<String> userList=new ArrayList<String>();
		
		for(MemberDO m:ms){
			userList.add(m.getUser().getEmail());
		}
		
		if(project.getChatRoomId()==null&&project.getChatRoomId().length()>0){
			String chatId=project.getProjectId().replaceAll("-", "");
			
			boolean ok = qyMessageService.createChatRoom(chatId, name, ownerId, userList);
			
			if(ok){
				qyMessageService.sendGroupMsg(chatId, ownerId, "【项目】："+project.getProjectName()+"\\n【当前讨论问题】："+bug.getBugContent()+"。\\n【讨论组创建人】："+user.getName());
			}
			
			project.setChatRoomId(chatId);
			projectService.update(project);
			
			return ok;
		}else{
			String users=qyMessageService.getCharRoomUserList(project.getChatRoomId());
			if(users!=null){
				JSONArray ja=new JSONArray(users);
				if(ja.length()!=userList.size()){
					List<String> addUsers=new ArrayList<String>();
					
					for(String u:userList){
						if(!users.contains(u)){
							addUsers.add(u);
						}
					}
					
					qyMessageService.updateChatRoom(project.getChatRoomId(), ownerId, name, addUsers, null);
				}
			}else{
				qyMessageService.createChatRoom(project.getChatRoomId(), name, ownerId, userList);
			}
			
			qyMessageService.sendGroupMsg(project.getChatRoomId(), ownerId, "【项目】："+project.getProjectName()+"\\n【当前讨论问题】："+bug.getBugContent()+"。\\n【讨论组创建人】："+user.getName());
		}
		
		return true;
	}
	
	public boolean deleteProject(String projectId){
		return this.projectService.deleteProject(projectId);
	}
	
	public void deleteProjectExt(String projectId){
		taskService.deleteTasks(projectId);
		bugService.deleteBugs(projectId);
		memberService.deleteMembers(projectId);
		
		projectService.removeProjectFromCached("", projectId);
	}
	
	public ProjectBugDO findBug(String userId,String projectId,String bugId){
		return this.projectService.findBugInCached(userId, projectId, bugId);
	}
	
	public List<ProjectBugDO> findBugs(String userId, String projectId) {
		return this.projectService.findBugsInCachedByProject(userId, projectId);
	}
	
	public List<MemberDO> findMembers(String userId, String projectId){
		return this.projectService.findMembersInCached(userId, projectId);
	}
	
	public TaskDO findTask(String userId, String projectId, String taskId){
		return this.projectService.findTaskInCached(userId, projectId, taskId);
	}
	
	public UserDO findUserByEmail(String email){
		return this.userService.findByEmail(email);
	}

	public String getGroup(String projectName) {
		String state=projectName.substring(0, 3);
		String stateName=groupDefs.get(state);
		if(stateName!=null){
			return stateName;
		}
		return "05其他类型";
	}
	
	public ProjectDO getProjectByID(String userId, String projectId){
		return this.projectService.getCachedProjectByID(userId, projectId);
	}
	
	public Iterator<Entry<String, String>> getTaskDefs() {
		return this.taskDefs.entrySet().iterator();
	}
	
	public List<TaskGroupDO> getTaskGroupRoot(String type){
		return this.projectService.getRoot(type);
	}
	
	public List<TaskDO> getTasksFromGroup(String taskGroupId, String userId, String projectId){
		return this.projectService.getTasksFromGroup(taskGroupId, userId, projectId);
	}
	
	public Object getUserName(String userId) {
		if(userId!=null&&userId.length()>0){
			UserDO user=projectService.findUser(userId);
			if(user!=null){
				return user.getName();
			}
		}
		return null;
	}
	
	private void iniGroupDefsData() {
		groupDefs=new HashMap<String, String>();
		groupDefs.put("Ing", "01接入中的项目");
		groupDefs.put("Pub", "02已上线的项目");
		groupDefs.put("Ref", "03已退单的项目");
		groupDefs.put("Stp", "04已停止的项目");
	}
	
	private void iniProjectTypes() {
		projectTypes=new ArrayList<String>();
		projectTypes.add("dev_type");//场景接入项目
		projectTypes.add("run_type");//运营项目
		projectTypes.add("fin_type");//资金接入项目
	}
	
	private void iniTaskDefsData() {
		taskDefs=new HashMap<String, String>();
		taskDefs.put("bf65707ba1c2427ab6ea0bdf5e49a510", "1.1 场景评估");
		taskDefs.put("00911e029cbc4cd9b2f37aefc526d597", "1.2 行业分析");
		taskDefs.put("c6ff516c1be54cc0a06aaffb3413e0b3", "2.1 目标客群确定");
		taskDefs.put("baac2b314fd845928fed79d3f13cedea", "2.2 产品原型确定");
		taskDefs.put("f850a7da85b9407fa25be0ccbc97d588", "2.3 合作模式及业务流程产出");
		taskDefs.put("23de72cc2aee4e8b9f10f1173c3f3abe", "2.4 行业背景描述");
		taskDefs.put("7765693e632b4450bf59093ad1863925", "2.5 产品规模收益预测");
		taskDefs.put("a0524de1ed5c4d91b1ff80571fa7b140", "2.6 风控要点确定");
		taskDefs.put("2bcd69aaa7844fb7a535875a5f97e4b8", "2.7 产品可行性描述");
		taskDefs.put("7db0b51870c8494dab19a7ae5874bf7d", "2.8 产品参数确定");
		taskDefs.put("2ac4685d58b147ffb64d617e230ef4bf", "3.1场景数据获取");
		taskDefs.put("1081f651366c4a568f804866f81eae94", "4.1 商务合同签署");
		taskDefs.put("2f72a834cd9c4d89ab802d006c6faed1", "5.1 数据分析报告产出");
		taskDefs.put("67bf28112d374b3ba90395bca4f2baa6", "5.2 场景数据宽表产出");
		taskDefs.put("d8b13bd186104f60aea1181baf951edc", "5.3 风险管理细则产出");
		taskDefs.put("07aef9b10fc74ff799b0d768c15f53b7", "5.4 业务投放规划产出");
		taskDefs.put("18fac3b44bf44b4f8e6bb26e655dd63d", "5.5 资金需求及保证金安排");
		taskDefs.put("ed548a7947e043aa8861558405f86c82", "5.6 首批投放名单产出");
		taskDefs.put("51a552b5545d4273b584929599a88d47", "6.1 项目内审");
		taskDefs.put("adc077e5e50640d2863f7f502be33df4", "6.2 资金立项");
		taskDefs.put("6bbba7dd42354cc5bba21dad1e1df269", "6.3 资金过会");
		taskDefs.put("d219f713965a406298071c694e2074d0", "6.4 资方协议签署");
		taskDefs.put("68caa347ff6b4cc4b9e74819844237e3", "6.5 政策更新");
		taskDefs.put("3a2866eccd09401e9af594c0a1862354", "7.1 BRD文档产出");
		taskDefs.put("3f40d9c789bb494791220ef57404a4a5", "7.2 系统开发排期确定");
		taskDefs.put("be773cd1871549749d9d5f90a28646ba", "7.3 系统开发");
		taskDefs.put("d6615b1be7ba487dbbb638695d03e6fe", "8.1 试运营操作手册产出");
		taskDefs.put("7e260e880cd9403b984165ae10d8b204", "8.2 试运营客户申请");
		taskDefs.put("07ba394fe0944703a19b96325aa0cf7e", "8.3 试运营客户支用");
	}
	
	public List<UserDO> listUsers(){
		return this.projectService.listUsers();
	}

	public List<UserDO> listUsers(String userId,String projectId){
		return this.projectService.listUsers(userId, projectId);
	}
	
	public void loadProject(ProjectDO project){
		project=projectService.getProjectExtendByID(project.getProjectId());
		
		project.setBugs(bugService.listByProjectId(project.getProjectId()));
		project.setTasks(taskService.findByProjectID(project.getProjectId()));
		project.setMembers(memberService.findByProjectId(project.getProjectId()));
		
		projectService.addNewProjectToCached("",project);
	}
	
	public List<ProjectDO> loadProjectExtendFromDB(List<Project> projects) {
		if(projects!=null&&this.projectService!=null){
			List<ProjectDO> list=new ArrayList<ProjectDO>();
			
			for(Project project:projects){
				ProjectDO p=this.projectService.getProjectExtendByID(project.getProjectID());
				if(p==null){
//					project.setDeployTime(p.getDeployTime());
//					project.setNowTaskID(p.getNowTaskID());
					p=new ProjectDO();
					p.setGmtCreate(new Date());
				}
				
				p.setProjectId(project.getProjectID());
				p.setProjectCode(project.getProjectName());
				p.setProjectGroup(getGroup(project.getProjectName()));
				
				List<TaskDO> tasks=this.projectService.findTasksInCached("", project.getProjectID());
				p.setTasks(tasks);
				List<MemberDO> mems=memberService.updateMember(project.getProjectID(), project.getMembers());
				p.setMembers(mems);
				List<ProjectBugDO> bugs=bugService.listByProjectId(project.getProjectID());
				p.setBugs(bugs);
				
				list.add(p);
				
				projectService.update(p);
			}
			
			return list;
		}
		return null;
	}

	public List<ProjectDO> loadProjectFromDB(String userId) {
		List<ProjectDO> list=projectService.getCachedProject(userId);
		if(list==null){
			list=projectService.loadProjectByUserID(userId);
			if(list!=null&&list.size()>0){
				for(ProjectDO project:list){
					project.setTasks(taskService.findByProjectID(project.getProjectId()));
					project.setMembers(memberService.findByProjectId(project.getProjectId()));
					project.setBugs(bugService.listByProjectId(project.getProjectId()));
				}
			}
			
			this.cachedUsers();
			
			projectService.cachedProject(userId, list);
			
			projectService.cachedTaskGroupTree(taskGroupService.loadTaskGroupTree(projectTypes));
		}
		return list;
	}
	
	public void removeBug(String userId,String projectId,String bugId){
		bugService.delete(bugId);
		projectService.removeBugFromCached(userId, projectId, bugId);
	}

	public void saveBug(JSONObject o, String projectId, String userId) {
		String bugId=null;
		boolean isNew=false;
		if(!o.isNull("id")){
			bugId=o.getString("id");
		}
		if(bugId!=null&&userId!=null&&projectId!=null){
			ProjectBugDO bug=this.findBug(userId, projectId, bugId); 
			
			if(bug==null){
				isNew=true;
				bug=new ProjectBugDO();
				bug.setBugId(bugId);
				bug.setProjectId(projectId);
				bug.setCreatorId(userId);
			}
			
			if(o.has("bug")){
				bug.setBugContent(o.getString("bug"));
			}
			
			if(o.has("owner")){
				bug.setOwnerId(o.getString("owner"));
			}
			if(o.has("dueDate")){
				bug.setDueTime(o.getString("dueDate").substring(0, 10));
			}
			if(o.has("priority")){
				bug.setPriority(o.getInt("priority"));
			}
			if(o.has("state")){
				bug.setState(o.getInt("state"));
			}
			if(o.has("taskId")){
				bug.setTaskId(o.getString("taskId"));
			}
			
			bug.setGmtCreate(new Date());
			bug.setGmtModify(new Date());
			
			bugService.saveBug(bug);
			
			if(isNew){
				bug=bugService.findById(bug.getBugId());
				projectService.addNewBugToCached(userId, bug);
			}
		}
	}
	
	public void saveBug(ProjectBugDO bug){
		if(bug!=null){
			bugService.saveBug(bug);
			bug=bugService.findById(bug.getBugId());
			
			projectService.addNewBugToCached(bug.getCreatorId(), bug);
		}
	}

	public void updateLogin(UserDO user, String token, String code) {
		loginService.update(user.getUserId(), code, token, new Date(), "");
	}

	public boolean updateProject(ProjectDO project){
		return this.projectService.update(project);
	}

	public void updateProject(String userId, JSONObject jjj) throws ParseException {
		String 	projectId=null,
				startTime=null,
				endTime=null;
		if(!jjj.isNull("id")){
			projectId=jjj.getString("id");
		}
		if(projectId!=null&&userId!=null){
			ProjectDO p=this.getProjectByID(userId, projectId);
			if(p!=null){
				if(!jjj.isNull("name")){
					p.setProjectName(jjj.getString("name"));
				}
				
				if(!jjj.isNull("deployTime")){
					p.setDeployTime(jjj.getString("deployTime"));
				}
				
				if(!jjj.isNull("nowTaskID")){
					p.setNowTaskID(jjj.getString("nowTaskID"));
				}
				
				if(!jjj.isNull("remark")){
					p.setRemark(jjj.getString("remark"));
				}
				
				if(!jjj.isNull("zijin")){
					p.setProjectZijin(jjj.getString("zijin"));
				}
				
				if(!jjj.isNull("group")){
					p.setProjectGroup(jjj.getString("group"));
				}
				
				if(!jjj.isNull("projectid")){
					p.setProjectCode(jjj.getString("projectid"));
				}
				
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				if(!jjj.isNull("startTime")){
					startTime=jjj.getString("startTime");
					p.setStartTime(sdf.parse(startTime.substring(0,10)));
				}
				
				if(!jjj.isNull("endTime")){
					endTime=jjj.getString("endTime");
					p.setEndTime(sdf.parse(endTime.substring(0,10)));
				}
				
				if(!jjj.isNull("finished")){
					p.setFinished(jjj.getBoolean("finished"));
				}
				
				if(!jjj.isNull("creator")){
					p.setProjectOwnerId(jjj.getString("creator"));
				}
		
				projectService.update(p);
			}
		}
		
	}
	
	public void updateTask(TaskDO task){
		this.taskService.updateTask(task);
		
		task=taskService.findById(task.getTaskId());
		
		this.projectService.addNewTaskToCached("", task);
	}
	
	public void updateUser(UserDO user) {
		userService.updateByDO(user);
	}

	public void removeMember(String userId, String projectId, String uId) {
		MemberDO mem=this.projectService.findMemberByUserId(userId, projectId, uId);
		if(mem!=null){
			memberService.deleteMember(mem.getMemberId());
			this.projectService.removeMemberFromProject(userId, projectId, mem.getMemberId());
		}
	}

	public void addMemberToProject(String userId, String projectId, String uId) {
		MemberDO mem=this.projectService.findMemberByUserId(userId, projectId, uId);
		if(mem==null){
			mem=new MemberDO();
			mem.setMemberId(UUID.randomUUID().toString().replaceAll("-", ""));
			mem.setProjectId(projectId);
			mem.setUserId(uId);
			
			memberService.updateMember(mem);
			
			mem.setUser(this.projectService.findUser(uId));
			
			this.projectService.addNewMemberToProject(userId, projectId, mem);
		}
	}

	public void removeTask(String userId, String projectId, String taskId) {
		TaskDO task=this.projectService.findTaskInCached(userId, projectId, taskId);
		if(task!=null){
			taskService.deleteTask(taskId);
			
			this.projectService.removeTaskFromCached(userId, projectId, taskId);
		}
	}

	public String getTaskGroupProgressPercentage(String projectId,String taskGroupId) {
		List<TaskDO> list=this.projectService.getTasksFromGroup(taskGroupId, "", projectId);
		double p=0;
		if(list!=null&&list.size()>0){
			for(TaskDO task:list){
				p+=task.getProgressPercentage();
			}
			p=p/list.size();
			
			return (p>=1?100:Utils.accuracyTxt(p*100, 2))+"%";
		}
		return "0%";
	}

	public List<TaskGroupDO> getTaskGroup(String projectType, String taskGroupId) {
		List<TaskGroupDO> roots=this.getTaskGroupRoot(projectType);
		if(roots!=null&&roots.size()>0){
			TaskGroupDO task=findTaskGroup(roots,taskGroupId);
			if(task!=null){
				return task.getChilden();
			}
		}
		return null;
	}

	private TaskGroupDO findTaskGroup(List<TaskGroupDO> roots,
			String taskGroupId) {
		if(roots!=null&&roots.size()>0){
			for(TaskGroupDO tg:roots){
				if(tg.getTaskGroupId().equals(taskGroupId)){
					return tg;
				}else{
					List<TaskGroupDO> children=tg.getChilden();
					TaskGroupDO tgg = findTaskGroup(children, taskGroupId);
					if(tgg!=null){
						return tgg;
					}
				}
			}
		}
		return null;
	}

	public void sendReport(String userId,String title, String report, String email,String projectType) {
		List<String> headers=createReportHeader(report,projectType);
		List<List<String>> data=createReportData(report,projectType);
		
		File file=null;
		try {
			file = File.createTempFile(title, ".xls");
			
			String tempFilePath=file.getAbsolutePath();
			
			if(headers!=null&&headers.size()>0&&data!=null&&data.size()>0){
				if(Utils.createReport(report, title,tempFilePath, headers, data)){
					sendEmail(email,title,tempFilePath);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(file!=null){
				file.delete();
			}
		}
	}

	private void sendEmail(String email, String title,String tempFilePath) {
		String[] filePaths={tempFilePath};
		UserDO user=userService.findByEmail(email);
		Map<String, Object> values=new HashMap<String, Object>();
		values.put("username", user.getName());
		
		mailService.sendMailByTempelete(email, title, "mail-tempelte.vm", values, filePaths);
	}

	private List<List<String>> createReportData(String report,String projectType) {
		List<ProjectDO> list=projectService.getCachedProject("");
		
		Collections.sort(list, new Comparator<ProjectDO>() {

			@Override
			public int compare(ProjectDO o1, ProjectDO o2) {
				String g1=getGroup(o1.getProjectCode());
				String g2=getGroup(o2.getProjectCode());
				return g1.compareTo(g2);
			}
		});
		
		List<List<String>> datas=new LinkedList<List<String>>();
		
		if(list!=null){
			for(ProjectDO p:list){
				if(p!=null&&p.getProjectType().equals(projectType)){
					List<String> d1=new LinkedList<String>();
					d1.add(p.getProjectName());
					d1.add(p.getProjectZijin());
					d1.add(p.getProjectCode());
					d1.add(this.getGroup(p.getProjectCode()));
	//				UserDO user=projectService.findUser(p.getProjectOwnerId());
					d1.add(p.getProjectOwnerId());
					d1.add(p.getDeployTime());
					d1.add("");
					d1.add(getBugsText(p.getProjectId()));
					
					List<TaskDO> ts = p.getTasks();
					if (ts != null && ts.size() > 0) {
						Iterator<Entry<String, String>> it = getTaskDefs();
						while (it.hasNext()) {
							Entry<String, String> en = it.next();
							String pp = getTaskGroupProgressPercentage(
											p.getProjectId(), en.getKey());
							d1.add(pp);
						}
					}
					
					datas.add(d1);
				}
			}
			
		}
		return datas;
	}
	
	public String getBugsArray(String projectId) {
		StringBuilder sb = new StringBuilder();
		List<ProjectBugDO> list = findBugs("", projectId);
		if (list != null) {
			sb.append("'");
			int i = 1;
			for (ProjectBugDO bug : list) {
				// if(i>0){
				// sb.append(",");
				// }
				// sb.append("{");
				// sb.append("id:'").append(bug.getBugId()).append("',");
				sb.append(i).append(".").append(bug.getBugContent())
						.append(",\\n");
				// sb.append("}");

				i++;
			}
			sb.append("'");

			return sb.toString();
		}
		return "''";
	}
	
	public String getBugsText(String projectId) {
		StringBuilder sb = new StringBuilder();
		List<ProjectBugDO> list = findBugs("", projectId);
		if (list != null) {
			int i = 1;
			for (ProjectBugDO bug : list) {
				sb.append(i).append(") ").append(bug.getBugContent())
						.append("。  ");
				i++;
			}

			return sb.toString();
		}
		return "";
	}

	private List<String> createReportHeader(String report,String projectType) {
		List<String> headers=new LinkedList<String>();
		headers.addAll(Arrays.asList("项目名称","资金方","项目编码","项目分组","负责人","上线时间","当前阶段","项目问题"));
		
		List<TaskGroupDO> roots=projectService.getRoot(projectType);
		if(roots!=null){
			for(TaskGroupDO tg:roots){
				List<TaskGroupDO> list=tg.getChilden();
				if(list!=null){
					for(TaskGroupDO ch:list){
						headers.add(ch.getTaskGroupName());
					}
				}
			}
		}
		return headers;
	}

	public void flush() {
		projectService.removeCachedProject();
	}
	
}
