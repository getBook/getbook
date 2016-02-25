package com.xfzj.getbook.views.gridview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.xfzj.getbook.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zj on 2016/2/11.
 */
public class PicAddView extends LinearLayout implements AdapterView.OnItemClickListener {
    private int maxPics;
    private int lastSrc;
    private int defaultSrc;
    private GridView gv;
    private PicAddAdapter adapter;
    private List<String> paths;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    private OnItemClick onItemClick;

    public PicAddView(Context context) {
        this(context, null);
    }

    public PicAddView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PicAddView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    public PicAddView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PicAddView, defStyleAttr, defStyleRes);
        maxPics = a.getInteger(R.styleable.PicAddView_MaxPics, 0);
        lastSrc = a.getResourceId(R.styleable.PicAddView_lastSrc, R.mipmap.ic_launcher);
        defaultSrc = a.getResourceId(R.styleable.PicAddView_defaultSrc, R.mipmap.ic_launcher);
        a.recycle();

        View view = LayoutInflater.from(context).inflate(R.layout.addpictures, null);
        gv = (GridView) view.findViewById(R.id.gridview);
        paths = new ArrayList<>();
        adapter = new PicAddAdapter(context, paths);
        adapter.setLastSrc(lastSrc);
        adapter.setDefaultSrc(defaultSrc);
        adapter.setMaxPics(maxPics);

        gv.setAdapter(adapter);
        addView(view);
        gv.setOnItemClickListener(this);
    }

    public void addAll(List<String> paths) {
        adapter.addAll(paths);
    }

    public List<String> getPaths() {
        return adapter.getPaths();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (adapter.isLast(position)) {
            if (null != onItemClick) {
                if (getPaths().size() < maxPics) {
                    onItemClick.onAddClick(position, getPaths().size(),maxPics);
                } else {
                    onItemClick.onUnAddClick(position, getPaths().size());
                }
            }
        } else {
            if (null != onItemClick) {
                onItemClick.onPicClick(position, getPaths().get(position));
            }

        }
    }

    public interface OnItemClick {
        void onAddClick(int position,int size,int maxPics);
        void onUnAddClick(int position,int size);
        void onPicClick(int position, String path);
    }

}
