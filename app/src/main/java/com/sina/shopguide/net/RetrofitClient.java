package com.sina.shopguide.net;

import com.sina.shopguide.util.AppConst;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by tiger on 18/4/25.
 */

public class RetrofitClient {
    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(AppConst.APP_URL) // 设置 网络请求 Url
                .addConverterFactory(GsonConverterFactory.create()) //设置使用Gson解析(记得加入依赖)
            .build();

    public static Retrofit getInstance(){
        return retrofit;
    }

}
