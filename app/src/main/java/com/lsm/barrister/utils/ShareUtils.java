package com.lsm.barrister.utils;

import android.content.Context;
import android.content.Intent;

public class ShareUtils {

    public static void share(Context context, String msg) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, msg); // 内容
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, "分享到"));
    }

}
