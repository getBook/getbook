package com.xfzj.getbook.async;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.xfzj.getbook.R;
import com.xfzj.getbook.common.Score;
import com.xfzj.getbook.net.BaseHttp;
import com.xfzj.getbook.net.HttpHelper;
import com.xfzj.getbook.net.IHttpHelper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zj on 2016/3/14.
 */
public class ScoreQueryAsync extends UcardAsyncTask<Void, Void, List<List<Score>>> {
    public ScoreQueryAsync(Context context) {
        super(context);
        setProgressDialog(null, context.getString(R.string.querying));
    }

 

    @Override
    protected List<List<Score>> excute(Void[] params) {
        try {
            List<List<Score>> lists = new ArrayList<>();
            for (int i = 1; i < 9; i++) {
                param.put("pageIndex", i + "");
                param.put("xn", "");
                byte[] bytes = new HttpHelper().DoConnection(BaseHttp.QUERYSCORE, IHttpHelper.METHOD_POST, param);
                String result = new String(bytes, "utf-8");
                JSONObject jsonObject = new JSONObject(result);
                String feed = jsonObject.getString("success");
                if ("true".equals(feed)) {
                    List<Score> scores = gson.fromJson(jsonObject.getString("obj"), new TypeToken<List<Score>>() {
                    }.getType());
                    if (null == scores) {
                        continue;
                    }
                    lists.add(scores);
                } else {
                    break;
                }
            }
            return lists;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
