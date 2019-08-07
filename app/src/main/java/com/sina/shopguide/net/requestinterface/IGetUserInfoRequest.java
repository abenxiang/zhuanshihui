package com.sina.shopguide.net.requestinterface;

import com.sina.shopguide.dto.LoginResult;
import com.sina.shopguide.dto.UserInfo;
import com.sina.shopguide.net.result.CommonApiResult;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by tiger on 18/5/10.
 */

public interface IGetUserInfoRequest {
    @POST("client/user/userinfo")
    @FormUrlEncoded
    Call<CommonApiResult<UserInfo>> getCall(@FieldMap Map<String, String> map);
}
