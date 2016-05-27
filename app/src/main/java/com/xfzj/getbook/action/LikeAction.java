package com.xfzj.getbook.action;

import android.content.Context;

import com.xfzj.getbook.BaseApplication;
import com.xfzj.getbook.common.Post;
import com.xfzj.getbook.common.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by zj on 2016/4/18.
 */
public class LikeAction extends BaseAction {
    private Context context;
    private User user;
    private OnLikeListener onLikeListener;
    private OnLikeCountListener onLikeCountListener;

    public LikeAction(Context context) {
        this.context = context;
        user = ((BaseApplication) context.getApplicationContext()).getUser();
    }

    public synchronized void queryLikeCount(Post post, final OnLikeCountListener onLikeCountListener) {
        BmobQuery<User> userBmobQuery = new BmobQuery<>();
        userBmobQuery.addWhereRelatedTo("likes", new BmobPointer(post));
        userBmobQuery.addQueryKeys("objectId");
        try {
            userBmobQuery.findObjects(context, new FindListener<User>() {
                @Override
                public void onSuccess(List<User> list) {
                    if (null != onLikeCountListener) {
                        if (null == list || list.size() == 0) {
                            onLikeCountListener.onLikeCount(0);
                        } else {
                            onLikeCountListener.onLikeCount(list.size());
                        }
                    }
                }

                @Override
                public void onError(int i, String s) {
                    if (null != onLikeCountListener) {
                        onLikeCountListener.onLikeCount(0);
                    }
                }
            });
        } catch (Exception e) {
            if (null != onLikeCountListener) {
                onLikeCountListener.onLikeCount(-1);
            }
        }
    }

    /**
     * 进行点赞相关的操作
     *
     * @param post
     */
    public void excute(final Post post) {
        if (post.getLikeState() == Post.LIKEDSTATE) {
            cancelLike(post);
        } else {
            doLike(post);
        }
    }

    /**
     * 查询自己是否点赞
     *
     * @param post
     */
    public synchronized void querySelfLiked(Post post, final OnLikeStateListener onLikedListener) {
        BmobQuery<User> userBmobQuery = new BmobQuery<>();
        userBmobQuery.addWhereRelatedTo("likes", new BmobPointer(post));
        userBmobQuery.addQueryKeys("objectId");
        try {
            userBmobQuery.findObjects(context, new FindListener<User>() {
                @Override
                public void onSuccess(List<User> list) {
                    if (null != list && list.size() > 0) {
                        boolean isFind = false;
                        for (User user : list) {
                            if (user.getObjectId().equals(LikeAction.this.user.getObjectId())) {
                                isFind = true;
                                break;
                            }
                        }
                        if (isFind) {
                            if (null != onLikedListener) {
                                onLikedListener.onLiked();
                            }
                        } else {
                            if (null != onLikedListener) {
                                onLikedListener.onNotLiked();
                            }
                        }
                    } else {
                        if (null != onLikedListener) {
                            onLikedListener.onNotLiked();
                        }
                    }
                }

                @Override
                public void onError(int i, String s) {
                    if (null != onLikedListener) {
                        onLikedListener.onNotLiked();
                    }
                }
            });
        } catch (Exception e) {
            if (null != onLikedListener) {
                onLikedListener.onUnknownLiked();
            }
        }

    }


    /**
     * 取消点赞
     */
    private void cancelLike(final Post post) {

        BmobRelation relation = new BmobRelation();
        relation.remove(user);
        post.setLikes(relation);
        post.update(context, new UpdateListener() {
            @Override
            public void onSuccess() {
                if (null != onLikeListener) {
                    onLikeListener.onCancelLikeSuccess();
                }

            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }

    /**
     * 进行点赞
     */
    private void doLike(final Post post) {

        BmobRelation relation = new BmobRelation();
        relation.add(user);
        post.setLikes(relation);
        post.update(context, new UpdateListener() {
            @Override
            public void onSuccess() {
                if (null != onLikeListener) {
                    onLikeListener.onDoLikeSuccess();
                }
            }

            @Override
            public void onFailure(int i, String s) {
                if (null != onLikeListener) {
                    onLikeListener.onLikeFail();
                }
            }
        });
    }

    public void setOnLikeListener(OnLikeListener onLikeListener) {
        this.onLikeListener = onLikeListener;
    }

    public void setOnLikeCountListener(OnLikeCountListener onLikeCountListener) {
        this.onLikeCountListener = onLikeCountListener;
    }

    public interface OnLikeListener {
        void onDoLikeSuccess();

        void onCancelLikeSuccess();

        void onLikeFail();

    }

    public interface OnLikeStateListener {
        void onLiked();

        void onNotLiked();

        void onUnknownLiked();
    }

    public interface OnLikeCountListener {
        void onLikeCount(int i);
    }
}
