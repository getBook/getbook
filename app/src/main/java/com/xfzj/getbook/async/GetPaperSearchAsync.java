package com.xfzj.getbook.async;

import android.content.Context;
import android.text.TextUtils;

import com.xfzj.getbook.common.Doc;
import com.xfzj.getbook.common.Paper;
import com.xfzj.getbook.net.BaseHttp;
import com.xfzj.getbook.net.HttpHelper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by zj on 2016/4/2.
 */
public class GetPaperSearchAsync extends BaseAsyncTask<String, Void, List<Paper>> {

    public GetPaperSearchAsync(Context context) {
        super(context);
    }

    @Override
    protected void onPost(List<Paper> papers) {
        if (null == papers) {
            if (null != onTaskListener) {
                onTaskListener.onFail();
            }
        }else{
            if (null != onTaskListener) {
                onTaskListener.onSuccess(papers);
            }
        }
    }

    @Override
    protected List<Paper> doExcute(String[] params) {

        if (null == params || params.length == 0 || TextUtils.isEmpty(params[0])) {
            return null;
        }
        try {
            byte[] bytes = new HttpHelper().DoConnection(BaseHttp.PAPERSEARCH + encode(params[0]));
            String result = new String(bytes, "utf-8");
            Doc doc = gson.fromJson(result, Doc.class);
            if (null == doc) {
                return null;
            }
            List<Paper> papers = doc.getDocs();
            return papers;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
    }

    private String encode(String param) throws UnsupportedEncodingException {
        return URLEncoder.encode(param, "utf-8");
    }
    

}
