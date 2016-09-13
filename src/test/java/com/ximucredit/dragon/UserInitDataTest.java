package com.ximucredit.dragon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

public class UserInitDataTest {

	@Test
	public void test() throws IOException {
		StringBuilder sb=new StringBuilder();
		
		File file=new File("src/test/resources/user.txt");
		InputStreamReader reader = new InputStreamReader(new FileInputStream(
                file), "UTF-8");
		BufferedReader br = new BufferedReader(reader);
		String line=null;
		while((line=br.readLine())!=null){
			String[] ss=line.split(",");
			if(ss!=null&&ss.length>0){
				String name=ss[0].trim();
				String id=ss[1].trim();
				String email=ss[2].trim();
				String title=ss[3].trim();
				
				String sql=insertSQL(name,id,email,title);
				sb.append(sql).append(";\n");
			}
		}
		
		FileWriter fw=null;
		try {
			File f=new File("target/users.sql");
			if(f.exists()){
				f.delete();
			}
			f.createNewFile();
			
			fw=new FileWriter(f);
			fw.write(sb.toString());
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				if(fw!=null){
					fw.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		br.close();
	}

	private String insertSQL(String name, String id, String email, String title) {
		return "INSERT INTO t_user (user_id, email, name, phone, title) VALUES ('"+id+"','"+email+"','"+name+"','','"+title+"') ON DUPLICATE KEY UPDATE name='"+name+"',email='"+email+"',title='"+title+"'";
	}

}
