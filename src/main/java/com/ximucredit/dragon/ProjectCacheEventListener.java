/**
 * 
 */
package com.ximucredit.dragon;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.event.CacheEventListener;

/**
 * @author dux.fangl
 *
 */
public class ProjectCacheEventListener implements CacheEventListener {
	private ProjectStoreManager storeManager;
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	public ProjectStoreManager getStoreManager() {
		if(storeManager==null){
			storeManager=(ProjectStoreManager)SpringContextHelper.getBean("storeManager");
		}
		return storeManager;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.ehcache.event.CacheEventListener#notifyElementRemoved(net.sf.ehcache.Ehcache, net.sf.ehcache.Element)
	 */
	public void notifyElementRemoved(Ehcache cache, Element element)
			throws CacheException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see net.sf.ehcache.event.CacheEventListener#notifyElementPut(net.sf.ehcache.Ehcache, net.sf.ehcache.Element)
	 */
	public void notifyElementPut(Ehcache cache, Element element)
			throws CacheException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see net.sf.ehcache.event.CacheEventListener#notifyElementUpdated(net.sf.ehcache.Ehcache, net.sf.ehcache.Element)
	 */
	public void notifyElementUpdated(Ehcache cache, Element element)
			throws CacheException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see net.sf.ehcache.event.CacheEventListener#notifyElementExpired(net.sf.ehcache.Ehcache, net.sf.ehcache.Element)
	 */
	public void notifyElementExpired(Ehcache cache, Element element) {
		cache.remove("allprojects");
		
		getStoreManager().loadProjectFromDB("");
	}

	/* (non-Javadoc)
	 * @see net.sf.ehcache.event.CacheEventListener#notifyElementEvicted(net.sf.ehcache.Ehcache, net.sf.ehcache.Element)
	 */
	public void notifyElementEvicted(Ehcache cache, Element element) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see net.sf.ehcache.event.CacheEventListener#notifyRemoveAll(net.sf.ehcache.Ehcache)
	 */
	public void notifyRemoveAll(Ehcache cache) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see net.sf.ehcache.event.CacheEventListener#dispose()
	 */
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
