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
public class GetTrjnAsync extends UcardAsyncTask<String, Void, List<HistoryTrjn>> {
    public GetTrjnAsync(Context context) {
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
            String url;
            if (params[1].equals(params[2])) {
                url = BaseHttp.GetCurrentTrjn;
            }else {
                param.put("begindate", params[1]);
                param.put("enddate", params[2]);
                url = BaseHttp.GetHistoryTrjn;
            }
            
            byte[] bytes = new HttpHelper().DoConnection(url, IHttpHelper.METHOD_POST, param);
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

}
