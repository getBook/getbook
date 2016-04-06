package com.xfzj.getbook.fragment;


import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xfzj.getbook.BaseApplication;
import com.xfzj.getbook.GetHeaderSerVice;
import com.xfzj.getbook.R;
import com.xfzj.getbook.action.QueryAction;
import com.xfzj.getbook.activity.CardAty;
import com.xfzj.getbook.activity.Libraryaty;
import com.xfzj.getbook.activity.MySaleAty;
import com.xfzj.getbook.activity.NewsAty;
import com.xfzj.getbook.activity.ScoreAty;
import com.xfzj.getbook.common.User;
import com.xfzj.getbook.utils.AppAnalytics;
import com.xfzj.getbook.utils.SharedPreferencesUtils;
import com.xfzj.getbook.views.view.CircleImageView;

import cn.bmob.v3.listener.CountListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyFrag extends BaseFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static final String MY = "my";


    private RelativeLayout llSecondBook, llDebris, llquerygrades, llyikatong, llSchoolAnnounce, lllibrary;
    private TextView tvSecondBookCount, tvDebrisCount, tvHuaName, tvName, tvUserName;
    private CircleImageView iv;
    private User user;
    private ImageView ivSex;
    private BaseApplication baseApplication;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment MyFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static MyFrag newInstance(String param1) {
        MyFrag fragment = new MyFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public MyFrag() {
        // Required empty public constructor
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
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        llSecondBook = (RelativeLayout) view.findViewById(R.id.llSecondBook);
        llDebris = (RelativeLayout) view.findViewById(R.id.llDebris);
        lllibrary = (RelativeLayout) view.findViewById(R.id.lllibrary);
        llquerygrades = (RelativeLayout) view.findViewById(R.id.llquerygrades);
        llSchoolAnnounce = (RelativeLayout) view.findViewById(R.id.llSchoolAnnounce);
        llyikatong = (RelativeLayout) view.findViewById(R.id.llyikatong);
        tvSecondBookCount = (TextView) view.findViewById(R.id.tvSecondBookCount);
        tvDebrisCount = (TextView) view.findViewById(R.id.tvDebrisCount);
        tvName = (TextView) view.findViewById(R.id.tvName);
        tvHuaName = (TextView) view.findViewById(R.id.tvHuaName);
        tvUserName = (TextView) view.findViewById(R.id.tvUserName);
        ivSex = (ImageView) view.findViewById(R.id.ivSex);
        iv = (CircleImageView) view.findViewById(R.id.iv);
        llSecondBook.setOnClickListener(this);
        llDebris.setOnClickListener(this);
        llquerygrades.setOnClickListener(this);
        llyikatong.setOnClickListener(this);
        llSchoolAnnounce.setOnClickListener(this);
        lllibrary.setOnClickListener(this);
        setHeader();
        updateUserInfo();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llSecondBook:
                AppAnalytics.onEvent(getActivity(), AppAnalytics.C_M_SB);
                Intent intent = new Intent(getActivity(), MySaleAty.class);
                intent.putExtra(MySaleAty.FROM, getString(R.string.secondbook));
                startActivityForResult(intent, MySaleAty.RESULT);
                break;
            case R.id.llDebris:
                AppAnalytics.onEvent(getActivity(), AppAnalytics.C_M_DB);
                Intent intent1 = new Intent(getActivity(), MySaleAty.class);
                intent1.putExtra(MySaleAty.FROM, getString(R.string.drugstore));
                startActivityForResult(intent1, MySaleAty.RESULT);
                break;
            case R.id.llquerygrades:
                AppAnalytics.onEvent(getActivity(), AppAnalytics.C_M_S);
                Intent intent4 = new Intent(getActivity(), ScoreAty.class);
                startActivity(intent4);
                break;
            case R.id.llyikatong:
                AppAnalytics.onEvent(getActivity(), AppAnalytics.C_M_C);
                Intent intent3 = new Intent(getActivity(), CardAty.class);
                startActivity(intent3);
                break;
            case R.id.lllibrary:
                AppAnalytics.onEvent(getActivity(), AppAnalytics.C_M_L);
                Intent intent6 = new Intent(getActivity(), Libraryaty.class);
                startActivity(intent6);
                break;
            case R.id.llSchoolAnnounce:
                AppAnalytics.onEvent(getActivity(), AppAnalytics.C_M_SA);
                Intent intent5 = new Intent(getActivity(), NewsAty.class);
                startActivity(intent5);
                break;
        }
    }
    
    public void setHeader() {
        if (null == getActivity()) {
            return;
        }
        String header = SharedPreferencesUtils.getUserHeader(getActivity());
        if (!TextUtils.isEmpty(header)) {
            iv.setBmobImage(header, BitmapFactory.decodeResource(getResources(), R.mipmap.default_user));
        } else {
            Intent intent = new Intent(getActivity(), GetHeaderSerVice.class);
            getActivity().startService(intent);

        }
    }

    public void updateUserInfo() {
        baseApplication = (BaseApplication) (getActivity().getApplicationContext());
        if (null != baseApplication) {
            User user = baseApplication.getUser();
            if (null != user) {
                this.user = user;
                setUserInfo(user);
            } else {
                User user1 = SharedPreferencesUtils.getUser(getActivity());
                this.user = user1;
                setUserInfo(user1);
            }

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MySaleAty.RESULT) {
            queryCount();
        }


    }

    private void queryCount() {
        QueryAction queryAction = new QueryAction(getActivity());
        queryAction.querySelfDebrisCount(user.getSno(), new CountListener() {
            @Override
            public void onSuccess(int i) {
                tvDebrisCount.setText(i + "");
            }

            @Override
            public void onFailure(int i, String s) {
                tvSecondBookCount.setText("0");
            }
        });
        queryAction.querySelfSecondBookCount(user.getSno(), new CountListener() {
            @Override
            public void onSuccess(int i) {
                tvSecondBookCount.setText(i + "");
            }

            @Override
            public void onFailure(int i, String s) {
                tvSecondBookCount.setText("0");
            }
        });
    }

    private void setUserInfo(User user) {
        tvName.setText(user.getName());
        tvHuaName.setText(user.getHuaName());
        tvUserName.setText(user.getSno());
        if (user.isGender()) {
            ivSex.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.male));
        } else {
            ivSex.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.female));
        }
        queryCount();

    }

//    public class HeaderReceive extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            MyLog.print("onreceive","kaishi receive");
//            if (intent.getAction().equals("com.xfzj.getbook.receiveHeader")) {
//                setHeader();
//                MyLog.print("onreceive","onreceive");
//            }
//        }
//    }
}
