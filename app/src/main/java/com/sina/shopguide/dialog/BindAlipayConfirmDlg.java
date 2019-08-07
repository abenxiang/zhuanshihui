package com.sina.shopguide.dialog;

import com.sina.shopguide.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

public class BindAlipayConfirmDlg extends Dialog {

	private TextView tvCancel;
	private TextView tvConfirm;
	private TextView tvTip;
	private View.OnClickListener mItemClick;
	private String strTip;
	private String strComfirm;
	public BindAlipayConfirmDlg(Context context, boolean cancelable,
			OnCancelListener cancelListener,View.OnClickListener itemsOnClick) {
		super(context, cancelable, cancelListener);
		mItemClick = itemsOnClick;
	}

	public BindAlipayConfirmDlg(Context context, int theme,View.OnClickListener itemsOnClick) {
		super(context, theme);
		mItemClick = itemsOnClick;
	}

	public BindAlipayConfirmDlg(Context context,View.OnClickListener itemsOnClick) {
		super(context, R.style.FullScreenDialogTheme);
		mItemClick = itemsOnClick;
	}

	public BindAlipayConfirmDlg(Context context,View.OnClickListener itemsOnClick,String tip) {
		super(context, R.style.FullScreenDialogTheme);
		strTip = tip;
		mItemClick = itemsOnClick;
	}

	public BindAlipayConfirmDlg(Context context,View.OnClickListener itemsOnClick,String tip,String comfirm) {
		super(context, R.style.FullScreenDialogTheme);
		strTip = tip;
		strComfirm = comfirm;
		mItemClick = itemsOnClick;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dlg_bind_confirm);

		getWindow().setLayout(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);

		tvCancel = (TextView) findViewById(R.id.id_dlg_cancel);
		tvConfirm = (TextView) findViewById(R.id.id_dlg_comfirm);
		tvTip = (TextView)findViewById(R.id.id_dlg_tip);
		if(StringUtils.isNotEmpty(strTip)){
			tvTip.setText(strTip);
		}

		if(StringUtils.isNotEmpty(strComfirm)){
			tvConfirm.setText(strComfirm);
		}

		if(mItemClick!=null)
			tvConfirm.setOnClickListener(mItemClick);
		// 取消按钮
		tvCancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// 销毁弹出框
				dismiss();
			}
		});

	}

	public void setTipContent(String text){
		if(tvTip!=null){
			tvTip.setText(text);
		}
	}

	public void setTipContent(int resid){
		if(tvTip!=null){
			tvTip.setText(resid);
		}
	}

	public void setComfirmContent(String text){
		if(tvConfirm!=null){
			tvConfirm.setText(text);
		}
	}

	public void setComfirmContent(int resid){
		if(tvConfirm!=null){
			tvConfirm.setText(resid);
		}
	}
}
