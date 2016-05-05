package com.xfzj.getbook.async;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;

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
public class GetBillAllAsync extends UcardAsyncTask<String, Void, SparseArray<List<Bill>>>{
    public GetBillAllAsync(Context context) {
        super(context);
        setProgressDialog(null, context.getString(R.string.producing_bill));
    }

    @Override
    protected SparseArray<List<Bill>> excute(String[] params) {
        try {
            if (null == params) {
                return null;
            }
            if (TextUtils.isEmpty(params[0]) || TextUtils.isEmpty(params[1])) {
                return null;
            }
            param.put("billType", "1");
            param.put("begindate", params[0]);
            param.put("enddate", params[1]);

            byte[] bytes = new HttpHelper().DoConnection(BaseHttp.GetMyBill, IHttpHelper.METHOD_POST, param);
            String str = new String(bytes, "utf-8");
            JSONObject jsonObject = new JSONObject(str);
            SparseArray<List<Bill>> billArrays = new SparseArray<>();
            if (MyUtils.isSuccess(jsonObject)) {
                billArrays.put(1, (List<Bill>) gson.fromJson(jsonObject.getString("obj"), new TypeToken<List<Bill>>() {
                }.getType()));
                param.put("billType", "2");
                bytes = new HttpHelper().DoConnection(BaseHttp.GetMyBill, IHttpHelper.METHOD_POST, param);
                str = new String(bytes, "utf-8");
                jsonObject = new JSONObject(str);
                if (MyUtils.isSuccess(jsonObject)) {
                    billArrays.put(2, (List<Bill>) gson.fromJson(jsonObject.getString("obj"), new TypeToken<List<Bill>>() {
                    }.getType()));
                }
                return billArrays;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
