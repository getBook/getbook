package com.xfzj.getbook.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xfzj.getbook.R;
import com.xfzj.getbook.async.BaseAsyncTask;
import com.xfzj.getbook.async.ChongzhiAsync;
import com.xfzj.getbook.async.GetCardInfoAsync;
import com.xfzj.getbook.async.UcardAsyncTask;
import com.xfzj.getbook.async.XiuGaiMiMaAsync;
import com.xfzj.getbook.common.Card;
import com.xfzj.getbook.utils.AryConversion;
import com.xfzj.getbook.utils.MyToast;
import com.xfzj.getbook.views.view.BaseToolBar;

import butterknife.Bind;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by zj on 2016/3/14.
 */
public class CardAty extends AppActivity implements View.OnClickListener, BaseAsyncTask.onTaskListener<Card> {
    @Bind(R.id.tvCardNo)
    TextView tvCardNo;
    @Bind(R.id.baseToolbar)
    BaseToolBar baseToolBar;
    @Bind(R.id.framStatus)
    FrameLayout framStatus;
    @Bind(R.id.tvUnnormal)
    TextView tvUnnormal;
    @Bind(R.id.tvRemain)
    TextView tvRemain;
    @Bind(R.id.ivChongZhi)
    ImageView ivChongZhi;
    @Bind(R.id.tvGuoduRemain)
    TextView tvGuoduRemain;
    @Bind(R.id.tvLiuShui)
    TextView tvLiuShui;
    @Bind(R.id.tvBuZhu)
    TextView tvBuZhu;
    @Bind(R.id.tvXiuGaiMiMa)
    TextView tvXiuGaiMiMa;
    @Bind(R.id.tvGuaShi)
    TextView tvGuaShi;
    @Bind(R.id.btn)
    Button btn;
    @Bind(R.id.ll)
    LinearLayout ll;
    @Bind(R.id.llError)
    LinearLayout llError;
    private GetCardInfoAsync getCardInfoAsync;

    @Override
    protected void onSetContentView() {
        setContentView(R.layout.card_layout);
    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {
        baseToolBar.initToolbar(this, getString(R.string.yikatong));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ivChongZhi.setOnClickListener(this);
        tvLiuShui.setOnClickListener(this);
        tvBuZhu.setOnClickListener(this);
        tvXiuGaiMiMa.setOnClickListener(this);
        tvGuaShi.setOnClickListener(this);
        btn.setOnClickListener(this);
        getCardInfo();

    }

    private void getCardInfo() {
        getCardInfoAsync = new GetCardInfoAsync(CardAty.this);
        getCardInfoAsync.setOnTaskListener(this);
        getCardInfoAsync.execute();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivChongZhi:
                chongzhi();
                break;
            case R.id.tvLiuShui:

                break;
            case R.id.tvBuZhu:
                break;
            case R.id.tvXiuGaiMiMa:
                xiugaiMiMa();
                break;
            case R.id.tvGuaShi:
                break;
            case R.id.btn:
                getCardInfo();
                break;
        }
    }

    private void xiugaiMiMa() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CardAty.this);
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.xiugaimima, null);
        final EditText etOld = (EditText) view.findViewById(R.id.etOld);
        final EditText etNew1 = (EditText) view.findViewById(R.id.etNew1);
        final EditText etNew2 = (EditText) view.findViewById(R.id.etNew2);
        final Button btn = (Button) view.findViewById(R.id.btn);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String old = etOld.getText().toString();
                if (TextUtils.isEmpty(old)) {
                    MyToast.show(getApplicationContext(), getString(R.string.please_to_input, getString(R.string.oldpassword)));
                    return;
                }
                final String new1 = etNew1.getText().toString();
                String new2 = etNew2.getText().toString();
                if (TextUtils.isEmpty(new1) || TextUtils.isEmpty(new2)) {
                    MyToast.show(getApplicationContext(), getString(R.string.please_to_input, getString(R.string.newpassword)));
                    return;
                }
                if (!new1.equals(new2)) {
                    MyToast.show(getApplicationContext(), getString(R.string.twice_password_not_equal));
                    return;
                }
                final InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(btn.getWindowToken(), 0);
                XiuGaiMiMaAsync xiuGaiMiMaAsync = new XiuGaiMiMaAsync(CardAty.this);
                xiuGaiMiMaAsync.execute(old, new1);
                xiuGaiMiMaAsync.setOnUcardTaskListener(new UcardAsyncTask.OnUcardTaskListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        dialog.dismiss();
                        try {
                            String oldp = AryConversion.binary2Hex(old).toUpperCase();
                            String newp = AryConversion.binary2Hex(new1).toUpperCase();
                        
                            BmobUser.updateCurrentUserPassword(getApplicationContext(), oldp, newp, new UpdateListener() {
                                @Override
                                public void onSuccess() {
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        new AlertDialog.Builder(CardAty.this).setTitle(getString(R.string.success)).setMessage(s).create().show();
                    }

                    @Override
                    public void onFail(String s) {
                        new AlertDialog.Builder(CardAty.this).setTitle(getString(R.string.fail)).setMessage(s).create().show();
                    }
                });


            }
        });


    }

    private void chongzhi() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CardAty.this);
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.chongzhi, null);
        final EditText etPassword = (EditText) view.findViewById(R.id.etPassword);
        final EditText etMoney = (EditText) view.findViewById(R.id.etMoney);
        final Button btn = (Button) view.findViewById(R.id.btn);
        ImageView iv = (ImageView) view.findViewById(R.id.ivTips);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(CardAty.this).setTitle(getString(R.string.tishi)).setMessage(getString(R.string.chongzhi_tips)).create().show();
            }
        });


        etMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                int index = str.indexOf(".");
                if (index == -1) {
                    return;
                }
                int length = str.length();
                while (length - index > 3) {
                    s.delete(length - 1, length);
                    length--;
                }
            }
        });
        final AlertDialog ad = builder.setView(view).create();
        ad.show();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String money = etMoney.getText().toString();
                if (TextUtils.isEmpty(money)) {
                    MyToast.show(getApplicationContext(), getString(R.string.please_to_input, getString(R.string.money)));
                    return;
                }
                float f = Float.valueOf(money);
                if (f - 100f > 0f) {
                    MyToast.show(getApplicationContext(), getString(R.string.min_max_money));
                    etMoney.setText("100");
                    return;
                }
                String passsword = etPassword.getText().toString();
                if (TextUtils.isEmpty(passsword)) {
                    MyToast.show(getApplicationContext(), getString(R.string.please_to_input, getString(R.string.pwd)));
                    return;
                }
                final InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(btn.getWindowToken(), 0);
                ChongzhiAsync chongzhiAsync = new ChongzhiAsync(CardAty.this);
                chongzhiAsync.execute(money, passsword);
                chongzhiAsync.setOnUcardTaskListener(new UcardAsyncTask.OnUcardTaskListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        ad.dismiss();
                        new AlertDialog.Builder(CardAty.this).setTitle(getString(R.string.success)).setMessage(s).create().show();
                    }

                    @Override
                    public void onFail(String s) {
                        new AlertDialog.Builder(CardAty.this).setTitle(getString(R.string.fail)).setMessage(s).create().show();
                    }
                });


            }
        });


    }

    @Override
    public void onSuccess(Card card) {
        ll.setVisibility(View.VISIBLE);
        llError.setVisibility(View.GONE);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "font/Farrington.ttf");
        tvCardNo.setTypeface(typeface);
        String cno = card.getBankno();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cno.length(); i++) {
            sb.append(cno.charAt(i) + " ");
        }
        tvCardNo.setText(sb.toString());
        tvRemain.setText(card.getCardbalance());
        tvGuoduRemain.setText(card.getPretmpbalance());
        if ("正常".equals(card.getCardstatus())) {
            tvUnnormal.setVisibility(View.GONE);
        } else {
            tvUnnormal.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onFail() {
        ll.setVisibility(View.GONE);
        llError.setVisibility(View.VISIBLE);
    }
}
