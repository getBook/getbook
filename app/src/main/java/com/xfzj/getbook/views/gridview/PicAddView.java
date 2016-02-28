package com.xfzj.getbook.views.gridview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.xfzj.getbook.R;
import com.xfzj.getbook.common.PicPath;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zj on 2016/2/11.
 */
public class PicAddView extends LinearLayout implements AdapterView.OnItemClickListener {
    private int maxPics;
    private int lastSrc;
    private int defaultSrc;
    private NoScrollGridView gv;
    private PicAddAdapter adapter;
    private List<PicPath> paths;

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
        gv = (NoScrollGridView) view.findViewById(R.id.gridview);
        paths = new ArrayList<>();
        adapter = new PicAddAdapter(context, paths, gv);
        adapter.setLastSrc(lastSrc);
        adapter.setDefaultSrc(defaultSrc);
        adapter.setMaxPics(maxPics);
        gv.setAdapter(adapter);
        addView(view);
        gv.setOnItemClickListener(this);

    }

    public void deleteAlbum() {
        List<PicPath> lists = adapter.getPaths();
        if (null == lists || lists.size() == 0) {
            return;
        }
        List<PicPath> ps = new ArrayList<>();

        for (PicPath picPath : lists) {
            if (picPath.getFlag() == PicPath.FLAG_ALBUM) {
                ps.add(picPath);
            }
        }
        for (PicPath picPath : ps) {
            adapter.delete(picPath);
        }
    }

    public void deleteAll() {
        adapter.deleteAll();
    }

    public void delete(int position) {
        adapter.delete(position);

    }

    public void addAll(List<PicPath> paths) {
        adapter.addAll(paths);
    }

    public void add(PicPath paths) {
        adapter.add(paths);
    }

    public List<PicPath> getPaths() {
        return adapter.getPaths();
    }
    
    
    public List<String> getPath() {
        List<PicPath> picPaths = adapter.getPaths();
        List<String> lists = new ArrayList<>();
        for (PicPath picPath : picPaths) {
            lists.add(picPath.getPath());
        }
        return lists;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (adapter.isLast(position)) {
            if (null != onItemClick) {
                onItemClick.onAddClick(position, getPaths().size(), maxPics);
            }
        } else {
            if (null != onItemClick) {
                onItemClick.onPicClick(position, getPaths().get(position).getPath());
            }

        }
    }

    public interface OnItemClick {
        void onAddClick(int position, int size, int maxPics);

        void onPicClick(int position, String path);
    }

}
