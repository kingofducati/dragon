package com.ximucredit.teambition;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Project {
	private Client client;
	private String projectID;
	private String projectName;
	private String creatorID;
	private String owner;
	private String projectTitle;
	private String deployTime;
	private String nowTaskID;
	
	private List<Task> tasks;
	private List<Member> members;
	
	public List<Member> getMembers() {
		return members;
	}
	
	public String getDeployTime() {
		return deployTime;
	}

	public void setDeployTime(String deployTime) {
		this.deployTime = deployTime;
	}

	public String getNowTaskID() {
		return nowTaskID;
	}

	public void setNowTaskID(String nowTaskID) {
		this.nowTaskID = nowTaskID;
	}

	public void setClient(Client client) {
		this.client = client;
	}
	
	public Client geClient(){
		return this.client;
	}
	
	public String getProjectID() {
		return projectID;
	}
	
	public void setProjectID(String projectID) {
		this.projectID = projectID;
	}
	
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getCreatorID() {
		return creatorID;
	}
	public void setCreatorID(String creatorID) {
		this.creatorID = creatorID;
	}
	
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getProjectTitle() {
		return projectTitle;
	}

	public void setProjectTitle(String projectTitle) {
		this.projectTitle = projectTitle;
	}

	public void loadTasks() throws ClientProtocolException, IOException, JSONException, ParseException{
		String res=HttpUtil.get(Client.API_BASE_URL+"projects/"+this.projectID+"/tasks", "all=true","Authorization=OAuth2 "+client.getAccess_token());
		
		if(res!=null&&res.length()>0&&res.startsWith("[")){
			JSONArray ro=new JSONArray(res);
			if(ro.length()>0){
				this.tasks=new ArrayList<Task>();
				for(int i=0;i<ro.length();i++){
					Task t=new Task();
					JSONObject o=ro.getJSONObject(i);
					t.setContent(o.isNull("content")?"":o.getString("content"));
					t.setProjectID(o.isNull("_projectId")?"":o.getString("_projectId"));
					t.setCreatorID(o.isNull("_creatorId")?"":o.getString("_creatorId"));
					t.setExecutorID(o.isNull("_executorId")?"":o.getString("_executorId"));
					t.setStageID(o.isNull("_stageId")?"":o.getString("_stageId"));
					t.setPriority(o.isNull("priority")?0:o.getInt("priority"));
					t.setNote(o.isNull("note")?"":o.getString("note"));
					t.setAccomplished(o.isNull("accomplished")?null:Util.parseDate(o.getString("accomplished")));
					t.setTaskID(o.isNull("_id")?"":o.getString("_id"));
					t.setDone(o.isNull("isDone")?false:o.getBoolean("isDone"));
					t.setEndDate(o.isNull("dueDate")?null:Util.parseDate(o.getString("dueDate")));
					t.setCreated(o.isNull("created")?null:Util.parseDate(o.getString("created")));
					t.setTasklistID(o.isNull("_tasklistId")?"":o.getString("_tasklistId"));
					t.setUpdated(o.isNull("updated")?null:Util.parseDate(o.getString("updated")));
					
					tasks.add(t);
					
				}
				
				if(tasks.size()>0){
					Collections.sort(tasks,new Comparator<Task>() {
						public int compare(Task o1, Task o2) {
							return o1.getContent().compareTo(o2.getContent());
						}
					});
				}
			}
		}
	}
	
	public Task getTaskByTaskName(String taskName){
		if(taskName!=null&&taskName.length()>0){
			for(Task tt:tasks){
				if(taskName.equals(tt.getContent())){
					return tt;
				}
			}
		}
		
		return null;
	}
	
	public Task getTaskByTaskID(String taskID){
		if(taskID!=null&&taskID.length()>0){
			for(Task tt:tasks){
				if(taskID.equals(tt.getTaskID())){
					return tt;
				}
			}
		}
		
		return null;
	}
	
	public void loadMembers() throws ClientProtocolException, IOException{
		String res=HttpUtil.get(Client.API_BASE_URL+"projects/"+this.projectID+"/members", "all=true","Authorization=OAuth2 "+client.getAccess_token());
		if(res!=null&&res.length()>0&&res.startsWith("[")){
			JSONArray ro=new JSONArray(res);
			if(ro.length()>0){
				members=new ArrayList<Member>();
				for(int i=0;i<ro.length();i++){
					Member mem=new Member();
					JSONObject o=ro.getJSONObject(i);
					mem.setId(o.getString("_memberId"));
					mem.setUserId(o.getString("_userId"));
					mem.setName(o.getString("name"));
					mem.setNickname(o.getString("nickname"));
					mem.setEmail(o.getString("email"));
					
					members.add(mem);
				}
			}
		}
	}
	
	public Member getMemberByID(String memID){
		if(memID!=null&&memID.length()>0){
			for(Member m:members){
				if(memID.equals(m.getId())){
					return m;
				}
			}
		}
		
		return null;
	}
	
	public Member getMemberByUserID(String userID){
		if(userID!=null&&userID.length()>0){
			for(Member m:members){
				if(userID.equals(m.getUserId())){
					return m;
				}
			}
		}
		
		return null;
	}
	
	public String getMember(String creatorID) {
		Member m=getMemberByID(creatorID);
		if(m==null){
			m=getMemberByUserID(creatorID);
		}
		return m==null?"":m.getName();
	}

	public List<Task> getTasks(){
		return this.tasks;
	}
}
