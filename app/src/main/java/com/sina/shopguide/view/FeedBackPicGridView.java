package com.sina.shopguide.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;

import com.sina.shopguide.R;
import com.sina.shopguide.activity.FeedBackActivity;
import com.sina.shopguide.dialog.PhotoOprDialog;
import com.sina.shopguide.util.FileUtils;
import com.sina.shopguide.util.PhotoOpr;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by XiangWei on 18/5/31.
 */

public class FeedBackPicGridView extends FrameLayout implements View.OnClickListener {

    private GridView gvPics;

    private final List<String> imageList = new ArrayList<>();

    private PicGridAdapter adapter = new PicGridAdapter();

    private PhotoOprDialog photoOprDialog;

    private PhotoOpr photoOpr;

    public List<String> getImageList() {
        return imageList;
    }

    public FeedBackPicGridView(@NonNull Context context) {
        super(context);

        init();
    }

    public FeedBackPicGridView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public FeedBackPicGridView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    public FeedBackPicGridView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init();
    }

    private void init() {
        inflate(getContext(), R.layout.vw_feedback_pic_grid, this);

        photoOprDialog = new PhotoOprDialog(getContext(), this);
        photoOpr = new PhotoOpr((Activity) getContext());

        gvPics = (GridView) findViewById(R.id.gv_pics);
        gvPics.setAdapter(adapter);

        gvPics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position < imageList.size()) {

                }
                else {
                    photoOprDialog.show();
                }
            }
        });
    }

    private static final int VIEW_TYPE_PIC = 0;
    private static final int VIEW_TYPE_CAMERA = 1;
    private static final int VIEW_TYPE_AMOUNT = 2;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.from_take:
                photoOpr.fromCamera(FeedBackActivity.REQ_CODE_USE_CAMERA);
                photoOprDialog.dismiss();
                break;
            case R.id.from_photo:
//                photoOpr.fromLocal(FeedBackActivity.REQ_CODE_USE_GALLERY);
                pickPhoto();
                photoOprDialog.dismiss();
                break;
        }
    }

    private void pickPhoto() {
        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        ((Activity)getContext()).startActivityForResult(i, FeedBackActivity.REQ_CODE_USE_GALLERY);
    }

    private class PicGridAdapter extends BaseAdapter {
        @Override
        public int getItemViewType(int position) {
            if(position < imageList.size()) {
                return VIEW_TYPE_PIC;
            }
            return VIEW_TYPE_CAMERA;
        }

        @Override
        public int getViewTypeCount() {
            return VIEW_TYPE_AMOUNT;
        }

        @Override
        public int getCount() {
            return imageList.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            if(position < imageList.size()) {
                return imageList.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            switch (getItemViewType(position)) {
                case VIEW_TYPE_PIC: {
                    FeedbackPicItemView v = (FeedbackPicItemView) convertView;
                    if(v == null) {
                        v = new FeedbackPicItemView(getContext());
                    }
                    v.update(imageList.get(position));
                    return v;
                }

                case VIEW_TYPE_CAMERA: {
                    View v = convertView;
                    if(v == null) {
                        v = inflate(getContext(), R.layout.vw_feedback_camera, null);
                    }
                    return  v;
                }
            }

            return null;
        }
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case FeedBackActivity.REQ_CODE_USE_CAMERA:
                if(resultCode == Activity.RESULT_OK && photoOpr.getOutputFile().exists()) {
                    final String imagePath = photoOpr.getOutputFile().getPath();
                    imageList.add(imagePath);
                    adapter.notifyDataSetChanged();
                }
                return true;

            case FeedBackActivity.REQ_CODE_USE_GALLERY:
                if(resultCode == Activity.RESULT_OK && data != null) {
                    Uri imageFileUri = data.getData();
                    imageList.add(FileUtils.getAbsolutePath((Activity) getContext(), imageFileUri));
                    adapter.notifyDataSetChanged();
                }

                return true;

            default:
                return false;
        }
    }
}
