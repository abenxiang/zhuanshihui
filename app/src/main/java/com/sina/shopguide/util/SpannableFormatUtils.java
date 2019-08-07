package com.sina.shopguide.util;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

public class SpannableFormatUtils {

  //列表里利润显示内容及格式 店铺首页列表
  public static SpannableString toEarnPriceSpanable(float profitprice) {
		
		int fullprice = (int)(profitprice*100);
		String mainprice = String.valueOf(fullprice/100);
		String partprice = String.valueOf(fullprice%100);
		if(partprice.length()==1){
			partprice ="0"+partprice;
		}
		
		String price = "¥" + mainprice+"." + partprice;
		int index = mainprice.length();
		SpannableString spanPrice = new SpannableString(price);
		
		spanPrice.setSpan(new AbsoluteSizeSpan(18, true), 
				1,1+index,
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);	
		
		return spanPrice;
	}
  
  	//列表里利润显示内容及格式 搜索和首场列表
	public static SpannableString toEarnPriceSpanableForPd(float profitprice) {
		
		int fullprice = (int)(profitprice*100);
		String mainprice = String.valueOf(fullprice/100);
		String partprice = String.valueOf(fullprice%100);
		if(partprice.length()==1){
			partprice ="0"+partprice;
		}
		
		String price = "利润:"+"¥" + mainprice+"." + partprice + "/件";
		int index = mainprice.length();
		SpannableString spanPrice = new SpannableString(price);
		spanPrice.setSpan(new ForegroundColorSpan(Color.parseColor("#db2013")), 
				3,price.length(),
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		spanPrice.setSpan(new AbsoluteSizeSpan(18, true), 
				4,4+index,
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);	
		
		return spanPrice;
	}

	public static SpannableString toEarnPriceNoTitleSpanableForPd(float profitprice) {

		int fullprice = (int)(profitprice*100);
		String mainprice = String.valueOf(fullprice/100);
		String partprice = String.valueOf(fullprice%100);
		if(partprice.length()==1){
			partprice ="0"+partprice;
		}

		String price = "¥" + mainprice+"." + partprice + "";
		int index = mainprice.length();
		SpannableString spanPrice = new SpannableString(price);
		spanPrice.setSpan(new ForegroundColorSpan(Color.parseColor("#db2013")),
				0,price.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		spanPrice.setSpan(new AbsoluteSizeSpan(18, true),
				1,1+index,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		return spanPrice;
	}

	//列表微卖价格显示内容及格式 
	public static SpannableString toWeimaiPriceSpanable(float sprice,String ucount) {
		
		int fullprice = (int)(sprice*100);
		String mainprice = String.valueOf(fullprice/100);
		String partprice = String.valueOf(fullprice%100);
		if(partprice.length()==1){
			partprice="0"+partprice;
		}
		String fsprice = mainprice+"."+partprice;
		String price = "¥" + fsprice+" " + ucount + "人正在分销";
		int index = fsprice.length()+1;
		SpannableString spanPrice = new SpannableString(price);
		spanPrice.setSpan(new ForegroundColorSpan(Color.parseColor("#db2013")), 
				index, 
				index+ucount.length()+1,
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			
		
		return spanPrice;
	}


	//确认订单里合计的金额
	public static SpannableString toComfirmPriceSpanableForPd(float profitprice) {

		int fullprice = (int)(profitprice*100);
		String mainprice = String.valueOf(fullprice/100);
		String partprice = String.valueOf(fullprice%100);
		if(partprice.length()==1){
			partprice ="0"+partprice;
		}

		String price = "合计："+"¥" + mainprice+"." + partprice;
		int index = mainprice.length();
		SpannableString spanPrice = new SpannableString(price);
		spanPrice.setSpan(new ForegroundColorSpan(Color.parseColor("#db2013")),
				3,price.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		spanPrice.setSpan(new AbsoluteSizeSpan(18, true),
				4,4+index,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		return spanPrice;
	}

	//确认订单里合计的金额
	public static SpannableString toComfirmPriceSpanableForPdHandr(int fullprice) {
		String mainprice = String.valueOf(fullprice/100);
		String partprice = String.valueOf(fullprice%100);
		if(partprice.length()==1){
			partprice ="0"+partprice;
		}

		String price = "合计："+"¥" + mainprice+"." + partprice;
		int index = mainprice.length();
		SpannableString spanPrice = new SpannableString(price);
		spanPrice.setSpan(new ForegroundColorSpan(Color.parseColor("#db2013")),
				3,price.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		spanPrice.setSpan(new AbsoluteSizeSpan(18, true),
				4,4+index,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		return spanPrice;
	}

	//列表微卖价格显示内容及格式
	public static SpannableString toSellerCountSpanable(String ucount) {

		String price = ucount + "人正在分销";
		SpannableString spanPrice = new SpannableString(price);
		spanPrice.setSpan(new ForegroundColorSpan(Color.parseColor("#db2013")),
				0,
				ucount.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


		return spanPrice;
	}

}
