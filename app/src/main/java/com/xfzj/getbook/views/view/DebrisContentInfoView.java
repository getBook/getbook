package com.xfzj.getbook.views.view;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.xfzj.getbook.BaseApplication;
import com.xfzj.getbook.R;
import com.xfzj.getbook.common.Debris;
import com.xfzj.getbook.loader.ImageLoader;
import com.xfzj.getbook.utils.MyUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zj on 2016/3/6.
 */
public class DebrisContentInfoView extends FrameLayout {

    private Context context;

    private NetImageView ivPic;

    private TextView tvTitle, tvDescribe, tvPrice, tvOldPrice, tvDate, tvCount, tvNewold;

    public DebrisContentInfoView(Context context) {
        this(context, null);
    }

    public DebrisContentInfoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DebrisContentInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public DebrisContentInfoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.debrisinfo_content, null);
        ivPic = (NetImageView) view.findViewById(R.id.ivPic);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvDescribe = (TextView) view.findViewById(R.id.tvDescribe);
        tvPrice = (TextView) view.findViewById(R.id.tvPrice);
        tvOldPrice = (TextView) view.findViewById(R.id.tvOldPrice);
        tvDate = (TextView) view.findViewById(R.id.tvDate);
        tvCount = (TextView) view.findViewById(R.id.tvCount);
        tvNewold = (TextView) view.findViewById(R.id.tvNewold);
        addView(view);
    }

    public void update(Debris debris) {
        if (null == debris) {
            return;
        }
        String[] str = debris.getPics();
        if (null == str || TextUtils.isEmpty(str[0])) {
            ivPic.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.image_default));
        } else {
            int i = (int) MyUtils.dp2px(context, 120f);
            ImageLoader imageLoader = ((BaseApplication) context.getApplicationContext()).getImageLoader();
            ivPic.setBmobImage(str[0], BitmapFactory.decodeResource(context.getResources(), R.mipmap.image_default), i,i);
        }


        String title = debris.getTitle();
        if (TextUtils.isEmpty(title)) {
            tvTitle.setText(context.getString(R.string.no_title));
        } else {
            tvTitle.setText(title);
        }

        String tips = debris.getTips();
        if (TextUtils.isEmpty(tips)) {
            tvDescribe.setText(context.getString(R.string.no_describe));
        } else {
            tvDescribe.setText(tips);
        }

        String price = debris.getDiscount();
        if (TextUtils.isEmpty(price)) {
            tvPrice.setText(context.getString(R.string.no_price));
        } else {
            tvPrice.setText(price);
        }
        String originPrice = debris.getOriginPrice();
        if (TextUtils.isEmpty(originPrice)) {
            tvOldPrice.setText(context.getString(R.string.no_price));
        } else {
            tvOldPrice.setText(originPrice);
        }
        tvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        String strDate = debris.getUpdatedAt();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (null == date) {
            tvDate.setText(context.getString(R.string.no_date));
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            tvDate.setText((calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH));
        }


        int count = debris.getCount();
        tvCount.setText(count + "");

        String newold = debris.getNewold();
        if (TextUtils.isEmpty(newold)) {
            tvNewold.setText(0 + context.getString(R.string.chengxin));
        } else {
            tvNewold.setText(newold + context.getString(R.string.chengxin));
        }
    }


}
