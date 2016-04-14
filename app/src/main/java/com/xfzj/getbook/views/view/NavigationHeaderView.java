package com.xfzj.getbook.views.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.xfzj.getbook.BaseApplication;
import com.xfzj.getbook.GetHeaderSerVice;
import com.xfzj.getbook.R;
import com.xfzj.getbook.common.User;
import com.xfzj.getbook.utils.AlertDialogUtils;
import com.xfzj.getbook.utils.SharedPreferencesUtils;

/**
 * Created by zj on 2016/4/10.
 */
public class NavigationHeaderView extends FrameLayout implements View.OnClickListener, DialogInterface.OnClickListener {
    public static final String ACTION = "com.xfzj.getbook.receiveHeader";
    public static final String ACTION_RECEIVE_HUANAME = "com.xfzj.getbook.receiveHuaName";
    private Context context;
    private CircleImageView cvHeader;
    private TextView tvName;
    private Button btnLogout;
    private OnLogoutClick onLogoutClick;
    private HeaderReceive headerReceive;
    private OnHeaderClickListener onHeaderClickListener;
    private OnHuaNameClick onHuaNameClick;
    public NavigationHeaderView(Context context) {
        this(context, null);
    }

    public NavigationHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NavigationHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    public NavigationHeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.navigation_header_layout, null);
        cvHeader = (CircleImageView) view.findViewById(R.id.iv);
        tvName = (TextView) view.findViewById(R.id.tvHuaName);
        btnLogout = (Button) view.findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(this);
        cvHeader.setOnClickListener(this);
        tvName.setOnClickListener(this);
        headerReceive = new HeaderReceive();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NavigationHeaderView.ACTION);
        intentFilter.addAction(NavigationHeaderView.ACTION_RECEIVE_HUANAME);
        context.registerReceiver(headerReceive, intentFilter);
        
        updateUserInfo();
        addView(view);
    }

    public void unregisterReceive() {
        context.unregisterReceiver(headerReceive);

    }

    private void updateUserInfo() {

        if (null == context) {
            return;
        }
        setHeader();
        updateUserHuaName();
    }

    public void updateUserHuaName() {
        BaseApplication baseApplication = (BaseApplication) context.getApplicationContext();
        if (null == baseApplication) {
            return;
        }
        User user = baseApplication.getUser();
        if (null == user) {
            return;
        }
        tvName.setText(user.getHuaName());
    }

    private void setHeader() {
        if (null == context) {
            return;
        }
        String header = SharedPreferencesUtils.getUserHeader(context);
        if (!TextUtils.isEmpty(header)) {
            cvHeader.setBmobImage(header);
        } else {
            Intent intent = new Intent(context, GetHeaderSerVice.class);
            context.startService(intent);
        }
    }

    @Override
    public void onClick(View v) {

        if (R.id.btnLogout == v.getId()) {
            AlertDialogUtils alertDialogUtils = new AlertDialogUtils();
            alertDialogUtils.buildMode1(context, "退出登录", "是否确认退出登录？");
            alertDialogUtils.setPositiveClick(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (null != onLogoutClick) {
                        onLogoutClick.logout();
                    }
                }
            });
            alertDialogUtils.setNegativeClick(null);
            alertDialogUtils.show();

        } else if (R.id.iv == v.getId()) {
            AlertDialogUtils alertDialogUtils = new AlertDialogUtils();
            alertDialogUtils.buildMode2(context, new String[]{context.getString(R.string.capture), context.getString(R.string.album)}, "选择头像",
                    this);
            alertDialogUtils.show();
        } else if (R.id.tvHuaName == v.getId()) {
            if (null != onHuaNameClick) {
                onHuaNameClick.changeHuaName();
            }   
        }

    }

    public void setOnHuaNameClick(OnHuaNameClick onHuaNameClick) {
        this.onHuaNameClick = onHuaNameClick;
    }

    public void setOnLogoutClick(OnLogoutClick onLogoutClick) {
        this.onLogoutClick = onLogoutClick;
    }

    public void setOnHeaderClickListener(OnHeaderClickListener onHeaderClickListener) {
        this.onHeaderClickListener = onHeaderClickListener;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case 0:
                if (null != onHeaderClickListener) {
                    onHeaderClickListener.onCapture();
                }
                break;
            case 1:
                if (null != onHeaderClickListener) {
                    onHeaderClickListener.onSelect();
                }
                break;
        }


    }

    public interface OnLogoutClick {
        void logout();
    }

    public interface OnHeaderClickListener {
        void onCapture();

        void onSelect();
    }
    public interface OnHuaNameClick {
        void changeHuaName();
    }
    private class HeaderReceive extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (NavigationHeaderView.ACTION.equals(intent.getAction())) {
                setHeader();
            } else if (NavigationHeaderView.ACTION_RECEIVE_HUANAME.equals(intent.getAction())) {
                updateUserHuaName();
            }
        }
    }
}
