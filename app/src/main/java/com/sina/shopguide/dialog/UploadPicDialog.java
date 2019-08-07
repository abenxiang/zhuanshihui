package com.sina.shopguide.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.sina.shopguide.R;

public class UploadPicDialog extends Dialog {

	@SuppressWarnings("unused")
	private View.OnClickListener mItemClick;
	public UploadPicDialog(Context context, boolean cancelable,
                           OnCancelListener cancelListener, View.OnClickListener itemsOnClick) {
		super(context, cancelable, cancelListener);
		mItemClick = itemsOnClick;
	}

	public UploadPicDialog(Context context, int theme, View.OnClickListener itemsOnClick) {
		super(context, theme);
		mItemClick = itemsOnClick;
	}

	public UploadPicDialog(Context context) {
		super(context, R.style.NodimDialogTheme);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vw_loading_pic);
	    getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//
//		getWindow().setLayout(DensityUtil.dip2px(getContext(), 170),
//				DensityUtil.dip2px(getContext(), 101));
		
		
	}
	
}
