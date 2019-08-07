package com.sina.shopguide.util;

import android.content.Context;
import android.content.SharedPreferences;

public class HistotySearchPreferences {
	public static final String NAME = "history";
	public static final String HISTORY = "hisinfo";
	public static final String SPLITE = "/;;/";
	private static SharedPreferences sharedPreferences = AppUtils
			.getAppContext().getSharedPreferences(NAME, Context.MODE_PRIVATE);

	public static String getHistoryInfo() {
		return sharedPreferences.getString(HISTORY, "");
	}

	public static void setHistoryInfo(String version) {
		sharedPreferences.edit().putString(HISTORY, version).commit();
	}

}
