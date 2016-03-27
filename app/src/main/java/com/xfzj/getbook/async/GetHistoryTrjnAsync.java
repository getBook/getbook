package com.xfzj.getbook.async;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.xfzj.getbook.common.HistoryTrjn;
import com.xfzj.getbook.net.BaseHttp;
import com.xfzj.getbook.net.HttpHelper;
import com.xfzj.getbook.net.IHttpHelper;
import com.xfzj.getbook.utils.MyUtils;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by zj on 2016/3/25.
 */
public class GetHistoryTrjnAsync extends UcardAsyncTask<String, Void, List<HistoryTrjn>> {
    public GetHistoryTrjnAsync(Context context) {
        super(context);
    }

    @Override
    protected List<HistoryTrjn> excute(String[] params) {
        try {
            if (null == params) {
                return null;
            }
            param.put("pageIndex", params[0]);
            param.put("pageSize", "10");
            param.put("cardno", null);
            param.put("trancode", "");
            param.put("begindate", params[1]);
            param.put("enddate", params[2]);

            byte[] bytes = new HttpHelper().DoConnection(BaseHttp.GetHistoryTrjn, IHttpHelper.METHOD_POST, param);
            String str = new String(bytes, "utf-8");
            JSONObject jsonObject = new JSONObject(str);
            if (MyUtils.isSuccess(jsonObject)) {


                List<HistoryTrjn> historyTrjns = gson.fromJson(jsonObject.getString("obj"), new TypeToken<List<HistoryTrjn>>() {
                }.getType());
                return historyTrjns;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPost(List<HistoryTrjn> historyTrjns) {
        if (null == historyTrjns) {
            if (null != onUcardTaskListener) {
                onUcardTaskListener.onFail("");
            }

        } else {
            if (null != onUcardTaskListener) {
                onUcardTaskListener.onSuccess(historyTrjns);
            }

        }


    }
}
