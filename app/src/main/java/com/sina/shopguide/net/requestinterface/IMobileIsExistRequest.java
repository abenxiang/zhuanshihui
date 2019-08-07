package com.sina.shopguide.net.requestinterface;

import com.sina.shopguide.net.result.CommonApiResult;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by tiger on 18/5/10.
 */

public interface IMobileIsExistRequest {
    @POST("client/user/isexist")
    @FormUrlEncoded
    Call<CommonApiResult<Boolean>> getCall(@FieldMap Map<String, String> map);
}
