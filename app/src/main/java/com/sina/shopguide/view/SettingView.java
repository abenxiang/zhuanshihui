package com.sina.shopguide.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.shopguide.R;
import com.sina.shopguide.activity.BindAlipayActivity;
import com.sina.shopguide.activity.BindInvateCodeActivity;
import com.sina.shopguide.activity.BindMobileActivity;
import com.sina.shopguide.activity.ResetPasswordActivity;
import com.sina.shopguide.activity.TextEditSingleActivity;
import com.sina.shopguide.dialog.PhotoOprDialog;
import com.sina.shopguide.dto.ImageInfo;
import com.sina.shopguide.dto.UserInfo;
import com.sina.shopguide.net.RetrofitClient;
import com.sina.shopguide.net.request.ModifyUserRequest;
import com.sina.shopguide.net.request.UploadRequest;
import com.sina.shopguide.net.requestinterface.IModifyUserRequest;
import com.sina.shopguide.net.requestinterface.IUploadRequest;
import com.sina.shopguide.net.result.CommonApiResult;
import com.sina.shopguide.net.result.ErrorCode;
import com.sina.shopguide.net.result.FairyApiResult;
import com.sina.shopguide.util.ActivityUtils;
import com.sina.shopguide.util.ImageLoaderUtils;
import com.sina.shopguide.util.PhotoOpr;
import com.sina.shopguide.util.SimpleDebugPrinterUtils;
import com.sina.shopguide.util.ToastUtils;
import com.sina.shopguide.util.UserPreferences;

import org.apache.commons.lang3.StringUtils;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_OK;

/**
 * Created by tiger on 18/5/7.
 */

public class SettingView extends RelativeLayout implements IUpdate<UserInfo>{

    public static final int REQUESTCODE_NICK = 10001;
    public static final int REQUESTCODE_WEIBO = 10002;
    public static final int REQUESTCODE_WEIXIN = 10003;
    public static final int REQUESTCODE_QQ = 10004;
    public static final int REQUESTCODE_PID = 10005;
    public static final int REQUESTCODE_INVATECODE = 10006;
    public static final int REQUESTCODE_MOBILE = 10007;
    public static final int REQUESTCODE_ALI = 10008;

    public static final int USE_CAMERA = 10009;
    public static final int USE_GALLERY = 10010;

    public static final int CROP_IMAGE = 10011;

    private TopPanelTipView tptvNick;
    private TopPanelTipView tptvInvate;
    private TopPanelTipView tptvMoble;
    private TopPanelTipView tptvWeixin;
    private TopPanelTipView tptvWeibo;
    private TopPanelTipView tptvQQ;
    private TopPanelTipView tptvPid;
    private TopPanelTipView tptvSet;
    private TopPanelTipView tptvZhifubao;

    private TextView tvChangeAvatar;
    private CircleImageView ivAvatar;
    private TextView tvTitle;
    private ImageView ivBack;
    private TextView tvExit;
    private UserInfo userInfo;

    private PhotoOprDialog dlgPhotoOpr;

    private PhotoOpr photoOpr;
    public SettingView(Context context, AttributeSet attrs,
                       int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public SettingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SettingView(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.vw_setting, this);
        tvTitle = (TextView) findViewById(R.id.title_content);
        tvTitle.setText(R.string.main_tab_me_set);

        tvChangeAvatar = (TextView) findViewById(R.id.id_changeAvatar);
        ivAvatar = (CircleImageView) findViewById(R.id.id_avatar);

        tptvInvate = (TopPanelTipView) findViewById(R.id.id_invate_code);
        tptvZhifubao = (TopPanelTipView) findViewById(R.id.id_zhifubao);
        tptvNick = (TopPanelTipView) findViewById(R.id.id_nick);

        tptvMoble = (TopPanelTipView) findViewById(R.id.id_mobile);
        tptvWeixin = (TopPanelTipView) findViewById(R.id.id_weixin);
        tptvWeibo = (TopPanelTipView) findViewById(R.id.id_weibo);
        tptvQQ = (TopPanelTipView) findViewById(R.id.id_qq);
        tptvPid = (TopPanelTipView) findViewById(R.id.id_pid);
        tptvSet = (TopPanelTipView) findViewById(R.id.id_setormodify);
        tvExit = (TextView) findViewById(R.id.id_exit);

        tvChangeAvatar.setOnClickListener(clickListener);
        tptvInvate.setOnClickListener(clickListener);
        tptvZhifubao.setOnClickListener(clickListener);
        tptvNick.setOnClickListener(clickListener);
        tptvMoble.setOnClickListener(clickListener);
        tptvWeixin.setOnClickListener(clickListener);
        tptvWeibo.setOnClickListener(clickListener);
        tptvQQ.setOnClickListener(clickListener);
        tptvPid.setOnClickListener(clickListener);
        tptvSet.setOnClickListener(clickListener);
        tvExit.setOnClickListener(clickListener);
        dlgPhotoOpr = new PhotoOprDialog(getContext(),photoClick);
        photoOpr = new PhotoOpr((Activity) getContext());
    }

    private OnClickListener photoClick = new OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id){
                case R.id.from_take:
                    photoOpr.fromCamera(USE_CAMERA);
                    dlgPhotoOpr.dismiss();
                    break;
                case R.id.from_photo:
                    photoOpr.fromLocal(USE_GALLERY);
                    dlgPhotoOpr.dismiss();
                    break;
            }
        }
    };

    public void update(UserInfo info){
        if(info == null){
            return;
        }
        userInfo = info;
//        if(userInfo.is_partner()){
//            tptvInvate.setTip("已绑定");
//        }
        //tptvInvate.setTip(userInfo.getInvitation_code());
        tptvZhifubao.setTip(userInfo.getAlipay());
        tptvNick.setTip(userInfo.getNick_name());
        tptvMoble.setTip(userInfo.getMobilenum());
        tptvWeixin.setTip(userInfo.getWechat());
        tptvWeibo.setTip(userInfo.getWeibo());
        tptvPid.setTip(userInfo.getAlimama_pid());

        if(StringUtils.isNotEmpty(userInfo.getQq())&& userInfo.getQq().equals("0")){
            userInfo.setQq("");
        }
        tptvQQ.setTip(userInfo.getQq());
        if(StringUtils.isNotEmpty(userInfo.getAvatar_url())) {
            ImageLoader.getInstance().displayImage(userInfo.getAvatar_url(), ivAvatar,
                    ImageLoaderUtils.getDefaultoptions());
        }
    }

    private  OnClickListener clickListener = new OnClickListener(){
        public void onClick(View v){
            int id = v.getId();
            switch (id){
                case R.id.id_changeAvatar:
                    dlgPhotoOpr.show();
                    break;
                case R.id.id_avatar:
                    dlgPhotoOpr.show();
                    break;
                case R.id.id_invate_code:
                    //if(!userInfo.is_partner()) {
                        goPanelSubActivity(BindInvateCodeActivity.class, REQUESTCODE_INVATECODE, userInfo.getInvitation_code(), null);
                    //}
                    break;
                case R.id.id_zhifubao:
                    goPanelSubActivity(BindAlipayActivity.class, REQUESTCODE_ALI, tptvZhifubao.getTip(),null);
                    break;
                case R.id.id_nick:
                    goPanelSubActivity(TextEditSingleActivity.class, REQUESTCODE_NICK, tptvNick.getTip(),"昵称");
                    break;
                case R.id.id_mobile:
                    ActivityUtils.gotoNextActivity(getContext(),BindMobileActivity.class);
                    break;
                case R.id.id_weixin:
                    goPanelSubActivity(TextEditSingleActivity.class, REQUESTCODE_WEIXIN, tptvWeixin.getTip(),"微信");
                    break;
                case R.id.id_weibo:
                    goPanelSubActivity(TextEditSingleActivity.class, REQUESTCODE_WEIBO, tptvWeibo.getTip(),"微博");
                    break;
                case R.id.id_qq:
                    goPanelSubActivity(TextEditSingleActivity.class, REQUESTCODE_QQ, tptvQQ.getTip(),"qq");
                    break;
                case R.id.id_pid:
                    goPanelSubActivity(TextEditSingleActivity.class, REQUESTCODE_PID, tptvPid.getTip(),"pid");
                    break;
                case R.id.id_setormodify:
                    setPassword();
                    break;
                case R.id.id_exit:
                    exit();
                    break;
            }
        }
    };

    private void goPanelSubActivity(Class<?> claz,int requestCode,String info,String title){
        Intent in = new Intent(getContext(),claz);
        in.putExtra(TextEditSingleActivity.BUDLE_EXTA_CONTENT,info);
        in.putExtra(TextEditSingleActivity.BUDLE_EXTA_TITLE,title);
        in.putExtra(TextEditSingleActivity.BUDLE_EXTA_REQUESTCODE,requestCode);
        ((Activity)getContext()).startActivityForResult(in,requestCode);
    }

    private void exit(){
        UserPreferences.removeUserInfo();
        ActivityUtils.backToMainActivity(getContext(), 2);
        ((Activity)getContext()).finish();
    }

    private void setPassword(){
        ActivityUtils.gotoNextActivity(getContext(),ResetPasswordActivity.class);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        String content = null;
        SimpleDebugPrinterUtils.println("gggggg   333 requestCode" + requestCode);
        if(data!=null && data.hasExtra("content")) {
            content = data.getStringExtra("content");
            SimpleDebugPrinterUtils.println("gggggg   333" + content + "  requestCode" + requestCode);
        }
        switch (requestCode) {
            case REQUESTCODE_NICK:
                tptvNick.setTip(content);
                break;
            case REQUESTCODE_ALI:
                tptvZhifubao.setTip(content);
                break;
            case REQUESTCODE_WEIBO:
                tptvWeibo.setTip(content);
                break;

            case REQUESTCODE_WEIXIN:
                tptvWeixin.setTip(content);
                break;

            case REQUESTCODE_QQ:
                tptvQQ.setTip(content);
                break;

            case REQUESTCODE_PID:
                tptvPid.setTip(content);
                break;

            case REQUESTCODE_INVATECODE:
                break;

            case USE_CAMERA:
                photoOpr.doCrop(false);
                break;

            case USE_GALLERY:
                photoOpr.doByPhotoLocal(data);
                break;

            case CROP_IMAGE:
                String path = data.getStringExtra("image-path");
                if (path == null) {
                    return;
                }

                if(photoOpr.getOutputFile()!=null ){
                    ivAvatar.setImageBitmap(BitmapFactory
                            .decodeFile(photoOpr.getOutputFile().getPath()));
                    uploadImg(photoOpr.getOutputFile());//.getPath());
                }
                break;
        }
    }

    private void uploadImg(File file){
        IUploadRequest request = RetrofitClient.getInstance().create(IUploadRequest.class);

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        String fileNameByTimeStamp = System.currentTimeMillis() + ".jpg";
        MultipartBody.Part body = MultipartBody.Part.createFormData("img", fileNameByTimeStamp, requestBody);
        UploadRequest req = new UploadRequest();

        Call<CommonApiResult<ImageInfo>> call = request.getCall(req.getParamsMap(),body);//.updateInfo(msg, requestBody );
        call.enqueue(new Callback<CommonApiResult<ImageInfo>>() {
            @Override
            public void onResponse(Call<CommonApiResult<ImageInfo>> call, retrofit2.Response<CommonApiResult<ImageInfo>> response) {

                if(response.body().getCode()!= ErrorCode.SUCCESS.getCode()){
                    ToastUtils.showToast(getContext(),response.body().getMsg());
                    return;
                }
                modifyUser(response.body().getData().getImg());
            }

            @Override
            public void onFailure(Call<CommonApiResult<ImageInfo>> call, Throwable t) {
                //。。。
            }
        });
    }

    private void modifyUser(String url){
        Retrofit retrofit = RetrofitClient.getInstance();
        IModifyUserRequest request = retrofit.create(IModifyUserRequest.class);
        ModifyUserRequest req = new ModifyUserRequest();
        req.setAvatarUrl(url);

        Call<FairyApiResult> call = request.getCall(req.getParamsMap());
        call.enqueue(new Callback<FairyApiResult>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<FairyApiResult> call, Response<FairyApiResult> response) {
                if(response.body().getCode()!= 100000){
                    ToastUtils.showToast(getContext(),response.body().getMsg());
                }
            }

            //请求失败时回调
            @Override
            public void onFailure(Call<FairyApiResult> call, Throwable throwable) {
                SimpleDebugPrinterUtils.println(throwable.getMessage());
            }
        });
    }
}
