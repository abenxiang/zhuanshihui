package com.sina.shopguide.dialog;

import com.sina.shopguide.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

public class PhotoOprDialog extends Dialog {
	private TextView mTake;
	private TextView mPhoto;
	private Button mCancel;
	@SuppressWarnings("unused")
	private TextView mTitle;
	private View.OnClickListener mItemClick;
	public PhotoOprDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener,View.OnClickListener itemsOnClick) {
		super(context, cancelable, cancelListener);
		mItemClick = itemsOnClick;
	}

	public PhotoOprDialog(Context context, int theme,View.OnClickListener itemsOnClick) {
		super(context, theme);
		mItemClick = itemsOnClick;
	}

	public PhotoOprDialog(Context context,View.OnClickListener itemsOnClick) {
		super(context, R.style.FullScreenDialogTheme);
		mItemClick = itemsOnClick;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dlg_photo_opr);

		getWindow().setLayout(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		mTake = (TextView) findViewById(R.id.from_take);
		mPhoto = (TextView) findViewById(R.id.from_photo);
		mCancel = (Button) findViewById(R.id.pop_cancel);
		mTitle = (TextView) findViewById(R.id.title);
		if(mItemClick!=null){
			mTake.setOnClickListener(mItemClick);
			mPhoto.setOnClickListener(mItemClick);
		}
		// 取消按钮
		mCancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// 销毁弹出框
				dismiss();
			}
		});
	}
	

}
