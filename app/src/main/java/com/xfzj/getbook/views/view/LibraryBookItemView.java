package com.xfzj.getbook.views.view;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xfzj.getbook.R;
import com.xfzj.getbook.activity.AppActivity;
import com.xfzj.getbook.async.BaseAsyncTask;
import com.xfzj.getbook.async.GetLibraryBookGuanCangAsync;
import com.xfzj.getbook.common.LibraryBook;
import com.xfzj.getbook.common.LibraryBookPosition;
import com.xfzj.getbook.net.BaseHttp;

import java.util.List;

/**
 * Created by zj on 2016/4/2.
 */
public class LibraryBookItemView extends FrameLayout {
    private Context context;
    private TextView tvName, tvBooktype, tvAP, tvCount, tvCanBorrow, tvPosition;
    private LibraryBook libraryBook;
    private LinearLayout llPos;

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
        llPos = (LinearLayout) view.findViewById(R.id.llPosition);
        addView(view);
    }


    public void update(Activity appActivity, final LibraryBook libraryBook, final OnLibraryBookPositionLoadListener listener) {
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
        List<LibraryBookPosition> bookPositions = libraryBook.getLibraryBookPositions();
        if (null == bookPositions || bookPositions.size() == 0) {
            loadFromGuanCang(appActivity, libraryBook.getGuancang(), listener);
        } else {
            updateLibraryBookItemPositionView(bookPositions);
        }


    }

    private void loadFromGuanCang(Activity appActivity, String guancang, final OnLibraryBookPositionLoadListener listener) {
        if (TextUtils.isEmpty(guancang)) {
            return;
        }
        llPos.setVisibility(GONE);
        GetLibraryBookGuanCangAsync getLibraryBookGuanCangAsync = new GetLibraryBookGuanCangAsync(appActivity);
        getLibraryBookGuanCangAsync.executeOnExecutor(AppActivity.getThreadPoolExecutor(), wrapUrl(guancang));
        getLibraryBookGuanCangAsync.setOnTaskListener(new BaseAsyncTask.onTaskListener<List<LibraryBookPosition>>() {
            @Override
            public void onSuccess(List<LibraryBookPosition> libraryBookPositions) {
                if (null == libraryBookPositions || libraryBookPositions.size() == 0) {
                    llPos.setVisibility(GONE);
                    return;
                }
                if (null != listener) {
                    listener.onLoad(libraryBookPositions);
                }
                updateLibraryBookItemPositionView(libraryBookPositions);
            }

            @Override
            public void onFail() {
                llPos.setVisibility(GONE);
            }
        });
        
    }

    public void updateLibraryBookItemPositionView(List<LibraryBookPosition> libraryBookPositions) {
        llPos.removeAllViews();
        for (LibraryBookPosition libraryBookPosition : libraryBookPositions) {
            LibraryBookItemPositionView libraryBookItemPositionView = new LibraryBookItemPositionView(context);
            libraryBookItemPositionView.update(libraryBookPosition);
            llPos.addView(libraryBookItemPositionView);
        }
        llPos.setVisibility(VISIBLE);
    }

    private String wrapUrl(String guancang) {

        return BaseHttp.GETLIBRARYBOOKGUANCANGINFO + guancang;
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

    public interface OnLibraryBookPositionLoadListener {
        void onLoad(List<LibraryBookPosition> libraryBookPositions);
    }
}
