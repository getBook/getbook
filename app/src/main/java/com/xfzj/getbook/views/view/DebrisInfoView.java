package com.xfzj.getbook.views.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.xfzj.getbook.R;
import com.xfzj.getbook.common.Debris;
import com.xfzj.getbook.common.User;

/**
 * Created by zj on 2016/3/6.
 */
public class DebrisInfoView extends LinearLayout implements View.OnClickListener, DebrisContentInfoView.onCLickListener {
    private Context context;

    private SimpleUserView simpleUserView;

    private DebrisContentInfoView debrisContentInfoView;
    
    
    private onClickListener onUserInfoClick, onDebrisInfoClick;

    private Debris debris;

    public DebrisInfoView(Context context) {
        this(context, null);
    }

    public DebrisInfoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DebrisInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public DebrisInfoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.debris_info, null);
        simpleUserView = (SimpleUserView) view.findViewById(R.id.simpleUserView);
        debrisContentInfoView = (DebrisContentInfoView) view.findViewById(R.id.debrisContentInfoView);
        simpleUserView.setOnClickListener(this);
        debrisContentInfoView.setOnCLickListener(this);
        addView(view);
    }

    public void update(Debris debris) {
        if (null == debris) {
            return;
        }
        this.debris = debris;
        User user = debris.getUser();
        simpleUserView.update(user);
        debrisContentInfoView.update(debris);
    }

   
    
    
    public <T> void setOnUserInfoClick(onClickListener<T> onUserInfoClick) {
        this.onUserInfoClick = onUserInfoClick;
    }

    public <T> void setOnDebrisInfoClick(onClickListener<T> onDebrisInfoClick) {
        this.onDebrisInfoClick = onDebrisInfoClick;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.simpleUserView:
                if (null != onUserInfoClick && null != debris && null != debris.getUser()) {
                    onUserInfoClick.onClick(debris.getUser());
                }
                break;
        }
    }

    @Override
    public void onClick(Debris debris) {
        if (null != onUserInfoClick && null != debris) {
            onDebrisInfoClick.onClick(debris);
        }
    }

    public interface onClickListener<T> {
        void onClick(T t);
    }
}
