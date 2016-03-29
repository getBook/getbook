package com.xfzj.getbook.async;

import android.content.Context;
import android.text.TextUtils;

import com.xfzj.getbook.R;
import com.xfzj.getbook.net.BaseHttp;
import com.xfzj.getbook.net.HttpHelper;
import com.xfzj.getbook.net.IHttpHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zj on 2016/3/22.
 */
public class GuaShiAsync extends UcardAsyncTask<String, Void, String> {
    public GuaShiAsync(Context context) {

        super(context);
        setProgressDialog(null, context.getString(R.string.lossing));
    }

    @Override
    protected String excute(String[] params) {

        try {
            if (null == params) {
                return null;
            }
            param.put("cardno", user.getCardno());
            param.put("password", params[0]);
            byte[] bytes = new HttpHelper().DoConnection(BaseHttp.SetCardLost, IHttpHelper.METHOD_POST, param);
            return new String(bytes, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPost(String s) {
        if (TextUtils.isEmpty(s)) {
            if (null != onUcardTaskListener) {
                onUcardTaskListener.onFail(context.getString(R.string.loss_fail_tryagain));
            }
        } else {
            try {
                JSONObject jsonObject = new JSONObject(s);
                String success = jsonObject.getString("success");
                if ("true".equals(success)) {
                    if (null != onUcardTaskListener) {
                        onUcardTaskListener.onSuccess(jsonObject.getString("msg"));
                    }
                } else {
                    if (null != onUcardTaskListener) {
                        onUcardTaskListener.onFail(jsonObject.getString("msg"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                if (null != onUcardTaskListener) {
                    onUcardTaskListener.onFail(context.getString(R.string.loss_fail_tryagain));
                }
            }
        }
    }
}
