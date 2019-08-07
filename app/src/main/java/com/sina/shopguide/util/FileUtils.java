package com.sina.shopguide.util;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;


public class FileUtils {

    private final static String CRASH_REPORT_DIR = "crash" + File.separator;

    public static void makeCrashReport(Throwable ex) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Date date = new Date();
            StringBuffer sb = new StringBuffer();
            String time = DateUtils.format("yyyy-MM-dd", date.getTime());

            String dir =
                    Environment.getExternalStorageDirectory() + File.separator + AppConst.APP_DIR
                            + File.separator + CRASH_REPORT_DIR;
            String file = "crash" + "-" + time + ".log";

            File d = new File(dir);
            if (!d.exists()) {
                d.mkdirs();
            }

            PrintWriter printWriter = null;
            try {
                sb.append("----------------------------------------------------------").append("\n");
                sb.append(DateUtils.format(new Date().getTime())).append(" ");
                Writer writer = new StringWriter();
                printWriter = new PrintWriter(writer);
                ex.printStackTrace(printWriter);
                Throwable cause = ex.getCause();

                while (cause != null) {
                    cause.printStackTrace(printWriter);
                    cause = cause.getCause();
                }

                sb.append(writer.toString());

                //uploadCrash(sb.toString());

                org.apache.commons.io.FileUtils.write(new File(dir + file), sb.toString(), true);
            } catch (Exception e) {
            } finally {
                if (printWriter != null) {
                    printWriter.close();
                }
            }
        }
    }

    public static void checkPhotoDir() {
        String path = Environment.getExternalStorageDirectory() + File.separator + AppConst.PHOTO_DIR;
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    public static String getPhotePath() {
        checkPhotoDir();
        return Environment.getExternalStorageDirectory() + File.separator + AppConst.PHOTO_DIR;
    }

    public static String getAbsolutePath(Activity context, Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = context.managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}
