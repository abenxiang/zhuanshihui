package com.sina.shopguide.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sina.shopguide.R;
import com.sina.shopguide.dto.UserInfo;
import com.sina.shopguide.net.RetrofitClient;
import com.sina.shopguide.net.request.GetUserInfoRequest;
import com.sina.shopguide.net.requestinterface.IGetUserInfoRequest;
import com.sina.shopguide.net.result.CommonApiResult;
import com.sina.shopguide.util.UserPreferences;
import com.sina.shopguide.view.MeCenterView;
import com.sina.shopguide.view.MeHeaderView;
import com.sina.shopguide.view.MeBottomView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MeFragment extends BaseFragment {
	private MeHeaderView vwHeader;
	private MeCenterView vwCenter;
	private MeBottomView vwBottom;

	public static UserInfo userInfo;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_me, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		vwHeader = (MeHeaderView)view.findViewById(R.id.id_me_header);
		vwCenter = (MeCenterView)view.findViewById(R.id.id_me_center);
		vwBottom = (MeBottomView)view.findViewById(R.id.id_me_bottom);
		//adjustUiShow();
	}

	public void adjustUiShow(){
		boolean isLogin = UserPreferences.isLogin();
		if(isLogin){
			vwCenter.setVisibility(View.VISIBLE);
			getUserInfo();
		}else{
			vwCenter.setVisibility(View.GONE);
		}

		vwHeader.adjustView(isLogin);
	}

	@Override
	public void onResume() {
		super.onResume();
		adjustUiShow();
	}

	@Override
	public void reload(String tab) {
	}

	public void updateUi(){
		vwHeader.update(userInfo);
		vwCenter.update(userInfo);
	}

	public void getUserInfo() {
		//步骤1:创建Retrofit对象
		Retrofit retrofit = RetrofitClient.getInstance();

		// 步骤2:创建 网络请求接口 的实例
		IGetUserInfoRequest request = retrofit.create(IGetUserInfoRequest.class);
		final GetUserInfoRequest req = new GetUserInfoRequest();
		req.setUid(UserPreferences.getUserId());
		//对发送请求进行封装
		Call<CommonApiResult<UserInfo>> call = request.getCall(req.getParamsMap());//"I love you");

		//步骤3:发送网络请求(异步)
		call.enqueue(new Callback<CommonApiResult<UserInfo>>() {

			//请求成功时回调
			@Override
			public void onResponse(Call<CommonApiResult<UserInfo>> call, Response<CommonApiResult<UserInfo>> response) {
				if(response.body().getCode()!=100000){
					Toast.makeText(getActivity(),response.body().getMsg(),Toast.LENGTH_SHORT).show();//response.body().getCode());
					return;
				}
				userInfo = response.body().getData();
				updateUi();
			}

			//请求失败时回调
			@Override
			public void onFailure(Call<CommonApiResult<UserInfo>> call, Throwable throwable) {
				System.out.println("请求失败");
				System.out.println(throwable.getMessage());
			}
		});
	}
}
