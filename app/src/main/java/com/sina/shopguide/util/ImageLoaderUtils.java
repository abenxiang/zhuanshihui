package com.sina.shopguide.util;

import java.io.File;

import org.apache.commons.lang3.StringUtils;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

public class ImageLoaderUtils {

    static {
        final Context ctx = AppUtils.getAppContext();
        final Resources res = ctx.getResources();
        final DisplayMetrics dm = res.getDisplayMetrics();
        {
            defaultOptions = new DisplayImageOptions.Builder()
                    .showImageOnLoading(null)
                    .cacheOnDisk(true)
                    .cacheInMemory(true)
                    .considerExifParams(true)
                    .build();
        }
    }

    public static void initImageLoader() {
        ImageLoaderConfiguration.Builder configBuilder = new ImageLoaderConfiguration.Builder(
                AppUtils.getAppContext())
                .memoryCacheExtraOptions(480, 800)
                // max width, max height，即保存的每个缓存文件的最大长宽
                .threadPoolSize(5)
                // 线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
                // You can pass your own memory cache
                // implementation/你可以通过自己的内存缓存实现
                // .memoryCacheSize(2 * 1024 * 1024)
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(
                        new BaseImageDownloader(AppUtils.getAppContext(),
                                5 * 1000, 30 * 1000));

        String sdCardPath = FileUtils.getPhotePath();
        File cacheDir = null;
        if (StringUtils.isNotEmpty(sdCardPath)) {
            cacheDir = new File(sdCardPath);
            cacheDir.mkdirs();

            configBuilder.diskCache(new UnlimitedDiskCache(cacheDir));
            // .diskCacheSize(50 * 1024 * 1024)
            // .diskCacheFileNameGenerator(new
            // Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
            // .diskCacheFileCount(100) //缓存的文件数量

        }

        ImageLoader.getInstance().init(configBuilder.build());
    }

    private static final DisplayImageOptions defaultOptions;

    public static DisplayImageOptions getDefaultoptions() {
        return defaultOptions;
    }
}
