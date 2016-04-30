package com.xfzj.getbook.views.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.xfzj.getbook.R;
import com.xfzj.getbook.common.LibraryBookPosition;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zj on 2016/4/30.
 */
public class LibraryBookItemPositionView extends FrameLayout {
    private Context context;
    @Bind(R.id.tvPos)
    TextView tvPos;
    @Bind(R.id.tvState)
    TextView tvState;

    private LibraryBookPosition libraryBookPosition;
    public LibraryBookItemPositionView(Context context) {
        this(context, null);
    }

    public LibraryBookItemPositionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LibraryBookItemPositionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public LibraryBookItemPositionView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.librarybook_item_poition, null);
        ButterKnife.bind(this, view);
        addView(view);
    }

    public void update( LibraryBookPosition libraryBookPosition) {
        if (null == libraryBookPosition) {
            return;
        }
        this.libraryBookPosition = libraryBookPosition;
        tvPos.setText(libraryBookPosition.getPosition());
        tvState.setText(libraryBookPosition.getState());
    }
}
