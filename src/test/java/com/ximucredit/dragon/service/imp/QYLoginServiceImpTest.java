package com.ximucredit.dragon.service.imp;

import static org.junit.Assert.*;

import org.junit.Test;

public class QYLoginServiceImpTest {

	@Test
	public void test() {
		QYLoginServiceImp service=new QYLoginServiceImp("031MJj9G0Rtoi62h2F5G0MGq9G0MJj9r");
		service.getQYLoginUser();
	}

}
