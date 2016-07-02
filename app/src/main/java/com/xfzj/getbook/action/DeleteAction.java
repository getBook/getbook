package com.xfzj.getbook.action;

import com.xfzj.getbook.common.Debris;
import com.xfzj.getbook.common.SecondBook;
import com.xfzj.getbook.newnet.ApiException;
import com.xfzj.getbook.newnet.GetFunApi;
import com.xfzj.getbook.newnet.NetRxWrap;
import com.xfzj.getbook.newnet.NormalSubscriber;

/**
 * Created by zj on 2016/3/13.
 */
public class DeleteAction extends BaseAction {
    
    
    public static void delete(String id, final OnDeleteCallBack onDeleteCallBack, Class Clazz) {
        if (Clazz.equals(SecondBook.class)) {
            NetRxWrap.wrap(GetFunApi.deleteSecondBook(id)).subscribe(new NormalSubscriber<String>() {
                @Override
                protected void onFail(ApiException ex) {
                    if (null == onDeleteCallBack) {
                        onDeleteCallBack.onDelteFail("删除失败："+ex.getDisplayMessage());
                    }
                }

                @Override
                protected void onNextResult(String s) {
                    if (null == onDeleteCallBack) {
                        onDeleteCallBack.onDeleteSucc();
                    }
                }
            });
            
            
            
        } else if (Clazz.equals(Debris.class)) {
            NetRxWrap.wrap(GetFunApi.deleteDebries(id)).subscribe(new NormalSubscriber<String>() {
                @Override
                protected void onFail(ApiException ex) {
                    if (null == onDeleteCallBack) {
                        onDeleteCallBack.onDelteFail("删除失败："+ex.getDisplayMessage());
                    }
                }

                @Override
                protected void onNextResult(String s) {
                    if (null == onDeleteCallBack) {
                        onDeleteCallBack.onDeleteSucc();
                    }
                }
            });
        }
    }

    public interface OnDeleteCallBack{
        void onDeleteSucc();

        void onDelteFail(String error);
    }
}
