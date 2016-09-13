package com.ximucredit.dragon;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Date;

import org.junit.Test;

import com.ximucredit.dragon.util.Utils;
import com.ximucredit.teambition.Util;

public class UtilTest {

	@Test
	public void testParse() {
		try {
			Date d=Util.parseDate("2016-05-31T04:06:36.532Z");
			System.out.println(d);
			
			double s=1.23423543645647536799855;
			System.out.println(Utils.accuracy(s, 2));
			System.out.println(Utils.accuracy(s, 4));
			System.out.println(Utils.accuracy(s, 6));
			
			System.out.println(Utils.accuracyTxt(s, 2));
			System.out.println(Utils.accuracyTxt(s, 4));
			System.out.println(Utils.accuracyTxt(s, 6));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}
