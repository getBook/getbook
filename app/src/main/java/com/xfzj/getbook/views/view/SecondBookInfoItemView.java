package com.xfzj.getbook.views.view;

import android.content.Context;
import android.graphics.Bitmap;
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

import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.xfzj.getbook.Constants;
import com.xfzj.getbook.R;
import com.xfzj.getbook.common.BookInfo;
import com.xfzj.getbook.common.SecondBook;
import com.xfzj.getbook.utils.MyUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zj on 2016/2/29.
 */
public class SecondBookInfoItemView extends FrameLayout implements View.OnLongClickListener, View.OnClickListener {
    private TextView tvBookName, tvIsbn, tvBookAuthor, tvPublisher, tvPrice, tvOldPrice, tvNewold, tvDate, tvCount, tvYuan;
    private NetImageView ivBook;
    private Context context;
    private onClickListener  onSecondBookInfoClick;
    private onLongClickListener onSecondBookInfoLongClick;
    private SecondBook secondBook;
    private ImageView ivDate, ivCount, ivNewOld,ivYxj;
    private LinearLayout llitemView;
    
    public <T> void setOnSecondBookInfoClick(onClickListener<T> onSecondBookInfoClick) {
        this.onSecondBookInfoClick = onSecondBookInfoClick;
    }

    public void setOnSecondBookInfoLongClick(onLongClickListener onSecondBookInfoLongClick) {
        this.onSecondBookInfoLongClick = onSecondBookInfoLongClick;
    }

    public SecondBookInfoItemView(Context context) {
        this(context, null);
    }

    public SecondBookInfoItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SecondBookInfoItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public SecondBookInfoItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.secondbookinfo_item, null);
        tvBookName = (TextView) view.findViewById(R.id.tvBookName);
        tvIsbn = (TextView) view.findViewById(R.id.tvIsbn);
        tvBookAuthor = (TextView) view.findViewById(R.id.tvBookAuthor);
        tvPublisher = (TextView) view.findViewById(R.id.tvPublisher);
        tvPrice = (TextView) view.findViewById(R.id.tvPrice);
        tvOldPrice = (TextView) view.findViewById(R.id.tvOldPrice);
        tvNewold = (TextView) view.findViewById(R.id.tvNewold);
        tvDate = (TextView) view.findViewById(R.id.tvDate);
        ivBook = (NetImageView) view.findViewById(R.id.ivBook);
        tvCount = (TextView) view.findViewById(R.id.tvCount);
        tvYuan = (TextView) view.findViewById(R.id.tvYuan);
        ivDate = (ImageView) view.findViewById(R.id.ivDate);
        ivCount = (ImageView) view.findViewById(R.id.ivCount);
        ivNewOld = (ImageView) view.findViewById(R.id.ivNewOld);
        ivYxj = (ImageView) view.findViewById(R.id.ivYxj);
        llitemView = (LinearLayout) view.findViewById(R.id.llitemView);
        llitemView.setOnClickListener(this);
        llitemView.setOnLongClickListener(this);
        addView(view);
    }

    public SecondBook getSecondBook() {
        return secondBook;
    }

    public void update(SecondBook secondBook) {
        if (null == secondBook) {
            return;
        }
        this.secondBook = secondBook;
        BookInfo bookInfo = secondBook.getBookInfo();
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
        } else {
            tvDate.setText("1-1");
        }


        updateBookInfo(bookInfo);

    }


    private void updateBookInfo(BookInfo bookInfo) {
        if (null == bookInfo) {
            return;
        }
        tvIsbn.setText(bookInfo.getIsbn());
        ivBook.setBmobFileImage(bookInfo.getBmobImage());
        String[] a = bookInfo.getAuthor();
        if (null != a && a.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < a.length; i++) {
                sb.append(a[i]);
            }
            tvBookAuthor.setText(sb.toString());
        } else {
            tvBookAuthor.setText(R.string.no_author);
        }

        if (!TextUtils.isEmpty(bookInfo.getBookName())) {
            tvBookName.setText(bookInfo.getBookName());
        } else {
            tvBookName.setText(R.string.no_bookname);
        }
        if (!TextUtils.isEmpty(bookInfo.getPublish())) {
            tvPublisher.setText(bookInfo.getPublish());
        } else {
            tvPublisher.setText(R.string.no_publisher);
        }
        if (!TextUtils.isEmpty(bookInfo.getOriginPrice())) {
            tvOldPrice.setText(bookInfo.getOriginPrice());
        } else {
            tvOldPrice.setText(R.string.no_price);
        }
        tvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

    }

    //    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.llUserInfo:
//                if (null != onUserInfoClick) {
//                    onUserInfoClick.onClick(secondBook.getUser());
//                }
//
//                break;
//            case R.id.llSecondBookInfo:
//                if (null != onUserInfoClick) {
//                    onSecondBookInfoClick.onClick(secondBook);
//                }
//                break;
//        }
//
//
//    }
    public void doInvalid() {
        if (null == secondBook) {
            return;
        }
        String updateTime = secondBook.getUpdatedAt();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(updateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (null == date) {
            handleInvalid();
        } else {
            long update = date.getTime();
            long now = System.currentTimeMillis();
            if (now - update >= Constants.day * 24 * 60 * 60 *1000) {
                handleInvalid();
            }else {
                restartOnSale(secondBook);
            }
        }


    }

    private void handleInvalid() {
        Bitmap bitmap = ((GlideBitmapDrawable) ivBook.getDrawable()).getBitmap();
        ivBook.setImageBitmap(MyUtils.toGrayscale(bitmap));
        int color = context.getResources().getColor(R.color.secondary_text);
        tvBookName.setTextColor(color);
        tvYuan.setTextColor(color);
        tvPrice.setTextColor(color);
        ivDate.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.date_invalid));
        ivCount.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.book_count_invalid));
        ivNewOld.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.quality_invalid));
        ivYxj.setVisibility(VISIBLE);
    }

    public void restartOnSale(SecondBook secondBook) {
        ivBook.setBmobFileImage(secondBook.getBookInfo().getBmobImage());
        int color1 = context.getResources().getColor(R.color.accent);
        int color2 = context.getResources().getColor(R.color.primary_text);
        tvBookName.setTextColor(color2);
        tvYuan.setTextColor(color1);
        tvPrice.setTextColor(color1);
        ivDate.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.date));
        ivCount.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.book_count));
        ivNewOld.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.quality));
        ivYxj.setVisibility(GONE);
    }

    @Override
    public boolean onLongClick(View v) {
        if (null != onSecondBookInfoLongClick) {
            onSecondBookInfoLongClick.onLongClick(v);
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (null != onSecondBookInfoClick) {
            onSecondBookInfoClick.onClick(v);
        }
    }

    public interface onClickListener<T> {
        void onClick(T t);
    }


    public interface onLongClickListener<T> {
        void onLongClick(T t);
    }


}
