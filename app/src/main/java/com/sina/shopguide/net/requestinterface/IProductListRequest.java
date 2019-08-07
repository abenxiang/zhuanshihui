package com.sina.shopguide.net.requestinterface;

import com.sina.shopguide.dto.LoginResult;
import com.sina.shopguide.dto.Product;
import com.sina.shopguide.net.result.CommonApiResult;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by XiangWei on 18/5/10.
 */

public interface IProductListRequest {
    @POST("client/product/product")
    @FormUrlEncoded
    Call<CommonApiResult<List<Product>>> getCall(@FieldMap Map<String, String> map);
}
