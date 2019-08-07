package com.sina.shopguide.net.requestinterface;

import com.sina.shopguide.dto.Product;
import com.sina.shopguide.net.result.CommonApiResult;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 获取商品详情
 * Created by XiangWei on 18/5/23.
 */

public interface IProductDetailRequest {
    @POST("client/product/detail")
    @FormUrlEncoded
    Call<CommonApiResult<Product>> getCall(@FieldMap Map<String, String> map);
}
