package com.xfzj.getbook.async;

import android.content.Context;
import android.text.TextUtils;

import com.xfzj.getbook.common.BmobThumbnail;
import com.xfzj.getbook.net.BmobHttpImp;
import com.xfzj.getbook.net.HttpHelper;
import com.xfzj.getbook.net.IHttpHelper;

import org.json.JSONObject;

/**
 * Created by zj on 2016/4/12.
 */
public class GetBmobthumbnail extends BaseAsyncTask<BmobThumbnail, Void, String> {
    public GetBmobthumbnail(Context context) {
        super(context);
    }

    @Override
    protected void onPost(String s) {
        if (!TextUtils.isEmpty(s)) {
            if (null != onTaskListener) {
                onTaskListener.onSuccess("http://file.bmob.cn/" + s);
            }
        } else {
            if (null != onTaskListener) {
                onTaskListener.onFail();
            }
        }
    }

    @Override
    protected String doExcute(BmobThumbnail[] params) {
        if (null == params[0]) {
            return null;
        }
        BmobHttpImp bmobHttpImp = new BmobHttpImp();

        try {
            byte[] bytes = new HttpHelper(bmobHttpImp).DoConnectionJson(BmobHttpImp.Get_THUMBNAIL, IHttpHelper.METHOD_POST, params[0].toJson());
            String result = new String(bytes, "utf-8");
            JSONObject jsonObject = new JSONObject(result);
            return jsonObject.getString("url");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
