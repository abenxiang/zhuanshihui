package com.sina.shopguide.net.result;

import java.util.List;

public class ListApiResult<T> extends BaseApiResult {

	private static final long serialVersionUID = 1922598934724068320L;

	private List<T> data;

	private int total;

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

}
