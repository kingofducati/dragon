/**
 * 
 */
package com.ximucredit.dragon;

import java.util.Properties;

import net.sf.ehcache.event.CacheEventListener;
import net.sf.ehcache.event.CacheEventListenerFactory;

/**
 * @author dux.fangl
 *
 */
public class ProjectCacheEventListenerFactory extends CacheEventListenerFactory {
	@Override
	public CacheEventListener createCacheEventListener(Properties properties) {
		return (CacheEventListener)SpringContextHelper.getBean("projectCacheEventListener");
	}
}
