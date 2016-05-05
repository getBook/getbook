package com.xfzj.getbook.async;

import android.content.Context;
import android.text.TextUtils;

import com.xfzj.getbook.R;
import com.xfzj.getbook.common.LibraryUserInfo;
import com.xfzj.getbook.net.BaseHttp;
import com.xfzj.getbook.net.HttpHelper;
import com.xfzj.getbook.net.IHttpHelper;
import com.xfzj.getbook.net.LibraryHttpImp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zj on 2016/3/29.
 */
public class LoginLibraryAsyc extends BaseAsyncTask<String, Void, LibraryUserInfo> {
    public LoginLibraryAsyc(Context context) {
        super(context);
        setProgressDialog(null, context.getString(R.string.logining_library));
    }

    private onLibraryLoginListener onLibraryLoginListener;

    @Override
    protected void onPost(LibraryUserInfo s) {
        if (null != s) {
            if (null != s.getBookInfo() && !TextUtils.isEmpty(s.getBookInfo()[0])) {
                if (onLibraryLoginListener != null) {
                    onLibraryLoginListener.onSuccess(s);
                }
            } else {
                if (onLibraryLoginListener != null) {
                    onLibraryLoginListener.onVerify();
                }
            }

        } else {
            if (null != onLibraryLoginListener) {
                onLibraryLoginListener.onFail();
            }
        }
    }

    public <T> void setOnLibraryLoginListener(LoginLibraryAsyc.onLibraryLoginListener<T> onLibraryLoginListener) {
        this.onLibraryLoginListener = onLibraryLoginListener;
    }

    @Override
    protected LibraryUserInfo doExcute(String[] params) {
        Map<String, String> param = new HashMap<>();
        param.put("number", params[0]);
        param.put("passwd", params[1]);
        param.put("captcha", params[2]);
        param.put("select", "cert_no");
        HttpHelper httpHelper = new HttpHelper(new LibraryHttpImp());
        httpHelper.setCookie(params[3]);
        try {
            byte[] bytes = httpHelper.DoConnection(BaseHttp.LIBRARYVERFY, IHttpHelper.METHOD_POST, param);
            String result = new String(bytes, "utf-8");
            if (!TextUtils.isEmpty(result)) {
                if (result.contains("您尚未完成身份认证，请进行身份核实")) {
                    return new LibraryUserInfo();
                } else if (result.contains(params[0])) {
                    return LoginParse.parse(context, result, params[0], params[1], params[3]);
                }
            }

            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public interface onLibraryLoginListener<T> {
        void onSuccess(T t);

        void onFail();

        void onVerify();
    }
}
