package com.xfzj.getbook.async;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.xfzj.getbook.R;
import com.xfzj.getbook.common.Bill;
import com.xfzj.getbook.net.BaseHttp;
import com.xfzj.getbook.net.HttpHelper;
import com.xfzj.getbook.net.IHttpHelper;
import com.xfzj.getbook.utils.MyUtils;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by zj on 2016/3/25.
 */
public class GetBillAsync extends UcardAsyncTask<String, Void, List<Bill>> {
    public GetBillAsync(Context context) {
        super(context);
        setProgressDialog(null, context.getString(R.string.querying));
        
    }

    @Override
    protected List<Bill> excute(String[] params) {
        try {
            if (null == params) {
                return null;
            }
            if (TextUtils.isEmpty(params[0]) || TextUtils.isEmpty(params[1]) || TextUtils.isEmpty(params[2])) {
                return null;
            }
            param.put("billType", params[0]);
            param.put("begindate", params[1]);
            param.put("enddate", params[2]);

            byte[] bytes = new HttpHelper().DoConnection(BaseHttp.GetMyBill, IHttpHelper.METHOD_POST, param);
            String str = new String(bytes, "utf-8");
            JSONObject jsonObject = new JSONObject(str);
            if (MyUtils.isSuccess(jsonObject)) {
                List<Bill> bills = gson.fromJson(jsonObject.getString("obj"), new TypeToken<List<Bill>>() {
                }.getType());
                return bills;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPost(List<Bill> bills) {
        if (null == bills) {
            if (null != onUcardTaskListener) {
                onUcardTaskListener.onFail("");
            }

        } else {
            if (null != onUcardTaskListener) {
                onUcardTaskListener.onSuccess(bills);
            }

        }


    }
}
