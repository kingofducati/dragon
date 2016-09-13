package com.ximucredit.dragon.dao;

import java.util.List;

import com.ximucredit.dragon.DO.UserDO;

public interface UserMapper {
	public UserDO findById(String userId);
	public UserDO findByEmail(String email);
	public void insert(UserDO userDO);
	public void updateByPrimaryKey(UserDO userDO);
	public List<UserDO> findByDepart(String departId);
	public List<UserDO> listAll();
	public UserDO findByQYId(String qyId);
}
