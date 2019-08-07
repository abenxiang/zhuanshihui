package com.sina.shopguide.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.sina.shopguide.R;
import com.sina.shopguide.view.SettingView;
import com.sina.shopguide.view.crop.CropImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by tiger on 18/5/17.
 */

public class PhotoOpr {
    private int width = 320;
    private int height = 320;

    private Activity activity;
    private File outputFile;

    public PhotoOpr(Activity act){
        activity = act;
    }

    public File getOutputFile(){return outputFile;}

    public void fromLocal(int requestCode) {
        if (!createOutputFile()) {
            ToastUtils.showToast(activity,"sd卡不可用");
            return;
        }

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(Intent.createChooser(intent, "请选择照片"),
                requestCode);
    }

    public void fromCamera(int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (!createOutputFile()) {
            ToastUtils.showToast(activity,"sd卡不可用");
            return;
        }

        try {
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(outputFile));
            intent.putExtra("return-data", true);
            activity.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            //Log.e("", "change shop background error.", e);
        }
    }

    private boolean createOutputFile() {
        boolean mounted = Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState());
        if (mounted && outputFile != null && outputFile.exists()) {
            outputFile.delete();
        }

        if (mounted) {
            String path = Environment.getExternalStorageDirectory()
                    + File.separator + AppConst.PHOTO_DIR;
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdir();
            }

            outputFile = new File(dir, "tmp_" + System.currentTimeMillis()
                    + AppConst.PHOTO_SUFFIX);
            try {
                return outputFile.createNewFile();
            } catch (Exception e) {
                System.out.println("gggggg     create output file error:");
                //Log.e("", "create output file error", e);
            }
        }

        return false;
    }

    public void doCrop(boolean isFromPhotoBg) {
        Intent intent = new Intent(activity, CropImage.class);
        System.out.println("gggggg     oufile:"+ outputFile.getPath());
        intent.putExtra("image-path", outputFile.getPath());
        intent.putExtra("outputX", width);
        intent.putExtra("outputY", height);
        intent.putExtra("aspectX", width);
        intent.putExtra("aspectY", height);
        intent.putExtra("scale", true);
        activity.startActivityForResult(intent, SettingView.CROP_IMAGE);
    }

    public void doByPhotoLocal(Intent data) {
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;

        try {
            inputStream = activity.getContentResolver().openInputStream(data.getData());
            fileOutputStream = new FileOutputStream(outputFile);
            byte[] buffer = new byte[inputStream.available()];
            int bytesRead = 0;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }

            doCrop(false);
        } catch (Exception e) {
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
            }
        }
    }

}
