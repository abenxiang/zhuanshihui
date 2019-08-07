package com.sina.shopguide.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sina.shopguide.R;
import com.sina.shopguide.net.RetrofitClient;
import com.sina.shopguide.util.SecretParams;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        request();
    }

    public void request() {

        //步骤4:创建Retrofit对象
        Retrofit retrofit = RetrofitClient.getInstance();

        // 步骤5:创建 网络请求接口 的实例
        PostRequest_Interface request = retrofit.create(PostRequest_Interface.class);
        final TestRequest req = new TestRequest();
        req.setI("I love you");
        Map<String,String> map = req.getParamsMapMd5Env();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println("ttttt  Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }
        //对 发送请求 进行封装(设置需要翻译的内容)
        Call<TestDListApiResult<TranslateResultBean>> call = request.getCall(req.getParamsMap());//"I love you");

        //步骤6:发送网络请求(异步)
        call.enqueue(new Callback<TestDListApiResult<TranslateResultBean>>() {

            //请求成功时回调
            @Override
            public void onResponse(Call<TestDListApiResult<TranslateResultBean>> call, Response<TestDListApiResult<TranslateResultBean>> response) {
                // 步骤7：处理返回的数据结果：输出翻译的内容

                System.out.println("ttttttthhhh"+response.body().getTranslateResult().get(0).get(0).getTgt()
                    +"  jni parame key:"+ SecretParams.getHttpTransMd5Key("1111")+"    source:"+SecretParams.getHttpTransSource());
            }

            //请求失败时回调
            @Override
            public void onFailure(Call<TestDListApiResult<TranslateResultBean>> call, Throwable throwable) {
                System.out.println("请求失败");
                System.out.println(throwable.getMessage());
            }
        });
    }
}
