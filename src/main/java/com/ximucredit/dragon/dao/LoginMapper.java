package com.ximucredit.dragon.dao;

import com.ximucredit.dragon.DO.LoginDO;

public interface LoginMapper {
	public LoginDO findById(String userId);
	public LoginDO findByCode(String code);
	public void insert(LoginDO loginDO);
	public void insertWechatLogin(LoginDO loginDO);
	public void updateByPrimaryKey(LoginDO loginDO);
}
