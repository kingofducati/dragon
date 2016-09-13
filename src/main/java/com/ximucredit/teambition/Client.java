package com.ximucredit.teambition;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.utils.URLEncodedUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Client {
	public static String API_BASE_URL = "https://api.teambition.com/";
	public static String ACCOUNT_BASE_URL = "https://account.teambition.com/";
	public static String COS_FILE_BASE_URL="http://projects-10050387.file.myqcloud.com/";

	private String client_key;
	private String client_secret;
	private String returnURI;
	private String access_token;
	private String refresh_token;

	private List<Project> projects;

	public Client(String client_key, String client_secret, String returnURI) {
		super();
		this.client_key = client_key;
		this.client_secret = client_secret;
		this.returnURI = returnURI;
	}
	
	public String getClient_key() {
		return client_key;
	}

	public void setClient_key(String client_key) {
		this.client_key = client_key;
	}

	public String getClient_secret() {
		return client_secret;
	}

	public void setClient_secret(String client_secret) {
		this.client_secret = client_secret;
	}

	public String getReturnURI() {
		return returnURI;
	}

	public void setReturnURI(String returnURI) {
		this.returnURI = returnURI;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getRefresh_token() {
		return refresh_token;
	}

	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}
	
	public String getAuthorizeURI(){
		StringBuilder ps=new StringBuilder();
		ps.append("client_id=").append(client_key).append(",");
		ps.append("redirect_uri=").append(returnURI);
		
		List<NameValuePair> nvs=URLEncodedUtils.parse(ps.toString(), Charset.forName("UTF-8"),',');
		StringBuilder sb=new StringBuilder(ACCOUNT_BASE_URL);
		sb.append("oauth2/authorize?");
		
		for(NameValuePair nv:nvs){
			sb.append(nv.getName()).append("=").append(nv.getValue()).append('&');
		}
		
		return sb.toString();
	}
	
	public String loadMe(String access_token) throws ClientProtocolException, IOException{
		String res = HttpUtil.get(API_BASE_URL + "users/me",null, "Authorization=OAuth2 "+access_token);
		return res;
	}

	public void loadToken(String code) throws ClientProtocolException, IOException {
		String res = HttpUtil.post(ACCOUNT_BASE_URL + "oauth2/access_token",
				"client_id=" + client_key + ",client_secret=" + client_secret
						+ ",code=" + code + ",redirect_uri=" + returnURI, null);
		if(res!=null&&res.length()>0){
			JSONObject ro=new JSONObject(res);
			access_token=ro.getString("access_token");
			refresh_token=ro.getString("refresh_token");
		}
			
	}
	
	public void loadProjects(String access_token) throws ClientProtocolException, IOException, JSONException, ParseException{
		String res = HttpUtil.get(API_BASE_URL + "projects",null, "Authorization=OAuth2 "+access_token);
		
		if(res!=null&&res.length()>0){
			JSONArray ro=new JSONArray(res);
			if(ro.length()>0){
				this.projects=new ArrayList<Project>();
				
				for(int i=0;i<ro.length();i++){
					JSONObject o=ro.getJSONObject(i);
					Project p=new Project();
					p.setClient(this);
					p.setCreatorID(o.getString("_creatorId"));
					p.setProjectID(o.getString("_id"));
					p.setProjectName(o.getString("name"));
					
					p.loadTasks();
					p.loadMembers();
					
					this.projects.add(p);
				}
			}
		}
	}

	public Project getProjectByID(String projectID){
		if(projects!=null&&projectID!=null){
			for(Project p:projects){
				if(projectID.equals(p.getProjectID())){
					return p;
				}
			}
		}
		
		return null;
	}

	public List<Project> getProjects(){
		return this.projects;
	}
}
