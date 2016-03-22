package com.xfzj.getbook.action;

import android.content.Context;

import com.xfzj.getbook.common.Debris;
import com.xfzj.getbook.common.SecondBook;

import java.util.List;

import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by zj on 2016/3/13.
 */
public class RefreshAction extends BaseAction {
    private Context context;

    public RefreshAction(Context context) {
        this.context = context;
    }


    public  void refresh(List<String> lists, Class Clazz) {
        if (Clazz.equals(SecondBook.class)) {
            SecondBook secondBook = new SecondBook();
            for (String id : lists) {
                secondBook.setObjectId(id);
                secondBook.update(context, new UpdateListener() {
                    @Override
                    public void onSuccess() {
//                        MyToast.show(context, context.getString(R.string.delete_success));
                    }

                    @Override
                    public void onFailure(int i, String s) {

                    }
                });
            }
            


        }else if (Clazz.equals(Debris.class)) {
            Debris debris = new Debris();
            for (String id : lists) {
                debris.setObjectId(id);
                debris.update(context, new UpdateListener() {
                    @Override
                    public void onSuccess() {
//                        MyToast.show(context, context.getString(R.string.delete_success));
                    }

                    @Override
                    public void onFailure(int i, String s) {

                    }
                });
            }

        }

    }


  
}
