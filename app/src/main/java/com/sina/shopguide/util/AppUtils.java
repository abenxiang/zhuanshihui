package com.sina.shopguide.util;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.sina.shopguide.net.result.BaseApiResult;

public class AppUtils {
	private static final String TAG="ShopGuide";
	private static Context context;

	private final static NumberFormat PRICE_FORNATTER = NumberFormat
			.getNumberInstance();

	private final static DecimalFormat NUMBER_FORNATTER = new DecimalFormat(
			"#0.00");

	private final static Random RANDOM = new Random();

	static {
		PRICE_FORNATTER.setMinimumFractionDigits(2);
		PRICE_FORNATTER.setMaximumFractionDigits(2);
	}

	public static void initAppContext(Context context) {
		AppUtils.context = context.getApplicationContext();
	}

	public static Context getAppContext() {
		return context;
	}

	public static int getSDKVersion() {
		return android.os.Build.VERSION.SDK_INT;
	}
	
	public static String getVersion(Context context) {
		PackageManager packageManager = context.getPackageManager();
		try {
			return packageManager.getPackageInfo(context.getPackageName(), 0).versionName;
		} catch (Exception e) {
			Log.e(TAG, "getVersion error", e);
		}

		return null;
	}

	public static int getVersionCode(Context context) {
		if (context == null) {
			context = AppUtils.context;
		}

		try {
			PackageManager packageManager = context.getPackageManager();
			return packageManager.getPackageInfo(context.getPackageName(), 0).versionCode;
		} catch (Exception e) {
			Log.e(TAG, "getVersion error", e);
		}

		return 0;
	}

	public static int getDefaultHeight(Activity activity) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		Display display = activity.getWindowManager().getDefaultDisplay();
		display.getMetrics(displayMetrics);
		return display.getHeight();
	}


	public static boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(str).matches();
	}

	public static boolean isDouble(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
		return pattern.matcher(str).matches();
	}


	public static String formatBankCard(String bankCard) {
		return String.format("%s %s %s %s", bankCard.substring(0, 4), "****",
				"****", bankCard.substring(12, bankCard.length()));
	}

	public static String formatPhoneNumber(String number) {
		if (TextUtils.isEmpty(number)) {
			return "";
		}
		return String.format("%s%s%s", number.substring(0, 3), "****",
				number.substring(7, number.length()));
	}

	public static String formatPrice(double price) {
		return PRICE_FORNATTER.format(price);
	}

	public static String formatDecimal(double decimal) {
		return NUMBER_FORNATTER.format(decimal);
	}


	public static String getAppDir() {
		return Environment.getExternalStorageDirectory() + File.separator
				+ AppConst.APP_DIR + File.separator;
	}

	public static boolean checkVersion(String last, String current) {
		String[] t1 = last.split("\\.");
		String[] t2 = current.split("\\.");
		String lastVersion = "";
		String currentVersion = "";
		for (String tmp : t1) {
			lastVersion += tmp;
		}
		for (String tmp : t2) {
			currentVersion += tmp;
		}
		return NumberUtils.toInt(lastVersion, -1) > NumberUtils.toInt(currentVersion, -1);
	}

	public static int getRandomNumber() {
		return RANDOM.nextInt(Integer.MAX_VALUE);
	}

	public static boolean isInstalled(String pkg) {
		PackageInfo packageInfo = null;
		try {
			packageInfo = context.getPackageManager().getPackageInfo(pkg, 0);
		} catch (Exception e) {
			packageInfo = null;
		}

		return packageInfo != null;
	}

	public static boolean isQQInstalled() {
		return isInstalled("com.tencent.mobileqq");
	}

	public static String formatAlipay(String alipay) {
		Pattern p = Pattern.compile("^(1)\\d{10}$");
		Matcher m = p.matcher(alipay);
		if (m.matches()) {
			return String.format("%s%s%s", alipay.substring(0, 3), "****",
					alipay.substring(7, alipay.length()));
		}

		String[] tmp = alipay.split("@");
		if (tmp[0].length() <= 3) {
			return alipay;
		}

		return tmp[0].substring(0, 3).concat("***@").concat(tmp[1]);
	}

	public static boolean isLockScreen() {
		KeyguardManager mKeyguardManager = (KeyguardManager) context
				.getSystemService(Context.KEYGUARD_SERVICE);
		return mKeyguardManager.inKeyguardRestrictedInputMode();
	}

	public static void hiddenInputType(View v) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}

	
	/**
	 * 是否为合法的手机号
	 * @param mobileNum
	 * @return
	 */
	public static boolean isMobileNum(String mobileNum) {
		Pattern p = Pattern.compile("^(1)\\d{10}$");
		Matcher m = p.matcher(mobileNum);
		return m.matches();
	}

	public static boolean checkEmail(String email) {
		boolean flag = false;
		try {
			String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	public static int checkNetworkConnection(Context context){
		final ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

		final android.net.NetworkInfo wifi =connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		final android.net.NetworkInfo mobile =connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

		if(wifi.isAvailable()){
			return 1;
		}else if(mobile.isAvailable())  //getState()方法是查询是否连接了数据网络
			return 0;
		else
			return -1;
	}

	public static String getNetworkConnection(Context context){
		int type = checkNetworkConnection(context);
		if(type==1){
			return "wifi";
		}else if(type==0){
			return "gprs";
		}else{
			return "none";
		}
	}

	public static String getErrorMsg(BaseApiResult result, String defStr){
		if(result!=null && StringUtils.isNotEmpty(result.getMsg())){
			return result.getMsg();
		}

		return defStr;
	}
}
