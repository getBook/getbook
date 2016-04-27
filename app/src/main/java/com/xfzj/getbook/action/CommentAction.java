package com.xfzj.getbook.action;

import android.content.Context;

import com.xfzj.getbook.BaseApplication;
import com.xfzj.getbook.common.Comment;
import com.xfzj.getbook.common.Post;
import com.xfzj.getbook.common.User;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.CountListener;

/**
 * Created by zj on 2016/4/18.
 */
public class CommentAction extends BaseAction {
    private final Context context;
    private final User user;

    public CommentAction(Context context) {
        this.context = context;
        user = ((BaseApplication) context.getApplicationContext()).getUser();
    }

    public void queryCount(Post post, CountListener countListener) {
        BmobQuery<Comment> commentBmobQuery = new BmobQuery<>();
        commentBmobQuery.addWhereEqualTo("post", post);
        commentBmobQuery.count(context, Comment.class, countListener);
    }
}
