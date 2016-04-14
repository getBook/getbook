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
public class ChangeLibraryPwdAsyc extends BaseAsyncTask<String, Void, String> {
    public ChangeLibraryPwdAsyc(Context context) {
        super(context);
        setProgressDialog(null, context.getString(R.string.change_password));
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
        param.put("old_passwd", params[0]);
        param.put("new_passwd", params[1]);
        param.put("chk_passwd", params[1]);
        param.put("submit1", "确定");
        HttpHelper httpHelper = new HttpHelper(new LibraryHttpImp());
        httpHelper.setCookie(params[2]);
        try {
            byte[] bytes = httpHelper.DoConnection(BaseHttp.CHANGELIBRARYPWD, IHttpHelper.METHOD_POST, param);
            String result = new String(bytes, "utf-8");
            if (!TextUtils.isEmpty(result) && result.contains("修改成功")) {
                return "您的密码修改成功,请重新登录";
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
