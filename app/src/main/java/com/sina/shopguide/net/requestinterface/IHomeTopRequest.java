package com.sina.shopguide.net.requestinterface;

import com.sina.shopguide.dto.GroupListMore;
import com.sina.shopguide.dto.HomeTopInfo;
import com.sina.shopguide.net.result.CommonApiResult;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by XiangWei on 18/7/25.
 */

public interface IHomeTopRequest {
    @POST("client/index/info")
    @FormUrlEncoded
    Call<CommonApiResult<HomeTopInfo>> getHomeTop(@FieldMap Map<String, String> map);
}
