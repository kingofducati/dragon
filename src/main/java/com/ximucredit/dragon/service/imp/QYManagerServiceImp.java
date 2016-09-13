/**
 * 
 */
package com.ximucredit.dragon.service.imp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ximucredit.dragon.DO.DepartDO;
import com.ximucredit.dragon.DO.UserDO;
import com.ximucredit.dragon.dao.DepartMapper;
import com.ximucredit.dragon.dao.UserMapper;
import com.ximucredit.dragon.service.QYManagerService;
import com.ximucredit.teambition.HttpUtil;

/**
 * @author dux.fangl
 *
 */
public class QYManagerServiceImp extends AbstractWechatService implements
		QYManagerService {
	@Autowired
	private DepartMapper departMapper;
	@Autowired
	private UserMapper userMapper;

	public QYManagerServiceImp() {
		this.corpID="wx7064134cd009c9ed";
		this.secret="0DfW1qynah36zeNwG1XrSagNliop-nkSGxAya7OxdTQy8Ggy_0Es3YjLHUl2Af7i";
	}
	
	public void updateQYDepart(DepartDO departDO){
		String token=getAccessToken();
		String url=WX_BASE+"cgi-bin/department/update?access_token="+token;
		
		try {
			JSONObject jo=new JSONObject();
			jo.put("id", Integer.parseInt(departDO.getDepartId()));
			jo.put("name", departDO.getDepartName());
			jo.put("parentid", Integer.parseInt(departDO.getParentId()));
			
			String res=HttpUtil.post(url, jo.toString(), null);
			jo=new JSONObject(res);
			if(!jo.isNull("errcode")){
				int code=jo.getInt("errcode");
				if(code==0){
					departMapper.updateByPrimaryKey(departDO);
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void createQYDepart(DepartDO departDO){
		String token=getAccessToken();
		String url=WX_BASE+"cgi-bin/department/create?access_token="+token;
		
		try {
			JSONObject jo=new JSONObject();
			jo.put("name", departDO.getDepartName());
			jo.put("parentid", Integer.parseInt(departDO.getParentId()));
			
			String res=HttpUtil.post(url, jo.toString(), null);
			jo=new JSONObject(res);
			if(!jo.isNull("id")){
				departDO.setDepartId(""+jo.getInt("id"));
				
				departMapper.insert(departDO);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void insertDepart(DepartDO departDO){
		this.departMapper.insert(departDO);
	}
	
	public DepartDO queryDepartByID(String departId){
		return departMapper.findById(departId);
	}
	
	public List<DepartDO> queryDepartByParent(String parentId){
		return departMapper.findByParent(parentId);
	}
	
	public List<DepartDO> queryDepartFromRemote(String parentId){
		String token=getAccessToken();
		String url=WX_BASE+"cgi-bin/department/list?access_token="+token+"&id="+parentId;
		
		try {
			String res=HttpUtil.get(url, null, null);
			JSONObject jo=new JSONObject(res);
			int root=0;
			if(parentId==null) root=0;
			else root=Integer.parseInt(parentId);
			
			if(!jo.isNull("department")){
				JSONArray ja = jo.getJSONArray("department");
				if(ja!=null&&ja.length()>0){
					List<DepartDO> list=new ArrayList<DepartDO>();
					
					for(int i=0;i<ja.length();i++){
						JSONObject o= ja.getJSONObject(i);
						
						DepartDO departDO=new DepartDO();
						departDO.setDepartId(""+o.getInt("id"));
						departDO.setDepartName(o.getString("name"));
						departDO.setParentId(""+o.getInt("parentid"));
						
						if(root==Integer.parseInt(departDO.getParentId())){
							list.add(departDO);
						}
					}
					
					return list;
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void deleteQYDepart(String departId){
		String token=getAccessToken();
		String url=WX_BASE+"cgi-bin/department/delete?access_token="+token+"&id"+departId;
		
		try {
			String res=HttpUtil.get(url, null, null);
			JSONObject jo=new JSONObject(res);
			if(!jo.isNull("errcode")){
				int code=jo.getInt("errcode");
				if(code==0){
					DepartDO departDO=departMapper.findById(departId);
					if(departDO!=null){
						departDO.setDelFlag(true);
						
						departMapper.updateByPrimaryKey(departDO);
					}
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void createQYUser(UserDO user){
		String token=getAccessToken();
		String url=WX_BASE+"cgi-bin/user/create?access_token="+token;
		
		try {
			JSONObject jo=new JSONObject();
			jo.put("userid", user.getQyId());
			jo.put("name", user.getName());
			jo.put("department", user.getDepartId());
			jo.put("position", user.getTitle());
			jo.put("mobile", user.getPhone());
			jo.put("gender", 1);
			jo.put("email", user.getEmail());
			jo.put("weixinid", "");
			
			String res=HttpUtil.post(url, jo.toString(), null);
			jo=new JSONObject(res);
			if(!jo.isNull("errcode")){
				int code=jo.getInt("errcode");
				if(code==0){
					userMapper.insert(user);
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void updateQYUser(UserDO user){
		String token=getAccessToken();
		String url=WX_BASE+"cgi-bin/user/update?access_token="+token;
		
		try {
			JSONObject jo=new JSONObject();
			jo.put("userid", user.getQyId());
			jo.put("name", user.getName());
			jo.put("department", user.getDepartId());
			jo.put("position", user.getTitle());
			jo.put("mobile", user.getPhone());
			jo.put("gender", 1);
			jo.put("email", user.getEmail());
			jo.put("weixinid", "");
			
			String res=HttpUtil.post(url, jo.toString(), null);
			jo=new JSONObject(res);
			if(!jo.isNull("errcode")){
				int code=jo.getInt("errcode");
				if(code==0){
					userMapper.updateByPrimaryKey(user);
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteQYUser(String userId){
		
	}
	
	public List<UserDO> queryUserFromRemote(String departId){
		String token=getAccessToken();
		String url=WX_BASE+"cgi-bin/user/list?access_token="+token+"&department_id="+departId+"&fetch_child=0&status=0";
		
		try {
			String res=HttpUtil.get(url, null, null);
			JSONObject jo=new JSONObject(res);
			
			if(!jo.isNull("userlist")){
				JSONArray ja = jo.getJSONArray("userlist");
				if(ja!=null&&ja.length()>0){
					List<UserDO> list=new ArrayList<UserDO>();
					
					for(int i=0;i<ja.length();i++){
						JSONObject o= ja.getJSONObject(i);
						
						UserDO user=new UserDO();
						user.setQyId(o.getString("userid"));
						user.setName(o.getString("name"));
						JSONArray ds=o.getJSONArray("department");
						user.setDepartId(ds.get(0).toString());
						user.setTitle(o.has("position")?o.getString("position"):"");
						user.setPhone(o.has("mobile")?o.getString("mobile"):"");
						user.setGender(o.getInt("gender"));
						user.setEmail(o.getString("email"));
						user.setWeixinId(o.has("weixinid")?o.getString("weixinid"):"");
						user.setAvatar(o.has("avatar")?o.getString("avatar"):"");
//						o.getInt("status");
//						o.getJSONObject("extattr").getJSONArray("attrs");
						
						list.add(user);
					}
					
					return list;
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public UserDO findBDUserByEmail(String email){
		return this.userMapper.findByEmail(email);
	}
	
	public void insertBDUser(UserDO user){
		this.userMapper.insert(user);
	}
	
	public void updateBDUser(UserDO user){
		this.userMapper.updateByPrimaryKey(user);
	}
}
