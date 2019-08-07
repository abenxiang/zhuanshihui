package com.sina.shopguide.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public final class BitmapHelper {

    /**
     * 自适应屏幕大小 得到最大的smapleSize
     * 同时达到此目标： 自动旋转 以适应view的宽高后, 不影响界面显示效果
     * @param vWidth view width
     * @param vHeight view height
     * @param bWidth bitmap width
     * @param bHeight bitmap height
     * @return
     */
    public static int getSampleSizeAutoFitToScreen( int vWidth, int vHeight, int bWidth, int bHeight ) {
        if( vHeight == 0 || vWidth == 0 ) {
            return 1;
        }

        int ratio = Math.max( bWidth / vWidth, bHeight / vHeight );

        int ratioAfterRotate = Math.max( bHeight / vWidth, bWidth / vHeight );

        return Math.min( ratio, ratioAfterRotate );
    }

    /**
     * 相册获取缩略图
     * 
     * @param path
     * @param maxWidth
     * @param maxHeight
     * @param isNeedCenterCrop 是否按照maxWidth做CenterCrop
     * @return
     */
    public static Bitmap getBitmapFromFile(String path, int maxWidth, int maxHeight, boolean isNeedCenterCrop) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }

        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        file = null;

        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        
        options.inJustDecodeBounds = true;

        bitmap = BitmapFactory.decodeFile(path, options);
        float widthRatio = ((float)options.outWidth) / maxWidth;
        float heightRatio = ((float) options.outHeight) / maxHeight;
        if (widthRatio > 1 || heightRatio > 1) {
            if (widthRatio > heightRatio) {
                options.inSampleSize = (int)(widthRatio + 0.5f);
            } else {
                options.inSampleSize = (int)(heightRatio + 0.5f);
            }
        }

        options.inJustDecodeBounds = false;
        
        options.inInputShareable = true;
        options.inPurgeable = true;
        
        options.inPreferredConfig = Config.RGB_565;

        bitmap = safeDecodeBimtapFile(path, options);

        if (isNeedCenterCrop) {
            bitmap = getBitmapCropCenter(bitmap, maxWidth, true);
        }

        return bitmap;
    }

    // copy from gallery
    public static Bitmap getBitmapCropCenter(Bitmap bitmap, int size, boolean recycle) {
        if(bitmap == null || bitmap.isRecycled()){
            return bitmap;
        }
        
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int minSide = Math.min(w, h);
        if (w == h && minSide <= size) {
            return bitmap;
        }
        size = Math.min(size, minSide);

        float scale = Math.max((float) size / bitmap.getWidth(), (float) size / bitmap.getHeight());
        Bitmap target = Bitmap.createBitmap(size, size, getConfig(bitmap));
        int width = Math.round(scale * bitmap.getWidth());
        int height = Math.round(scale * bitmap.getHeight());
        Canvas canvas = new Canvas(target);
        canvas.translate((size - width) / 2f, (size - height) / 2f);
        canvas.scale(scale, scale);
        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        if (recycle) {
            bitmap.recycle();
        }
        return target;
    }

    private static Config getConfig(Bitmap bitmap) {
        Config config = bitmap.getConfig();
        if (config == null) {
            config = Config.ARGB_8888;
        }
        return config;
    }
    

    /**
     * 如果加载时遇到OutOfMemoryError,则将图片加载尺寸缩小一半并重新加载
     * @param bmpFile
     * @param opts 注意：opts.inSampleSize 可能会被改变
     * @return
     */
    public static Bitmap safeDecodeBimtapFile( String bmpFile, BitmapFactory.Options opts ) {
        BitmapFactory.Options optsTmp = opts;
        if ( optsTmp == null ) {
            optsTmp = new BitmapFactory.Options();
            optsTmp.inSampleSize = 1;
        }
        
        Bitmap bmp = null;
        FileInputStream input = null;
        
        final int MAX_TRIAL = 5;
        for( int i = 0; i < MAX_TRIAL; ++i ) {
            try {
                input = new FileInputStream( bmpFile );
                bmp = BitmapFactory.decodeStream(input, null, opts);
                break;
            }
            catch( OutOfMemoryError e ) {
                e.printStackTrace();
                optsTmp.inSampleSize *= 2;
            }
            catch (FileNotFoundException e) {
                break;
            }
            finally {
                if(input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        
        return bmp;
    }

}
