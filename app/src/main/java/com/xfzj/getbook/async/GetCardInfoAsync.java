package com.xfzj.getbook.async;

import android.content.Context;

import com.xfzj.getbook.R;
import com.xfzj.getbook.common.Card;
import com.xfzj.getbook.net.BaseHttp;
import com.xfzj.getbook.net.HttpHelper;
import com.xfzj.getbook.net.IHttpHelper;
import com.xfzj.getbook.utils.MyLog;

import org.json.JSONObject;

/**
 * Created by zj on 2016/3/14.
 */
public class GetCardInfoAsync extends UcardAsyncTask<Void, Void, Card> {
    public GetCardInfoAsync(Context context) {
        super(context);
        setProgressDialog(null, context.getString(R.string.querying));
    }

    @Override
    protected Card excute(Void[] params) {


        try {
            byte[] bytes = new HttpHelper().DoConnection(BaseHttp.GETCARDINFO, IHttpHelper.METHOD_POST, param);
            String str = new String(bytes, "utf-8");
            JSONObject jsonObject = new JSONObject(str);
            String feed=jsonObject.getString("success");
            Card card=null;
            if ("true".equals(feed)) {
                card = gson.fromJson(jsonObject.getString("msg"), Card.class);
            }
            
            MyLog.print("card", card.toString());

            return card;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    protected void onPost(Card card) {
        if (null == card) {
            if (null != onTaskListener) {
                onTaskListener.onFail();

            }
        } else {
            if (null != onTaskListener) {
                onTaskListener.onSuccess(card);

            }
        }


    }
}
