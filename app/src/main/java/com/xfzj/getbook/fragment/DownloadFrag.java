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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xfzj.getbook.R;
import com.xfzj.getbook.async.BaseAsyncLoader;
import com.xfzj.getbook.common.DownloadFile;
import com.xfzj.getbook.db.DownLoadFileManager;
import com.xfzj.getbook.utils.MyUtils;
import com.xfzj.getbook.views.listview.BaseListViewAdapter;

import java.io.File;
import java.util.List;

/**
 * Created by zj on 2016/3/22.
 */
public class DownloadFrag extends BaseFragment implements LoaderManager.LoaderCallbacks<List<DownloadFile>>, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    public static final String PARAM = "DownloadFrag.class";
    private String param;
    private ListView lv;
    private LinearLayout llNodata;
    private DownloadAdapter downloadAdapter;
    private DownLoadFileManager downLoadFileManager;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_download, container,false);
        lv = (ListView) view.findViewById(R.id.lv);
        llNodata = (LinearLayout) view.findViewById(R.id.llnodata);
        downloadAdapter = new DownloadAdapter(getActivity());
        lv.setAdapter(downloadAdapter);
        downLoadFileManager = new DownLoadFileManager(getActivity());
        if (null == savedInstanceState) {
            getLoaderManager().initLoader(1, null, this);
        }
        lv.setOnItemClickListener(this);
        lv.setOnItemLongClickListener(this);
        return view;
    }

    @Override
    public Loader<List<DownloadFile>> onCreateLoader(int id, Bundle args) {
        return new BaseAsyncLoader<List<DownloadFile>>(getActivity()) {
            @Override
            protected List<DownloadFile> doInBackground() {
                return downLoadFileManager.findAll();
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
                downLoadFileManager.delete(downloadFile);
            }
        }).setNegativeButton(R.string.cancel, null).create().show();


        return true;
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
}
