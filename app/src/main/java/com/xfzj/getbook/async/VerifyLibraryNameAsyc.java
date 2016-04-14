package com.xfzj.getbook.async;

import android.content.Context;
import android.text.TextUtils;

import com.xfzj.getbook.R;
import com.xfzj.getbook.net.BaseHttp;
import com.xfzj.getbook.net.HttpHelper;
import com.xfzj.getbook.net.IHttpHelper;
import com.xfzj.getbook.net.LibraryHttpImp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zj on 2016/3/29.
 */
public class VerifyLibraryNameAsyc extends BaseAsyncTask<String, Void, String> {
    public VerifyLibraryNameAsyc(Context context) {
        super(context);
        setProgressDialog(null, context.getString(R.string.logining_library));
    }


    @Override
    protected void onPost(String s) {

        if (!TextUtils.isEmpty(s)) {
            if (onTaskListener != null) {
                onTaskListener.onSuccess(s);
            }
        } else {
            if (onTaskListener != null) {
                onTaskListener.onFail();
            }

        }
    }

    @Override
    protected String doExcute(String[] params) {
        Map<String, String> param = new HashMap<>();
        param.put("name", params[0]);
        HttpHelper httpHelper = new HttpHelper(new LibraryHttpImp());
        httpHelper.setCookie(params[1]);
        try {
            byte[] bytes = httpHelper.DoConnection(BaseHttp.VERIFYLIBRARYNAME, IHttpHelper.METHOD_POST, param);
            String result = new String(bytes, "utf-8");
            if (!TextUtils.isEmpty(result) && result.contains("请输入旧密码！")) {
                return "1";
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
