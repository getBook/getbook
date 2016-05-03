package com.xfzj.getbook.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.xfzj.getbook.R;
import com.xfzj.getbook.async.BaseAsyncLoader;
import com.xfzj.getbook.common.DownloadFile;
import com.xfzj.getbook.utils.FileUtils;
import com.xfzj.getbook.utils.MyUtils;
import com.xfzj.getbook.views.listview.BaseListView;
import com.xfzj.getbook.views.listview.BaseListViewAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zj on 2016/3/22.
 */
public class DownloadFrag extends BaseFragment implements LoaderManager.LoaderCallbacks<List<DownloadFile>>, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, AbsListView.OnScrollListener, BaseListView.OnScrollCallBack {

    public static final String PARAM = "DownloadFrag.class";
    private String param;
    private BaseListView lv;
    private LinearLayout llNodata;
    private DownloadAdapter downloadAdapter;
    private FloatingActionButton fab;
    public DownloadFrag() {

    }


    public static DownloadFrag newInstance(String param) {
        DownloadFrag downloadFrag = new DownloadFrag();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM, param);
        downloadFrag.setArguments(bundle);
        return downloadFrag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            param = getArguments().getString(PARAM);
        }


    }

    public void setFab(FloatingActionButton fab) {
        this.fab = fab;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_download, container,false);
        lv = (BaseListView) view.findViewById(R.id.lv);
        llNodata = (LinearLayout) view.findViewById(R.id.llnodata);
        downloadAdapter = new DownloadAdapter(getActivity());
        lv.setAdapter(downloadAdapter);
        if (null == savedInstanceState) {
            getLoaderManager().initLoader(1, null, this);
        }
        lv.setOnScrollListener(this);
        lv.setOnScrollCallBack(this);
        lv.setOnItemClickListener(this);
        lv.setOnItemLongClickListener(this);
        return view;
    }

    @Override
    public Loader<List<DownloadFile>> onCreateLoader(int id, Bundle args) {
        return new BaseAsyncLoader<List<DownloadFile>>(getActivity()) {
            @Override
            protected List<DownloadFile> doInBackground() {
                List<DownloadFile> downloadFiles = new ArrayList<>();
                File f= FileUtils.getDownloadLibrary(getActivity());
                File[] files = f.listFiles();
                for (File file : files) {
                    DownloadFile downloadFile;
                    String uri=file.getAbsolutePath();
                    String filename = file.getName();
                    if (uri.contains("doc") || uri.contains("docx")) {
                        downloadFile = new DownloadFile(file.getPath(), filename,
                                0);
                    } else if (uri.contains("xls") || uri.contains("xlsx")) {
                        downloadFile = new DownloadFile(file.getPath(), filename,
                                1);
                    } else if (uri.contains("ppt") || uri.contains("pptx")) {
                        downloadFile = new DownloadFile(file.getPath(), filename,
                                2);
                    } else if (uri.contains("jpg") || uri.contains("png") || uri.contains("jpeg")) {
                        downloadFile = new DownloadFile(file.getPath(), filename,
                                3);
                    } else {
                        downloadFile = new DownloadFile(file.getPath(), filename,
                                4);
                    }
                    downloadFiles.add(downloadFile);
                }


                return downloadFiles;
            }
        };
    }
   
    @Override
    public void onLoadFinished(Loader<List<DownloadFile>> loader, List<DownloadFile> data) {
        if (null == data || data.size() == 0) {
            llNodata.setVisibility(View.VISIBLE);
            lv.setVisibility(View.GONE);
        } else {
            downloadAdapter.clear();
            lv.setVisibility(View.VISIBLE);
            llNodata.setVisibility(View.GONE);
            downloadAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<DownloadFile>> loader) {
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DownloadFile downloadFile = downloadAdapter.getItem(position);
        Intent intent = new Intent("android.intent.action.VIEW");
        if (MyUtils.isPicture(downloadFile.path)) {
            intent.setDataAndType(Uri.fromFile(new File(downloadFile.path)),
                    "image/*");
        } else if (MyUtils.isWord(downloadFile.path)) {
            intent.setDataAndType(Uri.fromFile(new File(downloadFile.path)),
                    "application/msword");
        } else if (MyUtils.isExcel(downloadFile.path)) {
            intent.setDataAndType(Uri.fromFile(new File(downloadFile.path)),
                    "application/vnd.ms-excel");
        } else {
            intent.setDataAndType(Uri.fromFile(new File(downloadFile.path)),
                    "application/*");
        }
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        final DownloadFile downloadFile = downloadAdapter.getItem(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.tishi)).setMessage(getString(R.string.confirm_delete, downloadFile.name)).setPositiveButton(R.string.ensure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                downloadAdapter.delete(position);
                new File(downloadFile.path).delete();
            }
        }).setNegativeButton(R.string.cancel, null).create().show();


        return true;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }


    private class DownloadAdapter extends BaseListViewAdapter<DownloadFile> {


        public DownloadAdapter(Context context) {
            super(context);
        }

        @Override
        protected BaseListViewAdapter<DownloadFile>.BaseViewHolder getViewHolder() {


            return new BaseViewHolder() {
                private ImageView iv;
                private TextView tvTitle;

                @Override
                protected View initView() {
                    View view = LayoutInflater.from(context).inflate(R.layout.download_item, null);
                    iv = (ImageView) view.findViewById(R.id.iv);
                    tvTitle = (TextView) view.findViewById(R.id.tvTitle);
                    return view;
                }

                @Override
                public void setData(DownloadFile downloadFile) {
                    int id;
                    switch (downloadFile.image) {
                        case 0:
                            id = R.mipmap.msword;
                            break;
                        case 1:
                            id = R.mipmap.excel;
                            break;
                        case 2:
                            id = R.mipmap.powerpoint;
                            break;
                        case 3:
                            id = R.mipmap.picture;
                            break;
                        default:
                            id = R.mipmap.office;
                            break;
                    }
                    iv.setImageResource(id);
                    tvTitle.setText(downloadFile.name);
                }
            };
        }


    }
    @Override
    public void onScroll(boolean b) {
        if (null == fab) {
            return ;
        }
        if (b) {
            fab.setVisibility(View.GONE);
        } else {
            fab.setVisibility(View.VISIBLE);
        }
    }

    public void setFloatingBUtton(FloatingActionButton fab) {
        this.fab = fab;
    }
}
