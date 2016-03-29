package com.xfzj.getbook.async;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.xfzj.getbook.common.SubsidyTrjn;
import com.xfzj.getbook.net.BaseHttp;
import com.xfzj.getbook.net.HttpHelper;
import com.xfzj.getbook.net.IHttpHelper;
import com.xfzj.getbook.utils.MyUtils;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by zj on 2016/3/25.
 */
public class GetSubsidyTrjnAsync extends UcardAsyncTask<String, Void, List<SubsidyTrjn>> {
    public GetSubsidyTrjnAsync(Context context) {
        super(context);
    }

    @Override
    protected List<SubsidyTrjn> excute(String[] params) {
        try {
            if (null == params) {
                return null;
            }
            param.put("pageIndex", params[0]);
            param.put("pageSize", "10");
            byte[] bytes = new HttpHelper().DoConnection(BaseHttp.GetAllSubsidyTrjn, IHttpHelper.METHOD_POST, param);
            String str = new String(bytes, "utf-8");
            JSONObject jsonObject = new JSONObject(str);
            if (MyUtils.isSuccess(jsonObject)) {


                List<SubsidyTrjn> subsidyTrjns = gson.fromJson(jsonObject.getString("obj"), new TypeToken<List<SubsidyTrjn>>() {
                }.getType());
                return subsidyTrjns;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPost(List<SubsidyTrjn> subsidyTrjns) {
        if (null == subsidyTrjns) {
            if (null != onUcardTaskListener) {
                onUcardTaskListener.onFail("");
            }

        } else {
            if (null != onUcardTaskListener) {
                onUcardTaskListener.onSuccess(subsidyTrjns);
            }

        }


    }
}
