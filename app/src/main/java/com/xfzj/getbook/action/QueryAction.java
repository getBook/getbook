package com.xfzj.getbook.action;

import android.content.Context;
import android.text.TextUtils;

import com.xfzj.getbook.R;
import com.xfzj.getbook.activity.AppActivity;
import com.xfzj.getbook.async.BaseAsyncTask;
import com.xfzj.getbook.common.BookInfo;
import com.xfzj.getbook.common.Comment;
import com.xfzj.getbook.common.Debris;
import com.xfzj.getbook.common.Post;
import com.xfzj.getbook.common.SecondBook;
import com.xfzj.getbook.common.UnreadPost;
import com.xfzj.getbook.common.User;
import com.xfzj.getbook.utils.MyLog;
import com.xfzj.getbook.utils.MyToast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by zj on 2016/2/29.
 */
public class QueryAction extends BaseAction {
    private OnQueryListener onQueryListener;
    private Context context;

    public <T> QueryAction(Context context, OnQueryListener<T> onQueryListener) {
        this.onQueryListener = onQueryListener;
        this.context = context;
    }

    public QueryAction(Context context) {
        this.context = context;
    }

    public void querySecondBook(BmobQuery<SecondBook> query, int limit, int skip) {

        query.order("-updatedAt,-createdAt");
        if (limit != 0) {
            query.setLimit(limit);
            query.setSkip(skip * limit);
        }
        query.findObjects(context, new FindListener<SecondBook>() {
            @Override
            public void onSuccess(List<SecondBook> list) {
                if (null == list) {
                    MyToast.show(context, context.getString(R.string.net_error));
                }
                if (null != onQueryListener) {
                    onQueryListener.onSuccess(list);
                }
            }

            @Override
            public void onError(int i, String s) {
                if (null != onQueryListener) {
                    onQueryListener.onFail();
                }
            }
        });
    }


    public void queryUnreadPostContent(final List<UnreadPost> uPosts) {
        final List<UnreadPost> unreadPosts = new ArrayList<>();
        unreadPosts.addAll(uPosts);
        new BaseAsyncTask<Void, Void, List<UnreadPost>>(context) {
            @Override
            protected void onPost(List<UnreadPost> posts) {
                if (null == posts || posts.size() == 0) {
                    if (null != onQueryListener) {
                        onQueryListener.onFail();
                    }
                } else {
                    if (null != onQueryListener) {
                        onQueryListener.onSuccess(posts);
                    }
                }
            }

            @Override
            protected List<UnreadPost> doExcute(Void[] params) {
                final List<UnreadPost> posts = new ArrayList<>();

                for (final UnreadPost unreadPost : unreadPosts) {
                    try {
                        BmobQuery<Post> query = new BmobQuery<>();
                        query.order("-createdAt,-updatedAt");
                        query.addQueryKeys("content");
                        query.addWhereEqualTo("objectId", unreadPost.getPost().getObjectId());
                        query.findObjects(context, new FindListener<Post>() {
                            @Override
                            public void onSuccess(List<Post> list) {
                                list.get(0).setCommentCount(unreadPost.getPost().getCommentCount());
                                posts.add(new UnreadPost(list.get(0), unreadPost.getUnReadCount()));
                            }

                            @Override
                            public void onError(int i, String s) {
                                posts.add(null);
                            }
                        });
                    } catch (Exception e) {
                        posts.add(null);
                    }
                }
                while (posts.size() < unreadPosts.size()) {

                }
                return posts;

            }
        }.execute();

    }


    public void queryPost(final String topic, final int limit, int skip, final OnQueryListener<List<Post>> onQueryListener) {

        BmobQuery<Post> query = new BmobQuery<>();
        query.order("-createdAt,-updatedAt");
        if (limit != 0) {
            query.setLimit(limit);
            query.setSkip(skip * limit);
        }
        query.addQueryKeys("topic,content,files");
        if (!TextUtils.isEmpty(topic)) {
            query.addWhereContains("topic", topic);
        }
        query.findObjects(context, new FindListener<Post>() {
            @Override
            public void onSuccess(final List<Post> list) {
                if (null == list) {
                    MyToast.show(context, context.getString(R.string.net_error));
                }
                new BaseAsyncTask<Void, Void, List<Post>>(context) {
                    @Override
                    protected void onPost(List<Post> posts) {
                        if (null != onQueryListener) {
                            onQueryListener.onSuccess(list);
                        }
                    }

                    @Override
                    protected List<Post> doExcute(Void[] params) {
                        final List<Post> posts = new ArrayList<>();
                        for (final Post post : list) {
                            CommentAction commentAction = new CommentAction(context);
                            commentAction.queryCount(post, new CommentAction.OnCountListener() {
                                @Override
                                public void onCommentCount(int i) {
                                    post.setCommentCount(i);
                                    final LikeAction likeAction = new LikeAction(context);
                                    likeAction.queryLikeCount(post, new LikeAction.OnLikeCountListener() {
                                        @Override
                                        public void onLikeCount(int i) {
                                            post.setLikeCount(i);
                                            LikeAction likeAction1 = new LikeAction(context);
                                            likeAction1.querySelfLiked(post, new LikeAction.OnLikeStateListener() {
                                                @Override
                                                public void onLiked() {
                                                    post.setLikeState(Post.LIKEDSTATE);
                                                    posts.add(post);
                                                }

                                                @Override
                                                public void onNotLiked() {
                                                    post.setLikeState(Post.NOLIKESTATE);
                                                    posts.add(post);
                                                }

                                                @Override
                                                public void onUnknownLiked() {
                                                    post.setLikeState(Post.UNKNOWNLIKESTATE);
                                                    posts.add(post);
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        }
                        while (posts.size() < list.size()) {

                        }
                        return posts;
                    }
                }.executeOnExecutor(AppActivity.THREAD_POOL_EXECUTOR);


            }

            @Override
            public void onError(int i, String s) {
                if (null != onQueryListener) {
                    onQueryListener.onFail();
                }
            }
        });
    }


    public void queryComment(Post post, final OnQueryListener<List<Comment>> listOnQueryListener) {
        BmobQuery<Comment> query = new BmobQuery<>();
        query.addWhereEqualTo("post", post);
        query.order("createdAt");
        query.include("comment");
        query.findObjects(context, new FindListener<Comment>() {
            @Override
            public void onSuccess(final List<Comment> list) {
                new BaseAsyncTask<Void, Void, List<Comment>>() {
                    @Override
                    protected void onPost(List<Comment> comments) {
                        if (null != listOnQueryListener) {
                            listOnQueryListener.onSuccess(comments);

                        }
                    }

                    @Override
                    protected List<Comment> doExcute(Void[] params) {
                        Set<Comment> cs = new HashSet<Comment>();
                        List<Comment> ls = new ArrayList<>();
                        for (Comment comment : list) {
                            cs.add(comment);
                            ls.add(comment);
                            if (null != comment.getComment()) {
                                cs.add(comment.getComment());
                                ls.add(comment.getComment());
                            }
                        }
//                        MyLog.print("c", list.toString());
                        Set<Comment> comments = new TreeSet<Comment>(new Comparator<Comment>() {
                            @Override
                            public int compare(Comment lhs, Comment rhs) {
                                String lhsT = lhs.getCreatedAt();
                                String rhsT = rhs.getCreatedAt();
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                try {
                                    Date l = simpleDateFormat.parse(lhsT);
                                    Date r = simpleDateFormat.parse(rhsT);
                                    if (l.getTime() > r.getTime()) {
                                        return 1;
                                    } else {
                                        return -1;
                                    }

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                return -1;
                            }

                        });
                        comments.addAll(cs);

                        Iterator<Comment> iterator = comments.iterator();
                        int i = 1;
                        while (iterator.hasNext()) {
                            Comment comment = iterator.next();
                            comment.setFloor(i++);
                        }
                        MyLog.print("comments", comments.toString());
                        Iterator<Comment> it = comments.iterator();
                        while (it.hasNext()) {
                            Comment comment = it.next();
                            Comment comment1 = comment.getComment();
                            if (null != comment1) {
                                Iterator<Comment> iter = comments.iterator();
                                while (iter.hasNext()) {
                                    Comment comment2 = iter.next();
                                    if (comment1.getObjectId().equals(comment2.getObjectId())) {
                                        comment1.setFloor(comment2.getFloor());
                                    }
                                }
                            }

                        }
                        return new ArrayList<>(comments);
                    }
                }.executeOnExecutor(AppActivity.THREAD_POOL_EXECUTOR);
            }

            @Override
            public void onError(int i, String s) {
                if (null != listOnQueryListener) {
                    listOnQueryListener.onFail();

                }
            }
        });
    }

    /**
     * 查询自己发布的二手书
     *
     * @param objectId
     */
    public void querySelfSecondBook(String objectId) {
        BmobQuery<SecondBook> query = new BmobQuery<>();
        BmobQuery<User> userBmobQuery = new BmobQuery<>();
        if (TextUtils.isEmpty(objectId)) {
            if (null != onQueryListener) {
                onQueryListener.onFail();
            }
            return;
        }
        userBmobQuery.addWhereEqualTo("objectId", objectId);
        query.addWhereMatchesQuery("user", "_User", userBmobQuery);
        query.include("bookInfo");
        querySecondBook(query, 0, 0);
    }

    /**
     * 查询自己发布的杂货铺
     *
     * @param objectId
     */
    public void querySelfDebris(String objectId) {
        BmobQuery<Debris> query = new BmobQuery<>();
        BmobQuery<User> userBmobQuery = new BmobQuery<>();
        if (TextUtils.isEmpty(objectId)) {
            if (null != onQueryListener) {
                onQueryListener.onFail();
            }
            return;
        }
        userBmobQuery.addWhereEqualTo("objectId", objectId);
        query.addWhereMatchesQuery("user", "_User", userBmobQuery);
        queryDebris(query, 0, 0);
    }

    /**
     * 查询几天前到今天的数据
     *
     * @param day   几天前
     * @param limit 每次查询几个
     * @param skip  跳过几次
     */
    public void querySecondBookInfo(int day, int limit, int skip) {
        querySecondBookInfo(day, limit, skip, null);
    }

    /**
     * 查询自己发布的二手书的数量
     *
     * @param sno
     * @param countListener
     */
    public void querySelfSecondBookCount(String sno, CountListener countListener) {
        BmobQuery<SecondBook> query = new BmobQuery<>();
        BmobQuery<User> userBmobQuery = new BmobQuery<>();
        userBmobQuery.addWhereEqualTo("sno", sno);
        query.addWhereMatchesQuery("user", "_User", userBmobQuery);
        query.count(context, SecondBook.class, countListener);
    }

    /**
     * 查询自己发布的杂货铺的数量
     *
     * @param sno
     * @param countListener
     */
    public void querySelfDebrisCount(String sno, CountListener countListener) {
        BmobQuery<Debris> query = new BmobQuery<>();
        BmobQuery<User> userBmobQuery = new BmobQuery<>();
        userBmobQuery.addWhereEqualTo("sno", sno);
        query.addWhereMatchesQuery("user", "_User", userBmobQuery);
        query.count(context, Debris.class, countListener);
    }

    /**
     * 查询几天前到今天的数据
     *
     * @param day   几天前
     * @param limit 每次查询几个
     * @param skip  跳过几次
     */
    public void querySecondBookInfo(int day, int limit, int skip, String name) {

        BmobQuery<SecondBook> query = new BmobQuery<>();
        List<BmobQuery<SecondBook>> and = new ArrayList<>();

        long now = System.currentTimeMillis();
        Date dateNow = new Date(now);
        Date dateBefore = new Date(now - day * 24 * 3600 * 1000);
        BmobQuery<SecondBook> and1 = new BmobQuery<>();
        and1.addWhereGreaterThanOrEqualTo("updatedAt", new BmobDate(dateBefore));
        BmobQuery<SecondBook> and2 = new BmobQuery<>();
        and2.addWhereLessThanOrEqualTo("updatedAt", new BmobDate(dateNow));
        and.add(and1);
        and.add(and2);
        query.and(and);
        if (!TextUtils.isEmpty(name)) {
            BmobQuery<BookInfo> bookInfoBmobQuery = new BmobQuery<>();
            bookInfoBmobQuery.addWhereMatches("bookName", name);
            query.addWhereMatchesQuery("bookInfo", "BookInfo", bookInfoBmobQuery);
        }
        query.include("user[huaName|bmobHeader|gender|sno],bookInfo");
        querySecondBook(query, limit, skip);
    }

    private void queryDebris(BmobQuery<Debris> query, int limit, int skip) {

        query.order("-updatedAt,-createdAt");
        if (limit != 0) {
            query.setLimit(limit);
            query.setSkip(skip * limit);
        }
        query.findObjects(context, new FindListener<Debris>() {
            @Override
            public void onSuccess(List<Debris> list) {
                if (null == list) {
                    MyToast.show(context, context.getString(R.string.net_error));
                }
                if (null != onQueryListener) {
                    onQueryListener.onSuccess(list);
                }
            }

            @Override
            public void onError(int i, String s) {
                if (null != onQueryListener) {
                    onQueryListener.onFail();
                }
            }
        });
    }

    /**
     * 查询自己的信息
     *
     * @param user
     */
    public void queryUserSelf(User user) {
        BmobQuery<User> userBmobQuery = new BmobQuery<>();
        userBmobQuery.addWhereEqualTo("sno", user.getSno());
        userBmobQuery.findObjects(context, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                if (null == list || list.size() == 0) {
                    if (null != onQueryListener) {
                        onQueryListener.onFail();
                    }
                } else {
                    if (null != onQueryListener) {
                        onQueryListener.onSuccess(list.get(0));
                    }
                }

            }

            @Override
            public void onError(int i, String s) {
                if (null != onQueryListener) {
                    onQueryListener.onFail();
                }
            }
        });
    }

    /**
     * 查询几天前到今天的数据
     *
     * @param day   几天前
     * @param limit 每次查询几个
     * @param skip  跳过几次
     */
    public void queryDebrisInfo(int day, int limit, int skip) {
        queryDebrisInfo(day, limit, skip, null);
    }

    public void queryDebrisInfo(int day, int limit, int skip, String name) {

        BmobQuery<Debris> query = new BmobQuery<>();
        List<BmobQuery<Debris>> and = new ArrayList<>();

        long now = System.currentTimeMillis();
        Date dateNow = new Date(now);
        Date dateBefore = new Date(now - day * 24 * 3600 * 1000);
        BmobQuery<Debris> and1 = new BmobQuery<>();
        and1.addWhereGreaterThanOrEqualTo("updatedAt", new BmobDate(dateBefore));
        BmobQuery<Debris> and2 = new BmobQuery<>();
        and2.addWhereLessThanOrEqualTo("updatedAt", new BmobDate(dateNow));
        and.add(and1);
        and.add(and2);
        query.and(and);
        if (!TextUtils.isEmpty(name)) {
            query.addWhereMatches("title", name);
        }
        query.include("user[huaName|bmobHeader|gender|sno]");
        queryDebris(query, limit, skip);
    }
//
//    /**
//     * 搜索查询
//     * 
//     * @param key
//     * @param count
//     */
//    public void associationQueryBookInfo(String key, int count) {
//        BmobQuery<BookInfo> query = new BmobQuery<>();
//        query.addWhereMatches("bookName", key);
//        query.addQueryKeys("objectId,bookName");
//        query.setLimit(count);
//        queryBookInfo(query, count);
//    }
//
//    private void queryBookInfo(BmobQuery<BookInfo> query, int count) {
//        query.findObjects(context, new FindListener<BookInfo>() {
//            @Override
//            public void onSuccess(List<BookInfo> list) {
//                if (null != onQueryListener) {
//                    onQueryListener.onSuccess(list);
//                }
//            }
//
//            @Override
//            public void onError(int i, String s) {
//                if (null != onQueryListener) {
//                    onQueryListener.onFail();
//                }
//            }
//        });
//
//
//    }

    /**
     * 查询花名是否已经存在
     *
     * @param huaName
     */
    public void queryHasHuaName(String huaName) {
        BmobQuery<User> userBmobQuery = new BmobQuery<>();
        userBmobQuery.addQueryKeys("huaName");
        userBmobQuery.addWhereEqualTo("huaName", huaName);
        userBmobQuery.findObjects(context, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                if (null == list || list.size() == 0) {
                    if (onQueryListener != null) {
                        onQueryListener.onSuccess(true);
                    }
                } else {
                    if (onQueryListener != null) {
                        onQueryListener.onFail();
                    }
                }

            }

            @Override
            public void onError(int i, String s) {
                if (onQueryListener != null) {
                    onQueryListener.onFail();
                }
            }
        });


    }

    public <T> void setOnQueryListener(OnQueryListener<T> onQueryListener) {
        this.onQueryListener = onQueryListener;
    }

    public interface OnQueryListener<T> {
        void onSuccess(T t);

        void onFail();
    }


}
