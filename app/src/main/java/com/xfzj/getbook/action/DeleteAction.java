package com.xfzj.getbook.action;

import android.content.Context;

import com.xfzj.getbook.common.Debris;
import com.xfzj.getbook.common.SecondBook;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DeleteListener;

/**
 * Created by zj on 2016/3/13.
 */
public class DeleteAction extends BaseAction {
    private Context context;

    public DeleteAction(Context context) {
        this.context = context;
    }

    public void deleteDebris(List<Debris> lists) {
        for (Debris id : lists) {
            List<BmobFile> bmobFiles = id.getFiles();
            for (BmobFile bmobFile : bmobFiles) {
                bmobFile.delete(context);
            }
            delete(id);
        }
    }

    public void deleteSecondBook(List<SecondBook> lists) {
        for (SecondBook id : lists) {
            List<BmobFile> bmobFiles = id.getFiles();
            for (BmobFile bmobFile : bmobFiles) {
                bmobFile.delete(context);
            }
            delete(id);
        }
    }

    private void delete(BmobObject bmobObject) {
        bmobObject.delete(context, new DeleteListener() {
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
