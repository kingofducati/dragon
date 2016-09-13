package com.ximucredit.dragon.util;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;

public class Utils {
	public static Date paeserDate(String dateString) {
		if(dateString.length()>=10){
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			try {
				return sdf.parse(dateString);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static String formatDate(Date date) {
		if(date!=null){
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			return sdf.format(date);
		}
		
		return null;
	}
	
	public static double proccess(Date startDate,Date endDate){
		long total=endDate.getTime()-startDate.getTime();
		long now=new Date().getTime()-startDate.getTime();
		
		if(total>0&&now>0&&total>=now){
			return now/total;
		}
		
		return 0;
	}
	
	public static double accuracy(double value,int accuracy){
		BigDecimal bd = new BigDecimal(value);   
		return bd.setScale(accuracy,BigDecimal.ROUND_HALF_UP).doubleValue();  
	}
	
	public static String accuracyTxt(double value,int accuracy){
		String dd="#.";
		if(accuracy>0){
			for(int i=0;i<accuracy;i++){
				dd+="#";
			}
		}else{
			dd+="##";
		}
		DecimalFormat df=new DecimalFormat(dd);
		return df.format(value);
	}
	
	public static boolean createReport(String report,String title,String outputFilePath,List<String> headers,List<List<String>> data){
		String reportTempeletePath="grid_template.xls";
		
		if(outputFilePath!=null){
			try {
				InputStream is = Utils.class.getResourceAsStream(reportTempeletePath);
				
				OutputStream os =new FileOutputStream(outputFilePath);
				Context context = new Context();
                context.putVar("headers", headers);
                context.putVar("data", data);
                context.putVar("title", title);
                JxlsHelper.getInstance().processTemplate(is, os, context);
                
                return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
}
