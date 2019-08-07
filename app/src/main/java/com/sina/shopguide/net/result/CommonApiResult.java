package com.sina.shopguide.net.result;

/**
 * 
 * @author SinaDev
 * 
 * @param <T>
 */
public class CommonApiResult<T> extends BaseApiResult {

	private static final long serialVersionUID = -205003987935413277L;

	private T data;

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
