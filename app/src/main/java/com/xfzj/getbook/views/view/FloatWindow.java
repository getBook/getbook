package com.xfzj.getbook.views.view;

import android.content.Context;
import android.graphics.drawable.PaintDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.xfzj.getbook.R;
import com.xfzj.getbook.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zj on 2016/2/25.
 */
public class FloatWindow implements AdapterView.OnItemClickListener {

    private final Context context;
    private ListView lv;
    private PopupWindow pw;
    private ArrayAdapter arrayAdapter;
    private List<String> lists = new ArrayList<>();
    private int statusBarHeight = 0;
    private int scrrenHeight = 0;

    public void setOnItemClickListener(FloatWindow.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private onItemClickListener onItemClickListener;

    public FloatWindow(Context context) {
        this.context = context;
        init(context);
    }


    private void init(Context context) {
        View v = LayoutInflater.from(context).inflate(R.layout.listview, null);
        lv = (ListView) v.findViewById(R.id.lv);
        pw = new PopupWindow(v);
        pw.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        DisplayMetrics dm = MyUtils.getScreenMetrics(context);
        scrrenHeight = dm.heightPixels;
        pw.setHeight(scrrenHeight * 3 / 5);
        statusBarHeight = MyUtils.getStatusBarHeight(context);
        pw.setOutsideTouchable(true);
        pw.setFocusable(true);
        pw.setAnimationStyle(R.style.popupwindowanim);
        pw.setBackgroundDrawable(new PaintDrawable());
        pw.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, lists);
        lv.setAdapter(arrayAdapter);
        lv.setOnItemClickListener(this);
        pw.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    pw.dismiss();
                    return true;
                }
                return false;
            }
        });
    }

    public void show(View v, List<String> lists) {
        if (null != this.lists && this.lists.size() > 0) {
            this.lists.clear();
        }
        this.lists.addAll(lists);
        arrayAdapter.notifyDataSetChanged();
        pw.showAtLocation(v, Gravity.BOTTOM | Gravity.CENTER, 0, v.getHeight()+MyUtils.getNavigationBarHeight(context));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != onItemClickListener) {
            String str = lists.get(position).replaceAll("\\([0-9]+\\)$", "");
            onItemClickListener.onItemClick(parent, view, position, id, str);

        }

    }


    public interface onItemClickListener {
        void onItemClick(AdapterView<?> parent, View view, int position, long id, String bucket);

    }

    public void dismiss() {
        if (pw.isShowing()) {
            pw.dismiss();
        }
    }
    public boolean isShowing() {
        return pw.isShowing();
    }
}
