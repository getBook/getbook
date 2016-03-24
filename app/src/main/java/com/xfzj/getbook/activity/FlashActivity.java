package com.xfzj.getbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.xfzj.getbook.MainActivity;
import com.xfzj.getbook.R;
import com.xfzj.getbook.common.User;

import cn.bmob.v3.BmobUser;


/**
 * Created by zj on 2016/2/27.
 */
public class FlashActivity extends AppActivity {
    public static final String FROM = "FlashActivity.class";

    private User user;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            jump2Login();
        }
    };

    @Override
    protected void onSetContentView() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        
        setContentView(R.layout.flash);

    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {
        user = BmobUser.getCurrentUser(getApplicationContext(), User.class);
        if (null != user) {
            jump2MainAty();
        } else {
            handler.sendEmptyMessageDelayed(0, 3000);
        }

    }


    private void jump2Login() {

        Intent i = new Intent(this, LoginAty.class);
        startActivity(i);
        finish();
    }


    private void jump2MainAty() {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra(MainActivity.FROM, FROM);
        startActivity(i);
        finish();
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}
