package com.xfzj.getbook.views.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.xfzj.getbook.R;
import com.xfzj.getbook.common.SubsidyTrjn;

/**
 * Created by zj on 2016/3/28.
 */
public class SubsidytrjnsView extends FrameLayout {
    private Context context;
    private TextView tvMoney, tvStatus, tvTranName, tvDate;
    private SubsidyTrjn subsidyTrjn;

    public SubsidytrjnsView(Context context) {
        this(context, null);
    }

    public SubsidytrjnsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SubsidytrjnsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public SubsidytrjnsView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.subsidytrjns_item, null);
        tvMoney = (TextView) view.findViewById(R.id.tvMoney);
        tvStatus = (TextView) view.findViewById(R.id.tvStatus);
        tvTranName = (TextView) view.findViewById(R.id.tvTranName);
        tvDate = (TextView) view.findViewById(R.id.tvDate);
        addView(view);
    }

    public void update(SubsidyTrjn subsidyTrjn) {
        if (null == subsidyTrjn) {
            return;
        }
        this.subsidyTrjn = subsidyTrjn;
        String money = subsidyTrjn.getSubAmt();
        String status = subsidyTrjn.getSubjnstatus();
        String tranName = subsidyTrjn.getTranName();
        String date = subsidyTrjn.getJndateTime();

        if (TextUtils.isEmpty(money)) {
            tvMoney.setText("");

        } else {
            tvMoney.setText(money);
        }

        if (TextUtils.isEmpty(status) || !"3".equals(status)) {
            tvStatus.setText(R.string.weiling);

        } else {
            tvStatus.setText(R.string.yiling);
        }

        if (TextUtils.isEmpty(tranName)) {
            tvTranName.setText("");

        } else {
            tvTranName.setText(tranName);
        }

        if (TextUtils.isEmpty(date)) {
            tvDate.setText("");

        } else {
            tvDate.setText(date);
        }


    }


}

