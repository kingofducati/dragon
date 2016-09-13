package com.ximucredit.dragon.service.imp;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.ximucredit.dragon.service.imp.QYMessageServiceImp.QY;

public class WeiXinServiceImpTest {

	@Test
	public void testCreateChatRoom() {
		QYMessageServiceImp imp=new QYMessageServiceImp();
		
		List<String> userList=new ArrayList<String>();
		userList.add("fang@ximucredit.com");
		userList.add("rong.chi@ximucredit.com");
		userList.add("lanlan.liu@ximucredit.com");
		
		imp.createChatRoom("2", "测试聊天室", "rong.chi@ximucredit.com", userList);
//		imp.quitChatRoom("1", "rong.chi@ximucredit.com");
	}

}
