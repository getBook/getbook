package com.xfzj.getbook.views.view;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xfzj.getbook.R;
import com.xfzj.getbook.common.BookInfo;
import com.xfzj.getbook.loader.ImageLoader;

/**
 * Created by zj on 2016/2/24.
 */
public class BookInfoView extends LinearLayout {

    private NetImageView iv;
    private TextView bookName, bookIsbn, bookAuthor, bookPublisher, bookOriginPrice;
    private Context context;
    public BookInfoView(Context context) {
        this(context, null);
    }

    public BookInfoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BookInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    public BookInfoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.bookinfo, null);
        iv = (NetImageView) view.findViewById(R.id.iv);
        bookName = (TextView) view.findViewById(R.id.bookName);
        bookIsbn = (TextView) view.findViewById(R.id.bookIsbn);
        bookAuthor = (TextView) view.findViewById(R.id.bookAuthor);
        bookPublisher = (TextView) view.findViewById(R.id.bookPublisher);
        bookOriginPrice = (TextView) view.findViewById(R.id.bookOriginPrice);
        LayoutParams p = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(p);
        addView(view);

    }


    /**
     * 发布时使用的，更新书籍信息
     *
     * @param bookInfo
     */
    public void updateBookInfo(BookInfo bookInfo) {
        if (null == bookInfo) {
            return;
        }
        iv.setUrlImage(bookInfo.getImage(), bookInfo.getIsbn());
        if (!TextUtils.isEmpty(bookInfo.getBookName())) {
            bookName.setText(bookInfo.getBookName());
        }
        if (!TextUtils.isEmpty(bookInfo.getIsbn())) {
            bookIsbn.setText(bookInfo.getIsbn());
        }
        String[] a = bookInfo.getAuthor();
        if (null != a) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < a.length; i++) {
                sb.append(a[i]);
            }
            bookAuthor.setText(sb.toString());
        }
        if (!TextUtils.isEmpty(bookInfo.getPublish())) {
            bookPublisher.setText(bookInfo.getPublish());
        }
        if (!TextUtils.isEmpty(bookInfo.getOriginPrice())) {
            bookOriginPrice.setText(bookInfo.getOriginPrice());
        }
    }


    /**
     * 查看时使用的，更新书籍信息
     *
     * @return
     */
    public void updateBookInfo(BookInfo bookInfo, ImageLoader imageLoader) {
        if (null == bookInfo) {
            return;
        }
        iv.setBmobImage(bookInfo.getImage(), BitmapFactory.decodeResource(context.getResources(), R.mipmap.default_book), imageLoader,0,0);
        if (!TextUtils.isEmpty(bookInfo.getBookName())) {
            bookName.setText(bookInfo.getBookName());
        }
        if (!TextUtils.isEmpty(bookInfo.getIsbn())) {
            bookIsbn.setText(bookInfo.getIsbn());
        }
        String[] a = bookInfo.getAuthor();
        if (null != a) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < a.length; i++) {
                sb.append(a[i]);
            }
            bookAuthor.setText(sb.toString());
        }
        if (!TextUtils.isEmpty(bookInfo.getPublish())) {
            bookPublisher.setText(bookInfo.getPublish());
        }
        if (!TextUtils.isEmpty(bookInfo.getOriginPrice())) {
            bookOriginPrice.setText(bookInfo.getOriginPrice());
        }
    }

    public void setOriginPriceMiddleLine() {
        bookOriginPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }
    
    public String getBookName() {
        return bookName.getText().toString();
    }
}
