package com.ximucredit.teambition;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
	public static Date parseDate(String teambitionDate) throws ParseException{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd'T'h:m:s.S'Z'");
		Date d=sdf.parse(teambitionDate);
		return d;
	}
}
