package com.xfzj.getbook.views.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.xfzj.getbook.R;
import com.xfzj.getbook.utils.AppAnalytics;
import com.xfzj.getbook.utils.MyToast;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by zj on 2016/3/26.
 */
public class DatePickerView extends FrameLayout implements View.OnClickListener {

    private Context context;
    private TextView tvStart, tvEnd;
    private ImageView ivQuery, ivToday,ivDwcy;
    private Calendar calendar;
    private String startTime, endTime;
    private int[] start = new int[3];
    private int[] end = new int[3];
    private OnTimeGetListener onTimeGetListener;
    private onDwcyClickListener onDwcyClickListener;
    public DatePickerView(Context context) {
        this(context, null);
    }

    public DatePickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DatePickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    public DatePickerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void setOnTimeGetListener(OnTimeGetListener onTimeGetListener) {
        this.onTimeGetListener = onTimeGetListener;
    }

    private void init(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.date_picker, null);
        tvStart = (TextView) view.findViewById(R.id.tvStart);
        tvEnd = (TextView) view.findViewById(R.id.tvEnd);
        ivQuery = (ImageView) view.findViewById(R.id.ivQuery);
        ivToday = (ImageView) view.findViewById(R.id.ivToday);
        ivDwcy=(ImageView) view.findViewById(R.id.ivDwcy);
        tvStart.setOnClickListener(this);
        tvEnd.setOnClickListener(this);
        ivQuery.setOnClickListener(this);
        ivToday.setOnClickListener(this);
        ivDwcy.setOnClickListener(this);
        addView(view);
        initTime();
    }

    private void initTime() {
        Date date = new Date(System.currentTimeMillis());
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        start[0] = calendar.get(Calendar.YEAR);
        start[1] = calendar.get(Calendar.MONTH);
        start[2] = calendar.get(Calendar.DAY_OF_MONTH);
        end[0] = calendar.get(Calendar.YEAR);
        end[1] = calendar.get(Calendar.MONTH) + 1;
        end[2] = calendar.get(Calendar.DAY_OF_MONTH) - 1;
        tvStart.setText(start[0] + "-" + start[1] + "-" + start[2]);
        tvEnd.setText(end[0] + "-" + end[1] + "-" + end[2]);

    }

    private String getTodayTime() {
        return calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
    }

    public void initTime(String startTime, String endTime) {
        if (TextUtils.isEmpty(startTime) && TextUtils.isEmpty(endTime)) {
            initTime();
            return;
        }
        String[] starts = startTime.split("-");
        String[] ends = endTime.split("-");

        start[0] = Integer.parseInt(starts[0]);
        start[1] = Integer.parseInt(starts[1]);
        start[2] = Integer.parseInt(starts[2]);
        end[0] = Integer.parseInt(ends[0]);
        end[1] = Integer.parseInt(ends[1]);
        end[2] = Integer.parseInt(ends[2]);

        tvStart.setText(startTime);
        tvEnd.setText(endTime);
    }

    public void setOnDwcyClickListener(DatePickerView.onDwcyClickListener onDwcyClickListener) {
        this.onDwcyClickListener = onDwcyClickListener;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tvStart:


                datePicker(tvStart, R.string.starttime, start[0], start[1], start[2]);

                break;
            case R.id.tvEnd:
                datePicker(tvEnd, R.string.endtime, end[0], end[1], end[2]);
                break;
            case R.id.ivQuery:
                startTime = tvStart.getText().toString();
                endTime = tvEnd.getText().toString();
                if (TextUtils.isEmpty(startTime)) {
                    MyToast.show(context, context.getString(R.string.please_to_input, context.getString(R.string.starttime)));
                    return;
                }
                if (TextUtils.isEmpty(endTime)) {
                    MyToast.show(context, context.getString(R.string.please_to_input, context.getString(R.string.endtime)));
                    return;
                }
                if (null != onTimeGetListener) {
                    onTimeGetListener.getTime(startTime, endTime);
                }
                break;
            case R.id.ivToday:
                if (null != onTimeGetListener) {
                    onTimeGetListener.getTime(getTodayTime(), getTodayTime());
                }
                break;
            case R.id.ivDwcy:
                AppAnalytics.onEvent(context, AppAnalytics.C_DWCY);
                if (null != onDwcyClickListener) {
                    onDwcyClickListener.onDwcyClick();
                }
                break;

        }
    }

    public void setDwcyVisiblity(int visiblity) {
        ivDwcy.setVisibility(visiblity);
    }
    public void setIvTodayVisiblity(int visiblity) {
        ivToday.setVisibility(visiblity);
    }
    
    private void datePicker(final TextView tv, final int title, int year, int month, int day) {

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                tv.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                if (R.string.starttime == title) {
                    start[0] = year;
                    start[1] = monthOfYear + 1;
                    start[2] = dayOfMonth;

                } else if (R.string.endtime == title) {
                    end[0] = year;
                    end[1] = monthOfYear + 1;
                    end[2] = dayOfMonth;
                }


            }
        }, year, month - 1, day);

        datePickerDialog.setTitle(title);
        datePickerDialog.create();
        datePickerDialog.show();
    }

    public interface OnTimeGetListener {
        void getTime(String startTime, String endTime);
    }
    public interface onDwcyClickListener{
        void onDwcyClick();
    }
}
