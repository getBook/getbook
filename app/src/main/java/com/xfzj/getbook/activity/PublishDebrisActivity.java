package com.xfzj.getbook.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.xfzj.getbook.R;
import com.xfzj.getbook.action.UploadAction;
import com.xfzj.getbook.common.Debris;
import com.xfzj.getbook.common.PicPath;
import com.xfzj.getbook.common.User;
import com.xfzj.getbook.fragment.PicSelectFrag;
import com.xfzj.getbook.utils.MyToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.bmob.v3.BmobUser;

/**
 * Created by zj on 2016/2/8.
 */
public class PublishDebrisActivity extends PublishAty {

    public static final String DEBRIS = "debris";
    public static final String FROM = "from";
    /**
     * 最多只能选择4张图片
     */
    @Bind(R.id.lloriginPrice)
    LinearLayout lloriginPrice;
    @Bind(R.id.originprice)
    EditText originPrice;
    @Bind(R.id.etTitle)
    EditText etTitle;


    @Override

    protected void onSetContentView() {
        OPTIONS = 10;
        setContentView(R.layout.debrispublish);

    }


    @Override
    protected void initFrag() {
        psf = (PicSelectFrag) getSupportFragmentManager().findFragmentByTag(PicSelectFrag.ARG_PARAM1);
        if (null == psf) {
            psf = PicSelectFrag.newInstance(PicSelectFrag.ARG_PARAM1);
        }
    }


    @Override
    public void onCreateView(Bundle savedInstanceState) {
        super.onCreateView(savedInstanceState);
        lloriginPrice.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.action_publish) {


        } else if (item.getItemId() == R.id.action_select) {

        }

        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvPublish:
                if (canPublish()) {
                    List<String> lists = picAddView.getPath();
                    String[] str = (String[]) lists.toArray(new String[lists.size()]);
                    Debris debris = new Debris(BmobUser.getCurrentUser(getApplicationContext(), User.class), etTitle.getText().toString(), etDescribe.getText().toString(), str, etPrice.getText().toString(), originPrice.getText().toString(), plusMinusView.getText(), etNewOld.getText().toString(), etTele.getText().toString());
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

    @Override
    protected boolean canPublish() {
        if (TextUtils.isEmpty(etTitle.getText().toString().trim())) {
            MyToast.show(getApplicationContext(), getString(R.string.please_to_input, getString(R.string.title)));
            return false;
        }
        if (TextUtils.isEmpty(originPrice.getText().toString().trim())) {
            MyToast.show(getApplicationContext(), getString(R.string.please_to_input, getString(R.string.originprice1)));
            return false;
        }
        if (TextUtils.isEmpty(etDescribe.getText().toString().trim())) {
            MyToast.show(getApplicationContext(), getString(R.string.please_to_input, getString(R.string.describe)));
            return false;
        }

        return super.canPublish();
    }

    @Override
    public void onFail() {

    }
}

