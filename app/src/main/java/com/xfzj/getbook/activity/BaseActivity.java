package com.xfzj.getbook.activity;

import com.xfzj.getbook.R;
import com.xfzj.getbook.utils.MyToast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zj on 2016/1/27.
 */
public abstract class BaseActivity extends AppActivity {
    private long pretime;
    private boolean isFirstClick = true;
    @Override
    public void onBackPressed() {
        long nowtime = System.currentTimeMillis();
        if (isFirstClick) {
            pretime = nowtime;
            isFirstClick = false;
            MyToast.show(getApplicationContext(), getString(R.string.exit_app_tips));
        } else if (nowtime - pretime <= 2000000) {
            finish();
        }
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                isFirstClick = true;
            }
        }, 2000);
    }
}
