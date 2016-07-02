package com.xfzj.getbook.views.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.xfzj.getbook.R;
import com.xfzj.getbook.common.SecondBook;
import com.xfzj.getbook.common.User;

/**
 * Created by zj on 2016/2/29.
 */
public class SecondBookInfoView extends FrameLayout implements View.OnClickListener, SecondBookInfoItemView.onClickListener<Object> {
    private RelativeLayout llUserInfo;
    private Context context;
    private onClickListener onUserInfoClick, onSecondBookInfoClick;
    private SecondBook secondBook;
    private SimpleUserView simpleUserView;
    private SecondBookInfoItemView itemView;

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
        simpleUserView = (SimpleUserView) view.findViewById(R.id.simpleUserView);
        llUserInfo = (RelativeLayout) view.findViewById(R.id.llUserInfo);
        llUserInfo.setOnClickListener(this);
        itemView = (SecondBookInfoItemView) view.findViewById(R.id.itemView);
        itemView.setOnSecondBookInfoClick(this);
        addView(view);
    }

    public void update(SecondBook secondBook) {
        if (null == secondBook) {
            return;
        }
        this.secondBook = secondBook;
        User user = secondBook.getUser();
        simpleUserView.update(user);
        itemView.update(secondBook);
    }

    public String getSecondBookImage() {
        if (null == itemView) {
            return null;
        }
        return itemView.getSecondBookImage();
    }
//    private void updateBookInfo(BookInfo bookInfo) {
//        if (null == bookInfo) {
//            return;
//        }
//        tvIsbn.setText(bookInfo.getIsbn());
//        ivBook.setNetImage(bookInfo.getImage(), BitmapFactory.decodeResource(context.getResources(), R.mipmap.default_book),0,0);
//        String[] a = bookInfo.getAuthor();
//        if (null != a && a.length > 0) {
//            StringBuilder sb = new StringBuilder();
//            for (int i = 0; i < a.length; i++) {
//                sb.append(a[i]);
//            }
//            tvBookAuthor.setText(sb.toString());
//        } else {
//            tvBookAuthor.setText(R.string.no_author);
//        }
//
//        if (!TextUtils.isEmpty(bookInfo.getBookName())) {
//            tvBookName.setText(bookInfo.getBookName());
//        } else {
//            tvBookName.setText(R.string.no_bookname);
//        }
//        if (!TextUtils.isEmpty(bookInfo.getPublish())) {
//            tvPublisher.setText(bookInfo.getPublish());
//        } else {
//            tvPublisher.setText(R.string.no_publisher);
//        }
//        if (!TextUtils.isEmpty(bookInfo.getOriginPrice())) {
//            tvOldPrice.setText(bookInfo.getOriginPrice());
//        } else {
//            tvOldPrice.setText(R.string.no_price);
//        }
//        tvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llUserInfo:
                if (null != onUserInfoClick) {
                    onUserInfoClick.onClick(secondBook.getUser());
                }

                break;
        }


    }

    @Override
    public void onClick(Object o) {
        if (null != onSecondBookInfoClick) {
            onSecondBookInfoClick.onClick(secondBook);
        }
    }


    public interface onClickListener<T> {
        void onClick(T t);
    }
}
