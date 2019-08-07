package com.sina.shopguide.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.shopguide.R;
import com.sina.shopguide.dto.ContactInfo;
import com.sina.shopguide.dto.Group;
import com.sina.shopguide.net.RetrofitClient;
import com.sina.shopguide.net.request.GetContactRequest;
import com.sina.shopguide.net.requestinterface.IGetContactRequest;
import com.sina.shopguide.net.result.CommonApiResult;
import com.sina.shopguide.net.result.ErrorCode;
import com.sina.shopguide.util.ImageLoaderUtils;
import com.sina.shopguide.util.SimpleDebugPrinterUtils;
import com.sina.shopguide.view.CircleImageView;

import org.apache.commons.lang3.StringUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MemberInfoDlg extends Dialog {

	private TextView tvWeixin;
	private TextView tvWeibo;
	private TextView tvQQ;
	private TextView tvName;

	private CircleImageView civAvatar;
	private ImageView ivClose;
	private View.OnClickListener mItemClick;

	private Group group;

	public MemberInfoDlg(Context context, boolean cancelable,
                         OnCancelListener cancelListener, View.OnClickListener itemsOnClick) {
		super(context, cancelable, cancelListener);
		mItemClick = itemsOnClick;
	}

	public MemberInfoDlg(Context context, int theme, View.OnClickListener itemsOnClick) {
		super(context, theme);
		mItemClick = itemsOnClick;
	}

	public MemberInfoDlg(Context context, View.OnClickListener itemsOnClick) {
		super(context, R.style.FullScreenDialogTheme);
		mItemClick = itemsOnClick;
	}

	public MemberInfoDlg(Context context, View.OnClickListener itemsOnClick, Object tip) {
		super(context, R.style.FullScreenDialogTheme);
		mItemClick = itemsOnClick;
	}

	public MemberInfoDlg(Context context, Group grp) {
		super(context, R.style.FullScreenDialogTheme);
		group = grp;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dlg_member_info);

		getWindow().setLayout(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);

		tvWeixin = (TextView) findViewById(R.id.id_dlg_weixin);
		tvWeibo = (TextView) findViewById(R.id.id_dlg_weibo);
		tvQQ = (TextView)findViewById(R.id.id_dlg_qq);
		tvName = (TextView)findViewById(R.id.id_dlg_name);

		civAvatar = (CircleImageView)findViewById(R.id.id_dlg_icon);
		ivClose = (ImageView)findViewById(R.id.id_dlg_close);

		// 取消按钮
		ivClose.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// 销毁弹出框
				dismiss();
			}
		});

		if(group !=null){
			tvName.setText(group.getNick_name());
			if(StringUtils.isNotEmpty(group.getAvatar_url())) {
				ImageLoader.getInstance().displayImage(group.getAvatar_url(), civAvatar,
						ImageLoaderUtils.getDefaultoptions());
			}
		}

	}

	private void getContact(){
		Retrofit retrofit = RetrofitClient.getInstance();
		IGetContactRequest request = retrofit.create(IGetContactRequest.class);
		final GetContactRequest req = new GetContactRequest();
		req.setUid(group.getUid());
		Call<CommonApiResult<ContactInfo>> call = request.getCall(req.getParamsMapMd5Env());

		call.enqueue(new Callback<CommonApiResult<ContactInfo>>() {
			//请求成功时回调
			@Override
			public void onResponse(Call<CommonApiResult<ContactInfo>> call, Response<CommonApiResult<ContactInfo>> response) {
				if(response.body().getCode()!= ErrorCode.SUCCESS.getCode()){
					//ToastUtils.showToast(getContext(),"绑定成功！");
					return;
				}

				updateUi(response.body().getData());
			}

			//请求失败时回调
			@Override
			public void onFailure(Call<CommonApiResult<ContactInfo>> call, Throwable throwable) {
				SimpleDebugPrinterUtils.println(throwable.getMessage());
			}
		});
	}

	private void updateUi(ContactInfo info){
		tvWeixin.setText("微信："+info.getWechat());
		tvWeibo.setText("微博："+info.getWeibo());
		tvQQ.setText("QQ："+info.getQq());
	}
}
