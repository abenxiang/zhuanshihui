package com.sina.shopguide.util;

import org.apache.commons.lang3.StringUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.shopguide.R;


public class ToastUtils {

	private static Toast		currentToast;

	public static Toast showToast(Context context, String msg) {
		return showToast(context, 0, msg);
	}

	public static Toast showToast(Context context, int msgId) {
		return showToast(context, 0, msgId);
	}

	@SuppressLint("InflateParams")
	public static Toast showToast(Context context, int resImgId, String msg) {
		if(StringUtils.isEmpty(msg)) {
			return null;
		}
		
		if(currentToast != null) {
			cancelCurrentToast();
			currentToast = null;
		}

		if(currentToast == null) {
			currentToast = new Toast(context.getApplicationContext());
			
			LayoutInflater inflate = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = inflate.inflate(R.layout.vw_common_toast, null);
			currentToast.setView(v);
		}
		final View v = currentToast.getView();
		final ImageView ivImg = (ImageView) v.findViewById(R.id.iv_pp_img);
		if(resImgId != 0) {
			ivImg.setImageResource(resImgId);
		}
		final TextView tvText = (TextView) v.findViewById(R.id.tv_pp_txt);
		tvText.setText(msg);
		setStyle(currentToast, R.style.Lite_Animation_Toast);
		currentToast.setGravity(Gravity.CENTER, 0, 0);
		currentToast.setDuration(Toast.LENGTH_SHORT);
		currentToast.show();
		return currentToast;
	}

	public static Toast showToast(Context context, int resImgId, int msgId) {
		return showToast(context, resImgId, context.getString(msgId));
	}

	/**
	 * 隐藏当前Toast
	 */
	public static void cancelCurrentToast() {
		Toast curToast = currentToast;
		if(curToast != null) {
			curToast.cancel();
		}
	}
	
	public static void setStyle(Toast toast, int styleId){
		try {
			Object mTN = null;
			mTN = ReflectionUtils.getPrivateProperty(toast, "mTN");
			if (mTN != null) {
				Object mParams = ReflectionUtils.getPrivateProperty(mTN, "mParams");
				if (mParams != null
						&& mParams instanceof WindowManager.LayoutParams) {
					WindowManager.LayoutParams params = (WindowManager.LayoutParams) mParams;
					params.windowAnimations = styleId;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
