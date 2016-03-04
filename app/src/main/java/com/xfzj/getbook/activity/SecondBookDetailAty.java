package com.xfzj.getbook.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xfzj.getbook.BaseApplication;
import com.xfzj.getbook.R;
import com.xfzj.getbook.common.BookInfo;
import com.xfzj.getbook.common.SecondBook;
import com.xfzj.getbook.common.User;
import com.xfzj.getbook.loader.ImageLoader;
import com.xfzj.getbook.utils.MyToast;
import com.xfzj.getbook.utils.MyUtils;
import com.xfzj.getbook.views.view.BaseToolBar;
import com.xfzj.getbook.views.view.BookInfoView;
import com.xfzj.getbook.views.view.NetImageView;
import com.xfzj.getbook.views.view.SimpleUserView;

import butterknife.Bind;

/**
 * Created by zj on 2016/3/4.
 */
public class SecondBookDetailAty extends AppActivity implements View.OnClickListener {
    @Bind(R.id.baseToolbar)
    BaseToolBar baseToolBar;
    @Bind(R.id.simpleUserView)
    SimpleUserView simpleUserView;
    @Bind(R.id.describe)
    EditText describe;
    @Bind(R.id.llPics)
    LinearLayout llPics;
    @Bind(R.id.ibSend)
    ImageButton ibSend;
    @Bind(R.id.bookInfoView)
    BookInfoView bookInfoView;
    @Bind(R.id.price)
    TextView price;
    @Bind(R.id.newold)
    TextView newold;
    @Bind(R.id.count)
    TextView count;
    public static final String DATA = "SecondBookDetailAty.class";


    private SecondBook secondBook;

    private User user;

    private BookInfo bookInfo;

    private ImageLoader imageLoader;

    @Override
    protected void onSetContentView() {
        setContentView(R.layout.aty_secondbook_detail);
    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {
        baseToolBar.initToolbar(this, getString(R.string.detail));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        secondBook = getIntentData();
        user = secondBook.getUser();
        bookInfo = secondBook.getBookInfo();
        imageLoader = ((BaseApplication) getApplicationContext()).getImageLoader();

        simpleUserView.update(user, imageLoader);
        describe.setMinimumHeight(0);
        describe.setText(secondBook.getTips());
        describe.setTextColor(getResources().getColor(R.color.primary_text));

        describe.setEnabled(false);
        ibSend.setOnClickListener(this);
        bookInfoView.updateBookInfo(bookInfo, imageLoader);
        bookInfoView.setOriginPriceMiddleLine();
        setBaseInfo();
        setSecondBookPics();

    }

    private void setBaseInfo() {
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
        count  .setText(strcount + "");
    }


    private void setSecondBookPics() {
        String[] pics = secondBook.getPictures();
        NetImageView[] ivs = new NetImageView[pics.length];
        int width = MyUtils.getScreenWidth(getApplicationContext());
        int margin = (int) MyUtils.dp2px(getApplicationContext(), 30.0f);
        int marginRight = (int) MyUtils.dp2px(getApplicationContext(), 15.0f);
        Bitmap bp = BitmapFactory.decodeResource(getResources(), R.mipmap.default_book);
        for (int i = 0; i < ivs.length; i++) {
            ivs[i] = new NetImageView(getApplicationContext());
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(width - margin, ViewGroup.LayoutParams.WRAP_CONTENT);
            p.gravity = Gravity.CENTER_HORIZONTAL;
            p.setMargins(0, 0, 0, marginRight);
            ivs[i].setLayoutParams(p);
            ivs[i].setAdjustViewBounds(true);
            ivs[i].setScaleType(ImageView.ScaleType.FIT_XY);

            ivs[i].setBmobImage(pics[i], bp, imageLoader, width, width);

            llPics.addView(ivs[i]);
        }
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
        String tele = secondBook.getTelePhone();
        if (TextUtils.isEmpty(tele)) {
            MyToast.show(getApplicationContext(), getString(R.string.no_tele));
            return;
        }
        String moudle = getString(R.string.send_message, getString(R.string.app_name), secondBook.getBookInfo().getBookName());
        Uri uri = Uri.parse("smsto:" + tele);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", moudle);
        startActivity(intent);
    }
}
