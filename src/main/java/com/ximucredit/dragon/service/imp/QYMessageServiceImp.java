package com.ximucredit.dragon.service.imp;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONObject;

import com.qq.weixin.mp.aes.WechatUser;
import com.ximucredit.dragon.service.QYMessageService;
import com.ximucredit.teambition.HttpUtil;

/**
 * @author dux.fangl
 *	企业会话服务
 */
public class QYMessageServiceImp extends AbstractWechatService implements QYMessageService {
	class QY{
		private String agentid;//企业应用的id
		private String report_location_flag;//企业应用是否打开地理位置上报 0：不上报；1：进入会话上报；2：持续上报
		private String logo_mediaid;//企业应用头像的mediaid，通过多媒体接口上传图片获得mediaid，上传后会自动裁剪成方形和圆形两个头像
		private String name;//企业应用名称
		private String description;//企业应用详情
		private String redirect_domain;//企业应用可信域名
		private String isreportuser;//是否接收用户变更通知。0：不接收；1：接收
		private String isreportenter;//是否上报用户进入应用事件。0：不接收；1：接收。
		private String home_url;//主页型应用url。url必须以http或者https开头。消息型应用无需该参数
		private String chat_extension_url;//关联会话url。设置该字段后，企业会话"+"号将出现该应用，点击应用可直接跳转到此url，支持jsapi向当前会话发送消息。
		
		public QY(){
			
		}
		
		public String getAgentid() {
			return agentid;
		}

		public void setAgentid(String agentid) {
			this.agentid = agentid;
		}

		public String getReport_location_flag() {
			return report_location_flag;
		}

		public void setReport_location_flag(String report_location_flag) {
			this.report_location_flag = report_location_flag;
		}

		public String getLogo_mediaid() {
			return logo_mediaid;
		}

		public void setLogo_mediaid(String logo_mediaid) {
			this.logo_mediaid = logo_mediaid;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getRedirect_domain() {
			return redirect_domain;
		}

		public void setRedirect_domain(String redirect_domain) {
			this.redirect_domain = redirect_domain;
		}

		public String getIsreportuser() {
			return isreportuser;
		}

		public void setIsreportuser(String isreportuser) {
			this.isreportuser = isreportuser;
		}

		public String getIsreportenter() {
			return isreportenter;
		}

		public void setIsreportenter(String isreportenter) {
			this.isreportenter = isreportenter;
		}

		public String getHome_url() {
			return home_url;
		}

		public void setHome_url(String home_url) {
			this.home_url = home_url;
		}

		public String getChat_extension_url() {
			return chat_extension_url;
		}

		public void setChat_extension_url(String chat_extension_url) {
			this.chat_extension_url = chat_extension_url;
		}
		
	}
	
	/**
	 * 设置企业信息
	 * @param qy
	 */
	public void setParams(QY qy){
		String token=getAccessToken();
		String url=WX_BASE+"cgi-bin/agent/set?access_token="+token;
		
		if(qy!=null){
			Map<String, String> od=new HashMap<String, String>();
			if(qy.getAgentid()!=null){
				od.put("agentid", qy.getAgentid());
			}
			
			if(qy.getChat_extension_url()!=null){
				od.put("chat_extension_url", qy.getChat_extension_url());
			}
			
			if(qy.getDescription()!=null){
				od.put("description", qy.getDescription());
			}
			
			if(qy.getHome_url()!=null){
				od.put("home_url", qy.getHome_url());
			}
			
			if(qy.getIsreportenter()!=null){
				od.put("isreportenter", qy.getIsreportenter());
			}
			
			if(qy.getIsreportuser()!=null){
				od.put("isreportuser", qy.getIsreportuser());
			}
			
			if(qy.getLogo_mediaid()!=null){
				od.put("logo_mediaid", qy.getLogo_mediaid());
			}
			
			if(qy.getName()!=null){
				od.put("name", qy.getName());
			}
			
			if(qy.getRedirect_domain()!=null){
				od.put("redirect_domain", qy.getRedirect_domain());
			}
			
			if(qy.getReport_location_flag()!=null){
				od.put("report_location_flag", qy.getReport_location_flag());
			}
			
			JSONObject jo=(JSONObject)JSONObject.wrap(od);
			
			try {
				String res=HttpUtil.post(url, jo.toString(), null);
				System.out.println(res);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public WechatUser getQYUser(String code){
		String token=getAccessToken();
		String url=WX_BASE+"cgi-bin/user/getuserinfo?access_token="+token+"&code="+code;
		
		try {
			String res=HttpUtil.get(url, null, null);
			JSONObject jo=new JSONObject(res);
			if(jo.has("UserId")){
				WechatUser user=new WechatUser();
				user.setUserId(jo.getString("UserId"));
				user.setDeviceId(jo.getString("DeviceId"));
				
				return user;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * @param chatId
	 * @param chatName
	 * @param ownerUserId
	 * @param userList			必须三个人以上才可以创建
	 * @return
	 */
	public boolean createChatRoom(String chatId,String chatName,String ownerUserId,List<String> userList){
		String token=getAccessToken();
		String url=WX_BASE+"cgi-bin/chat/create?access_token="+token;
		
		JSONObject ps=new JSONObject();
		ps.put("chatid", chatId);
		ps.put("name", chatName);
		ps.put("owner", ownerUserId);
		ps.put("userlist", userList);
		
		try {
			String res=HttpUtil.post(url, ps.toString(), null);
			JSONObject jo=new JSONObject(res);
			if(jo.has("errcode")){
				int code=jo.getInt("errcode");
				if(code==0){
					return true;
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public String getCharRoomUserList(String chatId){
		String token=getAccessToken();
		String url=WX_BASE+"cgi-bin/chat/get?access_token="+token+"&chatid="+chatId;
		
		try {
			String res=HttpUtil.get(url, null, null);
			JSONObject jo=new JSONObject(res);
			if(jo.has("chat_info")){
				JSONObject co=jo.getJSONObject("chat_info");
				if(co.has("userlist")){
					return co.getJSONArray("userlist").toString();
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public boolean updateChatRoom(String chatId,String opUserId,String name,List<String> addUserList,List<String> delUserList){
		String token=getAccessToken();
		String url=WX_BASE+"cgi-bin/chat/update?access_token="+token;
		
		JSONObject jo=new JSONObject();
		jo.put("chatid", chatId);
		jo.put("op_user", opUserId);
		jo.put("name", name);
		if(addUserList!=null)
			jo.put("add_user_list", addUserList);
		if(delUserList!=null)
			jo.put("del_user_list", delUserList);
		
		try {
			String res=HttpUtil.post(url, jo.toString(), null);
			jo=new JSONObject(res);
			if(jo.has("errcode")){
				int code=jo.getInt("errcode");
				if(code==0){
					return true;
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean quitChatRoom(String chatId,String opUserId){
		String token=getAccessToken();
		String url=WX_BASE+"cgi-bin/chat/quit?access_token="+token;
		
		JSONObject jo=new JSONObject();
		jo.put("chatid", chatId);
		jo.put("op_user", opUserId);
		
		try {
			String res=HttpUtil.post(url, jo.toString(), null);
			jo=new JSONObject(res);
			if(jo.has("errcode")){
				int code=jo.getInt("errcode");
				if(code==0){
					return true;
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean sendGroupMsg(String chatId,String sendUserId,String msg){
		String token=getAccessToken();
		String url=WX_BASE+"cgi-bin/chat/send?access_token="+token;
		
		JSONObject jo=new JSONObject();
		jo.put("type", "group");
		jo.put("id", chatId);
		
		JSONObject jo1=new JSONObject();
		jo1.put("receiver", jo);
		jo1.put("sender", sendUserId);
		jo1.put("msgtype", "text");
		jo1.put("text", new JSONObject("{\"content\":\""+msg+"\"}"));
		
		try {
			String res=HttpUtil.post(url, jo1.toString(), null);
			jo=new JSONObject(res);
			if(jo.has("errcode")){
				int code=jo.getInt("errcode");
				if(code==0){
					return true;
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	
	
}
