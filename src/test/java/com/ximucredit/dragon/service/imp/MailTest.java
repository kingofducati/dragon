/**
 * 
 */
package com.ximucredit.dragon.service.imp;

import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ximucredit.dragon.ProjectStoreManager;
import com.ximucredit.dragon.service.MailService;

/**
 * @author dux.fangl
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-servlet.xml")
public class MailTest extends AbstractJUnit4SpringContextTests {
	@Resource
	private JavaMailSender mailSender;
	@Resource
	private MailService mailService;
	@Resource
	private ProjectStoreManager projectStoreManager;

	@Test
	public void testMail() {
		Assert.assertNotNull(mailSender);
		
		SimpleMailMessage mailMessage=new SimpleMailMessage();
		mailMessage.setFrom("fang@ximucredit.com");
		mailMessage.setTo("fang@ximucredit.com");
		mailMessage.setText("test mail,say Hello!你好！１２３４５６７．＠＠。");
		mailMessage.setSubject("测试邮件发自管理员");
		
		mailSender.send(mailMessage);
	}
	@Test
	public void testMimeMail(){
		Map<String, Object> values= new HashMap<String, Object>();
		values.put("username", "用户");
		String[] filePaths={"src/test/resources/grid_template.xls"};
		mailService.sendMailByTempelete("fang@ximucredit.com", "来自管理员的测试邮件", "mail-tempelte.vm", values , filePaths);
	}
	
	@Test
	public void testProjectMail(){
		projectStoreManager.loadProjectFromDB("");
		
		projectStoreManager.sendReport("", "项目问题跟进","", "fang@ximucredit.com", "dev_type");
	}

}
