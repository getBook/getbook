package com.xfzj.getbook.views.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.xfzj.getbook.R;
import com.xfzj.getbook.common.Bill;

/**
 * Created by zj on 2016/3/28.
 */
public class PayInfoVIew extends FrameLayout {

    private Context context;
    private TextView title, money, count;
    private Bill bill;

    public PayInfoVIew(Context context) {
        this(context, null);
    }

    public PayInfoVIew(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PayInfoVIew(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public PayInfoVIew(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.payinfo, null);
        title = (TextView) view.findViewById(R.id.title);
        money = (TextView) view.findViewById(R.id.money);
        count = (TextView) view.findViewById(R.id.count);
        addView(view);
    }


    public void update(Bill bill) {
        if (null == bill) {
            return;
        }
        this.bill = bill;
        if (TextUtils.isEmpty(bill.getK())) {
            title.setText("");
        } else {
            title.setText(bill.getK());
        }
        String[] s = bill.getV().split(",");
        if (TextUtils.isEmpty(s[0])) {
            money.setText("");
        } else {
            money.setText(s[0]);
        }
        if (TextUtils.isEmpty(s[1])) {
            count.setText("");
        } else {
            count.setText(s[1]);
        }
    }
}
