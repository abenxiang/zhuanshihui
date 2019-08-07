package com.sina.shopguide.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.shopguide.R;
import com.sina.shopguide.dto.UploadImageItem;

import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * Created by XiangWei on 18/5/31.
 */

public class FeedbackPicItemView extends FrameLayout implements IUpdate<String> {

    private ImageView imageView;

    private String imagePath;

    private DisplayImageOptions options;

    public FeedbackPicItemView(@NonNull Context context) {
        super(context);

        init();
    }

    public FeedbackPicItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public FeedbackPicItemView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    public FeedbackPicItemView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init();
    }

    private void init() {
        inflate(getContext(), R.layout.vw_feedback_pic_item, this);

        imageView = (ImageView) findViewById(R.id.image_preview);

        options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.ic_default_avatar)
                .showImageForEmptyUri(R.drawable.ic_default_avatar).cacheInMemory(false)
                .cacheOnDisk(false).showImageOnFail(R.drawable.ic_default_avatar).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    @Override
    public void update(String imagePath) {
        if (imagePath == null) {
            return;
        }

        this.imagePath = imagePath;
        if (StringUtils.isNotEmpty(imagePath)) {
            ImageLoader.getInstance().displayImage(
                    Uri.decode(Uri.fromFile(new File(imagePath)).toString()), imageView,
                    options);
        }

    }
}
