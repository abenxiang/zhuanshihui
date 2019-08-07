package com.sina.shopguide.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sina.shopguide.R;
import com.sina.shopguide.dialog.UploadPicDialog;
import com.sina.shopguide.dto.ImageInfo;
import com.sina.shopguide.dto.UploadImageItem;
import com.sina.shopguide.net.RetrofitClient;
import com.sina.shopguide.net.request.SuggestBackParams;
import com.sina.shopguide.net.request.UploadRequest;
import com.sina.shopguide.net.requestinterface.ISuggestBack;
import com.sina.shopguide.net.requestinterface.IUploadRequest;
import com.sina.shopguide.net.result.CommonApiResult;
import com.sina.shopguide.net.result.ErrorCode;
import com.sina.shopguide.net.result.FairyApiResult;
import com.sina.shopguide.util.AppUtils;
import com.sina.shopguide.util.BitmapHelper;
import com.sina.shopguide.util.FileUtils;
import com.sina.shopguide.util.ToastUtils;
import com.sina.shopguide.view.FeedBackPicGridView;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedBackActivity extends BaseActivity implements View.OnClickListener {

    public static final int REQ_CODE_USE_CAMERA = 0;

    public static final int REQ_CODE_USE_GALLERY = 1;

    private static final int CONTENT_LEN3 = 300;

    private List<UploadImageItem> itemImages = new ArrayList<>();

    protected UploadPicDialog upDlg;

    private FeedBackPicGridView imageGridView;

    private ImageView ivException;
    private ImageView ivOthers;
    private EditText etContent;
    private TextView tvCommit;
    private RelativeLayout rvException;
    private RelativeLayout rvOthers;
    private TextView tvContent;

    private int  iType = 1; // 1:功能异常；2:其他问题

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_feedback);
        upDlg = new UploadPicDialog(this);

        setTitle("我要反馈");
        this.findViewById(R.id.title_right_opr).setVisibility(View.GONE);

        ivException = (ImageView) findViewById(R.id.exception_sel);
        ivOthers = (ImageView) findViewById(R.id.other_sel);

        rvException = (RelativeLayout) findViewById(R.id.type_rv);
        rvOthers = (RelativeLayout) findViewById(R.id.reason_rv);

        etContent = (EditText) findViewById(R.id.annouce_content);
        tvCommit = (TextView) findViewById(R.id.drawback_commit);
        tvContent = (TextView) findViewById(R.id.annouce_content_count);
        imageGridView = (FeedBackPicGridView) findViewById(R.id.v_pics);
        rvException.setOnClickListener(this);
        rvOthers.setOnClickListener(this);
        tvCommit.setOnClickListener(this);
        findViewById(R.id.title_left_button).setOnClickListener(this);

        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= CONTENT_LEN3) {
                    s.delete(CONTENT_LEN3, s.length());
                }

                tvContent.setText(String.format(getResources().getString(R.string.input_remain3),
                        CONTENT_LEN3 - s.length()));
            }
        });

        tvContent.setText(String.format(getResources().getString(R.string.input_remain3), 300));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageGridView.onActivityResult(requestCode, resultCode, data);
    }

    private void setException() {

        ivException.setTag("1");
        ivException.setImageResource(R.drawable.ic_supply_sel);

        ivOthers.setTag("0");
        ivOthers.setImageResource(R.drawable.ic_supply_nosel);

        iType = 1;
    }

    private void setOthers() {
        ivException.setTag("0");
        ivException.setImageResource(R.drawable.ic_supply_nosel);

        ivOthers.setTag("1");
        ivOthers.setImageResource(R.drawable.ic_supply_sel);

        iType = 2;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.type_rv:
                if (ivException.getTag().toString().equals("0")) {
                    setException();

                } else {
                    setOthers();
                }
                break;
            case R.id.reason_rv:
                if (ivOthers.getTag().toString().equals("0")) {
                    setOthers();
                } else {
                    setException();
                }
                break;
            case R.id.drawback_commit:
                asyncSubmit();
                break;

            case R.id.title_left_button:
                finish();
                break;
        }
    }

    private void asyncSubmit() {
        itemImages.clear();
        for (String imagePath: imageGridView.getImageList()) {
            UploadImageItem item = new UploadImageItem();
            item.setImagePath(imagePath);
            itemImages.add(item);
        }

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                uploadImages();
                submit();
                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (upDlg != null) {
                    upDlg.show();
                }
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (upDlg != null) {
                    upDlg.dismiss();
                }
            }


        }.execute();
    }

    private void fitImageToScreenSize(final UploadImageItem uploadImageItem) {
        String imagePath = uploadImageItem.getImagePath();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        Bitmap bmp = BitmapHelper.getBitmapFromFile(imagePath, dm.widthPixels, dm.heightPixels, false);
        try {
            String newPath = imagePath + ".fit";
            bmp.compress(Bitmap.CompressFormat.JPEG, 75, new FileOutputStream(newPath));
            uploadImageItem.setImagePath(newPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void uploadImage(final UploadImageItem uploadImageItem, final Runnable callOnFinish){
        IUploadRequest request = RetrofitClient.getInstance().create(IUploadRequest.class);

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),
                new File(uploadImageItem.getImagePath()));
        String fileNameByTimeStamp = System.currentTimeMillis() + ".jpg";
        MultipartBody.Part body = MultipartBody.Part.createFormData("img", fileNameByTimeStamp, requestBody);
        UploadRequest req = new UploadRequest();

        Call<CommonApiResult<ImageInfo>> call = request.getCall(req.getParamsMap(),body);//.updateInfo(msg, requestBody );
        call.enqueue(new Callback<CommonApiResult<ImageInfo>>() {
            @Override
            public void onResponse(Call<CommonApiResult<ImageInfo>> call, retrofit2.Response<CommonApiResult<ImageInfo>> response) {

                if(response.body().getCode()!= ErrorCode.SUCCESS.getCode()){
                    ToastUtils.showToast(getApplicationContext(),response.body().getMsg());
                    return;
                }
                uploadImageItem.setImageUrl(response.body().getData().getImg());

                onFinish();
            }

            @Override
            public void onFailure(Call<CommonApiResult<ImageInfo>> call, Throwable t) {
                onFinish();
            }

            private void onFinish() {
                if(callOnFinish != null) {
                    callOnFinish.run();
                }
            }
        });
    }

    private void uploadImages() {
        if(itemImages == null || itemImages.isEmpty()) {
            return;
        }

        final CountDownLatch latch = new CountDownLatch(itemImages.size());
        for (UploadImageItem uploadImageItem: itemImages) {
            fitImageToScreenSize(uploadImageItem);
            uploadImage(uploadImageItem, new Runnable() {
                @Override
                public void run() {
                    latch.countDown();
                }
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void submit() {
        ISuggestBack request = RetrofitClient.getInstance().create(ISuggestBack.class);
        SuggestBackParams reqParams = new SuggestBackParams();
        String comment = etContent.getText().toString();
        if (StringUtils.isEmpty(comment)) {
            ToastUtils.showToast(FeedBackActivity.this, R.drawable.ic_toast_failed, "问题和意见不能为空");
            return;
        }
        reqParams.setText(comment);
        reqParams.setVersion(AppUtils.getVersion(getApplicationContext()));
        reqParams.setDeviceVersion(android.os.Build.MODEL);
        reqParams.setOsVersion(android.os.Build.VERSION.RELEASE);
        reqParams.setOtherQuestion(iType);

        List<String> imageUrlList = new ArrayList<>();
        for (UploadImageItem item: itemImages) {
            imageUrlList.add(item.getImageUrl());
        }
        String imageUrls = StringUtils.join(imageUrlList, "|");
        if(StringUtils.isNotEmpty(imageUrls)) {
            reqParams.setImgPath(imageUrls);
        }

        Call<FairyApiResult> call = request.getCall(reqParams.getParamsMapMd5Env());
        call.enqueue(new Callback<FairyApiResult>() {
            @Override
            public void onResponse(Call<FairyApiResult> call, Response<FairyApiResult> response) {
                if(response.body().getCode()!= ErrorCode.SUCCESS.getCode()){
                    ToastUtils.showToast(getApplicationContext(),response.body().getMsg());
                    return;
                }

                finish();
                ToastUtils.showToast(getApplicationContext(), 0, "反馈上报成功");
            }

            @Override
            public void onFailure(Call<FairyApiResult> call, Throwable t) {
                ToastUtils.showToast(getApplicationContext(), 0, "反馈上报失败");
            }
        });
    }
}
