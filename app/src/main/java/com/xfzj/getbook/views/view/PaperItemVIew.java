package com.xfzj.getbook.views.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xfzj.getbook.R;
import com.xfzj.getbook.common.Paper;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zj on 2016/3/25.
 */
public class PaperItemVIew extends FrameLayout {

    @Bind(R.id.tvTitle)
    ScrollTextView tvTitle;
    @Bind(R.id.tvType)
    TextView tvType;
    @Bind(R.id.tvCount)
    TextView tvCount;
    @Bind(R.id.rl)
    RelativeLayout rl;
    private Context context;


    private Paper paper;

    public PaperItemVIew(Context context) {
        this(context, null);
    }

    public PaperItemVIew(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PaperItemVIew(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public PaperItemVIew(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.paper_item, null);
        ButterKnife.bind(this, view);
        addView(view);
    }

    public void update(Paper paper) {
        if (null == paper) {
            return;
        }
        this.paper = paper;
        String title = paper.getTitle();
        String type = paper.getType();
        int downloads = paper.getDownloads();
        tvCount.setText(downloads + "");
        setContent(tvTitle, title);
        if (Paper.GENERAL.equals(type)) {
            setContent(tvType, context.getString(R.string.general));
        } else if (Paper.PROFESSIONAL.equals(type)) {
            setContent(tvType, context.getString(R.string.professional));
        } else {
            tvType.setText(null);
        }

    }

    private void setContent(TextView tv, String str) {
        if (TextUtils.isEmpty(str)) {
            tv.setText(null);
        } else {
            tv.setText(str);
        }

    }

    @OnClick(R.id.rl)
    public void onClick() {
        if (null != paper && null != onPaperClickListener) {
            onPaperClickListener.onPaperClick(paper.getLink());
        }
        
    }

    private OnPaperClickListener onPaperClickListener;

    public void setOnPaperClickListener(OnPaperClickListener onPaperClickListener) {
        this.onPaperClickListener = onPaperClickListener;
    }

    public interface OnPaperClickListener{
        void onPaperClick(String link);
    }
}
