package com.ximucredit.dragon.service.imp;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ximucredit.dragon.DO.LoginDO;
import com.ximucredit.dragon.dao.LoginMapper;
import com.ximucredit.dragon.service.LoginService;

@Service("loginService")
public class LoginServiceImp implements LoginService {
	@Autowired
	private LoginMapper loginMapper;

	public LoginDO findById(String userId) {
		return loginMapper.findById(userId);
	}

	public LoginDO findByCode(String code) {
		return loginMapper.findByCode(code);
	}

	public void update(String userId, String code, String accessToken,
			Date loginTime, String remark) {
		LoginDO loginDO=findById(userId);
		boolean isNew=false;
		
		if(loginDO==null){
			isNew=true;
			loginDO=new LoginDO();
			loginDO.setUserId(userId);
		}
		
		loginDO.setAccessToken(accessToken);
		loginDO.setCode(code);
		loginDO.setGmtModify(new Date());
		loginDO.setLoginTime(loginTime);
		loginDO.setRemark(remark);
		
		if(isNew){
			loginMapper.insert(loginDO);
		}else{
			loginMapper.updateByPrimaryKey(loginDO);
		}

	}
	
	public void updateWechat(String userId, String wechatCode, String deviceId,
			String accessToken, Date loginTime, String remark) {
		LoginDO loginDO=findById(userId);
		boolean isNew=false;
		
		if(loginDO==null){
			isNew=true;
			loginDO=new LoginDO();
			loginDO.setUserId(userId);
		}
		
		loginDO.setAccessToken(accessToken);
		loginDO.setWechatCode(wechatCode);
		loginDO.setDeviceId(deviceId);
		loginDO.setGmtModify(new Date());
		loginDO.setLoginTime(loginTime);
		loginDO.setRemark(remark);
		
		if(isNew){
			loginMapper.insertWechatLogin(loginDO);
		}else{
			loginMapper.updateByPrimaryKey(loginDO);
		}
		
	}
}
