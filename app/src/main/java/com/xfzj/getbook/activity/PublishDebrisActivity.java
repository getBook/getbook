package com.xfzj.getbook.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.xfzj.getbook.R;
import com.xfzj.getbook.action.UploadAction;
import com.xfzj.getbook.common.Debris;
import com.xfzj.getbook.common.User;
import com.xfzj.getbook.utils.MyToast;

import java.util.List;

import butterknife.Bind;
import cn.bmob.v3.BmobUser;

/**
 * Created by zj on 2016/2/8.
 */
public class PublishDebrisActivity extends PublishThingAty {

    public static final String DEBRIS = "debris";
    public static final String FROM = "from";
  
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
    public void onCreateView(Bundle savedInstanceState) {
        super.onCreateView(savedInstanceState);
        lloriginPrice.setVisibility(View.VISIBLE);
    }

    

    @Override
    protected void doPublish() {
        List<String> lists = picAddView.getPath();
        String[] str = (String[]) lists.toArray(new String[lists.size()]);
        Debris debris = new Debris(BmobUser.getCurrentUser(getApplicationContext(), User.class), etTitle.getText().toString(), etDescribe.getText().toString(), str, etPrice.getText().toString(), originPrice.getText().toString(), plusMinusView.getText(), etNewOld.getText().toString(), etTele.getText().toString());
        UploadAction.publishDebris(PublishDebrisActivity.this,debris,this);
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

