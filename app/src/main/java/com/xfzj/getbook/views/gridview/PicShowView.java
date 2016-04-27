package com.xfzj.getbook.views.gridview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.xfzj.getbook.R;

import java.util.ArrayList;

/**
 * Created by zj on 2016/4/16.
 */
public class PicShowView extends PicAddView {


    public PicShowView(Context context) {
        super(context);
    }

    public PicShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PicShowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PicShowView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.addpictures, null);
        gv = (NoScrollGridView) view.findViewById(R.id.gridview);
        paths = new ArrayList<>();
        adapter = new PicShowAdapter(context, paths, gv);
        gv.setAdapter(adapter);
        addView(view);
        gv.setOnItemClickListener(this);
      
    }


    public void removeLast() {
        adapter.delete(adapter.getCount() - 1);
    }
}
