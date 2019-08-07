package com.sina.shopguide.util;

public class SimpleDebugPrinterUtils {
	public static final boolean isDebug = false;
	public static void println(String str){
		if(!isDebug){
			return;
		}

		System.out.println(str);
	}
}
