package com.xfzj.getbook.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by zj on 2016/3/5.
 */
public class Sms {

    public static void sendSms(Context context, String tele, String body) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + tele));
        intent.putExtra("sms_body", body);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
