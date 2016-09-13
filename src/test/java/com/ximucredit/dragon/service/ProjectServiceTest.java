/**
 * 
 */
package com.ximucredit.dragon.service;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ximucredit.dragon.DO.ProjectDO;

/**
 * @author dux.fangl
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
//配置了@ContextConfiguration注解并使用该注解的locations属性指明spring和配置文件之后，
@ContextConfiguration(locations = {"classpath:spring-servlet.xml" })
public class ProjectServiceTest {
	@Autowired
	private ProjectService projectService;
	
	@Test
	public void testAdd() {
		ProjectDO projectDO=new ProjectDO();
		projectDO.setProjectId("sfsdfsdgsdfsdfsdfs");
		projectDO.setDeployTime("2007-01-01");
		projectDO.setGmtCreate(new Date());
		projectDO.setGmtModify(new Date());
		projectDO.setNowTaskID("sdegrgrgfvergwefwgvg");
		projectDO.setRemark("qer4tthnhpksbmthbno");
		
		projectService.insertProject(projectDO);
	}

}
