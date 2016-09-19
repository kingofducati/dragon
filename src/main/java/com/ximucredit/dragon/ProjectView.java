package com.ximucredit.dragon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.qq.weixin.mp.aes.WechatUser;
import com.ximucredit.dragon.DO.MemberDO;
import com.ximucredit.dragon.DO.ProjectBugDO;
import com.ximucredit.dragon.DO.ProjectDO;
import com.ximucredit.dragon.DO.TaskDO;
import com.ximucredit.dragon.DO.TaskGroupDO;
import com.ximucredit.dragon.DO.UserDO;
import com.ximucredit.dragon.service.imp.QYLoginServiceImp;
import com.ximucredit.dragon.util.Utils;
import com.ximucredit.teambition.Client;
import com.ximucredit.teambition.Util;

/**
 * Servlet implementation class ProjectView
 */
public class ProjectView extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8559238120239719838L;
	private Client client;
	private Logger log = Logger.getLogger(ProjectView.class.getName());

	private ProjectStoreManager storeManager;
	private Properties pop;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProjectView() {//
		super();

		client = new Client("9d009ff0-39b0-11e6-8ed7-e3410151ca80",
				"a0c4f1f1-0cf7-4483-b230-7a5499247eb5",
				"http://pm.ximucredit.com:8080/dragon/view");
		// client=new
		// Client("9d009ff0-39b0-11e6-8ed7-e3410151ca80","a0c4f1f1-0cf7-4483-b230-7a5499247eb5",
		// "http://localhost:8080/dragon/view");
	}

	private void cloneProject(HttpServletRequest request, String userId) {
		if (request.getParameter("parentProjectId") != null
				&& request.getParameter("parentProjectId").length() > 0) {
			String projectId = request.getParameter("parentProjectId");
			ProjectDO srcProject = storeManager.getProjectByID(userId,
					projectId);

			if (srcProject != null) {
				ProjectDO newProject = createProjectFromRequest(request);
				if (storeManager.updateProject(newProject)) {
					storeManager.cloneProject(userId, srcProject, newProject);

					storeManager.addNewProject(userId, newProject);
				}
			}
		}
	}

	private ProjectBugDO createBugFormRequest(HttpServletRequest request,
			String projectId) {
		ProjectBugDO bug = new ProjectBugDO();
		if (request.getParameter("bugId") != null
				&& request.getParameter("bugId").length() > 0) {
			bug.setBugId(request.getParameter("bugId"));
		}
		if (request.getParameter("bug") != null
				&& request.getParameter("bug").length() > 0) {
			bug.setBugContent(request.getParameter("bug"));
		}
		if (request.getParameter("creator") != null
				&& request.getParameter("creator").length() > 0) {
			bug.setCreatorId(request.getParameter("creator"));
		}
		if (request.getParameter("dueDate") != null
				&& request.getParameter("dueDate").length() > 0) {
			bug.setDueTime(request.getParameter("dueDate"));
		}
		if (request.getParameter("owner") != null
				&& request.getParameter("owner").length() > 0) {
			bug.setOwnerId(request.getParameter("owner"));
		}
		if (request.getParameter("priority") != null
				&& request.getParameter("priority").length() > 0) {
			bug.setPriority(Integer.parseInt(request.getParameter("priority")));
		}
		if (request.getParameter("projectId") != null
				&& request.getParameter("projectId").length() > 0) {
			bug.setProjectId(request.getParameter("projectId"));
		} else {
			bug.setProjectId(projectId);
		}
		if (request.getParameter("remark") != null
				&& request.getParameter("remark").length() > 0) {
			bug.setRemark(request.getParameter("remark"));
		}
		if (request.getParameter("state") != null
				&& request.getParameter("state").length() > 0) {
			bug.setState(Integer.parseInt(request.getParameter("state")));
		}

		bug.setGmtCreate(new Date());
		bug.setGmtModify(new Date());
		return bug;
	}

	private JSONArray createList(String userId, ProjectDO p,
			List<TaskGroupDO> roots,String taskGroupId) {
		JSONArray ja = new JSONArray();
		if(roots!=null&&roots.size()>0){
			for (TaskGroupDO group : roots) {
				JSONObject jo = new JSONObject();
				jo.put("id", group.getTaskGroupId());
				jo.put("name", group.getTaskGroupName());
				jo.put("group", true);
				jo.put("leaf", false);
	
				ja.put(jo);
			}
		}

		if(taskGroupId!=null){
			proccessLeaf(ja, userId, p, taskGroupId);
		}
		
		return ja;
	}

	private ProjectDO createProjectFromRequest(HttpServletRequest request) {
		ProjectDO project = new ProjectDO();

		if (request.getParameter("projectId") != null
				&& request.getParameter("projectId").length() > 0) {
			project.setProjectId(request.getParameter("projectId"));
		} else {
			project.setProjectId(UUID.randomUUID().toString()
					.replaceAll("-", ""));
			project.setGmtCreate(new Date());
		}
		if (request.getParameter("projectName") != null
				&& request.getParameter("projectName").length() > 0) {
			project.setProjectName(request.getParameter("projectName"));
		}

		if (request.getParameter("projectCode") != null
				&& request.getParameter("projectCode").length() > 0) {
			project.setProjectCode(request.getParameter("projectCode"));
		}

		if (request.getParameter("projectType") != null
				&& request.getParameter("projectType").length() > 0) {
			project.setProjectType(request.getParameter("projectType"));
		}

		if (request.getParameter("projectGroup") != null
				&& request.getParameter("projectGroup").length() > 0) {
			project.setProjectGroup(request.getParameter("projectGroup"));
		}

		if (request.getParameter("projectOwnerId") != null
				&& request.getParameter("projectOwnerId").length() > 0) {
			project.setProjectOwnerId(request.getParameter("projectOwnerId"));
		}

		if (request.getParameter("projectZijin") != null
				&& request.getParameter("projectZijin").length() > 0) {
			project.setProjectZijin(request.getParameter("projectZijin"));
		}else{
			project.setProjectZijin("");
		}

		if (request.getParameter("deployTime") != null
				&& request.getParameter("deployTime").length() >= 10) {
			project.setDeployTime(request.getParameter("deployTime").substring(
					0, 10));
		}else{
			project.setDeployTime("");
		}

		if (request.getParameter("endTime") != null
				&& request.getParameter("endTime").length() >= 10) {
			project.setEndTime(Utils.paeserDate(request.getParameter("endTime")
					.substring(0, 10)));
		}

		if (request.getParameter("startTime") != null
				&& request.getParameter("startTime").length() >= 10) {
			project.setStartTime(Utils.paeserDate(request.getParameter(
					"startTime").substring(0, 10)));
		}

		if (request.getParameter("finished") != null
				&& request.getParameter("finished").length() > 0) {
			project.setFinished(Boolean.getBoolean(request
					.getParameter("finished")));
		}

		if (request.getParameter("nowTaskId") != null
				&& request.getParameter("nowTaskId").length() > 0) {
			project.setNowTaskID(request.getParameter("nowTaskId"));
		}else{
			project.setNowTaskID("");
		}

		if (request.getParameter("remark") != null
				&& request.getParameter("remark").length() > 0) {
			project.setRemark(request.getParameter("remark"));
		}else{
			project.setRemark("");
		}

		project.setGmtModify(new Date());

		return project;
	}

	private TaskDO createTaskFromRequest(HttpServletRequest request) {
		TaskDO task = new TaskDO();

		if (request.getParameter("taskId") != null
				&& request.getParameter("taskId").length() > 0) {
			task.setTaskId(request.getParameter("taskId"));
		} else {
			task.setTaskId(UUID.randomUUID().toString().replaceAll("-", ""));
			task.setGmtCreate(new Date());
		}

		if (request.getParameter("projectId") != null
				&& request.getParameter("projectId").length() > 0) {
			task.setProjectId(request.getParameter("projectId"));
		}

		if (request.getParameter("creatorId") != null
				&& request.getParameter("creatorId").length() > 0) {
			task.setCreatorId(request.getParameter("creatorId"));
		}

		if (request.getParameter("accomplished") != null
				&& request.getParameter("accomplished").length() > 0) {
			task.setAccomplished(Utils.paeserDate(request
					.getParameter("accomplished")));
		}

		if (request.getParameter("dueDate") != null
				&& request.getParameter("dueDate").length() > 0) {
			task.setDueDate(Utils.paeserDate(request.getParameter("dueDate")));
		}

		if (request.getParameter("startDate") != null
				&& request.getParameter("startDate").length() > 0) {
			task.setStartDate(Utils.paeserDate(request
					.getParameter("startDate")));
		}

		if (request.getParameter("endDate") != null
				&& request.getParameter("endDate").length() > 0) {
			task.setEndDate(Utils.paeserDate(request.getParameter("endDate")));
		}

		if (request.getParameter("content") != null
				&& request.getParameter("content").length() > 0) {
			task.setContent(request.getParameter("content"));
		}

		if (request.getParameter("executorId") != null
				&& request.getParameter("executorId").length() > 0) {
			task.setExecutorId(request.getParameter("executorId"));
		}

		if (request.getParameter("note") != null
				&& request.getParameter("note").length() > 0) {
			task.setNote(request.getParameter("note"));
		}

		if (request.getParameter("taskGroupId") != null
				&& request.getParameter("taskGroupId").length() > 0) {
			task.setTaskGroupId(request.getParameter("taskGroupId"));
		}

		if (request.getParameter("priority") != null
				&& request.getParameter("priority").length() > 0) {
			task.setPriority(Integer.parseInt(request.getParameter("priority")));
		}
		
		if (request.getParameter("done") != null
				&& request.getParameter("done").length() > 0) {
			task.setDone(Boolean.parseBoolean(request.getParameter("done")));
		}

		task.setGmtModify(new Date());

		return task;
	}

	private void doAddMember(HttpServletRequest request,
			HttpServletResponse response) {
		PrintWriter out = null;
		StringBuilder sb = new StringBuilder();

		try {
			String userId = (String) request.getSession().getAttribute("me");
			if (userId != null) {
				String uId = request.getParameter("userId");
				String projectId = request.getParameter("projectId");
				if (uId != null && projectId != null) {
					storeManager.addMemberToProject(userId, projectId, uId);
				}
			}
			sb.append("{success:true}");
			out = response.getWriter();
			out.print(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null)
				out.close();
		}
	}

	private void doCloneProject(HttpServletRequest request,
			HttpServletResponse response) {
		PrintWriter out = null;
		try {
			out = response.getWriter();
			StringBuilder sb = new StringBuilder();

			String userId = (String) request.getSession().getAttribute("me");
			if (userId != null) {
				cloneProject(request, userId);

				sb = new StringBuilder("{success:true}");
			}

			out.print(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}

	private void doCreateChat(HttpServletRequest request,
			HttpServletResponse response) {
		PrintWriter out = null;
		StringBuilder sb = new StringBuilder();

		try {
			out = response.getWriter();
			String userId = (String) request.getSession().getAttribute("me");
			String bugId = request.getParameter("id");
			String projectId = request.getParameter("projectId");
			if (bugId != null && userId != null && projectId != null) {
				ProjectBugDO bug = storeManager.findBug(userId, projectId,
						bugId);
				ProjectDO project=storeManager.getProjectByID(userId, projectId);

				if (storeManager.createBugChatRoom(project,bug)) {
					sb.append("{success:true,msg:'创建讨论组成功'}");
				} else {
					sb.append("{success:false}");
				}
			} else {
				sb.append("{success:false}");
			}
			out.print(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null)
				out.close();
		}
	}

	private void doDeleteProject(HttpServletRequest request,
			HttpServletResponse response) {
		PrintWriter out = null;
		try {
			out = response.getWriter();
			StringBuilder sb = new StringBuilder();

			String userId = (String) request.getSession().getAttribute("me");
			String projectId = request.getParameter("projectId");
			if (userId != null && projectId != null) {
				if (storeManager.deleteProject(projectId)) {
					storeManager.deleteProjectExt(projectId);
				}

				sb.append("{success:true}");
			}

			out.print(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}

	private void doExportExcel(HttpServletRequest request,
			HttpServletResponse response) {
		PrintWriter out = null;
		try {
			out = response.getWriter();
			StringBuilder sb = new StringBuilder();

			String userId = (String) request.getSession().getAttribute("me");
			String report = request.getParameter("report");
			String email = request.getParameter("email");
			String projectType = request.getParameter("projectType");
			if (userId != null && report != null && email!=null&&projectType!=null) {
				storeManager.sendReport(userId,"项目问题跟进表",report,email,projectType);

				sb.append("{success:true,msg:'导出并发送成功！'}");
			}

			out.print(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	private void doIndex(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			String code = request.getParameter("code");
			if (code != null && code.length() > 0) {
				log.log(Level.INFO, "Code:" + code);
				request.getSession().setAttribute("code", code);

				//client.loadToken(code);

				//String token = client.getAccess_token();
				//log.log(Level.INFO, "Token:" + token);

				//String userRes = client.loadMe(token);

				//UserDO user = loadUser(userRes);

//				if (token != null && token.length() > 0) {
//					request.getSession().setAttribute("token", token);
//					request.getSession().setAttribute("me", user.getUserId());
//				}
				
				UserDO user=getWechatUser(code);
				if(user!=null){
					request.getSession().setAttribute("me", user.getUserId());
				}else{
					response.sendRedirect("login.jsp");
					return;
				}
			} else if (request.getSession().getAttribute("code") == null) {
//				client.setAccess_token("1Lo6iKSEZphKjkvv8zK1uaWldKw=NXQ7usJY70b066345a601bfbd3e78a8959a368b89fdd68609ef51ec020ff186c0836df6065268f321675c0cf215d94f2fd99d6b67812a2d58d8d8d487ecbd61c8bb42ec30d29a06ff8d22eb83c56f752a7d29671a1a6cf1296372197eb84bd77f1a96529c85d13735b520eb183cea587fffc50c1");
//				String token = client.getAccess_token();
//				if (token != null && token.length() > 0) {
//					request.getSession().setAttribute("token", token);
//					request.getSession().setAttribute("me",
//							"5760c13e7ce00e9d6bb8ea1e");
//				}
				 request.getRequestDispatcher("error.html").forward(request,
				 response);
			}
			response.sendRedirect("project.jsp");
		} catch (Exception e) {
			e.printStackTrace();
			request.getRequestDispatcher("error.html").forward(request,
					response);
		}
	}

	private UserDO getWechatUser(String code) {
		
		QYLoginServiceImp loginService=(QYLoginServiceImp)storeManager.getLoginService(code);
		
		if(loginService==null) {
			loginService=new QYLoginServiceImp(code);
			
			storeManager.registerLoginService(code, loginService);
		}
		
		String token=loginService.getToken();
		
		UserDO user=loginService.getQYLoginUser();
		
		if(user!=null){
			storeManager.updateLogin(user, token, code);
		}
		
		return user;
	}

	private void doList(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			PrintWriter out = null;
		try {
			String userId = (String) request.getSession()
					.getAttribute("me");
			List<ProjectDO> plist = null;
			// 刷新项目列表
			if (userId != null) {
				try {
					// loadCOSProjectData();
					plist = storeManager.loadProjectFromDB(userId);
				} catch (Exception e) {
					e.printStackTrace();
					request.getRequestDispatcher("error.html").forward(
							request, response);
					return;
				}
			} else {
				request.getRequestDispatcher("error.html").forward(request,
						response);
				return;
			}

			StringBuilder sb = new StringBuilder("{projects:[");
			int i = 0;
			for (ProjectDO p : plist) {
				if (i != 0) {
					sb.append(",");
				}
				sb.append("{");
				sb.append("projectid:\"").append(p.getProjectCode())
						.append("\",");
				sb.append("id:\"").append(p.getProjectId()).append("\",");
				sb.append("name:\"").append(p.getProjectName())
						.append("\",");
				sb.append("group:\"").append(storeManager.getGroup(p.getProjectCode()))
						.append("\",");
				sb.append("zijin:\"")
						.append(p.getProjectZijin() == null ? "" : p
								.getProjectZijin()).append("\",");
				sb.append("projectType:\"").append(p.getProjectType())
						.append("\",");

				List<TaskDO> ts = p.getTasks();
				if (ts != null && ts.size() > 0) {
					Iterator<Entry<String, String>> it = storeManager
							.getTaskDefs();
					while (it.hasNext()) {
						Entry<String, String> en = it.next();
						String pp = storeManager
								.getTaskGroupProgressPercentage(
										p.getProjectId(), en.getKey());
						sb.append("'"+en.getKey() + "':'").append(pp)
								.append("',");
					}
				}
				sb.append("deployTime:\"")
						.append(p.getDeployTime() == null || p.getDeployTime().length()< 10 ? "" : p
								.getDeployTime().substring(0, 10))
						.append("\",");
				sb.append("nowTaskID:\"").append(p.getNowTaskID())
						.append("\",");
				sb.append("nowTask:\"")
						.append(p.getNowTaskID() == null ? "" : p
								.getTaskByTaskID(p.getNowTaskID()))
						.append("\",");
				sb.append("bugs:").append(storeManager.getBugsArray(p.getProjectId()))
						.append(",");
				sb.append("creator:\"").append(p.getProjectOwnerId())
						.append("\"}");
				i++;

			}

			sb.append("]}");

			out = response.getWriter();
			out.print(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}

	private void doListBugs(HttpServletRequest request,
			HttpServletResponse response) {
		PrintWriter out = null;
		StringBuilder sb = new StringBuilder();

		try {
			String userId = (String) request.getSession().getAttribute("me");
			String projectId = request.getParameter("projectId");
			sb.append("{success:true,bugs:");
			if (userId != null) {
				if (projectId != null && projectId.length() > 0) {
					List<ProjectBugDO> list = storeManager.findBugs(userId,
							projectId);
					if (list != null && list.size() > 0) {
						JSONArray ja = new JSONArray();
						for (ProjectBugDO bugDO : list) {
							JSONObject jo = new JSONObject();
							jo.put("bugId", bugDO.getBugId());
							jo.put("projectId", bugDO.getProjectId());
							jo.put("taskId", bugDO.getTaskId());
							jo.put("bug", bugDO.getBugContent());
							jo.put("owner", bugDO.getOwnerId());
							jo.put("ownerName", storeManager.getUserName(bugDO
									.getOwnerId()));
							jo.put("creator", bugDO.getCreatorId());
							jo.put("creatorName", storeManager
									.getUserName(bugDO.getCreatorId()));
							jo.put("dueDate",
									bugDO.getDueTime() != null
											&& bugDO.getDueTime().length() >= 10 ? bugDO
											.getDueTime().substring(0, 10) : "");
							jo.put("priority", bugDO.getPriority());
							jo.put("state", bugDO.getState());
							double d = bugDO.getProgressPercentage();
							jo.put("proccess",
									""
											+ (d > 1 ? 100 : Utils.accuracyTxt(
													d * 100, 2)) + "%");
							jo.put("color", getColor(d));
							jo.put("remark", bugDO.getRemark());

							ja.put(jo);
						}

						sb.append(ja.toString());
					}else{
						sb.append("[]");
					}
				}
			}
			sb.append("}");

			out = response.getWriter();
			out.print(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null)
				out.close();
		}
	}

	private void doListMember(HttpServletRequest request,
			HttpServletResponse response) {
		PrintWriter out = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append("{success:true,members:[");
			String projectId = request.getParameter("projectId");
			String userId = (String) request.getSession().getAttribute("me");
			if (userId != null) {
				int i = 0;
				if (projectId != null && projectId.length() > 0) {
					List<MemberDO> mems = storeManager.findMembers(userId,
							projectId);
					if (mems != null && mems.size() > 0) {
						for (MemberDO mem : mems) {
							if (i > 0) {
								sb.append(",");
							}
							UserDO user = mem.getUser();
							if (user != null) {
								sb.append("{");
								sb.append("user_id:'").append(user.getUserId())
										.append("',");
								sb.append("user_name:'").append(user.getName())
										.append("'}");
							}
							i++;
						}
					}
				}
			}
			sb.append("]}");
			out = response.getWriter();
			out.print(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null)
				out.close();
		}
	}

	private void doListTasks(HttpServletRequest request,
			HttpServletResponse response) {
		PrintWriter out = null;
		try {
			out = response.getWriter();
			StringBuilder sb = new StringBuilder();

			JSONArray ja = new JSONArray();
			String userId = (String) request.getSession().getAttribute("me");
			if (userId != null) {
				String projectId = request.getParameter("projectId");
				String node=request.getParameter("node");
				if (projectId != null && projectId.length() > 0&&node!=null&&node.length()>0) {
					ProjectDO p = storeManager
							.getProjectByID(userId, projectId);
					
					if(node.contains("rootgroup")){
						List<TaskGroupDO> roots = storeManager.getTaskGroupRoot(p
								.getProjectType());
						ja = createList(userId, p, roots, null);
						
						sb.append("{success:true,children:");
						sb.append(ja.toString());
						sb.append(",name:'任务分组',group:true,leaf:false,id:'rootgroup'}");
					}else{
						List<TaskGroupDO> list=storeManager.getTaskGroup(p.getProjectType(),node);
						ja = createList(userId, p, list, node);
						
						sb.append("{success:true,children:");
						sb.append(ja.toString());
						sb.append("}");
					}
					
				}
			}

			out.print(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}

	private void doListUsers(HttpServletRequest request,
			HttpServletResponse response) {
		PrintWriter out = null;
		try {
			out = response.getWriter();
			StringBuilder sb = new StringBuilder();

			JSONArray ja = new JSONArray();

			String userId = (String) request.getSession().getAttribute("me");
			if (userId != null) {
				String all = request.getParameter("all");
				if (all != null) {
					List<UserDO> users = storeManager.listUsers();
					if (users == null) {
						users = storeManager.cachedUsers();
					}

					for (UserDO user : users) {
						JSONObject jo = new JSONObject();
						jo.put("userId", user.getUserId());
						jo.put("email", user.getEmail());
						jo.put("name", user.getName());
						jo.put("phone", user.getPhone());
						jo.put("title", user.getTitle());

						ja.put(jo);
					}
				} else {
					String projectId = request.getParameter("projectId");
					List<UserDO> users = storeManager.listUsers(userId,
							projectId);

					for (UserDO user : users) {
						JSONObject jo = new JSONObject();
						jo.put("userId", user.getUserId());
						jo.put("email", user.getEmail());
						jo.put("name", user.getName());
						jo.put("phone", user.getPhone());
						jo.put("title", user.getTitle());

						ja.put(jo);
					}
				}
			}

			sb.append("{success:true,users:");
			sb.append(ja.toString());
			sb.append("}");

			out.print(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}

	private void doLoadProject(HttpServletRequest request,
			HttpServletResponse response) {
		PrintWriter out = null;
		try {
			out = response.getWriter();
			StringBuilder sb = new StringBuilder();

			String userId = (String) request.getSession().getAttribute("me");
			String projectId = request.getParameter("projectId");
			if (userId != null && projectId != null) {
				sb.append("{success:true,project:");
				ProjectDO project = storeManager.getProjectByID(userId,
						projectId);
				if (project != null) {
					JSONObject o = new JSONObject();
					o.put("projectId", project.getProjectId());
					o.put("projectName", project.getProjectName());
					o.put("projectCode", project.getProjectCode());
					o.put("projectGroup", storeManager.getGroup(project.getProjectCode()));
					o.put("projectOwnerId", project.getProjectOwnerId());
					o.put("projectZijin", project.getProjectZijin());
					o.put("projectType", project.getProjectType());
					o.put("deployTime",
							project.getDeployTime() != null && project.getDeployTime().length() >=10 ? project
									.getDeployTime().substring(0, 10) : "");
					o.put("endTime",
							project.getEndTime() != null ? Utils
									.formatDate(project.getEndTime()) : null);
					o.put("nowTaskId", project.getNowTaskID());
					o.put("remark", project.getRemark());
					o.put("startTime",
							project.getStartTime() != null ? Utils
									.formatDate(project.getStartTime()) : null);

					sb.append(o.toString());
				}
				sb.append("}");
			}

			out.print(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=utf-8");

		String command = request.getParameter("command");
		if (command != null && command.length() > 0) {
			if ("authorize".equals(command)) {
				//response.sendRedirect(client.getAuthorizeURI());
				String userId=(String)request.getSession().getAttribute("me");
				if(userId==null){
					response.sendRedirect("login.jsp");
				}else{
					response.sendRedirect("project.jsp");
				}
			} else if("login".equals(command)){
				doLogin(request, response);
			} else if("confirm_email".equals(command)){
				doBundle(request, response);
			}else if ("exportexcel".equals(command)) {
				doExportExcel(request, response);
			} else if ("save".equals(command)) {
				doSave(request, response);
			} else if ("list".equals(command)) {
				doList(request, response);
			} else if ("teambition".equals(command)) {
				doTeambition(request, response);
			} else if ("listbug".equals(command)) {
				doListBugs(request, response);
			} else if ("saveBug".equals(command)) {
				doSaveBug(request, response);
			} else if ("members".equals(command)) {
				doListMember(request, response);
			} else if ("removeBug".equals(command)) {
				doRemoveBug(request, response);
			} else if ("createChat".equals(command)) {
				doCreateChat(request, response);
			} else if ("tdprojectlist".equals(command)) {
				doTDProjectList(request, response);
			} else if ("queryProject".equals(command)) {
				doLoadProject(request, response);
			} else if ("saveProject".equals(command)) {
				doSaveProject(request, response);
			} else if ("cloneProject".equals(command)) {
				doCloneProject(request, response);
			} else if ("deleteProject".equals(command)) {
				doDeleteProject(request, response);
			} else if ("listUsers".equals(command)) {
				doListUsers(request, response);
			} else if ("addMember".equals(command)) {
				doAddMember(request, response);
			} else if ("removeMember".equals(command)) {
				doRemoveMember(request, response);
			} else if ("listTasks".equals(command)) {
				doListTasks(request, response);
			} else if ("queryTask".equals(command)) {
				doQueryTask(request, response);
			} else if ("saveTask".equals(command)) {
				doSaveTask(request, response);
			} else if ("removeTask".equals(command)) {
				doRemoveTask(request, response);
			} else if ("saveUser".equals(command)) {

			} else if ("removeUser".equals(command)) {

			} else if ("flush".equals(command)){
				doFlushCached(request, response);
			}
		} else {
			doIndex(request, response);
		}
	}

	private void doBundle(HttpServletRequest request, HttpServletResponse response) {
		try {
			String code=request.getParameter("code");
			String unionid=request.getParameter("pc");
			String email=request.getParameter("user");
			QYLoginServiceImp loginService=(QYLoginServiceImp)storeManager.getLoginService(code);
			if(loginService!=null&&loginService.getUnionId().equals(unionid)){
				UserDO user=storeManager.findUserByEmail(email);
				user.setWeixinId(unionid);
				
				storeManager.updateUser(user);
				
				request.getSession().setAttribute("me", user.getUserId());
				
				response.sendRedirect("project.jsp");
			}else{
				request.getRequestDispatcher("error.html").forward(request,
							response);
			}
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		}
		
	}

	private void doLogin(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String email=request.getParameter("username");
		if(email!=null){
			UserDO user = storeManager.findUserByEmail(email);
			if(user!=null){
				String code=(String)request.getSession().getAttribute("code");
				QYLoginServiceImp loginService=(QYLoginServiceImp)storeManager.getLoginService(code);
				storeManager.sendBundleEmail(user, code, loginService.getUnionId());
				
				response.sendRedirect("bundle.jsp");
			}
		}
	}

	private void doFlushCached(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		storeManager.flush();
		doList(request, response);
	}

	private void doQueryTask(HttpServletRequest request,
			HttpServletResponse response) {
		PrintWriter out = null;
		try {
			out = response.getWriter();
			StringBuilder sb = new StringBuilder();

			String userId = (String) request.getSession().getAttribute("me");
			if (userId != null) {
				String projectId = request.getParameter("projectId");
				String taskId = request.getParameter("taskId");
				if (projectId != null && taskId != null) {
					TaskDO task = storeManager.findTask(userId, projectId,
							taskId);
					if (task != null) {
						sb.append("{success:true,task:");
						JSONObject jo = new JSONObject();
						jo.put("priority", task.getPriority());
						jo.put("accomplished", task.getAccomplished());
						jo.put("content", task.getContent());
						jo.put("creatorId", task.getCreatorId());
						jo.put("dueDate", Utils.formatDate(task.getDueDate()));
						jo.put("startDate", Utils.formatDate(task.getStartDate()));
						jo.put("endDate", Utils.formatDate(task.getEndDate()));
						jo.put("executorId", task.getExecutorId());
						jo.put("note", task.getNote());
						jo.put("projectId", task.getProjectId());
						jo.put("taskGroupId", task.getTaskGroupId());
						jo.put("taskId", task.getTaskId());
						jo.put("done", task.isDone());

						sb.append(jo.toString());
						sb.append("}");
					}
				}
			}
			out.print(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}

	private void doRemoveBug(HttpServletRequest request,
			HttpServletResponse response) {
		PrintWriter out = null;
		StringBuilder sb = new StringBuilder();

		try {
			String userId = (String) request.getSession().getAttribute("me");
			if (userId != null) {
				String bugId = request.getParameter("bugId");
				String projectId = request.getParameter("projectId");
				if (bugId != null && projectId != null) {
					storeManager.removeBug(userId, projectId, bugId);
				}
			}
			sb.append("{success:true}");
			out = response.getWriter();
			out.print(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null)
				out.close();
		}
	}

	private void doRemoveMember(HttpServletRequest request,
			HttpServletResponse response) {
		PrintWriter out = null;
		StringBuilder sb = new StringBuilder();

		try {
			String userId = (String) request.getSession().getAttribute("me");
			if (userId != null) {
				String uId = request.getParameter("userId");
				String projectId = request.getParameter("projectId");
				if (uId != null && projectId != null) {
					storeManager.removeMember(userId, projectId, uId);
				}
			}
			sb.append("{success:true}");
			out = response.getWriter();
			out.print(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null)
				out.close();
		}
	}

	private void doRemoveTask(HttpServletRequest request,
			HttpServletResponse response) {
		PrintWriter out = null;
		StringBuilder sb = new StringBuilder();

		try {
			String userId = (String) request.getSession().getAttribute("me");
			if (userId != null) {
				String taskId = request.getParameter("taskId");
				String projectId = request.getParameter("projectId");
				if (taskId != null && projectId != null) {
					storeManager.removeTask(userId, projectId, taskId);
				}
			}
			sb.append("{success:true}");
			out = response.getWriter();
			out.print(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null)
				out.close();
		}
	}

	private void doSaveBug(HttpServletRequest request,
			HttpServletResponse response) {
		PrintWriter out = null;
		StringBuilder sb = new StringBuilder();

		try {
			String userId = (String) request.getSession().getAttribute("me");
			if (userId != null) {
				String projectId = request.getParameter("projectId");
				// String taskId=request.getParameter("taskId");

				BufferedReader read = new BufferedReader(new InputStreamReader(
						request.getInputStream(), "UTF-8"));
				String s = null;
				while ((s = read.readLine()) != null) {
					sb.append(s);
				}
				read.close();
				String ss = sb.toString();
				if (ss.startsWith("{")) {
					JSONObject o = new JSONObject(ss);
					storeManager.saveBug(o, projectId, userId);
				} else if (ss.startsWith("[")) {
					JSONArray os = new JSONArray(ss);
					for (int i = 0; i < os.length(); i++) {
						JSONObject o = os.getJSONObject(i);
						storeManager.saveBug(o, projectId, userId);
					}
				} else {
					ProjectBugDO bug = createBugFormRequest(request, projectId);
					storeManager.saveBug(bug);
				}

				sb = new StringBuilder();
				sb.append("{success:true}");
			}
			out = response.getWriter();
			out.print(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null)
				out.close();
		}
	}

	private void doSave(HttpServletRequest request, HttpServletResponse response) {
		PrintWriter out = null;
		try {
			String userId = (String) request.getSession().getAttribute("me");
			if (userId != null) {
				BufferedReader read = new BufferedReader(new InputStreamReader(
						request.getInputStream(), "UTF-8"));
				StringBuilder sb = new StringBuilder();
				String s = null;
				while ((s = read.readLine()) != null) {
					sb.append(s);
				}
				read.close();
				String ss = sb.toString();
				if (ss.startsWith("{")) {
					JSONObject jjj = new JSONObject(ss);

					storeManager.updateProject(userId, jjj);
				} else {
					JSONArray ar = new JSONArray(sb.toString());
					if (ar.length() > 0) {
						for (int i = 0; i < ar.length(); i++) {
							JSONObject jo = ar.getJSONObject(i);

							storeManager.updateProject(userId, jo);
						}
					}
				}
			}
			out = response.getWriter();
			out.print("{success:true}");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}

	private void doSaveProject(HttpServletRequest request,
			HttpServletResponse response) {
		PrintWriter out = null;
		try {
			out = response.getWriter();
			StringBuilder sb = new StringBuilder();

			String userId = (String) request.getSession().getAttribute("me");
			if (userId != null) {
				saveProject(request, userId);

				sb = new StringBuilder("{success:true}");
			}

			out.print(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}

	private void doSaveTask(HttpServletRequest request,
			HttpServletResponse response) {
		PrintWriter out = null;
		try {
			out = response.getWriter();
			StringBuilder sb = new StringBuilder();

			String userId = (String) request.getSession().getAttribute("me");
			if (userId != null) {
				saveTask(request, userId);

				sb = new StringBuilder("{success:true}");
			}

			out.print(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}

	private void doTDProjectList(HttpServletRequest request,
			HttpServletResponse response) {
		PrintWriter out = null;
		try {
			out = response.getWriter();
			StringBuilder sb = new StringBuilder();

			List<ProjectDO> ps = null;
			// 刷新项目列表
			try {
//				 WechatUser user=(WechatUser)request.getSession().getAttribute("wcuser");
				WechatUser user = new WechatUser();
				user.setUserId("rong.chi@ximucredit.com");

				if (user != null) {
					UserDO u = storeManager.findUserByEmail(user.getUserId());
					if (u != null) {
						request.getSession().setAttribute("me", u.getUserId());
						ps = storeManager.loadProjectFromDB(u.getUserId());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				request.getRequestDispatcher("error.html").forward(request,
						response);
			}

			if (ps != null) {
				JSONArray ja = new JSONArray();

				for (ProjectDO p : ps) {
					JSONObject po = new JSONObject();
					po.put("id", p.getProjectId());
					po.put("name", p.getProjectName());
					List<ProjectBugDO> bs = p.getBugs();
					po.put("bug_number", bs != null ? bs.size() : 0);
					if (bs != null) {
						JSONArray bas = new JSONArray();
						for (ProjectBugDO bug : bs) {
							JSONObject bo = new JSONObject();
							bo.put("bug", bug.getBugContent());
							bas.put(bo);
						}
						po.put("bugs", bas);
					} else {
						po.put("bugs", "[]");
					}
					double prc = p.getProgressPercentage();
					po.put("proccess",
							""
									+ (prc > 1 ? 100 : Utils.accuracyTxt(
											prc * 100, 2)) + "%");
					po.put("color", getColor(prc));
					po.put("group", storeManager.getGroup(p.getProjectCode()));
					po.put("url", getProjectImageURL(p.getProjectGroup()));

					ja.put(po);
				}

				JSONObject jo = new JSONObject();
				jo.put("projects", ja);

				sb.append(jo.toString());
			}

			out.print(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}

	private void doTeambition(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Object token = request.getSession().getAttribute("token");
		if (token != null) {
			PrintWriter out = null;
			try {
				StringBuilder sb = new StringBuilder();

				// loadCOSProjectData();
				client.loadProjects((String) token);
				List<ProjectDO> plist = storeManager
						.loadProjectExtendFromDB(client.getProjects());

				String userId = (String) request.getSession()
						.getAttribute("me");
				if (userId != null) {
					storeManager.cachedProject(userId, plist);
				}

				sb.append("{success:true,msg:'同步Teambition数据成功'}");

				out = response.getWriter();
				out.print(sb.toString());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				out.close();
			}
		} else {
			request.getRequestDispatcher("error.html").forward(request,
					response);
		}
	}

	private String getColor(double prc) {
		if (prc < 0.25) {
			return "#DC143C";
		}

		if (prc >= 0.25 && prc < 0.75) {
			return "#FFD700";
		}

		if (prc > 1) {
			return "#DC143C";
		}

		return "#008000";
	}

	private String getProjectImageURL(String group) {
		if (group != null && group.startsWith("01")) {
			return "../td/images/project_start.png";
		}

		if (group != null && group.startsWith("02")) {
			return "../td/images/project_back.png";
		}
		return "../td/images/project_stop.png";
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		WebApplicationContext wac = WebApplicationContextUtils
				.getRequiredWebApplicationContext(config.getServletContext());
		storeManager = wac.getBean(ProjectStoreManager.class);
	}

	public void loadCOSProjectData() {
		String url = Client.COS_FILE_BASE_URL + "projects.properties";

		URLConnection con;
		try {
			con = new URL(url).openConnection();
			con.connect();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					con.getInputStream(), "UTF-8"));
			pop = new Properties();
			pop.load(reader);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}

	private UserDO loadUser(String userRes) throws JSONException,
			ParseException {
		JSONObject o = new JSONObject(userRes);
		UserDO user = new UserDO();
		user.setUserId(o.getString("_id"));
		user.setEmail(o.getString("email"));
		user.setName(o.getString("name"));
		user.setPhone(o.getString("phone"));
		user.setTitle(o.getString("title"));

		Date d = Util.parseDate(o.getString("created"));
		user.setGmtCreate(d);
		if (o.has("updated")) {
			d = Util.parseDate(o.getString("updated"));
			user.setGmtModify(d);
		}
		return user;
	}

	// private void loadCOSData(Project p,ProjectDO projectDO) {
	// if(pop!=null&&pop.size()>0){
	// String name=p.getProjectName();
	// String id=name.substring(name.lastIndexOf('_')+1, name.length());
	// String pps=pop.getProperty(id);
	// if(pps!=null){
	// String[] ps=pps.split(",");
	// if(ps!=null&&ps.length>1){
	// projectDO.setProjectName(ps[0]);
	// projectDO.setProjectOwnerId(ps[1]);
	// }
	// }else{
	// projectDO.setProjectName(name);
	// projectDO.setProjectOwnerId(p.getMember(p.getCreatorID()));
	// }
	// }
	// }

	private void proccessLeaf(JSONArray ja, String userId, ProjectDO p,
			String taskGroupId) {
		List<TaskDO> list = storeManager.getTasksFromGroup(taskGroupId, userId,
				p.getProjectId());
		if (list != null) {
			for (TaskDO task : list) {
				JSONObject jo = new JSONObject();
				jo.put("id", task.getTaskId());
				jo.put("name", task.getContent());
				jo.put("group", false);
				jo.put("leaf", true);
				double d = task.getProgressPercentage();
				jo.put("proccess",
						"" + (d > 1 ? 100 : Utils.accuracyTxt(d * 100, 2))
								+ "%");
				jo.put("color", getColor(d));

				ja.put(jo);
			}
		}
	}

	private void saveProject(HttpServletRequest request, String userId) {
		ProjectDO project = createProjectFromRequest(request);

		if (storeManager.updateProject(project)) {
			storeManager.loadProject(project);
		}
	}

	private void saveTask(HttpServletRequest request, String userId) {
		TaskDO task = createTaskFromRequest(request);
		storeManager.updateTask(task);
	}

}
