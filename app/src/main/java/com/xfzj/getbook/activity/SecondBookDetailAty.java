package com.xfzj.getbook.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.xfzj.getbook.R;
import com.xfzj.getbook.common.BookInfo;
import com.xfzj.getbook.common.SecondBook;
import com.xfzj.getbook.common.User;
import com.xfzj.getbook.utils.MyToast;
import com.xfzj.getbook.views.view.BookInfoView;

import butterknife.Bind;
import cn.bmob.v3.BmobUser;

/**
 * Created by zj on 2016/3/4.
 */
public class SecondBookDetailAty extends DetailActivity {

    @Bind(R.id.bookInfoView)
    BookInfoView bookInfoView;

    public static final String DATA = "SecondBookDetailAty.class";


    private SecondBook secondBook;


    private BookInfo bookInfo;


    @Override
    protected void onSetContentView() {
        setContentView(R.layout.aty_secondbook_detail);
    }

    @Override
    protected void onViewCreate(Bundle savedInstanceState) {
        secondBook = getIntentData();
        if (null == secondBook) {
            finish();
            return;
        }
        user = secondBook.getUser();
        if (null == user) {
            finish();
            return;
        }

        bookInfo = secondBook.getBookInfo();

        simpleUserView.update(user);
        String tips = secondBook.getTips();
        if (TextUtils.isEmpty(tips)) {
            tv.setText(tv.getText().toString() + getString(R.string.zanwu));
            describe.setVisibility(View.GONE);
        } else {
            describe.setText(secondBook.getTips());
            describe.setTextColor(getResources().getColor(R.color.primary_text));
        }

        bookInfoView.updateBookInfoUrl(bookInfo);
        bookInfoView.setOriginPriceMiddleLine();
    }

    /**
     * 发送信息模块是否要显示
     */
    @Override
    protected void needHiden() {
        User currentUser = BmobUser.getCurrentUser(getApplicationContext(), User.class);
        if (null != currentUser && user.equals(currentUser)) {
            llSendSms.setVisibility(View.GONE);
        } else {
            llSendSms.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void setBaseInfo() {
        if (null == secondBook) {
            return;
        }
        String strprice = secondBook.getDiscount();
        if (TextUtils.isEmpty(strprice)) {
            price.setText(getString(R.string.no_price));
        } else {
            price.setText(strprice);
        }

        String strnewold = secondBook.getNewold();
        if (TextUtils.isEmpty(strnewold)) {
            newold.setText("0" + getString(R.string.chengxin));
        } else {
            newold.setText(strnewold + getString(R.string.chengxin));
        }
        int strcount = secondBook.getCount();
        count.setText(strcount + "");
    }

    @Override
    protected void setPics() {
        bmobFiles = secondBook.getFiles();
    }

    private SecondBook getIntentData() {
        SecondBook secondBook = (SecondBook) getIntent().getSerializableExtra(DATA);
        if (null == secondBook || null == secondBook.getUser() || null == secondBook.getBookInfo()) {
            MyToast.show(getApplicationContext(), getString(R.string.error));
            finish();
        }
        return secondBook;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            finish();
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ibSend) {
            sendSms(secondBook.getTelePhone(), secondBook.getBookInfo().getBookName(), R.string.secondbook);
        }

    }

    @Override
    protected String getName() {
        if (null != secondBook) {
          return   secondBook.getBookInfo().getBookName();
        }
        return null;
    }

    @Override
    protected String getDiscount() {
        if (null != secondBook) {
          return   secondBook.getDiscount();
        }
        return null;
    }
}
