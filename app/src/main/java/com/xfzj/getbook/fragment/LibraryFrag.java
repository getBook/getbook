package com.xfzj.getbook.fragment;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.xfzj.getbook.BaseApplication;
import com.xfzj.getbook.R;
import com.xfzj.getbook.activity.AppActivity;
import com.xfzj.getbook.async.BaseAsyncTask;
import com.xfzj.getbook.async.ChangeLibraryPwdAsyc;
import com.xfzj.getbook.async.GetBookListAsync;
import com.xfzj.getbook.async.GetLibraryCaptureAsync;
import com.xfzj.getbook.async.GetLibraryMyInfo;
import com.xfzj.getbook.async.LoginLibraryAsyc;
import com.xfzj.getbook.async.VerifyLibraryNameAsyc;
import com.xfzj.getbook.common.BorrowBook;
import com.xfzj.getbook.common.LibraryInfo;
import com.xfzj.getbook.common.LibraryUserInfo;
import com.xfzj.getbook.common.User;
import com.xfzj.getbook.net.BaseHttp;
import com.xfzj.getbook.utils.AppAnalytics;
import com.xfzj.getbook.utils.InputMethodManagerUtils;
import com.xfzj.getbook.utils.MyToast;
import com.xfzj.getbook.utils.MyUtils;
import com.xfzj.getbook.utils.SharedPreferencesUtils;
import com.xfzj.getbook.views.view.BookListView;
import com.xfzj.getbook.views.view.CircleImageView;
import com.xfzj.getbook.views.view.NodataView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LibraryFrag extends BaseFragment {
    public static final String ARG_PARAM1 = "AsordFrag.class";


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
    @Bind(R.id.tvComingOverdue)
    TextView tvComingOverdue;
    @Bind(R.id.tvhasOverdue)
    TextView tvhasOverdue;
    @Bind(R.id.tvOrder)
    TextView tvOrder;
    @Bind(R.id.tvEntrust)
    TextView tvEntrust;
    @Bind(R.id.tvRecommend)
    TextView tvRecommend;
    @Bind(R.id.tvRecommendHandle)
    TextView tvRecommendHandle;
    @Bind(R.id.llBookList)
    LinearLayout llBookList;
    @Bind(R.id.rl)
    RelativeLayout rl;
    @Bind(R.id.fab1)
    FloatingActionButton fab1;
    @Bind(R.id.fab2)
    FloatingActionButton fab2;
    @Bind(R.id.fab3)
    FloatingActionButton fab3;
    @Bind(R.id.fab4)
    FloatingActionButton fab4;
    @Bind(R.id.fab)
    FloatingActionsMenu fab;
    private boolean verify = true;
    private User user;
    private LibraryInfo libraryInfo;
    private GetLibraryCaptureAsync.Capture c;
    private boolean isLoginSUccess = false;
    public String mParam1;
    /**
     * 续借的验证码
     */
    private String verfy;
    private OnFabClickLinstener onFabClickLinstener;

    public String getVerfy() {
        return verfy;
    }

    public void setVerfy(String verfy) {
        this.verfy = verfy;
    }

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
        ButterKnife.bind(this, view);
        getMyInfo();
       

        return view;
    }

    /**
     * 获取借阅信息
     */
    private void getBooklist() {
        llBookList.removeAllViews();
        final GetBookListAsync getBookListAsync = new GetBookListAsync(getActivity());
        getBookListAsync.executeOnExecutor(AppActivity.getThreadPoolExecutor(), BaseHttp.GETBOOKLIST);
        getBookListAsync.setOnTaskListener(new BaseAsyncTask.onTaskListener<List<BorrowBook>>() {
            @Override
            public void onSuccess(List<BorrowBook> borrowBooks) {
                if (null == borrowBooks || borrowBooks.size() == 0) {
                    TextView textView = new TextView(getActivity());
                    Drawable drawable = ContextCompat.getDrawable(getActivity(), R.mipmap.borrow);
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                    textView.setCompoundDrawables(null, drawable, null, null);
                    textView.setText(R.string.no_borrow_book);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) MyUtils.dp2px(getActivity(), 100f));
                    textView.setCompoundDrawablePadding((int) MyUtils.dp2px(getActivity(), 0f));
                    textView.setLayoutParams(lp);
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextSize(16f);
                    llBookList.addView(textView);
                    return;
                }
                for (BorrowBook borrowBook : borrowBooks) {
                    BookListView bookListView = new BookListView(getActivity());
                    bookListView.update(LibraryFrag.this,borrowBook);
                    
                    llBookList.addView(bookListView);
                }
            }

            @Override
            public void onFail() {
                NodataView nodataView = new NodataView(getActivity());
                int padding = (int) MyUtils.dp2px(getActivity(), 10f);
                nodataView.setPadding(padding, padding, padding, padding);
                nodataView.show();
                llBookList.addView(nodataView);
                nodataView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getBooklist();
                    }
                });
            }
        });


    }
    

    private void changeLibraryPwd() {
        AppAnalytics.onEvent(getActivity(), AppAnalytics.C_L_C_P);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.xiugaimima, null);
        final EditText etOld = (EditText) view.findViewById(R.id.etOld);
        final EditText etNew1 = (EditText) view.findViewById(R.id.etNew1);
        final EditText etNew2 = (EditText) view.findViewById(R.id.etNew2);
        final TextView btn = (TextView) view.findViewById(R.id.btn);
        builder.setView(view);
        etOld.setHint("图书馆旧密码");
        etNew1.setHint("图书馆新密码,只能是数字和字母");
        etNew2.setHint("图书馆新密码,只能是数字和字母");
        etOld.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
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

    private void exitLibrary() {
        AppAnalytics.onEvent(getActivity(), AppAnalytics.E_L_E_A);
        SharedPreferencesUtils.clearLibrary(getActivity());
        openLoginDialog();
        rl.setVisibility(View.GONE);
        isLoginSUccess = false;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!isLoginSUccess && !hidden) {
            openLoginDialog();
            rl.setVisibility(View.GONE);
        }
        if (null != fab) {
            fab.collapseImmediately();
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
                isLoginSUccess = true;
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
        getBooklist();
        libraryInfo = SharedPreferencesUtils.getLibraryLoginInfo(getActivity());
        rl.setVisibility(View.VISIBLE);
        String[] bookInfo = libraryUserInfo.getBookInfo();
        tvComingOverdue.setText(bookInfo[0]);
        tvhasOverdue.setText(bookInfo[1]);
        tvOrder.setText(bookInfo[2]);
        tvEntrust.setText(bookInfo[3]);
        tvRecommend.setText(bookInfo[4]);
        tvRecommendHandle.setText(bookInfo[5]);
        tvOwnMoney.setText(libraryUserInfo.getOwnMoney());
        tvBorrowCount.setText(libraryUserInfo.getBorrowCount());
        tvMaxBorrow.setText(libraryUserInfo.getMaxBorrow());
        tvIllegalCount.setText(libraryUserInfo.getIllegalCount());
        String header = SharedPreferencesUtils.getUserHeader(getActivity());
        if (!TextUtils.isEmpty(header) && null != libraryInfo && null != user && libraryInfo.getAccount().equals(user.getSno())) {
            circleImageView.setVisibility(View.VISIBLE);
            circleImageView.setNetImage(header);
        } else {
           circleImageView.setVisibility(View.GONE);
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
        final TextView btn = (TextView) view.findViewById(R.id.btn);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.fab1, R.id.fab2,R.id.fab3,R.id.fab4})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab1:
                fab.collapseImmediately();
                exitLibrary();
                break;
            case R.id.fab2:
                fab.collapseImmediately();
                changeLibraryPwd();
                break;
            case R.id.fab3:
                fab.collapseImmediately();
                if (null != onFabClickLinstener) {
                    onFabClickLinstener.onClickRecommend();
                }
                break;
            case R.id.fab4:
                fab.collapseImmediately();
                AppAnalytics.onEvent(getActivity(), AppAnalytics.C_R_B_H);
                if (null != onFabClickLinstener) {
                    onFabClickLinstener.onClickRecommendHistory();
                }
                break;
        }
    }

    public void setOnFabClickLinstener(OnFabClickLinstener onFabClickLinstener) {
        this.onFabClickLinstener = onFabClickLinstener;
    }

    public interface  OnFabClickLinstener{
        void onClickRecommendHistory();

        void onClickRecommend();
    }
}
