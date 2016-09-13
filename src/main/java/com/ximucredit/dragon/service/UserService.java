package com.ximucredit.dragon.service;

import java.util.Date;
import java.util.List;

import com.ximucredit.dragon.DO.UserDO;

public interface UserService {
	public UserDO findById(String userId);
	public UserDO findByEmail(String userEmail);
	public void update(String userId,String email,String name,String phone,String title,Date createTime,Date updateTime);
	public void updateByDO(UserDO user);
	
	public List<UserDO> listAllUsers();
}
