/**
 * 
 */
package com.ximucredit.dragon.service.imp;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONObject;

import com.ximucredit.teambition.HttpUtil;

/**
 * @author dux.fangl
 * 基础频率:
 * 	每企业调用单个cgi/api不可超过1000次/分，30000次/小时
 * 	企业每ip调用接口不可超过20000次/分，600000次/小时
 * 	第三方应用提供商每ip调用接口不可超过40000次/分，1200000次/小时
 * 
 * 发消息频率:
 * 	每企业不可超过帐号上限数*30人次/天
 * 
 * 创建帐号频率:
 * 	每企业创建帐号数不可超过帐号上限数*3/月
 * 	每企业最大应用数限制为30个，创建应用次数不可超过30*3/月
 */
public abstract class AbstractWechatService {
	public final static String WX_BASE="https://qyapi.weixin.qq.com/";
	
	protected String corpID="wx7064134cd009c9ed";
	protected String secret="rtwOPVbOC-KLuPsnS9qi4bv1_badDUmUtYspmESK107rMvAERHzN-jipT4Nxl4gl";
	
	protected String accessToken;
	protected long expiresTime;
	
	public String getToken() {
		return getAccessToken();
	}
	
	public String getCorpID() {
		return corpID;
	}

	public void setCorpID(String corpID) {
		this.corpID = corpID;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}
	
	protected String getAccessToken(){
		if(System.currentTimeMillis()>expiresTime){
			String url=WX_BASE+"cgi-bin/gettoken?corpid="+corpID+"&corpsecret="+secret;
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
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return this.accessToken;
	}
}
