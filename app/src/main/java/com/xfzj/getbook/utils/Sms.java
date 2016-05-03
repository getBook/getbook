package com.xfzj.getbook.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.xfzj.getbook.R;
import com.xfzj.getbook.activity.AppActivity;

import java.util.List;

/**
 * Created by zj on 2016/3/5.
 */
public class Sms {

    public static final int requestCode = 1010;

    public static void sendSms(final AppActivity aty, final String tele, String body, final int id) {
        if (R.string.secondbook == id) {
            AppAnalytics.onEvent(aty.getApplicationContext(), AppAnalytics.CLICK_SMS_SECONDBOOK);
        } else if (R.string.debris == id) {
            AppAnalytics.onEvent(aty.getApplicationContext(), AppAnalytics.CLICK_SMS_DEBRIS);
        }
        final View view = LayoutInflater.from(aty).inflate(R.layout.sms_send_dialog, null);
        final EditText et = (EditText) view.findViewById(R.id.et);
        et.setText(body);
        ImageButton ibtn = (ImageButton) view.findViewById(R.id.ibSend);
        AlertDialog.Builder builder = new AlertDialog.Builder(aty);
        int margin = (int) MyUtils.dp2px(aty, 5f);
        builder.setView(view, margin, margin, margin, margin);
        final AlertDialog ad = builder.create();
        ad.show();
        ibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = et.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    MyToast.show(aty, aty.getString(R.string.please_to_input, aty.getString(R.string.sms_content)));
                    return;
                }
                InputMethodManagerUtils.hide(aty, view);
                ad.dismiss();
                sendMessage(aty, tele, content + aty.getString(R.string.thismsgfrom) + aty.getString(R.string.app_name), id);

            }
        });
    }
    private static void sendMessage(final AppActivity aty, String tele, String content, final int id) {
        Intent sentIntent = new Intent("SENT_SMS_ACTION");
        SmsManager smsManager = SmsManager.getDefault();
        PendingIntent sentPI = PendingIntent.getBroadcast(aty, 0, sentIntent, 0);
        aty.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        MyToast.show(context, aty.getString(R.string.sms_success));
                        aty.unregisterReceiver(this);
                        if (R.string.secondbook == id) {
                            AppAnalytics.onEvent(aty.getApplicationContext(), AppAnalytics.SMS_SECONDBOOK_SUCCESS);
                        } else if (R.string.debris == id) {
                            AppAnalytics.onEvent(aty.getApplicationContext(), AppAnalytics.SMS_DEBRIS_SUCCESS);
                        }
                        break;
                }
            }
        }, new IntentFilter("SENT_SMS_ACTION"));

        List<String> contents = smsManager.divideMessage(content);
        for (String str : contents) {
            try {
                smsManager.sendTextMessage(tele, null, str, sentPI, null);
            } catch (Exception e) {
                MyToast.show(aty, aty.getString(R.string.sms_fail));
                e.printStackTrace();
            }
        }

    }
}
