package com.xfzj.getbook.views.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.xfzj.getbook.R;
import com.xfzj.getbook.common.HistoryTrjn;

/**
 * Created by zj on 2016/3/25.
 */
public class TrjnItemVIew extends FrameLayout {

    private Context context;
    private TextView dealTime, dealName, shopName, dealMoney, dealRemain;
    private HistoryTrjn historyTrjn;
    public TrjnItemVIew(Context context) {
        this(context, null);
    }

    public TrjnItemVIew(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TrjnItemVIew(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public TrjnItemVIew(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.trjn_item, null);
        dealTime = (TextView) view.findViewById(R.id.dealTime);
        dealName = (TextView) view.findViewById(R.id.dealName);
        shopName = (TextView) view.findViewById(R.id.shopName);
        dealMoney = (TextView) view.findViewById(R.id.dealMoney);
        dealRemain = (TextView) view.findViewById(R.id.dealRemain);
        addView(view);
    }

    public void update(HistoryTrjn historyTrjn) {
        if (null == historyTrjn) {
            return;
        }
        this.historyTrjn = historyTrjn;
        String time = historyTrjn.getDate();
        String shopN = historyTrjn.getMercName();
        String dealN = historyTrjn.getTranName();
        String money = historyTrjn.getCustomMoney();
        String remain = historyTrjn.getCardBalance();
        setContent(dealTime, time);
        setContent(dealName, dealN);
        setContent(shopName, shopN);
        setContent(dealMoney, money);
        setContent(dealRemain, remain);
    }

    private void setContent(TextView tv, String str) {
        if (TextUtils.isEmpty(str)) {
            tv.setText(null);
        }else {
            tv.setText(str);
        }

    }

}
