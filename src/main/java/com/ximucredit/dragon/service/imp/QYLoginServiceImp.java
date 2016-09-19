/**
 * 
 */
package com.ximucredit.dragon.service.imp;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONObject;

import com.ximucredit.dragon.SpringContextHelper;
import com.ximucredit.dragon.DO.UserDO;
import com.ximucredit.dragon.service.QYLoginService;
import com.ximucredit.dragon.service.UserService;
import com.ximucredit.teambition.HttpUtil;

/**
 * @author fang
 *
 */
public class QYLoginServiceImp extends AbstractWechatService implements QYLoginService {
	public final static String LOGIN_BASE="https://api.weixin.qq.com/";
	
	private String code;
	private String openid;
	private String unionId;
	
	public QYLoginServiceImp(String code) {
		this.code=code;
		this.corpID="wxe81ab6efa72ce31a";
		this.secret="1fbb043d5799f4ea8abf88e7b16b61de";
	}
	
	public String getCode() {
		return code;
	}
	
	public String getOpenid() {
		return openid;
	}
	
	public String getUnionId() {
		return unionId;
	}
	
	protected String getAccessToken() {
		if(System.currentTimeMillis()>expiresTime){
			String url=LOGIN_BASE+"sns/oauth2/access_token?appid="+corpID+"&secret="+secret+"&code="+code+"&grant_type=authorization_code";
			try {
				String res=HttpUtil.get(url, null, null);
				if(res!=null){
					JSONObject o=new JSONObject(res);
					if(!o.isNull("access_token")){
						accessToken=o.getString("access_token");
					}
					
					if(!o.isNull("expires_in")){
						expiresTime=System.currentTimeMillis()+o.getLong("expires_in")*1000;
					}
					
					if(!o.isNull("openid")){
						this.openid=o.getString("openid");
					}
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return this.accessToken;
	}

	/* (non-Javadoc)
	 * @see com.ximucredit.dragon.service.QYLoginService#getQYLoginUser()
	 */
	@Override
	public UserDO getQYLoginUser() {
		UserDO user=null;
		String token=getAccessToken();
		String url=LOGIN_BASE+"sns/userinfo?access_token="+token+"&openid="+openid;
		try {
			String res=HttpUtil.get(url, null, null);
			System.out.println("res"+res);
			if(res!=null){
				JSONObject o=new JSONObject(res);
				
				if(!o.isNull("unionid")){
					this.unionId=o.getString("unionid");
					
					UserService userService=(UserService)SpringContextHelper.getBean("userService");
					if(userService!=null){
						user=userService.findByWechatId(this.unionId);
					}
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return user;
	}

}
