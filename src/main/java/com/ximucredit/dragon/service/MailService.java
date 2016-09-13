/**
 * 
 */
package com.ximucredit.dragon.service;

import java.util.Map;

/**
 * @author dux.fangl
 *
 */
public interface MailService {
	public void sendText(String toEmail,String title,String content);
	public void sendMailByTempelete(String toEmail,String title,String tempelete,Map<String, Object> values,String[] filePaths);
}