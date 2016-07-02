package com.xfzj.getbook.views.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.xfzj.getbook.R;
import com.xfzj.getbook.common.Debris;
import com.xfzj.getbook.utils.MyUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zj on 2016/3/6.
 */
public class DebrisContentInfoView extends FrameLayout implements View.OnClickListener, View.OnLongClickListener, NetImageView.OnImageCallBack {

    private Context context;

    private NetImageView ivPic;

    private TextView tvTitle, tvDescribe, tvPrice, tvOldPrice, tvDate, tvCount, tvNewold, tvYuan;
    private LinearLayout ll;
    private onCLickListener onCLickListener;
    private onLongCLickListener onLongCLickListener;
    private OnImageCallBack onImageCallBack;
    private Debris debris;
    private ImageView ivDate, ivCount, ivNewOld, ivYxj;

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

    public Debris getDebris() {
        return debris;
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
        tvYuan = (TextView) view.findViewById(R.id.tvYuan);
        ivDate = (ImageView) view.findViewById(R.id.ivDate);
        ivCount = (ImageView) view.findViewById(R.id.ivCount);
        ivNewOld = (ImageView) view.findViewById(R.id.ivNewOld);
        ivYxj = (ImageView) view.findViewById(R.id.ivYxj);
        ll = (LinearLayout) view.findViewById(R.id.ll);
        ll.setOnClickListener(this);
        ll.setOnLongClickListener(this);
        addView(view);
    }

    public void update(Debris debris) {
        if (null == debris) {
            return;
        }
        this.debris = debris;
        ivPic.setNetImage(debris.getPicture(), this);
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

        String strDate = debris.getCreatedAt();
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

    public String getDebrisImage() {
        if (null == ivPic) {
            return null;
        }
        return ivPic.getCahceName();
    }

    public void doInvalid() {
        if (null == debris) {
            return;
        }
        if (debris.isOvertime()) {
            handleInvalid();
        
        }
    }

    private void handleInvalid() {
        Drawable d = ivPic.getDrawable();
        Bitmap bitmap = null;
        if (d instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) d).getBitmap();
        } else if (d instanceof GlideBitmapDrawable) {
            bitmap = ((GlideBitmapDrawable) d).getBitmap();
        }
        if (null != bitmap) {
            ivPic.setImageBitmap(MyUtils.toGrayscale(bitmap));
        }
        Resources res = context.getResources();
        int color = res.getColor(R.color.secondary_text);
        tvYuan.setTextColor(color);
        tvPrice.setTextColor(color);
        tvTitle.setTextColor(color);
        ivDate.setImageBitmap(BitmapFactory.decodeResource(res, R.mipmap.date_invalid));
        ivCount.setImageBitmap(BitmapFactory.decodeResource(res, R.mipmap.book_count_invalid));
        ivNewOld.setImageBitmap(BitmapFactory.decodeResource(res, R.mipmap.quality_invalid));
        ivYxj.setVisibility(VISIBLE);
    }

    public void restartOnSale(Debris debris) {
        ivPic.setNetImage(debris.getPicture());
        Resources res = context.getResources();
        int color1 = res.getColor(R.color.primary_text);
        int color2 = res.getColor(R.color.accent);

        tvYuan.setTextColor(color2);
        tvPrice.setTextColor(color2);
        tvTitle.setTextColor(color1);

        ivDate.setImageBitmap(BitmapFactory.decodeResource(res, R.mipmap.date));
        ivCount.setImageBitmap(BitmapFactory.decodeResource(res, R.mipmap.book_count));
        ivNewOld.setImageBitmap(BitmapFactory.decodeResource(res, R.mipmap.quality));
        ivYxj.setVisibility(GONE);
    }

    public void setOnCLickListener(DebrisContentInfoView.onCLickListener onCLickListener) {
        this.onCLickListener = onCLickListener;
    }

    public void setOnLongCLickListener(DebrisContentInfoView.onLongCLickListener onLongCLickListener) {
        this.onLongCLickListener = onLongCLickListener;
    }

    public void setOnImageCallBack(OnImageCallBack onImageCallBack) {
        this.onImageCallBack = onImageCallBack;
    }

    @Override
    public void onClick(View v) {
        if (null != onCLickListener) {
            onCLickListener.onClick(debris);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (null != onLongCLickListener) {
            onLongCLickListener.onLongClick(debris);
        }
        return true;
    }

    @Override
    public void onLoaded() {
        if (null != onImageCallBack) {
            onImageCallBack.onLoaded();
        }
    }

    public interface OnImageCallBack {
        void onLoaded();
    }

    public interface onCLickListener {
        void onClick(Debris debris);
    }

    public interface onLongCLickListener {
        void onLongClick(Debris debris);
    }
}
