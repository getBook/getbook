package com.xfzj.getbook.views.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.xfzj.getbook.R;
import com.xfzj.getbook.common.BorrowBook;

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
    private Context context;
    private BorrowBook borrowBook;

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

    public void update(BorrowBook borrowBook) {
        if (null == borrowBook) {
            return;
        }
        this.borrowBook = borrowBook;
        String[] strs = borrowBook.getBookName().split("/\\s+");
        tvName.setText(strs[0]);
        tvAuthor.setText(strs[1]);
    }
    
    @OnClick(R.id.btnBorrow)
    public void onClick() {
        
    }
}
