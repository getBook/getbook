package com.xfzj.getbook.fragment;


import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xfzj.getbook.R;
import com.xfzj.getbook.common.Album;
import com.xfzj.getbook.common.PicPath;
import com.xfzj.getbook.utils.MyToast;
import com.xfzj.getbook.utils.MyUtils;
import com.xfzj.getbook.views.gridview.BaseGridViewAdapter;
import com.xfzj.getbook.views.view.FloatWindow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */
public class PicSelectFrag extends BaseFragment implements View.OnClickListener, FloatWindow.onItemClickListener {

    public static final String ARG_PARAM1 = "PicSelectFrag";
    private String mParam1;
    private ProgressDialog pd;
    private final static int SCAN_OK = 1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SCAN_OK:
                    pd.dismiss();
                    picAdapter.deleteAll();
                    picAdapter.addAll(allPaths);
                    break;
            }
        }
    };
    private List<String> lists = new ArrayList<>();
    private List<String> allPaths = new ArrayList<>();

    private int options;
    private GridView gv;
    private TextView tv;
    private PicsAdapter picAdapter;
    private Map<String, List<String>> buckets;
    private List<Album> listBuckets = new ArrayList<>();

    public FloatWindow getFloatWindow() {
        return floatWindow;
    }

    private FloatWindow floatWindow;
    private LinearLayout ll;

    public void setOptions(int options) {
        this.options = options;
    }

    public PicSelectFrag() {
        // Required empty public constructor
    }

    public static PicSelectFrag newInstance(String param) {
        PicSelectFrag picSelectFrag = new PicSelectFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param);
        picSelectFrag.setArguments(args);
        return picSelectFrag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pic_select2, container, false);
        gv = (GridView) view.findViewById(R.id.gv);
        tv = (TextView) view.findViewById(R.id.tv);
        ll = (LinearLayout) view.findViewById(R.id.ll);

        picAdapter = new PicsAdapter(getActivity(), lists);
        gv.setAdapter(picAdapter);
        tv.setOnClickListener(this);
        getImages();

        floatWindow = new FloatWindow(getActivity());
        floatWindow.setOnItemClickListener(this);

        return view;
    }

    public void setTextView(TextView tv) {
        picAdapter.setTextView(tv);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 获取图片
     */
    private void getImages() {
        //显示进度条
        pd = ProgressDialog.show(getActivity(), null, "正在加载图片...", false, false);
        allScan();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver resolver = getActivity().getContentResolver();
                String[] projection = {MediaStore.Images.Media._ID,
                        MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                        MediaStore.Images.Media.DISPLAY_NAME,
                        MediaStore.Images.Media.DATA};
                String selection = MediaStore.Images.Media.SIZE + ">?";
                Cursor cursor = resolver.query(imageUri, projection, selection, new String[]{"1024"}, MediaStore.Images.Media.DATE_MODIFIED + " desc");
                while (cursor.moveToNext()) {
                    //获取图片的路径
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    String bucketsPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                    saveBuckets(bucketsPath, path);
                    saveAllPaths(path);
                }
                cursor.close();
//                print();
                listBuckets.addAll(getBuckets());

                handler.sendEmptyMessage(SCAN_OK);

            }
        }).start();
    }

    /**
     * 存储所有的图片路径
     *
     * @param path
     */
    private void saveAllPaths(String path) {
        allPaths.add(path);
    }

    private List<Album> getBuckets() {
        List<Album> lists = new ArrayList<>();

        Set<Map.Entry<String, List<String>>> sets = buckets.entrySet();
        for (Map.Entry<String, List<String>> entries : sets) {
            String buckets = entries.getKey();
            int size = entries.getValue().size();
            lists.add(new Album(buckets, size));
        }
        lists.add(new Album("所有图片", allPaths.size()));

        Collections.sort(lists, new Comparator<Album>() {
            @Override
            public int compare(Album lhs, Album rhs) {
                if (null == lhs || null == rhs) {
                    return 0;
                }
                if (lhs.getCount() < rhs.getCount()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        return lists;
    }

    private void saveBuckets(String bucket, String path) {
        if (null == buckets) {
            buckets = new HashMap<>();
        }
        if (buckets.containsKey(bucket)) {
            List<String> paths = buckets.get(bucket);
            paths.add(path);
        } else {
            List<String> paths = new ArrayList<>();
            paths.add(path);
            buckets.put(bucket, paths);

        }


    }

    // 必须在查找前进行全盘的扫描，否则新加入的图片是无法得到显示的(加入对sd卡操作的权限)  
    public void allScan() {
        //判断sdk 版本是否高于4.4
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            MediaScannerConnection.scanFile(getActivity(), new String[]{
                    Environment.getExternalStorageDirectory().toString()
            }, null, null);
        } else {
            getActivity().sendBroadcast(new Intent(
                    Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        }
    }

    @Override
    public void onClick(View v) {

        if (!floatWindow.isShowing()) {
            List<String> lists = new ArrayList<>();
            for (Album a : listBuckets) {
                lists.add(a.getName() + "(" + a.getCount() + ")");
            }
            floatWindow.show(ll, lists);
        } else {
            floatWindow.dismiss();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id, String bucket) {
        if (null == picAdapter || TextUtils.isEmpty(bucket)) {
            return;
        }
        picAdapter.deleteAll();
        if (null != buckets && !buckets.containsKey(bucket) && getActivity().getString(R.string.allPics).equals(bucket)) {
            picAdapter.addAll(allPaths);
        } else {
            picAdapter.addAll(buckets.get(bucket));
        }
        tv.setText(bucket + "◢");
        floatWindow.dismiss();
    }

    public List<String> getPath() {
        return picAdapter.getPath();
    }

    public void setPaths(List<PicPath> paths) {
        List<String> lists = new ArrayList<>();
        for (PicPath picPath : paths) {
            if (picPath.getFlag() == PicPath.FLAG_ALBUM) {
                lists.add(picPath.getPath());
            }
        }
        picAdapter.setPath(lists);

    }

    private class PicsAdapter extends BaseGridViewAdapter<String> {

        private int dimen;
        /**
         * 选中的图片路径
         */
        private List<String> path = new ArrayList<>();

        public void setTextView(TextView tv) {
            this.tv = tv;
            if (path.size() == 0) {
                this.tv.setText(R.string.complete);
            }
        }

        public void setPath(List<String> path) {
            this.path = path;
            setText();
            notifyDataSetChanged();
        }

        private void setText() {
            if (this.path.size() == 0) {
                this.tv.setText(R.string.complete);
            } else {
                tv.setText(getActivity().getResources().getString(R.string.has_selected, this.path.size(), options));
            }
        }

        public List<String> getPath() {
            return path;
        }

        private TextView tv;

        public PicsAdapter(Context c, List paths) {
            super(c, paths);
            dimen = (int) MyUtils.dp2px(c, 85);

        }

        @Override
        protected BaseViewHolder getViewHolder() {
            return new BaseViewHolder() {
                ImageView iv;
                CheckBox cb;

                @Override
                protected View initView() {
                    View view = LayoutInflater.from(getActivity()).inflate(R.layout.pic_select_item, null);
                    iv = (ImageView) view.findViewById(R.id.iv);
                    cb = (CheckBox) view.findViewById(R.id.cb);
                    return view;
                }

                @Override
                public void setData(final String s) {

                    FrameLayout.LayoutParams p = (FrameLayout.LayoutParams) iv.getLayoutParams();
                    p.width = dimen;
                    p.height = dimen;
                    p.gravity = Gravity.CENTER;
                    iv.setLayoutParams(p);
                    iv.setImageResource(R.mipmap.image_default);
                    if (!IsGridViewIdle) {
//                        imageLoader.bindBitmap(s, gv, iv, 85, 85);
                        Glide.with(mContext).load(s).placeholder(R.mipmap.placeholder).error(R.mipmap.error).override(dimen, dimen).into(iv);
                    }

                    cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                            if (isChecked) {
                                if (path.size() == options) {
                                    if (path.indexOf(s) != -1) {
                                        return;
                                    }
                                    MyToast.show(getActivity(), getResources().getString(R.string.max_selected, options));
                                    cb.setChecked(false);
                                    return;
                                }
                                if (path.indexOf(s) == -1) {
                                    path.add(s);
                                }
                            } else {
                                int position = path.indexOf(s);
                                if (position != -1) {
                                    path.remove(position);
                                }
                            }
                            setText();

                        }
                    });
                    if (path.indexOf(s) != -1) {
                        cb.setChecked(true);
                    } else {
                        cb.setChecked(false);
                    }

                }
            };
        }


    }
}

