package com.xfzj.getbook.action;

import android.content.Context;

import com.xfzj.getbook.BaseApplication;
import com.xfzj.getbook.common.Comment;
import com.xfzj.getbook.common.Post;
import com.xfzj.getbook.common.User;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

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

    public  void queryCount(Post post, final OnCountListener onCountListener) {
        BmobQuery<Comment> commentBmobQuery = new BmobQuery<>();
        commentBmobQuery.addWhereEqualTo("post", post);
        try {
            commentBmobQuery.count(context, Comment.class, new CountListener() {
                @Override
                public void onSuccess(int i) {
                    if (null != onCountListener) {
                        onCountListener.onCommentCount(i);
                    }
                }

                @Override
                public void onFailure(int i, String s) {
                    if (null != onCountListener) {
                        onCountListener.onCommentCount(0);
                    }
                }
            });
        } catch (Exception e) {
            if (null != onCountListener) {
                onCountListener.onCommentCount(-1);
            }
        }
    }


    public void upload(final Comment comment, final Post post, final UpLoadCommentListener upLoadCommentListener) {

        comment.save(context, new SaveListener() {
            @Override
            public void onSuccess() {
                BmobRelation bmobRelation = new BmobRelation();
                bmobRelation.add(user);
                post.setFocus(bmobRelation);
                post.update(context, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        if (null != upLoadCommentListener) {
                            upLoadCommentListener.onCommentSucc();
                        }
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        if (null != upLoadCommentListener) {
                            upLoadCommentListener.onCommentFail();
                        }
                    }
                });
            }

            @Override
            public void onFailure(int i, String s) {
                if (null != upLoadCommentListener) {
                    upLoadCommentListener.onCommentFail();
                }
            }
        });
    }


    public interface UpLoadCommentListener {
        void onCommentSucc();

        void onCommentFail();
    }

    public interface OnCountListener {
        void onCommentCount(int i);
    }
}
