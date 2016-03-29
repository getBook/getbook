package com.xfzj.getbook.async;

import android.content.Context;

import com.xfzj.getbook.net.BaseHttp;
import com.xfzj.getbook.net.HttpHelper;
import com.xfzj.getbook.net.IHttpHelper;
import com.xfzj.getbook.utils.MyUtils;

import org.json.JSONObject;

/**
 * Created by zj on 2016/3/25.
 */
public class GetTrjnCountAsync extends UcardAsyncTask<String, Void, Integer> {
    public GetTrjnCountAsync(Context context) {
        super(context);
    }

    @Override
    protected Integer excute(String[] params) {
        try {
            if (null == params) {
                return -1;
            }
         
            param.put("trancode", "");
            param.put("cardno", "");
            if (!params[0].equals(params[1])) {
                param.put("begindate", params[0]);
                param.put("enddate", params[1]);
                param.put("type", "2");
            }else{
                param.put("type", "1");
            }

            byte[] bytes = new HttpHelper().DoConnection(BaseHttp.GetTrjnCount, IHttpHelper.METHOD_POST, param);
            String str = new String(bytes, "utf-8");
            JSONObject jsonObject = new JSONObject(str);
            if (MyUtils.isSuccess(jsonObject)) {


                return jsonObject.getInt("obj");

            } else {
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    protected void onPost(Integer i) {
        if (i == -1) {
            if (null != onUcardTaskListener) {
                onUcardTaskListener.onFail("");
            }

        } else {
            if (null != onUcardTaskListener) {
                onUcardTaskListener.onSuccess(i);
            }

        }


    }
}
