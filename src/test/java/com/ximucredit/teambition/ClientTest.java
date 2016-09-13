package com.ximucredit.teambition;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class ClientTest {

	@Test
	public void testLoad() {
		Client client=new Client("9d009ff0-39b0-11e6-8ed7-e3410151ca80","a0c4f1f1-0cf7-4483-b230-7a5499247eb5", "http://pm.ximucredit.com:8080/dragon/view");
		try{
			System.out.println(client.getAuthorizeURI());
			client.loadToken("yaQVYjIrCCVsXSkTVB7wMD");
//			client.setAccess_token("wnu4y1aUvV8RFix17FhDooU2CiE=gCm3GiY5e3623f323e2e2ac0c2a006641ffe05935a33e28b96ef7c5ed0e99318fe9768c351e25e30c675612c1dc318f735eb1e0533e6d7333a27a4ee51aa9c77d178d57491ecde7ead5f348e9ef12a0554aae4fa9283977ac142bf51d81cf61e4e728f4b8a4860f64175f2509a1f48aba217cd29");
//			client.loadProjects(client.getAccess_token());
			
//			List<Project> ps=client.getProjects();
//			for(Project p:ps){
//				System.out.print("project name:"+p.getProjectName()+"  Tasks:");
//				List<Task> ts=p.getTasks();
//				for(Task t:ts){
//					System.out.print(t.getContent()+",");
//				}
//				System.out.println();
//			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
