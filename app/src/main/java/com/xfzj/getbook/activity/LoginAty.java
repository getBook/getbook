package com.xfzj.getbook.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.xfzj.getbook.MainActivity;
import com.xfzj.getbook.R;
import com.xfzj.getbook.action.LoginAction;
import com.xfzj.getbook.newnet.ApiException;
import com.xfzj.getbook.utils.MyToast;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zj on 2016/1/28.
 */
public class LoginAty extends AppActivity {
    public static final String ACCOUNT = "account";
    @Bind(R.id.edtUserName)
    EditText edtUserName;
    @Bind(R.id.edtPassword)
    EditText edtPassword;
    @Bind(R.id.btnLogin)
    Button btnLogin;

    private String userName, password;

    @Override
    protected void onSetContentView() {
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setFlags(flag, flag);
        setContentView(R.layout.aty_login);
    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {
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
  

        LoginAction.login(true, LoginAty.this, userName, password, new LoginAction.CallBack() {
            @Override
            public void onSuccess() {
                MyToast.show(getApplicationContext(), getString(R.string.login_success));
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                startService(new Intent(getApplicationContext(), GetHeaderSerVice.class));
                finish();
            }

            @Override
            public void onFail(ApiException ex) {
                MyToast.show(getApplicationContext(), getString(R.string.login_fail) + ex.getDisplayMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        exitApp();
    }

    public ProgressDialog setProgressDialog(String title, String message) {
        ProgressDialog pd = new ProgressDialog(LoginAty.this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setTitle(title);
        pd.setMessage(message);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        return pd;
    }
}
