package com.xfzj.getbook.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.xfzj.getbook.R;
import com.xfzj.getbook.utils.MyToast;
import com.xfzj.getbook.utils.SharedPreferencesUtils;
import com.xfzj.getbook.views.view.PlusMinusView;

import butterknife.Bind;

/**
 * Created by zj on 2016/3/31.
 */
public abstract class PublishThingAty extends BasePublishAty {

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


   
    @Override
    public void onCreateView(Bundle savedInstanceState) {
        super.onCreateView(savedInstanceState);
        plusMinusView.setText("1");
        etTele.setText(SharedPreferencesUtils.getPhoneNumber(getApplicationContext()));

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
        } else {
            SharedPreferencesUtils.savePhoneNumber(getApplicationContext(), etTele.getText().toString().trim());
        }
        if (picAddView.getPaths().size() == 0) {
            MyToast.show(getApplicationContext(), getString(R.string.please_to_selectPics));
            return false;
        }
        return true;
    }

}
