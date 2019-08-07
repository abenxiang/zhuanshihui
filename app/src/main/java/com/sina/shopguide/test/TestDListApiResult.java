package com.sina.shopguide.test;

import com.sina.shopguide.net.result.BaseApiResult;

import java.util.List;

public class TestDListApiResult<T> extends BaseApiResult {

	private static final long serialVersionUID = 1922598934724068320L;

	private List<List<T>> translateResult;

	private int total;

	public List<List<T>> getTranslateResult() {
		return translateResult;
	}

	public void setTranslateResult(List<List<T>> translateResult) {
		this.translateResult = translateResult;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

}
