package com.xfzj.getbook.views.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.RadioButton;

import com.xfzj.getbook.R;
import com.xfzj.getbook.common.Debris;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zj on 2016/2/29.
 */
public class WrapDebrisInfoItemView extends FrameLayout implements DebrisContentInfoView.onCLickListener, DebrisContentInfoView.onLongCLickListener, DebrisContentInfoView.OnImageCallBack {
    private CheckBox cb;
    private DebrisContentInfoView debrisContentInfoView;
    private Context context;
    private Debris debris;
    private onClickListener onClickListener;
    private onLongClickListener onLongClickListener;
    private List<RadioButton> rbs = new ArrayList<>();
    public WrapDebrisInfoItemView(Context context) {
        this(context, null);
    }

    public WrapDebrisInfoItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WrapDebrisInfoItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public WrapDebrisInfoItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.wrap_debriscontentinfo_item, null);
        cb = (CheckBox) view.findViewById(R.id.cb);
        debrisContentInfoView = (DebrisContentInfoView) view.findViewById(R.id.debrisContentInfoView);
        debrisContentInfoView.setOnCLickListener(this);
        debrisContentInfoView.setOnLongCLickListener(this);
        addView(view);
    }

    public DebrisContentInfoView getDebrisContentInfoView() {
        return debrisContentInfoView;
    }

    public void update(Debris debris) {
        if (null == debris) {
            return;
        }
        this.debris = debris;
        debrisContentInfoView.setOnImageCallBack(this);
        debrisContentInfoView.update(debris);
    }


    public void refresh() {
        debrisContentInfoView.restartOnSale(debris);
    }

    public CheckBox getCb() {
        return cb;
    }

    public void setViewOnClickListener(WrapDebrisInfoItemView.onClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setViewOnLongClickListener(WrapDebrisInfoItemView.onLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }
    @Override
    public void onClick(Debris debris) {
        if (null != onClickListener) {
            onClickListener.onClick(debris);
        }
    }

    @Override
    public void onLongClick(Debris debris) {
        cb.setChecked(true);
        if (null != onLongClickListener) {
            onLongClickListener.onLongClick(debris);
        }
    }

    @Override
    public void onLoaded() {
        debrisContentInfoView.doInvalid();
    }

    public interface onClickListener<T> {
        void onClick(T t);
    }


    public interface onLongClickListener<T> {
        void onLongClick(T t);
    }


}
