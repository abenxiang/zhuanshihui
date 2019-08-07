package com.sina.shopguide.net.request;

import com.sina.shopguide.net.HttpParam;
import com.sina.shopguide.util.AppConst;
import com.sina.shopguide.util.AppUtils;
import com.sina.shopguide.util.ReflectionUtils;
import com.sina.shopguide.util.SecretParams;
import com.sina.shopguide.util.UserPreferences;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.text.WordUtils;

public abstract class BaseRequestParams {
	private String id;
	private String token;
	@HttpParam("sign_type")
	private String signType = "token";
	private String source = SecretParams.getHttpTransSource();
	private String sign;
	private String platform = AppConst.APP_PLATFORM;
	public String version = AppUtils.getVersion(AppUtils.getAppContext());

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public BaseRequestParams() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	/**
	 * 将参数结构转化为map
	 * @return
	 */
	public Map<String, String> getParamsMapInner() {
		Map<String, String> ret = new HashMap<String, String>();
		Class<? extends BaseRequestParams> clzz = this.getClass();
		getParamsMap(ret, clzz);
		getParamsMap(ret, BaseRequestParams.class);
		return ret;
	}

	private void getParamsMap(Map<String, String> ret,
			Class<? extends BaseRequestParams> clzz) {
		try {
			for (Field field : clzz.getDeclaredFields()) {
				String fieldName = field.getName();
				if (ReflectionUtils.isPropertyExists(clzz, fieldName)) {
					HttpParam param = field.getAnnotation(HttpParam.class);
					String key = param != null ? param.value() : fieldName;
					String getMethodName = "get"
							+ WordUtils.capitalize(fieldName);
					Object obj = clzz.getMethod(getMethodName).invoke(this);
					if (obj != null) {
						ret.put(key, obj.toString());
					}
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch(IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException  e) {
			e.printStackTrace();
		}
	}

	public Map<String, String> getParamsMapMd5Env(){
		this.setSource(SecretParams.getHttpTransSource());
		this.setSignType(AppConst.ENCRYPT_MD5);
		final String sign = genSign(this.getParamsMapInner());
		this.setSign(sign);
		return getParamsMapInner();
	}

	public Map<String, String> getParamsMap(){
		this.setSource(SecretParams.getHttpTransSource());
		this.setId(UserPreferences.getUserId());
		this.setToken(UserPreferences.getUserToken());
		return getParamsMapInner();
	}

	public static String genSign(Map<String,String> paramsMap) {
		StringBuilder sb = new StringBuilder();
		Map<String, String> sortedParamsMap = new TreeMap<String, String>(paramsMap);
		for (Map.Entry<String, String> entry : sortedParamsMap.entrySet()) {
			if (entry.getKey().equals("sign_type")) {
				continue;
			}
			if (sb.length() != 0) {
				sb.append("&");
			}
			sb.append(String.format("%s=%s", entry.getKey(), entry.getValue()));
		}

		return SecretParams.getHttpTransMd5Key(sb.toString());

	}
}
