package com.xfzj.getbook.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.xfzj.getbook.MainActivity;
import com.xfzj.getbook.R;
import com.xfzj.getbook.activity.AppActivity;
import com.xfzj.getbook.activity.CaptureAty;
import com.xfzj.getbook.async.BaseAsyncTask;
import com.xfzj.getbook.async.GetBookInfoAsync;
import com.xfzj.getbook.async.LibraryRecommendAsync;
import com.xfzj.getbook.common.BookInfo;
import com.xfzj.getbook.net.BaseHttp;
import com.xfzj.getbook.utils.AppAnalytics;
import com.xfzj.getbook.utils.MyToast;
import com.xfzj.getbook.views.view.BaseEditText;
import com.xfzj.getbook.views.view.BaseToolBar;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zj on 2016/5/3.
 */
public class RecommendFrag extends BaseFragment implements View.OnClickListener, View.OnFocusChangeListener {
    public static final String ARG_PARAM1 = "RecommendFrag.class";
    public static final int SCAN = 23;
    public static final String ISBN = "isbn";
    @Bind(R.id.betIsbn)
    BaseEditText betIsbn;
    @Bind(R.id.ivScan)
    ImageView ivScan;
    @Bind(R.id.betName)
    BaseEditText betName;
    @Bind(R.id.betAuthor)
    BaseEditText betAuthor;
    @Bind(R.id.betPublisher)
    BaseEditText betPublisher;
    @Bind(R.id.betPublisherDate)
    BaseEditText betPublisherDate;
    @Bind(R.id.betRecommend)
    BaseEditText betRecommend;
    @Bind(R.id.rg)
    RadioGroup rg;
    private String mParam1;
    private BaseToolBar baseToolBar;
    private TextView tvPublish;
    private String scanIsbn;

    public static RecommendFrag newInstance(String param1) {
        RecommendFrag fragment = new RecommendFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public RecommendFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
        setHasOptionsMenu(true);

    }

    public void initToolbar(BaseToolBar baseToolBar) {
        this.baseToolBar = baseToolBar;
        tvPublish = this.baseToolBar.getTvRight2();
        tvPublish.setText(R.string.recom);
        tvPublish.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recommend, container, false);
        setVisibilty(View.VISIBLE);

        ButterKnife.bind(this, view);
        betIsbn.setOnFocusChangeListener(this);
        betIsbn.setEditTextInputType(InputType.TYPE_CLASS_NUMBER);
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
    }

    @Override
    public void onClick(View v) {
        if (canRecommend()) {
            AppAnalytics.onEvent(getActivity(), AppAnalytics.R_L_B);
            LibraryRecommendAsync libraryRecommendAsync = new LibraryRecommendAsync(getActivity());
            libraryRecommendAsync.executeOnExecutor(AppActivity.THREAD_POOL_EXECUTOR, wrapUrl());
            libraryRecommendAsync.setOnTaskListener(new BaseAsyncTask.onTaskListener<String>() {
                @Override
                public void onSuccess(String s) {
                    MyToast.show(getActivity(), s);
                    AppAnalytics.onEvent(getActivity(), AppAnalytics.R_L_B_S);
                    exitFragment();
                }

                @Override
                public void onFail() {
                    AppAnalytics.onEvent(getActivity(), AppAnalytics.R_L_B_F);
                    MyToast.show(getActivity(), getActivity().getString(R.string.recommendfail));
                }
            });
            
            
        }

    }

    private void exitFragment() {
        getParentFragment().getChildFragmentManager().popBackStack();
        setVisibilty(View.GONE);
    }

    private String wrapUrl() {
        String type = "C";
        switch (rg.getCheckedRadioButtonId()) {
            case R.id.rbC:
                type = "C";
                break;
            case R.id.rbU:
                type = "U";
                break;
        }
        return BaseHttp.RECOMMENDBOOK + "&title=" + encode(betName) +
                "&a_name=" + encode(betAuthor) + "&b_pub=" + encode(betPublisher) +
                "&b_date=" + encode(betPublisherDate) + "&b_type=" +encode(type)+
                "&b_isbn=" + encode(betIsbn) + "&b_remark=" + encode(betRecommend);
    }

    private String encode(String str) {
        try {
            return URLEncoder.encode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private String encode(BaseEditText bet) {
        try {
            return URLEncoder.encode(bet.getText(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean canRecommend() {
        if (TextUtils.isEmpty(betName.getText())) {
            betName.setError(getString(R.string.bookname_not_null));
            return false;
        } else {
            betName.setErrorEnable(false);
        }
        if (TextUtils.isEmpty(betAuthor.getText())) {
            betAuthor.setError(getString(R.string.author_not_null));
            return false;
        } else {
            betName.setErrorEnable(false);
        }
        return true;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            setVisibilty(View.VISIBLE);
        } else {
            setVisibilty(View.GONE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.setGroupVisible(R.id.group1, false);
    }

    public boolean isOriginState() {
        if (tvPublish.getVisibility() == View.VISIBLE) {
            return false;
        }
        return true;
    }

    public void setVisibilty(int i) {
        tvPublish.setVisibility(i);
        if (i == View.VISIBLE) {
            if (null != getActivity() && null != ((MainActivity) getActivity()).menu) {
                ((MainActivity) getActivity()).menu.setGroupVisible(R.id.group1, false);
            }
        } else {
            if (null != getActivity() && null != ((MainActivity) getActivity()).menu) {
                ((MainActivity) getActivity()).menu.setGroupVisible(R.id.group1, true);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.ivScan)
    public void onClick() {
        ivScan.setClickable(false);
        ivScan.postDelayed(new Runnable() {
            @Override
            public void run() {
                ivScan.setClickable(true);
            }
        }, 2000);
        MyToast.show(getActivity(), getActivity().getString(R.string.opencamera_wait));
        Intent intent = new Intent(getActivity(), CaptureAty.class);
        intent.putExtra(CaptureAty.FROM, RecommendFrag.class.getName());
        getParentFragment().startActivityForResult(intent, SCAN);
    }

    public void setIsbn(String isbn) {
        this.scanIsbn = isbn;
        betIsbn.setText(isbn);
        getBookinfo(isbn);
    }

    private void getBookinfo(String isbn) {
        GetBookInfoAsync getBookInfoAsync = new GetBookInfoAsync(getActivity());
        getBookInfoAsync.executeOnExecutor(((AppActivity) getActivity()).getThreadPoolExecutor(), isbn);
        getBookInfoAsync.setOnTaskListener(new BaseAsyncTask.onTaskListener<BookInfo>() {
            @Override
            public void onSuccess(BookInfo bookInfo) {
                updateBookinfo(bookInfo);
            }

            @Override
            public void onFail() {
                MyToast.show(getActivity(), getResources().getString(R.string.get_bookinfo_fail));
            }
        });
    }

    private void updateBookinfo(BookInfo bookInfo) {
        betName.setText(bookInfo.getBookName());
        String[] a = bookInfo.getAuthor();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < a.length; i++) {
            sb.append(a[i] + " ");
        }
        betAuthor.setText(sb.toString());
        betPublisher.setText(bookInfo.getPublish());
        betPublisherDate.setText(bookInfo.getPubdate());
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            String str = betIsbn.getText().trim();
            if (!TextUtils.isEmpty(str) && !str.equals(scanIsbn)) {
                getBookinfo(str);
            }
        }
    }
}
