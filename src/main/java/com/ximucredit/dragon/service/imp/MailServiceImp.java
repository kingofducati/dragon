/**
 * 
 */
package com.ximucredit.dragon.service.imp;

import java.io.File;
import java.util.Map;

import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.ximucredit.dragon.service.MailService;

/**
 * @author dux.fangl
 *
 */
public class MailServiceImp implements MailService {
	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private VelocityEngine velocityEngine;

	@Override
	public void sendText(String toEmail, String title, String content) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom("xmadmin@ximucredit.com");
		mailMessage.setSubject(title);
		mailMessage.setText(content);
		mailMessage.setTo(toEmail);
		
		mailSender.send(mailMessage);
	}

	/* (non-Javadoc)
	 * @see com.ximucredit.dragon.service.MailService#sendMailByTempelete(java.lang.String, java.lang.String, java.util.Map, java.lang.String[])
	 */
	@Override
	public void sendMailByTempelete(String toEmail, String title, String tempelete,
			Map<String, Object> values, String[] filePaths) {
		MimeMessage mailMessage = mailSender.createMimeMessage();
		try {
			MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage,true,"utf-8");
			messageHelper.setFrom("xmadmin@ximucredit.com");
			messageHelper.setTo(toEmail);
			messageHelper.setSubject(title);
			
			String content=createContentFromTempelte(tempelete,values);
			messageHelper.setText(content, true);
			
			if(filePaths!=null&&filePaths.length>0){
				for(String filePath:filePaths){
					if(filePath!=null&&filePath.length()>0){
						File file=new File(filePath);
						
						messageHelper.addAttachment(MimeUtility.encodeWord(file.getName()), file); 
					}
				}
			}
			
			mailSender.send(mailMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private String createContentFromTempelte(String tempelete,
			Map<String, Object> values) {
		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, tempelete, "UTF-8",values);
	}

}
