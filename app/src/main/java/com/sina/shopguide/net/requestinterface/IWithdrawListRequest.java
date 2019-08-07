package com.sina.shopguide.net.requestinterface;

import com.sina.shopguide.dto.WithDrawItem;
import com.sina.shopguide.net.result.FairyApiResult;
import com.sina.shopguide.net.result.ListApiResult;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by tiger on 18/5/10.
 */

public interface IWithdrawListRequest {
    @POST("client/user/withdrawlist")
    @FormUrlEncoded
    Call<ListApiResult<WithDrawItem>> getCall(@FieldMap Map<String, String> map);
}
