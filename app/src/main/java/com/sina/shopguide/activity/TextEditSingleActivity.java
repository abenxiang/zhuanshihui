package com.sina.shopguide.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.sina.shopguide.R;
import com.sina.shopguide.net.RetrofitClient;
import com.sina.shopguide.net.request.ModifyUserRequest;
import com.sina.shopguide.net.requestinterface.IModifyUserRequest;
import com.sina.shopguide.net.result.ErrorCode;
import com.sina.shopguide.net.result.FairyApiResult;
import com.sina.shopguide.util.SimpleDebugPrinterUtils;
import com.sina.shopguide.util.ToastUtils;
import com.sina.shopguide.view.SettingView;

import org.apache.commons.lang3.StringUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TextEditSingleActivity extends BaseActivity implements
		OnClickListener {
	public static final String BUDLE_EXTA_CONTENT="CONTENT";
	public static final String BUDLE_EXTA_TITLE="TITLE";
	public static final String BUDLE_EXTA_REQUESTCODE="REQUESTCODE";
	private TextView mSubmit;
	private TextView mTitle2;
	private EditText mContent;
	private String mTitle;
	private String mDefaultContent = "";
	private int requestCode;
	private String modStr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_singleline_textedit);
		Intent in = getIntent();
		if (in != null) {
			mTitle = in.getStringExtra(BUDLE_EXTA_TITLE);
			mDefaultContent = in.getStringExtra(BUDLE_EXTA_CONTENT);
			requestCode = in.getIntExtra(BUDLE_EXTA_REQUESTCODE,0);
		}
		getViews();
	}

	private void getViews() {

		mSubmit = (TextView) findViewById(R.id.id_gobind);

		mContent = (EditText) findViewById(R.id.single_content);
		mContent.setText(mDefaultContent);
		mContent.setSelection(mContent.getText().toString().length());

		mTitle2 = (TextView) findViewById(R.id.id_title2);
		mTitle2.setText(mTitle);

		if(requestCode == SettingView.REQUESTCODE_NICK){
			setTitle(mTitle);
			mSubmit.setText("提交");
		}else{
			setTitle("绑定" + mTitle);
		}
		mContent.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		mSubmit.setOnClickListener(this);
	}
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_gobind:
			modifyUser(mContent.getText().toString());
			break;
		}
	}

	@Override
	public void finish(){
		if(StringUtils.isNotEmpty(modStr)){
			Intent data = new Intent().putExtra("content", modStr);
			setResult(RESULT_OK, data);
		}
		super.finish();
	}

	private void modifyUser(final String content){
		if(StringUtils.isEmpty(content)){
			return;
		}

		ModifyUserRequest req = new ModifyUserRequest();
		switch (requestCode) {
			case SettingView.REQUESTCODE_NICK:
				req.setNickName(content);
				break;

			case SettingView.REQUESTCODE_WEIBO:
				req.setWeibo(content);
				break;

			case SettingView.REQUESTCODE_WEIXIN:
				req.setWechat(content);
				break;

			case SettingView.REQUESTCODE_QQ:
				req.setQq(content);
				break;

			case SettingView.REQUESTCODE_PID:
				req.setAlimamaPid(content);
				break;
		}

		Retrofit retrofit = RetrofitClient.getInstance();
		IModifyUserRequest request = retrofit.create(IModifyUserRequest.class);

		Call<FairyApiResult> call = request.getCall(req.getParamsMap());
		call.enqueue(new Callback<FairyApiResult>() {
			//请求成功时回调
			@Override
			public void onResponse(Call<FairyApiResult> call, Response<FairyApiResult> response) {
				if(response.body().getCode()!= ErrorCode.SUCCESS.getCode()){
					ToastUtils.showToast(TextEditSingleActivity.this,response.body().getMsg());
				}else{
					ToastUtils.showToast(TextEditSingleActivity.this,"修改成功");
					modStr = mContent.getText().toString();
					finish();
				}
			}

			//请求失败时回调
			@Override
			public void onFailure(Call<FairyApiResult> call, Throwable throwable) {
				SimpleDebugPrinterUtils.println(throwable.getMessage());
			}
		});
	}
}
