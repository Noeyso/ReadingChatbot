package com.example.mobilesw.info;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import java.net.URLConnection;

public class Util {
    public Util(){/* */}

    public static final String INTENT_PATH = "path";
    public static final String INTENT_MEDIA = "media";

    public static final int GALLERY_IMAGE = 0;
    public static final int GALLERY_VIDEO = 1;

    public static void showToast(Activity activity, String msg){
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

    public static boolean isStorageUrl(String url){
        return Patterns.WEB_URL.matcher(url).matches() && url.contains("https://firebasestorage.googleapis.com/v0/b/mobilesw-30740.appspot.com/o/post");
    }

    public static boolean isProfileUrl(String url){
        return Patterns.WEB_URL.matcher(url).matches() && url.contains("https://firebasestorage.googleapis.com/v0/b/mobilesw-30740.appspot.com/o/users");
    }

    public static String storageUrlToName(String url){
        return url.split("\\?")[0].split("%2F")[url.split("\\?")[0].split("%2F").length - 1];
    }

    public static boolean isImageFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("image");
    }

    public static void makeDialog(String title, String message, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)        // 제목
                .setMessage(message);
        AlertDialog dialog = builder.create();    // 알림창 객체 생성
        dialog.show();    // 알림창 띄우기
        handleDialog(2000, dialog);
    }

    public static void handleDialog(long time, final Dialog dialog) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if(dialog == null)
                    return;
                try {
                    if(dialog instanceof AlertDialog) {
                        if(dialog.isShowing())
                            dialog.dismiss();

                        return;
                    }
                } catch(Exception e) {
                    Log.e("dissmiss error", "no..");
                }
            }
        }, time);
    }
}