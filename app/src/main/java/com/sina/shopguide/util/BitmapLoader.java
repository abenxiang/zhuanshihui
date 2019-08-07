package com.sina.shopguide.util;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class BitmapLoader {

  private final static String TAG = BitmapLoader.class.getName();

  private static LruCache<String, Bitmap> CACHE;

  private static BitmapLoader instance = new BitmapLoader();

  private static Set<BitmapLoaderWork> workers;

  private BitmapLoader() {
    // 获取应用程序的最大内存
    final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

    // 用最大内存的1/8来存储图片
    CACHE = new LruCache<String, Bitmap>(maxMemory / 8);

    workers = new HashSet<BitmapLoaderWork>();
  }

  public static BitmapLoader getInstance() {
    return instance;
  }

  public void loadBitmaps(AbsListView view, List<String> urls, int firstVisibleItem,
      int visibleItemCount) {
    try {
      for (int i = firstVisibleItem; i < firstVisibleItem + visibleItemCount; i++) {
        Log.d(TAG, "--------loadBitmaps---i:" + i + ", firstVisibleItem:" + firstVisibleItem
            + ", visibleItemCount" + visibleItemCount + ", size:" + urls.size());
        if (i - firstVisibleItem < urls.size()) {
          String imageUrl = urls.get(i - firstVisibleItem);
          Bitmap bitmap = getBitmap(imageUrl);
          if (bitmap == null) {
            BitmapLoaderWork worker =
                new BitmapLoaderWork((ImageView) view.findViewWithTag(imageUrl));
            workers.add(worker);
            worker.execute(imageUrl);
          } else {
            ImageView imageView = (ImageView) view.findViewWithTag(imageUrl);
            if (imageView != null && bitmap != null) {
              imageView.setImageBitmap(bitmap);
            }
          }
        }
      }
    } catch (Exception e) {
      Log.e(TAG, "loadBitmaps error", e);
    }
  }

  public void loadBitmaps(ImageView view, String url) {
    Bitmap bitmap = getBitmap(url);
    if (bitmap == null) {
      BitmapLoaderWork worker = new BitmapLoaderWork(view);
      workers.add(worker);
      worker.execute(url);
    } else {
      if (view != null && bitmap != null) {
        view.setImageBitmap(bitmap);
      }
    }
  }


  public void addBitmap(String key, Bitmap bitmap) {
    if (getBitmap(key) == null) {
      CACHE.put(key, bitmap);
    }
  }

  public Bitmap getBitmap(String key) {
    Bitmap bitmap = CACHE.get(key);
    Log.d(TAG, "--------get cache2:" + bitmap);
    return bitmap;
  }

  public void cancelTasks() {
    if (workers != null) {
      for (BitmapLoaderWork work : workers) {
        work.cancel(false);
      }
    }
  }

  class BitmapLoaderWork extends AsyncTask<String, Void, Bitmap> {

    private ImageView view;

    private String imageUrl;

    public BitmapLoaderWork(ImageView view) {
      this.view = view;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
      imageUrl = params[0];
      // 在后台开始下载图片
      Bitmap bitmap = downloadBitmap(params[0]);
      Log.d(TAG, "--------downloadBitmap:" + imageUrl);
      if (bitmap != null) {
        // 图片下载完成后缓存到LrcCache中
        addBitmap(params[0], bitmap);
      }
      return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
      super.onPostExecute(bitmap);
      if (bitmap != null && view != null) {
        view.setImageBitmap(bitmap);
        view.setScaleType(ScaleType.FIT_XY);
      }
      workers.remove(this);
    }

    /**
     * 建立HTTP请求并获取Bitmap对象
     * 
     * @param imageUrl 图片的URL地址
     * @return 解析后的Bitmap对象
     */
    private Bitmap downloadBitmap(String imageUrl) {
      Bitmap bitmap = null;
      HttpURLConnection con = null;
      try {
        URL url = new URL(imageUrl);
        con = (HttpURLConnection) url.openConnection();
        con.setConnectTimeout(5 * 1000);
        con.setReadTimeout(10 * 1000);
        con.setDoInput(true);
        con.setDoOutput(true);
        if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
          bitmap = BitmapFactory.decodeStream(con.getInputStream());
        }
      } catch (Exception e) {
        Log.e(TAG, "downloadBitmap error", e);
      } finally {
        if (con != null) {
          con.disconnect();
        }
      }
      return bitmap;
    }

  }

}
