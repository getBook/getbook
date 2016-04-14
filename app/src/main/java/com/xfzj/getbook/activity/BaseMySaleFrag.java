package com.xfzj.getbook.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xfzj.getbook.MainActivity;
import com.xfzj.getbook.R;
import com.xfzj.getbook.fragment.BaseFragment;
import com.xfzj.getbook.views.view.BaseToolBar;

/**
 * Created by zj on 2016/3/11.
 */
public class BaseMySaleFrag extends BaseFragment implements View.OnClickListener {
    public static final String FROM = "from";
    public static final String ARG_PARAM1 = "BaseMySaleFrag.this";
    public static final int SECONDBOOK = R.string.secondbook;

    public static final int DEBRIS = R.string.drugstore;

    private Toolbar toolbar;
    private BaseToolBar baseToolBar;
    private Fragment secondBookgrag, debrisFrag;
    private FragmentManager fm;
    private com.xfzj.getbook.fragment.MySaleFrag mySaleFrag;
    private TextView tvAll, tvDele;
    public static final int RESULT = 1010;
    private int mParam1;


    public static BaseMySaleFrag newInstance(int param1) {
        BaseMySaleFrag fragment = new BaseMySaleFrag();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public BaseMySaleFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
        }
        fm = getChildFragmentManager();
        if (mParam1 == SECONDBOOK) {
            initSBFrag();
        } else if (mParam1 == DEBRIS) {
            initDebrisFrag();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.aty_mysale, container, false);


        return view;
    }

    public void initToolbar(BaseToolBar baseToolBar) {
        this.baseToolBar = baseToolBar;
        tvAll = this.baseToolBar.getTvRight1();
        tvAll.setText(R.string.all);
        tvDele = this.baseToolBar.getTvRight2();
        tvDele.setText(R.string.operations);
        tvAll.setOnClickListener(this);
        tvDele.setOnClickListener(this);
    }


    private void initDebrisFrag() {
        mySaleFrag = (com.xfzj.getbook.fragment.MySaleFrag) fm.findFragmentByTag(com.xfzj.getbook.fragment.MySaleFrag.COLUMNDEBRIS);
        if (null == mySaleFrag || mySaleFrag.isDetached()) {
            mySaleFrag = com.xfzj.getbook.fragment.MySaleFrag.newInstance(com.xfzj.getbook.fragment.MySaleFrag.COLUMNDEBRIS);
        }
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fram, mySaleFrag);
        ft.commit();
        fm.executePendingTransactions();
    }

    private void initSBFrag() {

        mySaleFrag = (com.xfzj.getbook.fragment.MySaleFrag) fm.findFragmentByTag(com.xfzj.getbook.fragment.MySaleFrag.COLUMNSECONDBOOK);
        if (null == mySaleFrag || mySaleFrag.isDetached()) {
            mySaleFrag = com.xfzj.getbook.fragment.MySaleFrag.newInstance(com.xfzj.getbook.fragment.MySaleFrag.COLUMNSECONDBOOK);
        }
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fram, mySaleFrag);
        ft.commit();
        fm.executePendingTransactions();

    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (android.R.id.home == item.getItemId()) {
//            setResult(RESULT);
//            finish();
//        }
//        return true;
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        setResult(RESULT);
//        finish();
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvRight1:
                mySaleFrag.selectAllRbs();
                break;
            case R.id.tvRight2:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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


    public boolean isOriginState() {
        if(tvAll.getVisibility()==View.VISIBLE){
            return false;
        }
        return true;
    }
    public void setVisibilty(int i) {
        tvAll.setVisibility(i);
        tvDele.setVisibility(i);
        if(null!=mySaleFrag) {
            mySaleFrag.setCbVisibility(i);
        }
        if (i == View.VISIBLE) {
            ((MainActivity)getActivity()).menu.setGroupVisible(R.id.group1,false);
        }else{
            ((MainActivity)getActivity()).menu.setGroupVisible(R.id.group1,true);
        }
    }
}
