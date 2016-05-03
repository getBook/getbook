package com.xfzj.getbook.views.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.xfzj.getbook.R;
import com.xfzj.getbook.activity.AppActivity;
import com.xfzj.getbook.async.BaseAsyncTask;
import com.xfzj.getbook.async.GetLibraryVerfyAsync;
import com.xfzj.getbook.async.RenewBookAsync;
import com.xfzj.getbook.common.BorrowBook;
import com.xfzj.getbook.fragment.LibraryFrag;
import com.xfzj.getbook.net.BaseHttp;
import com.xfzj.getbook.utils.AppAnalytics;
import com.xfzj.getbook.utils.MyLog;
import com.xfzj.getbook.utils.MyToast;
import com.xfzj.getbook.utils.MyUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zj on 2016/5/1.
 */
public class BookListView extends FrameLayout {
    @Bind(R.id.tvName)
    TextView tvName;
    @Bind(R.id.tvAuthor)
    TextView tvAuthor;
    @Bind(R.id.btnBorrow)
    Button btnBorrow;
    @Bind(R.id.tvBorrowDate)
    TextView tvBorrowDate;
    @Bind(R.id.tvReturnDate)
    TextView tvReturnDate;
    @Bind(R.id.tvFeedBack)
    TextView tvFeedBack;
    private Context context;
    private BorrowBook borrowBook;

    EditText etVerfy;

    ImageView iv;

    TextView btn;
    private LibraryFrag libraryFrag;

    public BookListView(Context context) {
        this(context, null);
    }

    public BookListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BookListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public BookListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.booklist, null);
        ButterKnife.bind(this, view);
        addView(view);
    }

    public void update(LibraryFrag libraryFrag, BorrowBook borrowBook) {
        if (null == borrowBook) {
            return;
        }
        this.libraryFrag = libraryFrag;
        this.borrowBook = borrowBook;
        String[] strs = borrowBook.getBookName().split("/\\s+");
        tvName.setText(strs[0]);
        tvAuthor.setText(strs[1]);
        tvBorrowDate.setText(context.getString(R.string.borrowdate) + borrowBook.getBorrowDate());
        tvReturnDate.setText(context.getString(R.string.returndate) + borrowBook.getReturnDate());
    }

    @OnClick(R.id.btnBorrow)
    public void onClick() {
        AppAnalytics.onEvent(context, AppAnalytics.C_R_B);
        if (!TextUtils.isEmpty(libraryFrag.getVerfy())) {
            renewBook(libraryFrag.getVerfy());
            return;
        }
        GetLibraryVerfyAsync getLibraryVerfyAsync = new GetLibraryVerfyAsync(libraryFrag.getActivity());
        getLibraryVerfyAsync.executeOnExecutor(AppActivity.THREAD_POOL_EXECUTOR,BaseHttp.CAPTCHA);
        getLibraryVerfyAsync.setOnTaskListener(new BaseAsyncTask.onTaskListener<Bitmap>() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                iv.setVisibility(View.VISIBLE);
                iv.setImageBitmap(bitmap);
            }

            @Override
            public void onFail() {
                onClick();
            }
        });
        View view = LayoutInflater.from(context).inflate(R.layout.library_verify, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(libraryFrag.getActivity());
        int margin = (int) MyUtils.dp2px(context, 5f);
        final AlertDialog ad = builder.setView(view, margin, margin, margin, margin).create();
        ad.show();
        etVerfy = (EditText) view.findViewById(R.id.etVerfy);
        iv = (ImageView) view.findViewById(R.id.iv);
        btn = (TextView) view.findViewById(R.id.btn);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String verfy = etVerfy.getText().toString().trim();
                if (TextUtils.isEmpty(verfy)) {
                    MyToast.show(context, context.getString(R.string.please_to_input, context.getString(R.string.verfy)));
                } else {
                    ad.dismiss();
                    renewBook(verfy);
                }
            }
        });
    }


    private void renewBook(final String verfy) {
        RenewBookAsync renewBookAsync = new RenewBookAsync(libraryFrag.getActivity());
        renewBookAsync.executeOnExecutor(AppActivity.THREAD_POOL_EXECUTOR, wrapUrl(verfy));
        renewBookAsync.setOnTaskListener(new BaseAsyncTask.onTaskListener<String>() {
            @Override
            public void onSuccess(String s) {
                tvFeedBack.setVisibility(VISIBLE);
                MyLog.print(s, "");
                if ("invalid call".equals(s)) {
                    tvFeedBack.setText(R.string.renew_fail);
                    AppAnalytics.onEvent(context, AppAnalytics.R_B_F);
                }else{
                    tvFeedBack.setText(Html.fromHtml(s));
                    libraryFrag.setVerfy(verfy);
                }
            }

            @Override
            public void onFail() {
                tvFeedBack.setVisibility(VISIBLE);
                tvFeedBack.setText(R.string.renew_fail);
            }
        });

    }


    private String wrapUrl(String verfy) {
        return BaseHttp.RENEWBOOK + "bar_code=" + borrowBook.getCode() + "&check=" + borrowBook.getCheck() + "&captcha=" + verfy + "&time=" + System.currentTimeMillis();
    }

    private interface OnVerfyGetListener {
        void getVerfy(String s);
    }
    
}
