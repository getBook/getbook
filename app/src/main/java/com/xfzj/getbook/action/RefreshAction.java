package com.xfzj.getbook.action;

import com.xfzj.getbook.common.CreatedAt;
import com.xfzj.getbook.common.Debris;
import com.xfzj.getbook.common.SecondBook;
import com.xfzj.getbook.newnet.ApiException;
import com.xfzj.getbook.newnet.GetFunApi;
import com.xfzj.getbook.newnet.NetRxWrap;
import com.xfzj.getbook.newnet.NormalSubscriber;

/**
 * Created by zj on 2016/3/13.
 */
public class RefreshAction extends BaseAction {
    
    public static void refresh(String id, final OnRefreshCallBack onRefreshCallBack, Class Clazz) {
        if (Clazz.equals(SecondBook.class)) {
            NetRxWrap.wrap(GetFunApi.refreshSecondBook(id)).subscribe(new NormalSubscriber<CreatedAt>() {
                @Override
                protected void onFail(ApiException ex) {

                }

                @Override
                protected void onNextResult(CreatedAt createdAt) {
                    if (null != onRefreshCallBack) {
                        onRefreshCallBack.onRefreshSucc(createdAt);
                    }
                }
            });


        }else if (Clazz.equals(Debris.class)) {
                NetRxWrap.wrap(GetFunApi.refreshDebries(id)).subscribe(new NormalSubscriber<CreatedAt>() {
                    @Override
                    protected void onFail(ApiException ex) {

                    }

                    @Override
                    protected void onNextResult(CreatedAt createdAt) {
                        if (null != onRefreshCallBack) {
                            onRefreshCallBack.onRefreshSucc(createdAt);
                        }
                    }
                });
        }

    }
  public interface OnRefreshCallBack{
      void onRefreshSucc(CreatedAt createdAt);
  }

  
}
