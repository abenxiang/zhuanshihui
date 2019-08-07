package com.sina.shopguide.net.requestinterface;

import com.sina.shopguide.net.result.FairyApiResult;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by tiger on 18/5/10.
 */

public interface IWithdrawRequest {
    @POST("client/user/withdraw")
    @FormUrlEncoded
    Call<FairyApiResult> getCall(@FieldMap Map<String, String> map);
}
