package com.sina.shopguide.net.requestinterface;

import com.sina.shopguide.dto.ZhuantiDetailMore;
import com.sina.shopguide.net.result.CommonApiResult;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IZhuantiDetailRequest {
    @POST("client/topic/info")
    @FormUrlEncoded
    Call<CommonApiResult<ZhuantiDetailMore>> getCall(@FieldMap Map<String, String> map);
}
