package com.xfzj.getbook.views.view;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xfzj.getbook.BaseApplication;
import com.xfzj.getbook.R;
import com.xfzj.getbook.common.User;
import com.xfzj.getbook.loader.ImageLoader;

/**
 * Created by zj on 2016/3/4.
 */
public class SimpleUserView extends LinearLayout {

    private CircleImageView ivHeader;
    private ImageView ivSex;
    private TextView tvName;
    private Context context;
    private ImageLoader imageLoader;
    public SimpleUserView(Context context) {
        this(context, null);
    }

    public SimpleUserView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleUserView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public SimpleUserView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }


    private void init(Context context) {
        this.context = context;
        imageLoader = ((BaseApplication) context.getApplicationContext()).getImageLoader();
        View view = LayoutInflater.from(context).inflate(R.layout.simple_user, null);
        ivHeader = (CircleImageView) view.findViewById(R.id.ivHeader);
        ivSex = (ImageView) view.findViewById(R.id.ivSex);
        tvName = (TextView) view.findViewById(R.id.tvName);
        addView(view);
        
    }

    public void update(User user) {
        if (null == user) {
            return;
        }
        ivHeader.setBmobImage(user.getHeader(), BitmapFactory.decodeResource(context.getResources(), R.mipmap.default_user),0,0);
        tvName.setText(user.getHuaName());
        if (user.isGender()) {
            ivSex.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.male));
        } else {
            ivSex.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.female));
        }
    }
}
