package com.xfzj.getbook.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xfzj.getbook.R;
import com.xfzj.getbook.activity.AppActivity;
import com.xfzj.getbook.activity.LiuShuiAty;
import com.xfzj.getbook.async.BaseAsyncTask;
import com.xfzj.getbook.async.ChongzhiAsync;
import com.xfzj.getbook.async.GetCardInfoAsync;
import com.xfzj.getbook.async.GuaShiAsync;
import com.xfzj.getbook.async.UcardAsyncTask;
import com.xfzj.getbook.async.XiuGaiMiMaAsync;
import com.xfzj.getbook.common.Card;
import com.xfzj.getbook.utils.AppAnalytics;
import com.xfzj.getbook.utils.AryConversion;
import com.xfzj.getbook.utils.MyToast;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by zj on 2016/3/14.
 */
public class CardFrag extends BaseFragment implements View.OnClickListener, BaseAsyncTask.onTaskListener<Card> {
    public static final String ARG_PARAM1 = "CardFrag.class";
    TextView tvCardNo;
 
    FrameLayout framStatus;
    
    TextView tvUnnormal;
    TextView tvRemain;
    ImageView ivChongZhi;
    TextView tvGuoduRemain;
    TextView tvLiuShui;
    TextView tvBuZhu;
    TextView tvXiuGaiMiMa;
    TextView tvGuaShi;
    Button btn;
    LinearLayout ll;
    LinearLayout llError;
    private GetCardInfoAsync getCardInfoAsync;
    private String mParam1;


    public static CardFrag newInstance(String param1) {
        CardFrag fragment = new CardFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public CardFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.card_layout, container, false);
        tvCardNo = (TextView) view.findViewById(R.id.tvCardNo);
        tvUnnormal = (TextView) view.findViewById(R.id.tvUnnormal);
        tvRemain = (TextView) view.findViewById(R.id.tvRemain);
        tvGuoduRemain = (TextView) view.findViewById(R.id.tvGuoduRemain);
        tvLiuShui = (TextView) view.findViewById(R.id.tvLiuShui);
        tvBuZhu = (TextView) view.findViewById(R.id.tvBuZhu);
        tvXiuGaiMiMa = (TextView) view.findViewById(R.id.tvXiuGaiMiMa);
        tvGuaShi = (TextView) view.findViewById(R.id.tvGuaShi);
        framStatus = (FrameLayout) view.findViewById(R.id.framStatus);
        ivChongZhi = (ImageView) view.findViewById(R.id.ivChongZhi);
        btn = (Button) view.findViewById(R.id.btn);
        ll = (LinearLayout) view.findViewById(R.id.ll);
        llError = (LinearLayout) view.findViewById(R.id.llError);
        ivChongZhi.setOnClickListener(this);
        tvLiuShui.setOnClickListener(this);
        tvBuZhu.setOnClickListener(this);
        tvXiuGaiMiMa.setOnClickListener(this);
        tvGuaShi.setOnClickListener(this);
        btn.setOnClickListener(this);
        getCardInfo();
        return view;
    }
    


   

    private void getCardInfo() {
        getCardInfoAsync = new GetCardInfoAsync(getActivity());
        getCardInfoAsync.setOnTaskListener(this);
        getCardInfoAsync.executeOnExecutor(((AppActivity)getActivity()).THREAD_POOL_EXECUTOR);
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivChongZhi:
                
                chongzhi();
                break;
            case R.id.tvLiuShui:
                AppAnalytics.onEvent(getActivity(),AppAnalytics.C_LSCX);
                Intent intent = new Intent(getActivity(), LiuShuiAty.class);
                intent.putExtra(LiuShuiAty.FROM, LiuShuiAty.FROMLIUSHUI);
                startActivity(intent);
                
                break;
            case R.id.tvBuZhu:
                AppAnalytics.onEvent(getActivity(),AppAnalytics.C_BZCX);
                Intent intent1 = new Intent(getActivity(), LiuShuiAty.class);
                intent1.putExtra(LiuShuiAty.FROM, LiuShuiAty.FROMBUZHU);
                startActivity(intent1);
                break;
            case R.id.tvXiuGaiMiMa:
                xiugaiMiMa();
                break;
            case R.id.tvGuaShi:

                guashi();
                
                break;
            case R.id.btn:
                getCardInfo();
                break;
        }
    }

    private void guashi() {
        AppAnalytics.onEvent(getActivity(), AppAnalytics.C_GS);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.xiugaimima, null);
        final EditText etOld = (EditText) view.findViewById(R.id.etOld);
        final EditText etNew1 = (EditText) view.findViewById(R.id.etNew1);
        final EditText etNew2 = (EditText) view.findViewById(R.id.etNew2);
        etOld.setHint(R.string.card_query_passsword);
        etNew1.setVisibility(View.GONE);
        etNew2.setVisibility(View.GONE);
        final Button btn = (Button) view.findViewById(R.id.btn);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String old = etOld.getText().toString();
                if (TextUtils.isEmpty(old)) {
                    MyToast.show(getActivity(), getString(R.string.please_to_input, getString(R.string.oldpassword)));
                    return;
                }
                final InputMethodManager manager = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(btn.getWindowToken(), 0);
                GuaShiAsync guaShiAsync = new GuaShiAsync(getActivity());
                guaShiAsync.executeOnExecutor(((AppActivity)getActivity()).THREAD_POOL_EXECUTOR,old);
                guaShiAsync.setOnUcardTaskListener(new UcardAsyncTask.OnUcardTaskListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        AppAnalytics.onEvent(getActivity(), AppAnalytics.GS_S);

                        dialog.dismiss();
                        new AlertDialog.Builder(getActivity()).setTitle(getString(R.string.success)).setMessage(s).create().show();
                    }

                    @Override
                    public void onFail(String s) {
                        AppAnalytics.onEvent(getActivity(), AppAnalytics.GS_F);
                        new AlertDialog.Builder(getActivity()).setTitle(getString(R.string.fail)).setMessage(s).create().show();
                    }
                });


            }
        });

    }

    private void xiugaiMiMa() {
        AppAnalytics.onEvent(getActivity(), AppAnalytics.C_CP);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.xiugaimima, null);
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
                    MyToast.show(getActivity(), getString(R.string.please_to_input, getString(R.string.oldpassword)));
                    return;
                }
                final String new1 = etNew1.getText().toString();
                String new2 = etNew2.getText().toString();
                if (TextUtils.isEmpty(new1) || TextUtils.isEmpty(new2)) {
                    MyToast.show(getActivity(), getString(R.string.please_to_input, getString(R.string.newpassword)));
                    return;
                }
                if (!new1.equals(new2)) {
                    MyToast.show(getActivity(), getString(R.string.twice_password_not_equal));
                    return;
                }
                final InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(btn.getWindowToken(), 0);
                XiuGaiMiMaAsync xiuGaiMiMaAsync = new XiuGaiMiMaAsync(getActivity());
                xiuGaiMiMaAsync.executeOnExecutor(((AppActivity)getActivity()).THREAD_POOL_EXECUTOR,old, new1);
                xiuGaiMiMaAsync.setOnUcardTaskListener(new UcardAsyncTask.OnUcardTaskListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        dialog.dismiss();
                        try {
                            String oldp = AryConversion.binary2Hex(old).toUpperCase();
                            String newp = AryConversion.binary2Hex(new1).toUpperCase();

                            BmobUser.updateCurrentUserPassword(getActivity(), oldp, newp, new UpdateListener() {
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
                        AppAnalytics.onEvent(getActivity(), AppAnalytics.CP_S);
                        new AlertDialog.Builder(getActivity()).setTitle(getString(R.string.success)).setMessage(s).create().show();
                    }

                    @Override
                    public void onFail(String s) {
                        AppAnalytics.onEvent(getActivity(), AppAnalytics.CP_F);
                        new AlertDialog.Builder(getActivity()).setTitle(getString(R.string.fail)).setMessage(s).create().show();
                    }
                });


            }
        });


    }

    private void chongzhi() {
        AppAnalytics.onEvent(getActivity(), AppAnalytics.C_CZ);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.chongzhi, null);
        final EditText etPassword = (EditText) view.findViewById(R.id.etPassword);
        final EditText etMoney = (EditText) view.findViewById(R.id.etMoney);
        final Button btn = (Button) view.findViewById(R.id.btn);
        ImageView iv = (ImageView) view.findViewById(R.id.ivTips);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity()).setTitle(getString(R.string.tishi)).setMessage(getString(R.string.chongzhi_tips)).create().show();
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
                    MyToast.show(getActivity(), getString(R.string.please_to_input, getString(R.string.money)));
                    return;
                }
                float f = Float.valueOf(money);
                if (f - 100f > 0f) {
                    MyToast.show(getActivity(), getString(R.string.min_max_money));
                    etMoney.setText("100");
                    return;
                }
                String passsword = etPassword.getText().toString();
                if (TextUtils.isEmpty(passsword)) {
                    MyToast.show(getActivity(), getString(R.string.please_to_input, getString(R.string.pwd)));
                    return;
                }
                final InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(btn.getWindowToken(), 0);
                ChongzhiAsync chongzhiAsync = new ChongzhiAsync(getActivity());
                chongzhiAsync.executeOnExecutor(((AppActivity)getActivity()).THREAD_POOL_EXECUTOR, money, passsword);
                chongzhiAsync.setOnUcardTaskListener(new UcardAsyncTask.OnUcardTaskListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        AppAnalytics.onEvent(getActivity(), AppAnalytics.CZ_S);
                        ad.dismiss();
                        new AlertDialog.Builder(getActivity()).setTitle(getString(R.string.success)).setMessage(s).create().show();
                    }

                    @Override
                    public void onFail(String s) {
                        AppAnalytics.onEvent(getActivity(), AppAnalytics.CZ_F);
                        new AlertDialog.Builder(getActivity()).setTitle(getString(R.string.fail)).setMessage(s).create().show();
                    }
                });


            }
        });


    }

    @Override
    public void onSuccess(Card card) {
        ll.setVisibility(View.VISIBLE);
        llError.setVisibility(View.GONE);

        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "font/Farrington.ttf");
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
