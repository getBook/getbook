package com.xfzj.getbook.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.xfzj.getbook.GetHeaderSerVice;
import com.xfzj.getbook.MainActivity;
import com.xfzj.getbook.R;
import com.xfzj.getbook.action.LoginAction;
import com.xfzj.getbook.async.LoginAsync;
import com.xfzj.getbook.utils.AppAnalytics;
import com.xfzj.getbook.utils.AryConversion;
import com.xfzj.getbook.utils.MyToast;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;

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
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                new AlertDialog.Builder(LoginAty.this).setTitle(getString(R.string.tishi)).setMessage(getString(R.string.change_password_tips)).setPositiveButton(getString(R.string.change_password), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginAty.this);
                        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.modifypassword, null);
                        final EditText etOld = (EditText) view.findViewById(R.id.etOld);
                        final EditText etNew = (EditText) view.findViewById(R.id.etNew);
                        builder.setView(view).setPositiveButton(R.string.ensure, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String oldp = etOld.getText().toString();
                                String newp = etNew.getText().toString();
                                if (TextUtils.isEmpty(oldp)) {
                                    MyToast.show(getApplicationContext(), getString(R.string.please_to_input, getString(R.string.oldpassword)));
                                    return;
                                }
                                if (TextUtils.isEmpty(newp)) {
                                    MyToast.show(getApplicationContext(), getString(R.string.please_to_input, getString(R.string.newpassword)));
                                    return;
                                }
                                try {
                                    oldp = AryConversion.binary2Hex(oldp).toUpperCase();
                                    newp = AryConversion.binary2Hex(newp).toUpperCase();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                BmobUser.updateCurrentUserPassword(getApplicationContext(), oldp, newp, new UpdateListener() {
                                    @Override
                                    public void onSuccess() {
                                        MyToast.show(getApplicationContext(), getString(R.string.password_change_success_login_again));
                                    }

                                    @Override
                                    public void onFailure(int i, String s) {
                                        MyToast.show(getApplicationContext(), getString(R.string.password_change_error));
                                    }
                                });


                            }
                        }).create().show();
                    }
                }).setNegativeButton(getString(R.string.relogin), null).create().show();


            } else if (msg.what == 1) {
                MyToast.show(getApplicationContext(), getString(R.string.login_fail));
            }
        }
    };

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
        try {
            password = AryConversion.binary2Hex(password).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        final ProgressDialog pd = setProgressDialog(null, getString(R.string.logining));
        pd.show();
        LoginAsync loginAsync = new LoginAsync(LoginAty.this, null, userName, password);
        loginAsync.setCallback(new LoginAction.CallBack() {
            @Override
            public void onSuccess() {
                pd.dismiss();
                AppAnalytics.onEvent(getApplicationContext(), AppAnalytics.LOGIN_SUCCESS);
                MyToast.show(getApplicationContext(), getString(R.string.login_success));
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                startService(new Intent(getApplicationContext(), GetHeaderSerVice.class));
                finish();
            }

            @Override
            public void onFail() {
                pd.dismiss();
                AppAnalytics.onEvent(getApplicationContext(), AppAnalytics.LOGIN_FAIL);
                handler.sendEmptyMessage(1);
            }

            @Override
            public void onModify() {
                pd.dismiss();
                AppAnalytics.onEvent(getApplicationContext(), AppAnalytics.LOGIN_MODIFY);
                handler.sendEmptyMessage(0);
            }
        });
        loginAsync.execute();
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
