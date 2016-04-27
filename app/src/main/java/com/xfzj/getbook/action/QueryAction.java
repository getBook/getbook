package com.xfzj.getbook.action;

import android.content.Context;
import android.text.TextUtils;

import com.xfzj.getbook.R;
import com.xfzj.getbook.common.BookInfo;
import com.xfzj.getbook.common.Debris;
import com.xfzj.getbook.common.Post;
import com.xfzj.getbook.common.SecondBook;
import com.xfzj.getbook.common.User;
import com.xfzj.getbook.utils.MyToast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public  void queryPost(int limit, int skip, final OnQueryListener<List<Post>> onQueryListener) {

        BmobQuery<Post> query = new BmobQuery<>();
        query.order("-createdAt,-updatedAt");
        if (limit != 0) {
            query.setLimit(limit);
            query.setSkip(skip * limit);
        }
        query.findObjects(context, new FindListener<Post>() {
            @Override
            public void onSuccess(List<Post> list) {
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
     * 查询自己发布的二手书
     *
     * @param sno
     */
    public void querySelfSecondBook(String sno) {
        BmobQuery<SecondBook> query = new BmobQuery<>();
        BmobQuery<User> userBmobQuery = new BmobQuery<>();
        userBmobQuery.addWhereEqualTo("sno", sno);
        query.addWhereMatchesQuery("user", "_User", userBmobQuery);
        query.include("bookInfo");
        querySecondBook(query, 0, 0);
    }

    /**
     * 查询自己发布的杂货铺
     *
     * @param sno
     */
    public void querySelfDebris(String sno) {
        BmobQuery<Debris> query = new BmobQuery<>();
        BmobQuery<User> userBmobQuery = new BmobQuery<>();
        userBmobQuery.addWhereEqualTo("sno", sno);
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
