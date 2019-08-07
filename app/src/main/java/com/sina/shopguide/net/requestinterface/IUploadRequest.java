package com.sina.shopguide.net.requestinterface;

import com.sina.shopguide.dto.ImageInfo;
import com.sina.shopguide.dto.LoginResult;
import com.sina.shopguide.net.result.CommonApiResult;

import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;

/**
 * Created by tiger on 18/5/10.
 */

public interface IUploadRequest {
    @Multipart
    @POST("/client/tool/upload")
    Call<CommonApiResult<ImageInfo>> getCall(@QueryMap Map<String, String> map, @Part MultipartBody.Part file);

//
//    @POST("/client/tool/upload")
//    @FormUrlEncoded
//    Call<CommonApiResult<ImageInfo>> getCall(@FieldMap Map<String, String> map);
}
