package com.xfzj.getbook.activity;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xfzj.getbook.R;
import com.xfzj.getbook.action.UploadAction;
import com.xfzj.getbook.common.Debris;
import com.xfzj.getbook.common.PicPath;
import com.xfzj.getbook.common.User;
import com.xfzj.getbook.fragment.PicSelectFrag;
import com.xfzj.getbook.utils.MyToast;
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
import cn.bmob.v3.BmobUser;

/**
 * Created by zj on 2016/2/8.
 */
public class PublishDebrisActivity extends AppActivity implements Toolbar.OnMenuItemClickListener, View.OnClickListener, UploadAction.UploadListener {

    public static final String DEBRIS = "debris";
    public static final String FROM = "from";
    private static final int IMAGE_FROM_CAPTURE = 123;
    /**
     * 最多只能选择4张图片
     */
    private static final int OPTIONS = 10;
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
    @Bind(R.id.lloriginPrice)
    LinearLayout lloriginPrice;
    @Bind(R.id.originprice)
    EditText originPrice;
    @Bind(R.id.etTitle)
    EditText etTitle;
    Toolbar toolbar;

    private PicSelectFrag psf;
    private TextView tvPublish, tvSelect;
    private boolean isFragView = false;

    private static File IMAGE_PATH;


    @Override

    protected void onSetContentView() {
        setContentView(R.layout.debrispublish);
        initFrag();
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

   
    

    private void initFrag() {
        psf = (PicSelectFrag) getSupportFragmentManager().findFragmentByTag(PicSelectFrag.ARG_PARAM1);
        if (null == psf) {
            psf = PicSelectFrag.newInstance(PicSelectFrag.ARG_PARAM1);
        }
    }

    private void openPicSelectFrag() {
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

    private void closePicSelectFrag() {
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

    @Override
    public void onCreateView(Bundle savedInstanceState) {
        baseToolBar.initToolbar(this, getString(R.string.publish));
        toolbar = baseToolBar.getToolbar();
        tvPublish = baseToolBar.getTv1();
        tvSelect = baseToolBar.getTv2();
        lloriginPrice.setVisibility(View.VISIBLE);

        tvPublish.setVisibility(View.VISIBLE);
        tvPublish.setText(R.string.publish);
        tvSelect.setText(R.string.select);
        tvPublish.setOnClickListener(this);
        tvSelect.setOnClickListener(this);
        toolbar.setOnMenuItemClickListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        plusMinusView.setText("1");
        picAddView.setOnItemClick(new PicAddView.OnItemClick() {

            @Override
            public void onAddClick(int position, int size, int maxPics) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PublishDebrisActivity.this);
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
                Intent intent = new Intent(PublishDebrisActivity.this, ViewPagerAty.class);
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


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.action_publish) {


        } else if (item.getItemId() == R.id.action_select) {

        }

        return false;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            if (isFragView) {
                closePicSelectFrag();
            } else {
                finish();
            }
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (isFragView) {
            closePicSelectFrag();
        } else {
            finish();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvPublish:
                if (canPublish()) {
                    List<String> lists = picAddView.getPath();
                    String[] str = (String[]) lists.toArray(new String[lists.size()]);
                    Debris debris = new Debris(BmobUser.getCurrentUser(getApplicationContext(), User.class),etTitle.getText().toString(), etDescribe.getText().toString(),str, etPrice.getText().toString(), originPrice.getText().toString(), plusMinusView.getText(), etNewOld.getText().toString(), etTele.getText().toString());
                    UploadAction uploadPicAction = new UploadAction(PublishDebrisActivity.this, debris);
                    uploadPicAction.publishDebris(this);
                }
                break;
            case R.id.tvSelect:
                if (null == psf) {
                    return;
                }
                closePicSelectFrag();
                picAddView.deleteAlbum();
                List<PicPath> lists = new ArrayList<>();
                for (String str : psf.getPath()) {
                    lists.add(new PicPath(PicPath.FLAG_ALBUM, str));
                }
                picAddView.addAll(lists);
                break;
        }


    }

    private boolean canPublish() {
        if (TextUtils.isEmpty(etTitle.getText().toString().trim())) {
            MyToast.show(getApplicationContext(), getString(R.string.please_to_input, getString(R.string.title)));
            return false;
        }
        if (TextUtils.isEmpty(etPrice.getText().toString().trim())) {
            MyToast.show(getApplicationContext(), getString(R.string.please_to_input, getString(R.string.price)));
            return false;
        }
        if (TextUtils.isEmpty(originPrice.getText().toString().trim())) {
            MyToast.show(getApplicationContext(), getString(R.string.please_to_input, getString(R.string.originprice)));
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
        }
        if (TextUtils.isEmpty(etDescribe.getText().toString().trim())) {
            MyToast.show(getApplicationContext(), getString(R.string.please_to_input, getString(R.string.describe)));
            return false;
        }
        if (picAddView.getPaths().size() == 0) {
            MyToast.show(getApplicationContext(), getString(R.string.please_to_selectPics));
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_FROM_CAPTURE) {
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

    @Override
    public void onFail() {

    }
}
