package com.sina.shopguide.net.result;

import java.util.List;

public class ListMoreApiResult<T> extends BaseApiResult {

	private static final long serialVersionUID = 1922598934724068320L;

	private List<T> lists;

	private int nums1;
	private int nums2;
	private int page;
	private int perpage;

	public List<T> getLists() {
		return lists;
	}

	public void setLists(List<T> lists) {
		this.lists = lists;
	}

	public int getNums1() {
		return nums1;
	}

	public void setNums1(int nums1) {
		this.nums1 = nums1;
	}

	public int getNums2() {
		return nums2;
	}

	public void setNums2(int nums2) {
		this.nums2 = nums2;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPerpage() {
		return perpage;
	}

	public void setPerpage(int perpage) {
		this.perpage = perpage;
	}
}
