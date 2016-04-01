package com.xfzj.getbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.xfzj.getbook.R;
import com.xfzj.getbook.action.UploadAction;
import com.xfzj.getbook.common.BookInfo;
import com.xfzj.getbook.common.PicPath;
import com.xfzj.getbook.common.SecondBook;
import com.xfzj.getbook.common.User;
import com.xfzj.getbook.fragment.BookInfoFrag;
import com.xfzj.getbook.fragment.PicSelectFrag;
import com.xfzj.getbook.utils.MyToast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by zj on 2016/2/8.
 */
public class PublishSecondBookActivity extends PublishAty  {

    public static final String ISBN = "isbn";
    public static final String FROM = "from";
    private BookInfoFrag bif;
    /**
     * 最多只能选择4张图片
     */


    private String isbn;
    private String from;


    @Override
    protected void onSetContentView() {
        OPTIONS = 4;
        setContentView(R.layout.bookpublish);
       

    }


    private void showFrag(BookInfoFrag bif) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.framBookInfo, bif).commit();
        fm.executePendingTransactions();


    }


    @Override
    protected void initFrag() {
        bif = (BookInfoFrag) getSupportFragmentManager().findFragmentByTag(BookInfoFrag.ARG_PARAM1);
        if (null == bif) {
            bif = BookInfoFrag.newInstance(BookInfoFrag.ARG_PARAM1);
        }
        psf = (PicSelectFrag) getSupportFragmentManager().findFragmentByTag(PicSelectFrag.ARG_PARAM1);
        if (null == psf) {
            psf = PicSelectFrag.newInstance(PicSelectFrag.ARG_PARAM1);
        }
    }


    @Override
    public void onCreateView(Bundle savedInstanceState) {
        super.onCreateView(savedInstanceState);
        showFrag(bif);
        Intent i = getIntent();
        isbn = i.getStringExtra(ISBN);
        from = i.getStringExtra(FROM);
        bif.setContent(isbn);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvPublish:
                if (canPublish()) {
                    List<String> lists = picAddView.getPath();
                    String[] str = (String[]) lists.toArray(new String[lists.size()]);
                    BookInfo info = bif.getBookInfo();
                    if (null == info) {
                        MyToast.show(getApplicationContext(), "未获取到书本信息，请重新扫描");
                        return;
                    }

                    SecondBook secondBook = new SecondBook(BmobUser.getCurrentUser(getApplicationContext(), User.class), info, etPrice.getText().toString().trim(), etNewOld.getText().toString().trim(), plusMinusView.getText(), str, etDescribe.getText().toString(), etTele.getText().toString().trim());
                    UploadAction uploadPicAction = new UploadAction(PublishSecondBookActivity.this, secondBook, bif.getBookInfo());
                    uploadPicAction.publishSecondBook(this);
                }
                break;
            case R.id.tvSelect:
                if (null == psf) {
                    return;
                }
                closePicSelectFrag();
                picAddView.deleteAlbum();
                List<PicPath> lists = new ArrayList<>();
                for (String str : psf.getPath()) {
                    lists.add(new PicPath(PicPath.FLAG_ALBUM, str));
                }
                picAddView.addAll(lists);
                break;
        }


    }

    @Override
    protected boolean canPublish() {
        return super.canPublish();
    }
    @Override
    public void onFail() {

    }
}

