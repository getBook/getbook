package com.xfzj.getbook.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xfzj.getbook.R;
import com.xfzj.getbook.action.UploadAction;
import com.xfzj.getbook.common.PicPath;
import com.xfzj.getbook.fragment.PicSelectFrag;
import com.xfzj.getbook.utils.InputMethodManagerUtils;
import com.xfzj.getbook.utils.MyToast;
import com.xfzj.getbook.utils.SharedPreferencesUtils;
import com.xfzj.getbook.views.gridview.PicAddView;
import com.xfzj.getbook.views.view.BaseToolBar;
import com.xfzj.getbook.views.view.PlusMinusView;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;

/**
 * Created by zj on 2016/3/31.
 */
public abstract class PublishAty extends AppActivity implements View.OnClickListener, Toolbar.OnMenuItemClickListener, UploadAction.UploadListener {
    protected static int OPTIONS;
    protected static final int IMAGE_FROM_CAPTURE = 123;
    public static final String REMAIN_PATHS = "remian_pic_paths";
    public static final int REMAIN_PATHS_CODE = 789;
    @Bind(R.id.addView)
    PicAddView picAddView;
    @Bind(R.id.picSelect)
    FrameLayout picSelectFram;

    @Bind(R.id.baseToolbar)
    BaseToolBar baseToolBar;
    @Bind(R.id.price)
    EditText etPrice;
    @Bind(R.id.newold)
    EditText etNewOld;
    @Bind(R.id.plusMinus)
    PlusMinusView plusMinusView;
    @Bind(R.id.tele)
    EditText etTele;
    @Bind(R.id.describe)
    EditText etDescribe;
    Toolbar toolbar;
    protected PicSelectFrag psf;
    protected TextView tvPublish, tvSelect;
    protected boolean isFragView = false;
    protected static File IMAGE_PATH;

    protected void exitPublish() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PublishAty.this);
        builder.setTitle(R.string.tishi).setMessage(getString(R.string.cofirm_to_exit_edit)).setPositiveButton(R.string.ensure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).create().show();
    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {
        initFrag();
        baseToolBar.initToolbar(this, getString(R.string.publish));
        toolbar = baseToolBar.getToolbar();
        tvPublish = baseToolBar.getTv1();
        tvSelect = baseToolBar.getTv2();
        tvPublish.setVisibility(View.VISIBLE);
        tvPublish.setText(R.string.publish);
        tvSelect.setText(R.string.select);
        tvPublish.setOnClickListener(this);
        tvSelect.setOnClickListener(this);
        toolbar.setOnMenuItemClickListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        plusMinusView.setText("1");
        etTele.setText(SharedPreferencesUtils.getPhoneNumber(getApplicationContext()));
        picAddView.setOnItemClick(new PicAddView.OnItemClick() {

            @Override
            public void onAddClick(int position, int size, int maxPics) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PublishAty.this);
                builder.setTitle(getString(R.string.max_pics, OPTIONS));

                builder.setItems(new String[]{getString(R.string.capture), getString(R.string.album)}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                IMAGE_PATH = getDiskCacheDir(getApplicationContext(), System.currentTimeMillis() + getString(R.string.jpg));
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(IMAGE_PATH));
                                startActivityForResult(intent, IMAGE_FROM_CAPTURE);
                                break;
                            case 1:
                                if (hasExternalStorage()) {
                                    openPicSelectFrag();
                                } else {
                                    closePicSelectFrag();
                                }
                                break;
                        }
                    }
                });

                builder.create().show();

            }

            @Override
            public void onPicClick(int position, String path) {
                Intent intent = new Intent(PublishAty.this, ViewPagerAty.class);
                intent.putExtra(ViewPagerAty.PATH, (Serializable) picAddView.getPaths());
                intent.putExtra(ViewPagerAty.INDEX, position);
                intent.putExtra(ViewPagerAty.FROM, ViewPagerAty.EDIT);
                startActivityForResult(intent, REMAIN_PATHS_CODE);

            }
        });
    }

    private boolean hasExternalStorage() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(getApplicationContext(), R.string.no_externalstorage, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public File getDiskCacheDir(Context context, String uniqueName) {
        boolean externalStorageAvailable = Environment
                .getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        final String cachePath;
        if (externalStorageAvailable) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }

        return new File(cachePath + File.separator + uniqueName);
    }

    protected void openPicSelectFrag() {
        InputMethodManagerUtils.hide(getApplicationContext(), picAddView);
        picSelectFram.setVisibility(View.VISIBLE);
        isFragView = true;
        tvSelect.setVisibility(View.VISIBLE);

        tvPublish.setVisibility(View.GONE);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (!psf.isAdded()) {
            ft.add(R.id.picSelect, psf);

        } else {
            ft.show(psf);
        }
        ft.commit();
        fm.executePendingTransactions();
        psf.setTextView(tvSelect);
        psf.setOptions(OPTIONS - getPicCapture().size());
        psf.setPaths(picAddView.getPaths());
    }

    protected void closePicSelectFrag() {
        if (null != psf && psf.isAdded()) {
            picSelectFram.setVisibility(View.GONE);
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.hide(psf);
            ft.commit();
            fm.executePendingTransactions();
            isFragView = false;
            tvSelect.setVisibility(View.GONE);
            tvPublish.setVisibility(View.VISIBLE);
            psf.getFloatWindow().dismiss();
        }
    }


    protected abstract void initFrag();

    @Override
    public void onClick(View v) {
        InputMethodManagerUtils.hide(getApplicationContext(), v);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            closePublish();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        closePublish();

    }

    private void closePublish() {
        if (isFragView) {
            closePicSelectFrag();
        } else {
            exitPublish();
        }
    }

    private List<String> getPicCapture() {
        List<PicPath> lists = picAddView.getPaths();
        List<String> strs = new ArrayList<>();
        for (PicPath str : lists) {
            if (str.getFlag() == PicPath.FLAG_CAPTURE) {
                strs.add(str.getPath());
            }
        }
        return strs;
    }

    protected boolean canPublish() {
        if (TextUtils.isEmpty(etPrice.getText().toString().trim())) {
            MyToast.show(getApplicationContext(), getString(R.string.please_to_input, getString(R.string.price)));
            return false;
        }
        if (TextUtils.isEmpty(etNewOld.getText().toString().trim())) {
            MyToast.show(getApplicationContext(), getString(R.string.please_to_input, getString(R.string.newold)));
            return false;
        }
        if (TextUtils.isEmpty(String.valueOf(plusMinusView.getText()))) {
            MyToast.show(getApplicationContext(), getString(R.string.please_to_input, getString(R.string.count)));
            return false;
        }
        if (TextUtils.isEmpty(etTele.getText().toString().trim())) {
            MyToast.show(getApplicationContext(), getString(R.string.please_to_input, getString(R.string.tele)));
            return false;
        }else {
            SharedPreferencesUtils.savePhoneNumber(getApplicationContext(),etTele.getText().toString().trim());
        }
        if (picAddView.getPaths().size() == 0) {
            MyToast.show(getApplicationContext(), getString(R.string.please_to_selectPics));
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_FROM_CAPTURE&&resultCode== Activity.RESULT_OK) {
            if (picAddView.getPaths().size() == OPTIONS) {
                picAddView.delete(0);
            }

            PicPath picPath = new PicPath(PicPath.FLAG_CAPTURE, IMAGE_PATH.getPath());
            picAddView.add(picPath);
        } else if (requestCode == REMAIN_PATHS_CODE && null != data) {
            picAddView.deleteAll();
            picAddView.addAll((List<PicPath>) data.getSerializableExtra(REMAIN_PATHS));
        }
    }

    @Override
    public void onSuccess() {
        MyToast.show(getApplicationContext(), getString(R.string.publish_success));
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                finish();
            }
        }, 1000);

    }
}
