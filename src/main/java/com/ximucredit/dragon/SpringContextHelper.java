/**
 * 
 */
package com.ximucredit.dragon;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author dux.fangl
 *
 */
public class SpringContextHelper implements ApplicationContextAware {
	private static ApplicationContext acx;
	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		acx=applicationContext;
	}
	
	public static Object getBean(String beanName){
		return acx.getBean(beanName);
	}

}
