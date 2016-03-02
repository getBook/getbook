package com.xfzj.getbook.views.view;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xfzj.getbook.R;
import com.xfzj.getbook.common.BookInfo;
import com.xfzj.getbook.common.SecondBook;
import com.xfzj.getbook.common.User;
import com.xfzj.getbook.loader.ImageLoader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zj on 2016/2/29.
 */
public class SecondBookInfoView extends FrameLayout implements View.OnClickListener {
    private CircleImageView ivHeader;
    private ImageView ivSex;
    private TextView tvName, tvBookName, tvIsbn, tvBookAuthor, tvPublisher, tvPrice, tvOldPrice, tvNewold, tvDate, tvCount, tvYuan;
    private NetImageView ivBook;
    private LinearLayout llUserInfo, llSecondBookInfo;
    private Context context;
    private onClickListener onUserInfoClick, onSecondBookInfoClick;
    private SecondBook secondBook;
    private ImageLoader imageLoader;
    public <T> void setOnUserInfoClick(onClickListener<T> onUserInfoClick) {
        this.onUserInfoClick = onUserInfoClick;
    }

    public <T> void setOnSecondBookInfoClick(onClickListener<T> onSecondBookInfoClick) {
        this.onSecondBookInfoClick = onSecondBookInfoClick;
    }

    public SecondBookInfoView(Context context) {
        this(context, null);
    }

    public SecondBookInfoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SecondBookInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public SecondBookInfoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.secondbook_info, null);
        ivHeader = (CircleImageView) view.findViewById(R.id.ivHeader);
        ivSex = (ImageView) view.findViewById(R.id.ivSex);
        tvName = (TextView) view.findViewById(R.id.tvName);
        tvBookName = (TextView) view.findViewById(R.id.tvBookName);
        tvIsbn = (TextView) view.findViewById(R.id.tvIsbn);
        tvBookAuthor = (TextView) view.findViewById(R.id.tvBookAuthor);
        tvPublisher = (TextView) view.findViewById(R.id.tvPublisher);
        tvPrice = (TextView) view.findViewById(R.id.tvPrice);
        tvOldPrice = (TextView) view.findViewById(R.id.tvOldPrice);
        tvNewold = (TextView) view.findViewById(R.id.tvNewold);
        tvDate = (TextView) view.findViewById(R.id.tvDate);
        ivBook = (NetImageView) view.findViewById(R.id.ivBook);
        llUserInfo = (LinearLayout) view.findViewById(R.id.llUserInfo);
        llSecondBookInfo = (LinearLayout) view.findViewById(R.id.llSecondBookInfo);
        tvCount = (TextView) view.findViewById(R.id.tvCount);
        tvYuan = (TextView) view.findViewById(R.id.tvYuan);
        llUserInfo.setOnClickListener(this);
        llSecondBookInfo.setOnClickListener(this);
        imageLoader = ImageLoader.build(context,BitmapFactory.decodeResource(context.getResources(), R.mipmap.default_user));
        addView(view);
    }

    public void update(SecondBook secondBook) {
        if (null == secondBook) {
            return;
        }
        this.secondBook = secondBook;
        BookInfo bookInfo = secondBook.getBookInfo();
        updateBookInfo(bookInfo);


        User user = secondBook.getUser();
        if (null != user) {
            ivHeader.setBmobImage(user.getHeader(), BitmapFactory.decodeResource(context.getResources(), R.mipmap.default_user),imageLoader);
            tvName.setText(user.getHuaName());
            if (user.isGender()) {
                ivSex.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.male));
            } else {
                ivSex.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.female));
            }
            if (!TextUtils.isEmpty(secondBook.getDiscount())) {
                tvPrice.setText(secondBook.getDiscount());
            }
            if (!TextUtils.isEmpty(secondBook.getNewold())) {
                tvNewold.setText(secondBook.getNewold() + "成新");
            }

            tvCount.setText(secondBook.getCount() + "");

            String str = secondBook.getUpdatedAt();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = sdf.parse(str);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (null != date) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                tvDate.setText(calendar.get(Calendar.MONTH) + 1 + "-" + calendar.get(Calendar.DAY_OF_MONTH));
            }


        }

    }


    private void updateBookInfo(BookInfo bookInfo) {
        if (null == bookInfo) {
            return;
        }
        tvIsbn.setText(bookInfo.getIsbn());
        ivBook.setBmobImage(bookInfo.getImage(), BitmapFactory.decodeResource(context.getResources(), R.mipmap.default_book),imageLoader);
        String[] a = bookInfo.getAuthor();
        if (null != a && a.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < a.length; i++) {
                sb.append(a[i]);
            }
            tvBookAuthor.setText(sb.toString());
        }

        if (!TextUtils.isEmpty(bookInfo.getBookName())) {
            tvBookName.setText(bookInfo.getBookName());
        }
        if (!TextUtils.isEmpty(bookInfo.getPublish())) {
            tvPublisher.setText(bookInfo.getPublish());
        }
        if (!TextUtils.isEmpty(bookInfo.getOriginPrice())) {
            tvOldPrice.setText(bookInfo.getOriginPrice());
            tvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            tvYuan.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llUserInfo:
                if (null != onUserInfoClick) {
                    onUserInfoClick.onClick(secondBook.getUser());
                }

                break;
            case R.id.llSecondBookInfo:
                if (null != onUserInfoClick) {
                    onSecondBookInfoClick.onClick(secondBook);
                }
                break;
        }


    }

    public interface onClickListener<T> {
        void onClick(T t);
    }
}
