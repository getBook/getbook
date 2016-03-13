package com.xfzj.getbook.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.xfzj.getbook.R;
import com.xfzj.getbook.fragment.MySaleFrag;
import com.xfzj.getbook.views.view.BaseToolBar;

import butterknife.Bind;

/**
 * Created by zj on 2016/3/11.
 */
public class MySaleAty extends AppActivity implements View.OnClickListener {
    public static final String FROM = "from";
    @Bind(R.id.baseToolbar)
    BaseToolBar baseToolBar;
    private Toolbar toolbar;
    private Fragment secondBookgrag, debrisFrag;
    private FragmentManager fm;
    private MySaleFrag mySaleFrag;
    private TextView tvAll, tvDele;
    public static final int RESULT = 1010;
    @Override
    protected void onSetContentView() {
        setContentView(R.layout.aty_mysale);

    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String from = intent.getStringExtra(FROM);
        baseToolBar.initToolbar(this, from);
        toolbar = baseToolBar.getToolbar();
        tvAll = baseToolBar.getTvRight1();
        tvAll.setText(R.string.all);
        tvDele = baseToolBar.getTvRight2();
        tvDele.setText(R.string.operations);
        tvAll.setOnClickListener(this);
        tvDele.setOnClickListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fm = getSupportFragmentManager();
        if (from.equals(getString(R.string.secondbook))) {
            initSBFrag();
        } else if (from.equals(getString(R.string.drugstore))) {
            initDebrisFrag();
        } else {
            finish();
        }
    }

    private void initDebrisFrag() {
        mySaleFrag = (MySaleFrag) fm.findFragmentByTag(MySaleFrag.COLUMNDEBRIS);
        if (null == mySaleFrag || mySaleFrag.isDetached()) {
            mySaleFrag = MySaleFrag.newInstance(MySaleFrag.COLUMNDEBRIS);
        }
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fram, mySaleFrag);
        ft.commit();
        fm.executePendingTransactions();
    }

    private void initSBFrag() {

        mySaleFrag = (MySaleFrag) fm.findFragmentByTag(MySaleFrag.COLUMNSECONDBOOK);
        if (null == mySaleFrag || mySaleFrag.isDetached()) {
            mySaleFrag = MySaleFrag.newInstance(MySaleFrag.COLUMNSECONDBOOK);
        }
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fram, mySaleFrag);
        ft.commit();
        fm.executePendingTransactions();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            setResult(RESULT);
            finish();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvRight1:
                mySaleFrag.selectAllRbs();
                break;
            case R.id.tvRight2:
                AlertDialog.Builder builder = new AlertDialog.Builder(MySaleAty.this);
                builder.setItems(new String[]{getString(R.string.delete), getString(R.string.refresh)}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                mySaleFrag.delete();
                                break;
                            case 1:
                                mySaleFrag.refresh();
                                break;
                        }
                    }
                }).setTitle(getString(R.string.operations)).create().show();


            


                break;

        }
    }

    public void setVisibilty(int i) {
        tvAll.setVisibility(i);
        tvDele.setVisibility(i);
    }
}
