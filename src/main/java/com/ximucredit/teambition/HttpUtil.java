package com.ximucredit.teambition;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpUtil {
	
	/**
	 * @param url
	 * @param params	参数格式：k1=v1,k2=v2,k3=v3; 或者json格式
	 * @param headers   参数格式：k1=v1,k2=v2,k3=v3
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static String post(String url,String params,String headers) throws ClientProtocolException, IOException{
		CloseableHttpClient httpclient = HttpClients.createDefault(); 
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(300000).setConnectTimeout(300000).build();//设置请求和传输超时时间
		
		HttpPost post=new HttpPost(url);
		post.setConfig(requestConfig);
		
		UrlEncodedFormEntity entity=null;
		
		if(params!=null&&params.length()>0){
			if(params.startsWith("{")){//如果是json直接发送
				HttpEntity en=new StringEntity(params,Charset.forName("UTF-8"));
				post.setEntity(en);
			}else{
				List<NameValuePair> nvs=URLEncodedUtils.parse(params, Charset.forName("UTF-8"),',');
				entity=new UrlEncodedFormEntity(nvs,"UTF-8");
				post.setEntity(entity);
			}
		}
		
		if(headers!=null&&headers.length()>0){
			List<NameValuePair> hnvs=URLEncodedUtils.parse(headers, Charset.forName("UTF-8"),',');
			for(NameValuePair nv:hnvs){
				post.addHeader(nv.getName(), nv.getValue());
			}
		}
		
		System.out.println(post.toString());
		System.out.println("POST header:" + headers);
		if(entity!=null){
			System.out.println("POST content:" + EntityUtils.toString(entity, "UTF-8"));
		}
		
		CloseableHttpResponse response = httpclient.execute(post);
		String rs;
		try{
			HttpEntity reEn = response.getEntity(); 
			rs=EntityUtils.toString(reEn, "UTF-8");
			System.out.println("Response content: " + rs); 
		}finally{
			response.close();
		}
		
		return rs;
	}

	public static String get(String url,String params,String headers) throws ClientProtocolException, IOException{
		CloseableHttpClient httpclient = HttpClients.createDefault(); 
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(300000).setConnectTimeout(300000).build();//设置请求和传输超时时间
		
				
		if(params!=null&&params.length()>0){
			List<NameValuePair> nvs=URLEncodedUtils.parse(params, Charset.forName("UTF-8"),',');
			StringBuilder sb=new StringBuilder(url);
			sb.append('?');
			
			for(NameValuePair nv:nvs){
				sb.append(nv.getName()).append("=").append(nv.getValue()).append('&');
			}
			
			url=sb.toString();
		}
		HttpGet post=new HttpGet(url);
		post.setConfig(requestConfig);
		
		if(headers!=null&&headers.length()>0){
			List<NameValuePair> hnvs=URLEncodedUtils.parse(headers, Charset.forName("UTF-8"),',');
			for(NameValuePair nv:hnvs){
				post.addHeader(nv.getName(), nv.getValue());
			}
		}
		
		System.out.println(post.toString());
		System.out.println("GET header:" + headers);
		
		CloseableHttpResponse response = httpclient.execute(post);
		String res="";
		try{
			HttpEntity reEn = response.getEntity(); 
			res=EntityUtils.toString(reEn, "UTF-8");
			System.out.println("Response content: " + res); 
		}finally{
			response.close();
		}
		
		return res;
	}
}
