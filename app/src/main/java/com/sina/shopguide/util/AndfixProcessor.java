package com.sina.shopguide.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.alipay.euler.andfix.patch.PatchManager;
import com.sina.shopguide.BuildConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by tiger on 18/4/25.
 */

//./apkpatch.sh -f guide_v1.0_b91new.apk -t guide_v1.0_b91.apk  -k guide.keystore -p 963852 -a guide -e 963852 -o output
public class AndfixProcessor {
    public static int BUFFER_SIZE = 2048;
    public static final String apatch_path = "out.apatch";
    private static PatchManager patchManager;

    public static synchronized void init(Context context){
        if(patchManager == null){
            patchManager = new PatchManager(context);
            SimpleDebugPrinterUtils.println("gggggggg 11111111111");
            patchManager.init(BuildConfig.VERSION_CODE + "");
            patchManager.loadPatch();
        }
    }

    public static void inject(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
               // download(context);
                update();
            }
        }).start();
    }

    public static void update(){
        try {
            String patchPath = Environment.getExternalStorageDirectory() + File.separator + AppConst.APP_DIR
                    + File.separator+apatch_path;//context.getDir("patch", Context.MODE_PRIVATE).getAbsolutePath() + "/" + apatch_path;
            File file = new File(patchPath);
            if (file.exists()) {
                patchManager.addPatch(patchPath);
                SimpleDebugPrinterUtils.println("gggggggg   budingwancheng");
            } else {
                SimpleDebugPrinterUtils.println("gggggggg   failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void download(final Context context){
        InputStream in = null;
        FileOutputStream out = null;
        HttpURLConnection urlConnection = null;
        File apk = null;
        String filename = null;
        try {
            URL downloadUrl = new URL(AppConst.APP_PATCH_URL);
            urlConnection = (HttpURLConnection) downloadUrl.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(false);
            urlConnection.setConnectTimeout(30 * 1000);
            urlConnection.setReadTimeout(30 * 1000);
            urlConnection.setRequestProperty("Connection", "Keep-Alive");
            urlConnection.setRequestProperty("Charset", "UTF-8");
            urlConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
            urlConnection.connect();

            long len = urlConnection.getContentLength();
            long total = 0;
            int b = 0;

            in = urlConnection.getInputStream();
            filename = context.getDir(AppConst.APP_PATCH_DIR, Context.MODE_PRIVATE).getAbsolutePath() + "/"+apatch_path;
            apk = new File(filename);//context.getDir(AppConst.APP_PATCH_DIR, Context.MODE_PRIVATE).getAbsolutePath() + "/", apatch_path));
            out = new FileOutputStream(apk);
            byte[] buffer = new byte[BUFFER_SIZE];
            int last = 0;

            while ((b = in.read(buffer)) != -1) {
                total += b;
                out.write(buffer, 0, b);

                int current = (int) (total * 100 / len);
                if (current != last) {
                    last = current;
                }
            }
        } catch (Exception e) {
            Log.e("", "update service download error", e);
            if (apk != null) {
                apk.delete();
            }
        } finally {
            if (out != null) {
                try {
                    out.close();
                    if(filename !=null)
                        patchManager.addPatch(filename);
                } catch (IOException e) {
                }
            }

            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }

            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

    }
}
