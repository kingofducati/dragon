package com.ximucredit.dragon.service.imp;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ximucredit.dragon.DO.UserDO;
import com.ximucredit.dragon.dao.UserMapper;
import com.ximucredit.dragon.service.UserService;

@Service("userService")
public class UserServiceImp implements UserService {
	@Autowired
	private UserMapper userMapper;

	public UserDO findById(String userId) {
		return userMapper.findById(userId);
	}
	
	public UserDO findByEmail(String userEmail) {
		return userMapper.findByEmail(userEmail);
	}

	public void update(String userId, String email, String name, String phone,
			String title, Date gmtCreate, Date gmtModify) {
		UserDO user=new UserDO();
		user.setEmail(email);
		user.setGmtCreate(gmtCreate);
		user.setGmtModify(gmtModify);
		user.setName(name);
		user.setPhone(phone);
		user.setTitle(title);
		user.setUserId(userId);
		
		updateByDO(user);
	}

	public void updateByDO(UserDO user) {
		UserDO u=findById(user.getUserId());
		if(u==null){
			userMapper.insert(user);
		}else{
			userMapper.updateByPrimaryKey(user);
		}
	}

	public List<UserDO> listAllUsers() {
		return userMapper.listAll();
	}
}
