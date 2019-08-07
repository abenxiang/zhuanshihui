package com.sina.shopguide.net.requestinterface;

import com.sina.shopguide.dto.GroupListMore;
import com.sina.shopguide.net.result.CommonApiResult;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by tiger on 18/5/10.
 */

public interface IGroupListRequest {
    @POST("client/user/subordinate")
    @FormUrlEncoded
    Call<CommonApiResult<GroupListMore>> getCall(@FieldMap Map<String, String> map);
}
