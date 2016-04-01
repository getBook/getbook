package com.xfzj.getbook.fragment;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.xfzj.getbook.BaseApplication;
import com.xfzj.getbook.R;
import com.xfzj.getbook.action.DeleteAction;
import com.xfzj.getbook.action.QueryAction;
import com.xfzj.getbook.action.RefreshAction;
import com.xfzj.getbook.activity.MySaleAty;
import com.xfzj.getbook.common.Debris;
import com.xfzj.getbook.common.SecondBook;
import com.xfzj.getbook.common.User;
import com.xfzj.getbook.utils.AppAnalytics;
import com.xfzj.getbook.views.view.WrapDebrisInfoItemView;
import com.xfzj.getbook.views.view.WrapSecondBookInfoItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MySaleFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MySaleFrag extends BaseFragment implements QueryAction.OnQueryListener<Object>, WrapSecondBookInfoItemView.onClickListener, WrapSecondBookInfoItemView.onLongClickListener, WrapDebrisInfoItemView.onClickListener, WrapDebrisInfoItemView.onLongClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    //    private BaseLoadRecycleView rc;
    private LinearLayout llnodata;
    // TODO: Rename and change types of parameters
    private String mParam1;
    public static final String COLUMNSECONDBOOK = "columnsecondbook";
    public static final String COLUMNDEBRIS = "columndebris";
    private QueryAction queryAction;
    private BaseApplication baseApplication;
    private User user;
    private List<SecondBook> secondBooks;
    private List<Debris> debrises;
    private List<WrapSecondBookInfoItemView> wrapSecondBookInfoItemViews;
    private List<WrapDebrisInfoItemView> wrapDebrisInfoItemViews;
    private LinearLayout ll;
    private List<CheckBox> cbs = new ArrayList<>();
    private ProgressDialog pd;


    public MySaleFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment MySaleFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static MySaleFrag newInstance(String param1) {
        MySaleFrag fragment = new MySaleFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }


    }

    private void doQuery() {
        if (null == user) {
            getActivity().finish();
            return;
        }
        pd = ProgressDialog.show(getActivity(), null, getString(R.string.loading));
        if (mParam1.equals(COLUMNSECONDBOOK)) {
            queryAction.querySelfSecondBook(user.getSno());
        } else if (mParam1.equals(COLUMNDEBRIS)) {
            queryAction.querySelfDebris(user.getSno());
        } else {
            getActivity().finish();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_second_book, container, false);
        llnodata = (LinearLayout) view.findViewById(R.id.llnodata);
        ll = (LinearLayout) view.findViewById(R.id.ll);
        if (mParam1.equals(COLUMNSECONDBOOK)) {
            secondBooks = new ArrayList<>();

        } else if (mParam1.equals(COLUMNDEBRIS)) {
            debrises = new ArrayList<>();

        } else {
            getActivity().finish();
        }

        baseApplication = (BaseApplication) getActivity().getApplication();
        user = baseApplication.getUser();
        if (null == user) {
            getActivity().finish();
        }
        queryAction = new QueryAction(getActivity());
        queryAction.setOnQueryListener(this);
        doQuery();
        return view;
    }


    @Override
    public void onSuccess(List<Object> lists) {
        pd.dismiss();
        if ((null == lists || lists.size() == 0)) {
            if (null != llnodata) {
                llnodata.setVisibility(View.VISIBLE);
            }
        } else {
            if (null != llnodata) {
                llnodata.setVisibility(View.GONE);
            }
            if (lists.get(0) instanceof SecondBook) {
                wrapSecondBookInfoItemViews = new ArrayList<>();
                for (int i = 0; i < lists.size(); i++) {
                    WrapSecondBookInfoItemView wrapSecondBookInfoItemView = new WrapSecondBookInfoItemView(getActivity());
                    wrapSecondBookInfoItemView.update((SecondBook) lists.get(i));
                    CheckBox cb = wrapSecondBookInfoItemView.getCb();
                    cbs.add(cb);
                    wrapSecondBookInfoItemView.setViewOnClickListener(this);
                    wrapSecondBookInfoItemView.setViewOnLongClickListener(this);
                    wrapSecondBookInfoItemViews.add(wrapSecondBookInfoItemView);
                    ll.addView(wrapSecondBookInfoItemView);
                }
            } else if (lists.get(0) instanceof Debris) {
                wrapDebrisInfoItemViews = new ArrayList<>();
                for (int i = 0; i < lists.size(); i++) {
                    WrapDebrisInfoItemView wrapDebrisInfoItemView = new WrapDebrisInfoItemView(getActivity());
                    wrapDebrisInfoItemView.update((Debris) lists.get(i));
                    CheckBox cb = wrapDebrisInfoItemView.getCb();
                    cbs.add(cb);
                    wrapDebrisInfoItemView.setViewOnClickListener(this);
                    wrapDebrisInfoItemView.setViewOnLongClickListener(this);
                    wrapDebrisInfoItemViews.add(wrapDebrisInfoItemView);
                    ll.addView(wrapDebrisInfoItemView);
                }

            } else {
                getActivity().finish();
            }
        }


    }

    @Override
    public void onFail() {
        if (null != llnodata) {
            llnodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(Object o) {
    }

    @Override
    public void onLongClick(Object o) {
        ((MySaleAty) getActivity()).setVisibilty(View.VISIBLE);
        for (CheckBox cb : cbs) {
            if (cb.getVisibility() == View.GONE) {
                cb.setVisibility(View.VISIBLE);
            }

        }
    }

    public void selectAllRbs() {
        for (CheckBox cb : cbs) {
            cb.setChecked(true);
        }
    }

    public void refresh() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.tishi)).setMessage(getString(R.string.refersh_can_find)).setPositiveButton(getString(R.string.ensure), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<String> lists = new ArrayList<>();
                List<CheckBox> ints = new ArrayList<>();
                List<WrapSecondBookInfoItemView> wraps = new ArrayList<>();
                List<WrapDebrisInfoItemView> wrapDebris = new ArrayList<>();
                for (int i = 0; i < cbs.size(); i++) {
                    if (cbs.get(i).isChecked()) {
                        ints.add(cbs.get(i));
                        if (mParam1.equals(COLUMNSECONDBOOK)) {
                            WrapSecondBookInfoItemView wrapSecondBookInfoItemView = wrapSecondBookInfoItemViews.get(i);
                            wraps.add(wrapSecondBookInfoItemView);
                            SecondBook secondBook = wrapSecondBookInfoItemView.getSecondBookInfoItemView().getSecondBook();
                            if (null != secondBook) {
                                lists.add(secondBook.getObjectId());
                            }
                        } else if (mParam1.equals(COLUMNDEBRIS)) {
                            WrapDebrisInfoItemView wrapDebrisInfoItemView = wrapDebrisInfoItemViews.get(i);
                            wrapDebris.add(wrapDebrisInfoItemView);
                            Debris debris = wrapDebrisInfoItemView.getDebrisContentInfoView().getDebris();
                            if (null != debris) {
                                lists.add(debris.getObjectId());
                            }
                        }
                    }
                }
                RefreshAction refreshAction = new RefreshAction(getActivity());
                if (mParam1.equals(COLUMNSECONDBOOK)) {
                    for (WrapSecondBookInfoItemView w : wraps) {
                        w.refresh();
                    }
                    AppAnalytics.onEvent(getActivity(), AppAnalytics.R_SB);
                    refreshAction.refresh(lists, SecondBook.class);
                } else if (mParam1.equals(COLUMNDEBRIS)) {
                    for (WrapDebrisInfoItemView w : wrapDebris) {
                        w.refresh();

                    }
                    AppAnalytics.onEvent(getActivity(), AppAnalytics.R_DB);
                    refreshAction.refresh(lists, Debris.class);
                }
            }
        }).setNegativeButton(getString(R.string.cancel), null).create().show();


    }

    public void delete() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.tishi)).setMessage(getString(R.string.delete_cannot_find)).setPositiveButton(getString(R.string.ensure), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<String> lists = new ArrayList<>();
                List<CheckBox> ints = new ArrayList<>();
                List<WrapSecondBookInfoItemView> wraps = new ArrayList<>();
                List<WrapDebrisInfoItemView> wrapDebris = new ArrayList<>();
                for (int i = 0; i < cbs.size(); i++) {
                    if (cbs.get(i).isChecked()) {
                        ints.add(cbs.get(i));
                        if (mParam1.equals(COLUMNSECONDBOOK)) {
                            WrapSecondBookInfoItemView wrapSecondBookInfoItemView = wrapSecondBookInfoItemViews.get(i);
                            wraps.add(wrapSecondBookInfoItemView);
                            SecondBook secondBook = wrapSecondBookInfoItemView.getSecondBookInfoItemView().getSecondBook();
                            if (null != secondBook) {
                                lists.add(secondBook.getObjectId());
                            }
                        } else if (mParam1.equals(COLUMNDEBRIS)) {
                            WrapDebrisInfoItemView wrapDebrisInfoItemView = wrapDebrisInfoItemViews.get(i);
                            wrapDebris.add(wrapDebrisInfoItemView);
                            Debris debris = wrapDebrisInfoItemView.getDebrisContentInfoView().getDebris();
                            if (null != debris) {
                                lists.add(debris.getObjectId());
                            }
                        }
                    }
                }
                DeleteAction deleteAction = new DeleteAction(getActivity());
                if (mParam1.equals(COLUMNSECONDBOOK)) {
                    for (WrapSecondBookInfoItemView w : wraps) {
                        ll.removeView(w);
                        wrapSecondBookInfoItemViews.remove(w);

                    }
                    for (CheckBox i : ints) {
                        cbs.remove(i);
                    }
                    if (wrapSecondBookInfoItemViews.size() == 0) {
                        ((MySaleAty) getActivity()).setVisibilty(View.GONE);
                        llnodata.setVisibility(View.VISIBLE);
                    }
                    AppAnalytics.onEvent(getActivity(), AppAnalytics.D_SB);
                    deleteAction.delete(lists, SecondBook.class);
                } else if (mParam1.equals(COLUMNDEBRIS)) {
                    for (WrapDebrisInfoItemView w : wrapDebris) {
                        ll.removeView(w);
                        wrapDebrisInfoItemViews.remove(w);

                    }
                    for (CheckBox i : ints) {
                        cbs.remove(i);
                    }
                    if (wrapDebrisInfoItemViews.size() == 0) {
                        ((MySaleAty) getActivity()).setVisibilty(View.GONE);
                        llnodata.setVisibility(View.VISIBLE);
                    }
                    AppAnalytics.onEvent(getActivity(), AppAnalytics.D_DB);
                    deleteAction.delete(lists, Debris.class);
                }
                
            }
        }).setNegativeButton(getString(R.string.cancel), null).create().show();


    }
}
