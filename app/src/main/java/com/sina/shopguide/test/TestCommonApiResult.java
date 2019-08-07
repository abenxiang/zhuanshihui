package com.sina.shopguide.test;

import com.sina.shopguide.net.result.BaseApiResult;

/**
 * 
 * @author SinaDev
 * 
 * @param <T>
 */
public class TestCommonApiResult<T> extends BaseApiResult {

	private static final long serialVersionUID = -205003987935413277L;

	private T translateResult;

	public T getTranslateResult() {
		return translateResult;
	}

	public void setTranslateResult(T translateResult) {
		this.translateResult = translateResult;
	}
}
