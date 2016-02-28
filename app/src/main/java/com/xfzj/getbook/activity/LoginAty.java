package com.xfzj.getbook.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.xfzj.getbook.MainActivity;
import com.xfzj.getbook.R;
import com.xfzj.getbook.action.LoginAction;
import com.xfzj.getbook.async.LoginAsync;
import com.xfzj.getbook.async.UserHeadAsync;
import com.xfzj.getbook.common.User;
import com.xfzj.getbook.utils.AryConversion;
import com.xfzj.getbook.utils.MyToast;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;

/**
 * Created by zj on 2016/1/28.
 */
public class LoginAty extends AppActivity {
    public static final String ACCOUNT ="account" ;
    @Bind(R.id.edtUserName)
    EditText edtUserName;
    @Bind(R.id.edtPassword)
    EditText edtPassword;
    @Bind(R.id.btnLogin)
    Button btnLogin;
    @Bind(R.id.ivUserHead)
    ImageView ivUserHead;
    private String userName, password;

    @Override
    protected void onSetContentView() {
        setContentView(R.layout.aty_login);

    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {
        UserHeadAsync userHeadAsync = new UserHeadAsync(getApplicationContext());
        userHeadAsync.setCallBack(new UserHeadAsync.LoadBitmapCallBack() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                ivUserHead.setImageBitmap(bitmap);
                
            }

            @Override
            public void onFail() {

            }
        });
        userHeadAsync.execute();

        String str = getIntent().getStringExtra(ACCOUNT);
        if (!TextUtils.isEmpty(str)) {
            edtUserName.setText(str);
            edtPassword.requestFocus();
        }
    }

    @OnClick(R.id.btnLogin)
    public void Login() {
        userName = edtUserName.getText().toString().trim();
        password = edtPassword.getText().toString().trim();
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
            MyToast.show(getApplicationContext(), getString(R.string.login_tips));
            return;
        }
        try {
            password = AryConversion.binary2Hex(password).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        LoginAsync loginAsync = new LoginAsync(LoginAty.this, userName, password, null, getString(R.string.logining));
        loginAsync.setCallback(new LoginAction.CallBack() {
            @Override
            public void onSuccess() {
                MyToast.show(getApplicationContext(), getString(R.string.login_success) + BmobUser.getCurrentUser(getApplicationContext(), User.class).toString());
                
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
            @Override
            public void onFail() {
                MyToast.show(getApplicationContext(), getString(R.string.login_fail));
                
            }
        });
        loginAsync.execute();
    }

}
