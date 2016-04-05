package com.xfzj.getbook.views.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.xfzj.getbook.R;
import com.xfzj.getbook.common.LibraryBook;

/**
 * Created by zj on 2016/4/2.
 */
public class LibraryBookItemView extends FrameLayout {
    private Context context;
    private TextView tvName, tvBooktype, tvAP, tvCount, tvCanBorrow, tvPosition;
    private LibraryBook libraryBook;

    public LibraryBookItemView(Context context) {
        this(context, null);
    }

    public LibraryBookItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LibraryBookItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public LibraryBookItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.librarybook_item, null);
        tvName = (TextView) view.findViewById(R.id.tvName);
        tvBooktype = (TextView) view.findViewById(R.id.tvBooktype);
        tvAP = (TextView) view.findViewById(R.id.tvAP);
        tvCount = (TextView) view.findViewById(R.id.tvCount);
        tvCanBorrow = (TextView) view.findViewById(R.id.tvCanBorrow);
        tvPosition = (TextView) view.findViewById(R.id.tvPosition);
        addView(view);
    }


    public void update(LibraryBook libraryBook) {
        if (null == libraryBook) {
            return;
        }
        this.libraryBook = libraryBook;
        String name = libraryBook.getTitle().trim();
        if (TextUtils.isEmpty(name)) {
            tvName.setText("");

        } else {
            tvName.setText(transfer(name));
        }

        String bookType = libraryBook.getLibraryType().trim();
        if (TextUtils.isEmpty(bookType)) {
            tvBooktype.setText("");

        } else {
            tvBooktype.setText(transfer(bookType));
        }
        String ap = libraryBook.getAp().trim();
        if (TextUtils.isEmpty(ap)) {
            tvAP.setText("");

        } else {
            tvAP.setText(transfer(ap));
        }
        String count = libraryBook.getCount().trim();
        if (TextUtils.isEmpty(count)) {
            tvCount.setText("");

        } else {
            tvCount.setText(transfer(count));
        }
        String canBorrow = libraryBook.getBorrowCount().trim();
        if (TextUtils.isEmpty(canBorrow)) {
            tvCanBorrow.setText("");

        } else {
            tvCanBorrow.setText(transfer(canBorrow));
        }
        String position = libraryBook.getPosition().trim();
        if (TextUtils.isEmpty(position)) {
            tvPosition.setText("");
        } else {
            tvPosition.setText(transfer(position));
        }


    }

    private String transfer(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        str = str.replaceAll("&amp;", "&");
        str = str.replaceAll("&lt;", "<");
        str = str.replaceAll("&gt;", ">");
        return str;
    }
    
}
