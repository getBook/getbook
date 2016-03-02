package com.xfzj.getbook.action;

import android.content.Context;

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

    public void query(BmobQuery<SecondBook> query, int limit, int skip) {
        query.include("user[huaName|header|gender|sno],bookInfo");
        query.order("-updatedAt,-createdAt");
        query.setLimit(limit);
        query.setSkip(skip * limit);
        query.findObjects(context, new onFindListener<SecondBook>() {
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
    public void querySaleInfo(int day, int limit, int skip) {

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
        query(query, limit, skip);
    }

    /**
     * 模糊查询书名中含有这个的关键词的
     *
     * @param bookName
     */
    public void queryMatchBookName(String bookName, int limit, int skip) {
        BmobQuery<SecondBook> query = new BmobQuery<>();
        query.addWhereMatches("bookName", bookName);
        query(query, limit, skip);
    }


    public <T> void setOnQueryListener(OnQueryListener<T> onQueryListener) {
        this.onQueryListener = onQueryListener;
    }

    public interface OnQueryListener<T> {
        void onSuccess(List<T> lists);

        void onFail();
    }

    private class onFindListener<T> extends FindListener<T> {

        @Override
        public void onSuccess(List<T> list) {

        }

        @Override
        public void onError(int i, String s) {

        }
    }


}
