package com.sina.shopguide.net.requestinterface;

import com.sina.shopguide.dto.Product;
import com.sina.shopguide.net.result.CommonApiResult;
import com.sina.shopguide.net.result.FairyApiResult;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by XiangWei on 18/6/1.
 */

public interface ISuggestBack {
    @POST("client/suggest/suggestback")
    @FormUrlEncoded
    Call<FairyApiResult> getCall(@FieldMap Map<String, String> map);
}
