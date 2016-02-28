package com.xfzj.getbook.views.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xfzj.getbook.R;

/**
 * Created by zj on 2016/2/27.
 */
public class PlusMinusView extends LinearLayout implements View.OnClickListener {
    private ImageView ivMinus, ivplus;
    private TextView tv;

    public PlusMinusView(Context context) {
        this(context, null);
    }

    public PlusMinusView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlusMinusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public PlusMinusView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        View v = LayoutInflater.from(context).inflate(R.layout.plus_minus_view, null);
        ivMinus = (ImageView) v.findViewById(R.id.ivMinus);
        ivplus = (ImageView) v.findViewById(R.id.ivPlus);
        tv = (TextView) v.findViewById(R.id.tv);
        ivMinus.setOnClickListener(this);
        ivplus.setOnClickListener(this);
        addView(v);

    }

    public void setText(String s) {
        try {
            Integer i = Integer.parseInt(s);
            tv.setText(i.intValue() + "");
        } catch (Exception e) {
            e.printStackTrace();
            tv.setText(s);
        }
    }

    public int getText() {
        String s = tv.getText().toString();
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return 0;
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ivPlus:
                setText(getText()+1+"");
                break;
            case R.id.ivMinus:
                int i = getText();
                if (i <= 1) {
                    setText(1+"");
                    return;
                }
                setText(i-1+"");
                break;
            
        }

    }


}
