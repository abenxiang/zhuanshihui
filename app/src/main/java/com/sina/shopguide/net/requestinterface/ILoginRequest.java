package com.sina.shopguide.net.requestinterface;

import com.sina.shopguide.dto.LoginResult;
import com.sina.shopguide.net.result.CommonApiResult;
import com.sina.shopguide.test.TestDListApiResult;
import com.sina.shopguide.test.TranslateResultBean;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by tiger on 18/5/10.
 */

public interface ILoginRequest {
    @POST("client/user/login")
    @FormUrlEncoded
    Call<CommonApiResult<LoginResult>> getCall(@FieldMap Map<String, String> map);
}
