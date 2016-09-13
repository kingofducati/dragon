package com.ximucredit.dragon.service;

import java.util.List;

import com.qq.weixin.mp.aes.WechatUser;

public interface QYMessageService {
	public String getToken();
	public WechatUser getQYUser(String code);
	public String getCharRoomUserList(String chatId);
	public boolean sendGroupMsg(String chatId,String sendUserId,String msg);
	public boolean createChatRoom(String chatId,String name,String ownerId,List<String> userList); 
	public boolean updateChatRoom(String chatId,String opUserId,String name,List<String> addUserList,List<String> delUserList);
}
