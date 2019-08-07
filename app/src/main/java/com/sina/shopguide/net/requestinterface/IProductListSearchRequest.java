package com.sina.shopguide.net.requestinterface;

import com.sina.shopguide.dto.SearchProduct;
import com.sina.shopguide.net.result.CommonApiResult;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IProductListSearchRequest {
    @POST("client/product/search")
    @FormUrlEncoded
    Call<CommonApiResult<SearchProduct>> getCall(@FieldMap Map<String, String> map);
}
