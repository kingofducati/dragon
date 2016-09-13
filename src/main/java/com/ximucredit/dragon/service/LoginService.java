package com.ximucredit.dragon.service;

import java.util.Date;

import com.ximucredit.dragon.DO.LoginDO;

public interface LoginService {
	public LoginDO findById(String userId);
	public LoginDO findByCode(String code);
	public void update(String userId,String code,String accessToken,Date loginTime,String remark);
	public void updateWechat(String userId,String wechatCode,String deviceId,String accessToken,Date loginTime,String remark);
}
