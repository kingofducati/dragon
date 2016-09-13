package com.ximucredit.teambition;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.utils.URLEncodedUtils;
import org.json.JSONObject;
import org.junit.Test;

public class HttpClientTest {

	/**
	 * code=l7XJo5H6Evy-DDrGRxMbqh
	 * access_token=wnu4y1aUvV8RFix17FhDooU2CiE=gCm3GiY5e3623f323e2e2ac0c2a006641ffe05935a33e28b96ef7c5ed0e99318fe9768c351e25e30c675612c1dc318f735eb1e0533e6d7333a27a4ee51aa9c77d178d57491ecde7ead5f348e9ef12a0554aae4fa9283977ac142bf51d81cf61e4e728f4b8a4860f64175f2509a1f48aba217cd29
	 * refresh_token=Px9RIzN8NuEy9vDUI8E5vL
	 * me  _id=574d0dcceca742e5249e68f1
	 */
	@Test
	public void testHttpClient(){
		try {
			//HttpUtil.post("https://account.teambition.com/oauth2/access_token", "client_id=9d009ff0-39b0-11e6-8ed7-e3410151ca80,client_secret=a0c4f1f1-0cf7-4483-b230-7a5499247eb5,code=l7XJo5H6Evy-DDrGRxMbqh,redirect_uri=http://localhost:8080/reoprt",null);
//			HttpUtil.get("https://api.teambition.com/projects", null,"Authorization=OAuth2 wnu4y1aUvV8RFix17FhDooU2CiE=gCm3GiY5e3623f323e2e2ac0c2a006641ffe05935a33e28b96ef7c5ed0e99318fe9768c351e25e30c675612c1dc318f735eb1e0533e6d7333a27a4ee51aa9c77d178d57491ecde7ead5f348e9ef12a0554aae4fa9283977ac142bf51d81cf61e4e728f4b8a4860f64175f2509a1f48aba217cd29");
			String res=HttpUtil.get("https://api.teambition.com/projects/5760ba47e88960e90937b755/tasks", null,"Authorization=OAuth2 wnu4y1aUvV8RFix17FhDooU2CiE=gCm3GiY5e3623f323e2e2ac0c2a006641ffe05935a33e28b96ef7c5ed0e99318fe9768c351e25e30c675612c1dc318f735eb1e0533e6d7333a27a4ee51aa9c77d178d57491ecde7ead5f348e9ef12a0554aae4fa9283977ac142bf51d81cf61e4e728f4b8a4860f64175f2509a1f48aba217cd29");
//			HttpUtil.get("https://api.teambition.com/tasklists/5760ba487391f4bb55e3e07e", null,"Authorization=OAuth2 wnu4y1aUvV8RFix17FhDooU2CiE=gCm3GiY5e3623f323e2e2ac0c2a006641ffe05935a33e28b96ef7c5ed0e99318fe9768c351e25e30c675612c1dc318f735eb1e0533e6d7333a27a4ee51aa9c77d178d57491ecde7ead5f348e9ef12a0554aae4fa9283977ac142bf51d81cf61e4e728f4b8a4860f64175f2509a1f48aba217cd29");
			
			JSONObject js=new JSONObject(res);
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testParamsPaser(){
		List<NameValuePair> list=URLEncodedUtils.parse("s=1,a=2,b=3,c=4", Charset.forName("UTF-8"), ',');
		
		for(NameValuePair nv:list){
			System.out.println("key="+nv.getName()+",value="+nv.getValue());
		}
		
		
	}

}
