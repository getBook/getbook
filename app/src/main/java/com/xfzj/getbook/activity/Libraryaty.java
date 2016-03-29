package com.xfzj.getbook.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xfzj.getbook.R;
import com.xfzj.getbook.async.BaseAsyncTask;
import com.xfzj.getbook.async.GetLibraryCaptureAsync;
import com.xfzj.getbook.async.GetLibraryMyInfo;
import com.xfzj.getbook.async.LoginLibraryAsyc;
import com.xfzj.getbook.common.LibraryInfo;
import com.xfzj.getbook.common.LibraryUserInfo;
import com.xfzj.getbook.common.User;
import com.xfzj.getbook.net.BaseHttp;
import com.xfzj.getbook.utils.InputMethodManagerUtils;
import com.xfzj.getbook.utils.MyToast;
import com.xfzj.getbook.utils.MyUtils;
import com.xfzj.getbook.utils.SharedPreferencesUtils;
import com.xfzj.getbook.views.view.BaseToolBar;
import com.xfzj.getbook.views.view.CircleImageView;

import butterknife.Bind;

public class Libraryaty extends AppActivity {
    @Bind(R.id.tvBookInfo)
    TextView tvBookInfo;
    @Bind(R.id.baseToolbar)
    BaseToolBar baseToolBar;
    @Bind(R.id.userHeader)
    CircleImageView circleImageView;
    @Bind(R.id.tvOwnMoney)
    TextView tvOwnMoney;
    @Bind(R.id.tvBorrowCount)
    TextView tvBorrowCount;
    @Bind(R.id.tvMaxBorrow)
    TextView tvMaxBorrow;
    @Bind(R.id.tvIllegalCount)
    TextView tvIllegalCount;
    @Bind(R.id.ll)
    LinearLayout ll;
    private GetLibraryCaptureAsync.Capture c;


    @Override
    protected void onSetContentView() {
        setContentView(R.layout.aty_library);
    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {
        baseToolBar.initToolbar(this, getString(R.string.library));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getMyInfo();
        String header = SharedPreferencesUtils.getUserHeader(getApplicationContext());
        if (!TextUtils.isEmpty(header)) {
            circleImageView.setBmobImage(header, BitmapFactory.decodeResource(getResources(), R.mipmap.default_user));
        }
    }

    private void getMyInfo() {
        LibraryInfo libraryInfo = SharedPreferencesUtils.getLibraryUserInfo(getApplicationContext());
        if (null == libraryInfo) {
            openLoginDialog();
            return;
        }
        GetLibraryMyInfo getLibraryMyInfo = new GetLibraryMyInfo(Libraryaty.this);
        getLibraryMyInfo.execute(BaseHttp.REDEINFO);
        getLibraryMyInfo.setOnTaskListener(new BaseAsyncTask.onTaskListener<LibraryUserInfo>() {
            @Override
            public void onSuccess(LibraryUserInfo libraryUserInfo) {
                setLibraryUserInfo(libraryUserInfo);
            }

            @Override
            public void onFail() {
                MyToast.show(getApplicationContext(), getString(R.string.login_library_fail));
                openLoginDialog();

            }
        });


    }

    private void setLibraryUserInfo(LibraryUserInfo libraryUserInfo) {
        ll.setVisibility(View.VISIBLE);
        tvBookInfo.setText(libraryUserInfo.getBookInfo());
        tvOwnMoney.setText(libraryUserInfo.getOwnMoney());
        tvBorrowCount.setText(libraryUserInfo.getBorrowCount());
        tvMaxBorrow.setText(libraryUserInfo.getMaxBorrow());
        tvIllegalCount.setText(libraryUserInfo.getIllegalCount());
    }

    private void openLoginDialog() {
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.library_login_dialog, null);
        final EditText etAccount, etPassword, etVerfy;
        etAccount = (EditText) view.findViewById(R.id.etAccount);
        User user = SharedPreferencesUtils.getUser(getApplicationContext());
        if (null != user) {
            etAccount.setText(user.getSno());
        }
        
        etPassword = (EditText) view.findViewById(R.id.etPassword);
        LibraryInfo libraryInfo = SharedPreferencesUtils.getLibraryUserInfo(getApplicationContext());
        if (null != libraryInfo) {
            etAccount.setText(libraryInfo.getAccount());
            etPassword.setText(libraryInfo.getPassword());
        }
        etVerfy = (EditText) view.findViewById(R.id.etVerfy);
        ImageView iv = (ImageView) view.findViewById(R.id.iv);
        getCapure(iv);
        final Button btn = (Button) view.findViewById(R.id.btn);
        AlertDialog.Builder builder = new AlertDialog.Builder(Libraryaty.this);
        int margin = (int) MyUtils.dp2px(getApplicationContext(), 5f);
        builder.setView(view, margin, margin, margin, margin);
        final AlertDialog dialog = builder.create();
        dialog.show();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = etAccount.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String verfy = etVerfy.getText().toString().trim();
                if (canLogin(account, password, verfy)) {
                    InputMethodManagerUtils.hide(getApplicationContext(), btn);
                    LoginLibraryAsyc loginLibraryAsyc = new LoginLibraryAsyc(Libraryaty.this);
                    
                    loginLibraryAsyc.execute(account, password, verfy, c.getCookie());
                    loginLibraryAsyc.setOnTaskListener(new BaseAsyncTask.onTaskListener<LibraryUserInfo>() {
                        @Override
                        public void onSuccess(LibraryUserInfo libraryUserInfo) {
                            dialog.dismiss();
                            setLibraryUserInfo(libraryUserInfo);

                        }

                        @Override
                        public void onFail() {
                            MyToast.show(getApplicationContext(), getString(R.string.login_library_fail));
                        }
                    });
                }


            }
        });
    }

    private boolean canLogin(String account, String password, String verfy) {
        if (TextUtils.isEmpty(account)) {
            MyToast.show(getApplicationContext(), getString(R.string.please_to_input, getString(R.string.account)));
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            MyToast.show(getApplicationContext(), getString(R.string.please_to_input, getString(R.string.pwd)));
            return false;
        }
        if (TextUtils.isEmpty(verfy)) {
            MyToast.show(getApplicationContext(), getString(R.string.please_to_input, getString(R.string.verfy)));
            return false;
        }
        return true;


    }

    private void getCapure(final ImageView iv) {
        GetLibraryCaptureAsync getLibraryCaptureAsync = new GetLibraryCaptureAsync(getApplicationContext());
        getLibraryCaptureAsync.execute();
        getLibraryCaptureAsync.setOnTaskListener(new BaseAsyncTask.onTaskListener<GetLibraryCaptureAsync.Capture>() {
            @Override
            public void onSuccess(GetLibraryCaptureAsync.Capture s) {
                iv.setImageBitmap(s.getBitmap());
                c = s;
            }

            @Override
            public void onFail() {
                getCapure(iv);

            }
        });


    }
}
