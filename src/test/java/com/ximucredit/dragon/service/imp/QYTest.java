/**
 * 
 */
package com.ximucredit.dragon.service.imp;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ximucredit.dragon.DO.DepartDO;
import com.ximucredit.dragon.DO.UserDO;
import com.ximucredit.dragon.service.QYManagerService;

/**
 * @author dux.fangl
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-servlet.xml")
public class QYTest extends AbstractJUnit4SpringContextTests {
	@Resource
	private QYManagerService qyManagerService;

	@Test
	public void testQYDepart() {
		List<DepartDO> list=qyManagerService.queryDepartFromRemote(""+0);
		
		Assert.assertNotNull(list);
		System.out.println("root:count="+list.size());
		
		loadDepart(list);
	}
	
	private void loadDepart(List<DepartDO> list){
		for(DepartDO departDO:list){
//			DepartDO d=qyManagerService.queryDepartByID(departDO.getDepartId());
//			if(d==null){
//				qyManagerService.insertDepart(departDO);
//			}
			
			List<UserDO> us=qyManagerService.queryUserFromRemote(departDO.getDepartId());
			if(us!=null){
				System.out.println("user["+departDO.getDepartId()+"]:count="+us.size());
				
				for(UserDO user:us){
					UserDO u=qyManagerService.findBDUserByEmail(user.getEmail());
					if(u!=null){
						u.setName(user.getName());
						u.setAvatar(user.getAvatar());
						u.setDepartId(user.getDepartId());
						u.setGender(user.getGender());
						u.setQyId(user.getQyId());
						u.setTitle(user.getTitle());
						u.setWeixinId(user.getWeixinId());
						
						qyManagerService.updateBDUser(u);
					}else{
						user.setUserId(UUID.randomUUID().toString().replaceAll("-", ""));
						
						qyManagerService.insertBDUser(user);
					}
				}
			}
			
			List<DepartDO> ll=qyManagerService.queryDepartFromRemote(departDO.getDepartId());
			if(ll!=null){
				System.out.println("depart["+departDO.getDepartId()+"]:count="+ll.size());
				
				loadDepart(ll);
			}
		}
	}
}
