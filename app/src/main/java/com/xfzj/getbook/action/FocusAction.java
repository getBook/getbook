package com.xfzj.getbook.action;

import android.content.Context;

import com.xfzj.getbook.common.Post;
import com.xfzj.getbook.common.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by zj on 2016/5/11.
 */
public class FocusAction extends BaseAction {
    private final Context context;

    public FocusAction(Context context) {
        this.context = context;
    }

    public synchronized void queryFocusCount(Post post, final OnFocusCountListener onFocusCountListener) {
        BmobQuery<User> userBmobQuery = new BmobQuery<>();
        userBmobQuery.addWhereRelatedTo("focus", new BmobPointer(post));
        userBmobQuery.addQueryKeys("objectId");
        try {
            userBmobQuery.findObjects(context, new FindListener<User>() {
                @Override
                public void onSuccess(List<User> list) {
                    if (null != onFocusCountListener) {
                        if (null == list || list.size() == 0) {
                            onFocusCountListener.onLikeCount(0);
                        } else {
                            onFocusCountListener.onLikeCount(list.size());
                        }
                    }
                }

                @Override
                public void onError(int i, String s) {
                    if (null != onFocusCountListener) {
                        onFocusCountListener.onLikeCount(0);
                    }
                }
            });
        } catch (Exception e) {
            if (null != onFocusCountListener) {
                onFocusCountListener.onLikeCount(-1);
            }
        }
    }
    public interface OnFocusCountListener {
        void onLikeCount(int i);
    }
}
