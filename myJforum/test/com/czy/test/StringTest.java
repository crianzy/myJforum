package com.czy.test;

import org.junit.Test;

public class StringTest {

	@Test
	public void testSubString() {
		StringBuffer sba = new StringBuffer("${aa}bb");
		int fIndex = sba.indexOf("${");
		int lIndex = sba.indexOf("}");
		int start = fIndex + 2;
		String varName = sba.substring(start, lIndex);
		System.out.println(varName);

		StringBuffer sbb = new StringBuffer("xxx${ccccc}bb");
		fIndex = sbb.indexOf("${");
		lIndex = sbb.indexOf("}");
		start = fIndex + 2;
		varName = sbb.substring(start, lIndex);
		System.out.println(varName);
		
		StringBuffer sbc = new StringBuffer("${ccccc}bb${aa}b");
		fIndex = sbc.indexOf("${");
		while(fIndex>-1){
			lIndex = sbc.indexOf("}");
			start = fIndex + 2;
			varName = sbc.substring(start,lIndex);
			System.out.println(varName);
			sbc.replace(fIndex, lIndex+1, "----");
			fIndex = sbc.indexOf("${");
		}
		System.out.println(sbc);
		
		
	}
}
