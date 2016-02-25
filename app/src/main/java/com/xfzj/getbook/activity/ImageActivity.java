package com.xfzj.getbook.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.xfzj.getbook.R;
import com.xfzj.getbook.fragment.BookInfoFrag;
import com.xfzj.getbook.utils.MyToast;
import com.xfzj.getbook.views.gridview.PicAddView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by zj on 2016/2/8.
 */
public class ImageActivity extends AppActivity {
    List<String> lists = new ArrayList<>();

    @Bind(R.id.addView)
    PicAddView picAddView;

    BookInfoFrag bif;
    @Override
    protected void onSetContentView() {
        
        setContentView(R.layout.bookpublish);
        initFrag();
        showFrag(bif);
    }

    private void showFrag(BookInfoFrag bif) {
       FragmentManager fm = getSupportFragmentManager();
       FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.framBookInfo, bif).commit();
        fm.executePendingTransactions();
                
        

    }

    private void initFrag() {
        bif = (BookInfoFrag) getSupportFragmentManager().findFragmentByTag(BookInfoFrag.ARG_PARAM1);
        if (null == bif) {
            bif = BookInfoFrag.newInstance(BookInfoFrag.ARG_PARAM1);
        }

    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {
        bif.setContent("9787101003048");
        String[] imageUrls = {
                "/sdcard/DCIM/P51021-181739.jpg",
                "/sdcard/DCIM/P51021-181739.jpg"};
        for (String url : imageUrls) {
            lists.add(url);
        }

        picAddView.setOnItemClick(new PicAddView.OnItemClick() {

            @Override
            public void onAddClick(int position, int size, int maxPics) {
                picAddView.addAll(lists);
            }

            @Override
            public void onUnAddClick(int position, int size) {
                MyToast.show(getApplicationContext(), "最多添加4个" + position);
            }

            @Override
            public void onPicClick(int position, String path) {
                MyToast.show(getApplicationContext(), "click" + position);
            }
        });
//        GridView gridview = (GridView) findViewById(R.id.gridview);
//
//
//        final BaseGridViewAdapter adapter = new BaseGridViewAdapter(this, lists);
//        gridview.setAdapter(adapter);
//        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (adapter.isLast(position)) {
//
//
//                } else {
//                    MyToast.show(getApplicationContext(), "click" + position);
//                }
//            }
//        });


    }

    private List<Integer> add() {
        List<Integer> lists = new ArrayList<>();
        lists.add(R.mipmap.ic_launcher);
        lists.add(R.mipmap.ic_launcher);
        lists.add(R.mipmap.ic_launcher);
        lists.add(R.mipmap.ic_launcher);
        return lists;
    }
}
