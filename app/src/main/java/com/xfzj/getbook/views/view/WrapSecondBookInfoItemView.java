package com.xfzj.getbook.views.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.RadioButton;

import com.xfzj.getbook.R;
import com.xfzj.getbook.common.SecondBook;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zj on 2016/2/29.
 */
public class WrapSecondBookInfoItemView extends FrameLayout implements SecondBookInfoItemView.onClickListener<Object>,SecondBookInfoItemView.onLongClickListener, SecondBookInfoItemView.OnImageCallBack {
    private CheckBox cb;
    private SecondBookInfoItemView secondBookInfoItemView;
    private Context context;
    private SecondBook secondBook;
    private onClickListener onClickListener;
    private onLongClickListener onLongClickListener;
    private List<RadioButton> rbs = new ArrayList<>();
    public WrapSecondBookInfoItemView(Context context) {
        this(context, null);
    }

    public WrapSecondBookInfoItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WrapSecondBookInfoItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public WrapSecondBookInfoItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.wrap_secondbookinfo_item, null);
        cb = (CheckBox) view.findViewById(R.id.cb);
        secondBookInfoItemView = (SecondBookInfoItemView) view.findViewById(R.id.secondbookinfoItemview);
        secondBookInfoItemView.setOnSecondBookInfoClick(this);
        secondBookInfoItemView.setOnSecondBookInfoLongClick(this);
        addView(view);
    }

    public SecondBookInfoItemView getSecondBookInfoItemView() {
        return secondBookInfoItemView;
    }

    public void update(SecondBook secondBook) {
        if (null == secondBook) {
            return;
        }
        this.secondBook = secondBook;
        secondBookInfoItemView.setOnImageCallBack(this);
        secondBookInfoItemView.update(secondBook);
     
  

    }

    public void refresh() {
        if (null == secondBook) {
            return;
        }
        secondBookInfoItemView.restartOnSale(secondBook);
        
    }
    
    public CheckBox getCb() {
        return cb;
    }

    public void setViewOnClickListener(WrapSecondBookInfoItemView.onClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setViewOnLongClickListener(WrapSecondBookInfoItemView.onLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    @Override
    public void onClick(Object o) {
        if (null != onClickListener) {
            onClickListener.onClick(o);
        }
    }

    @Override
    public void onLongClick(Object o) {
        cb.setChecked(true);
        if (null != onLongClickListener) {
            onLongClickListener.onLongClick(o);
        }
    }

    @Override
    public void onLoaded() {
        secondBookInfoItemView.doInvalid();
    }

    public interface onClickListener<T> {
        void onClick(T t);
    }


    public interface onLongClickListener<T> {
        void onLongClick(T t);
    }


}
