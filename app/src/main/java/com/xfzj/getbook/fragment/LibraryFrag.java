package com.xfzj.getbook.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xfzj.getbook.BaseApplication;
import com.xfzj.getbook.R;
import com.xfzj.getbook.activity.AppActivity;
import com.xfzj.getbook.async.BaseAsyncTask;
import com.xfzj.getbook.async.ChangeLibraryPwdAsyc;
import com.xfzj.getbook.async.GetLibraryCaptureAsync;
import com.xfzj.getbook.async.GetLibraryMyInfo;
import com.xfzj.getbook.async.LoginLibraryAsyc;
import com.xfzj.getbook.async.VerifyLibraryNameAsyc;
import com.xfzj.getbook.common.LibraryInfo;
import com.xfzj.getbook.common.LibraryUserInfo;
import com.xfzj.getbook.common.User;
import com.xfzj.getbook.net.BaseHttp;
import com.xfzj.getbook.utils.InputMethodManagerUtils;
import com.xfzj.getbook.utils.MyToast;
import com.xfzj.getbook.utils.MyUtils;
import com.xfzj.getbook.utils.SharedPreferencesUtils;
import com.xfzj.getbook.views.view.CircleImageView;

import butterknife.Bind;
import cn.bmob.v3.listener.SaveListener;

public class LibraryFrag extends BaseFragment {
    public static final String ARG_PARAM1 = "LibraryFrag.class";

    TextView tvBookInfo;

    CircleImageView circleImageView;
    TextView tvOwnMoney;
    TextView tvBorrowCount;
    TextView tvMaxBorrow;
    TextView tvIllegalCount;
    private boolean verify = true;
    private User user;
    private LibraryInfo libraryInfo;
    @Bind(R.id.ll)
    LinearLayout ll;
    private GetLibraryCaptureAsync.Capture c;
    private boolean isLoginSUccess = false;
    private String mParam1;


    public static LibraryFrag newInstance(String param1) {
        LibraryFrag fragment = new LibraryFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public LibraryFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
        user = ((BaseApplication) getActivity().getApplicationContext()).getUser();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.aty_library, container, false);
        tvBookInfo = (TextView) view.findViewById(R.id.tvBookInfo);
        circleImageView = (CircleImageView) view.findViewById(R.id.userHeader);
        tvOwnMoney = (TextView) view.findViewById(R.id.tvOwnMoney);
        tvBorrowCount = (TextView) view.findViewById(R.id.tvBorrowCount);
        tvMaxBorrow = (TextView) view.findViewById(R.id.tvMaxBorrow);
        tvIllegalCount = (TextView) view.findViewById(R.id.tvIllegalCount);
        ll = (LinearLayout) view.findViewById(R.id.ll);
        getMyInfo();
//test
        setBtn(view);
        return view;
    }

    private void setBtn(View view) {
        Button btnSend, btnlogout, btnChange;
        btnSend = (Button) view.findViewById(R.id.btnSend);
        btnlogout = (Button) view.findViewById(R.id.btnLogout);
        btnChange = (Button) view.findViewById(R.id.btnChange);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                libraryInfo.save(getActivity(), new SaveListener() {
                    @Override
                    public void onSuccess() {
                        MyToast.show(getActivity(), "发送成功");
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        MyToast.show(getActivity(), "发送失败");
                    }
                });
            }
        });
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitLibrary();
            }
        });

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.xiugaimima, null);
                final EditText etOld = (EditText) view.findViewById(R.id.etOld);
                final EditText etNew1 = (EditText) view.findViewById(R.id.etNew1);
                final EditText etNew2 = (EditText) view.findViewById(R.id.etNew2);
                final Button btn = (Button) view.findViewById(R.id.btn);
                builder.setView(view);
                etOld.setHint("图书馆旧密码");
                etNew1.setHint("图书馆新密码");
                etNew2.setHint("图书馆新密码");
                etNew1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                etNew2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                final AlertDialog dialog = builder.create();
                dialog.show();
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String old = etOld.getText().toString().trim();
                        if (TextUtils.isEmpty(old)) {
                            MyToast.show(getActivity(), getString(R.string.please_to_input, getString(R.string.oldpassword)));
                            return;
                        }
                        final String new1 = etNew1.getText().toString().trim();
                        String new2 = etNew2.getText().toString().trim();
                        if (TextUtils.isEmpty(new1) || TextUtils.isEmpty(new2)) {
                            MyToast.show(getActivity(), getString(R.string.please_to_input, getString(R.string.newpassword)));
                            return;
                        }
                        if (!new1.equals(new2)) {
                            MyToast.show(getActivity(), getString(R.string.twice_password_not_equal));
                            return;
                        }
                        InputMethodManagerUtils.hide(getActivity(), btn);
                        ChangeLibraryPwdAsyc changeLibraryPwdAsyc = new ChangeLibraryPwdAsyc(getActivity());

                        changeLibraryPwdAsyc.setOnTaskListener(new BaseAsyncTask.onTaskListener<String>() {
                            @Override
                            public void onSuccess(String s) {
                                MyToast.show(getActivity(), s);
                                dialog.dismiss();
                                exitLibrary();
                            }

                            @Override
                            public void onFail() {
                                MyToast.show(getActivity(), "密码修改失败");
                            }
                        });
                        changeLibraryPwdAsyc.executeOnExecutor(((AppActivity) getActivity()).THREAD_POOL_EXECUTOR, old, new1, SharedPreferencesUtils.getLibraryCookie(getActivity()));
                    }

                });
            }
        });

    }

    private void exitLibrary() {
        SharedPreferencesUtils.clearLibrary(getActivity());
        getMyInfo();
        ll.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!isLoginSUccess && !hidden) {
            openLoginDialog();
        }
    }

    private void getMyInfo() {
        libraryInfo = SharedPreferencesUtils.getLibraryLoginInfo(getActivity());
        if (null == libraryInfo) {
            openLoginDialog();
            return;
        }

        doGetMyInfo();


    }

    private void doGetMyInfo() {
        GetLibraryMyInfo getLibraryMyInfo = new GetLibraryMyInfo(getActivity());
        getLibraryMyInfo.executeOnExecutor(((AppActivity) getActivity()).THREAD_POOL_EXECUTOR, BaseHttp.REDEINFO);
        getLibraryMyInfo.setOnTaskListener(new BaseAsyncTask.onTaskListener<LibraryUserInfo>() {
            @Override
            public void onSuccess(LibraryUserInfo libraryUserInfo) {
                setLibraryUserInfo(libraryUserInfo);
            }

            @Override
            public void onFail() {
                MyToast.show(getActivity(), getString(R.string.login_library_fail));
                openLoginDialog();

            }
        });
    }

    private void setLibraryUserInfo(LibraryUserInfo libraryUserInfo) {
        libraryInfo = SharedPreferencesUtils.getLibraryLoginInfo(getActivity());
        ll.setVisibility(View.VISIBLE);
        tvBookInfo.setText(libraryUserInfo.getBookInfo());
        tvOwnMoney.setText(libraryUserInfo.getOwnMoney());
        tvBorrowCount.setText(libraryUserInfo.getBorrowCount());
        tvMaxBorrow.setText(libraryUserInfo.getMaxBorrow());
        tvIllegalCount.setText(libraryUserInfo.getIllegalCount());
        String header = SharedPreferencesUtils.getUserHeader(getActivity());
        if (!TextUtils.isEmpty(header) && null != libraryInfo && null != user && libraryInfo.getAccount().equals(user.getSno())) {
            circleImageView.setBmobImage(header);
        } else {
            circleImageView.setImageResource(R.mipmap.default_user);
        }
    }

    private void openLoginDialog() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.library_login_dialog, null);
        final EditText etAccount, etPassword, etVerfy, etName;
        etAccount = (EditText) view.findViewById(R.id.etAccount);
        etName = (EditText) view.findViewById(R.id.etName);
        if (null != user) {
            etAccount.setText(user.getSno());
        }

        etPassword = (EditText) view.findViewById(R.id.etPassword);
        LibraryInfo libraryInfo = SharedPreferencesUtils.getLibraryLoginInfo(getActivity());
        if (null != libraryInfo && !TextUtils.isEmpty(libraryInfo.getAccount()) && !TextUtils.isEmpty(libraryInfo.getPassword())) {
            etAccount.setText(libraryInfo.getAccount());
            etPassword.setText(libraryInfo.getPassword());
        }
        etVerfy = (EditText) view.findViewById(R.id.etVerfy);
        final ImageView iv = (ImageView) view.findViewById(R.id.iv);
        getCapure(iv);
        final Button btn = (Button) view.findViewById(R.id.btn);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        int margin = (int) MyUtils.dp2px(getActivity(), 5f);
        builder.setView(view, margin, margin, margin, margin);
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (!isLoginSUccess) {
//                    finish();
                }
            }
        });
        etAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                verify = true;
                etName.setVisibility(View.GONE);

            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = etAccount.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String verfy = etVerfy.getText().toString().trim();
                if (canLogin(account, password, verfy)) {
                    InputMethodManagerUtils.hide(getActivity(), btn);
                    if (verify) {
                        LoginLibraryAsyc loginLibraryAsyc = new LoginLibraryAsyc(getActivity());

                        loginLibraryAsyc.executeOnExecutor(((AppActivity) getActivity()).THREAD_POOL_EXECUTOR, account, password, verfy, c.getCookie());
                        loginLibraryAsyc.setOnLibraryLoginListener(new LoginLibraryAsyc.onLibraryLoginListener<LibraryUserInfo>() {
                            @Override
                            public void onSuccess(LibraryUserInfo libraryUserInfo) {
                                isLoginSUccess = true;
                                dialog.dismiss();
                                setLibraryUserInfo(libraryUserInfo);
                            }

                            @Override
                            public void onFail() {
                                MyToast.show(getActivity(), getString(R.string.login_library_fail));
                                getCapure(iv);
                            }

                            @Override
                            public void onVerify() {
                                MyToast.show(getActivity(), "您尚未完成身份认证，请进行身份核实");
                                etName.setVisibility(View.VISIBLE);
                                etName.requestFocus();
                                if (null != user) {
                                    etName.setText(user.getName());

                                }
                                verify = false;
                            }
                        });
                    } else {
                        if (TextUtils.isEmpty(etName.getText().toString().trim())) {
                            MyToast.show(getActivity(), getString(R.string.please_to_input, getString(R.string.name)));
                            return;
                        }
                        VerifyLibraryNameAsyc verifyLibraryNameAsyc = new VerifyLibraryNameAsyc(getActivity());
                        verifyLibraryNameAsyc.executeOnExecutor(((AppActivity) getActivity()).THREAD_POOL_EXECUTOR, etName.getText().toString().trim(), c.getCookie());
                        verifyLibraryNameAsyc.setOnTaskListener(new VerifyLibraryNameAsyc.onTaskListener<String>() {
                            @Override
                            public void onSuccess(String s) {
                                isLoginSUccess = true;
                                dialog.dismiss();
                                doGetMyInfo();
                            }

                            @Override
                            public void onFail() {
                                MyToast.show(getActivity(), "身份验证失败");
                            }
                        });
                    }
                }
            }

        });
    }

    private boolean canLogin(String account, String password, String verfy) {
        if (TextUtils.isEmpty(account)) {
            MyToast.show(getActivity(), getString(R.string.please_to_input, getString(R.string.account)));
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            MyToast.show(getActivity(), getString(R.string.please_to_input, getString(R.string.pwd)));
            return false;
        }
        if (TextUtils.isEmpty(verfy)) {
            MyToast.show(getActivity(), getString(R.string.please_to_input, getString(R.string.verfy)));
            return false;
        }
        return true;


    }

    private void getCapure(final ImageView iv) {
        GetLibraryCaptureAsync getLibraryCaptureAsync = new GetLibraryCaptureAsync(getActivity());
        getLibraryCaptureAsync.executeOnExecutor(((AppActivity) getActivity()).THREAD_POOL_EXECUTOR);
        getLibraryCaptureAsync.setOnTaskListener(new BaseAsyncTask.onTaskListener<GetLibraryCaptureAsync.Capture>() {
            @Override
            public void onSuccess(GetLibraryCaptureAsync.Capture s) {
                iv.setVisibility(View.VISIBLE);
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
