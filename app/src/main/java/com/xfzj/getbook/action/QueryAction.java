package com.xfzj.getbook.action;

import android.content.Context;
import android.text.TextUtils;

import com.xfzj.getbook.common.BookInfo;
import com.xfzj.getbook.common.Debris;
import com.xfzj.getbook.common.SecondBook;
import com.xfzj.getbook.utils.MyToast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by zj on 2016/2/29.
 */
public class QueryAction extends BaseAction {
    private OnQueryListener onQueryListener;
    private Context context;

    public QueryAction(Context context, OnQueryListener onQueryListener) {
        this.onQueryListener = onQueryListener;
        this.context = context;
    }

    public QueryAction(Context context) {
        this.context = context;
    }

    public void querySecondBook(BmobQuery<SecondBook> query, int limit, int skip) {
        query.include("user[huaName|header|gender|sno],bookInfo");
        query.order("-updatedAt,-createdAt");
        query.setLimit(limit);
        query.setSkip(skip * limit);
        query.findObjects(context, new FindListener<SecondBook>() {
            @Override
            public void onSuccess(List<SecondBook> list) {
                if (null == list) {
                    MyToast.show(context, "找不到");
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
        querySecondBook(query, limit, skip);
    }

    private void queryDebris(BmobQuery<Debris> query, int limit, int skip) {
        query.include("user[huaName|header|gender|sno]");
        query.order("-updatedAt,-createdAt");
        query.setLimit(limit);
        query.setSkip(skip * limit);
        query.findObjects(context, new FindListener<Debris>() {
            @Override
            public void onSuccess(List<Debris> list) {
                if (null == list) {
                    MyToast.show(context, "找不到");
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

    public <T> void setOnQueryListener(OnQueryListener<T> onQueryListener) {
        this.onQueryListener = onQueryListener;
    }

    public interface OnQueryListener<T> {
        void onSuccess(List<T> lists);

        void onFail();
    }


}
