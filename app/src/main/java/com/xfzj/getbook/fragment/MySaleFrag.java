package com.xfzj.getbook.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.xfzj.getbook.BaseApplication;
import com.xfzj.getbook.R;
import com.xfzj.getbook.action.DeleteAction;
import com.xfzj.getbook.action.RefreshAction;
import com.xfzj.getbook.activity.BaseMySaleFrag;
import com.xfzj.getbook.common.CreatedAt;
import com.xfzj.getbook.common.Debris;
import com.xfzj.getbook.common.DebrisModel;
import com.xfzj.getbook.common.SecondBook;
import com.xfzj.getbook.common.SecondBookModel;
import com.xfzj.getbook.newnet.ApiException;
import com.xfzj.getbook.newnet.GetFunApi;
import com.xfzj.getbook.newnet.NetRxWrap;
import com.xfzj.getbook.newnet.NormalSubscriber;
import com.xfzj.getbook.utils.AppAnalytics;
import com.xfzj.getbook.utils.MyToast;
import com.xfzj.getbook.views.recycleview.FooterLoadMoreRVAdapter;
import com.xfzj.getbook.views.recycleview.LoadMoreListen;
import com.xfzj.getbook.views.recycleview.LoadMoreView;
import com.xfzj.getbook.views.view.WrapDebrisInfoItemView;
import com.xfzj.getbook.views.view.WrapSecondBookInfoItemView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MySaleFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MySaleFrag extends BaseFragment implements WrapSecondBookInfoItemView.onLongClickListener, WrapDebrisInfoItemView.onLongClickListener, LoadMoreView.RefreshListener, LoadMoreListen {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    //    private BaseLoadRecycleView rc;
    private LinearLayout llnodata;
    // TODO: Rename and change types of parameters
    private String mParam1;
    public static final String COLUMNSECONDBOOK = "columnsecondbook";
    public static final String COLUMNDEBRIS = "columndebris";
    //    private QueryAction queryAction;
    private BaseApplication baseApplication;
    private List<SecondBook> secondBooks;
    private List<Debris> debrises;
    private Set<WrapSecondBookInfoItemView> wrapSecondBookInfoItemViews;
    private Set<WrapDebrisInfoItemView> wrapDebrisInfoItemViews;
    private LoadMoreView loadMoreView;
    private Set<CheckBox> cbs = new HashSet<>();
    private DebrisAdapter debrisAdapter;
    private SecondBookAdapter secondBookAdapter;
    private int skip = 0;


    public MySaleFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment BaseMySaleFrag.
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

    private void onRefreshDebries() {
        NetRxWrap.wrap(GetFunApi.getUserDebris(skip))
                .subscribe(new NormalSubscriber<DebrisModel>() {
                    @Override
                    protected void onFail(ApiException ex) {
                        if (null != loadMoreView) {
                            loadMoreView.setVisibility(View.GONE);
                        }
                        if (null != llnodata) {
                            llnodata.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onNextResult(DebrisModel debrisModel) {
                        List<Debris> debrises = debrisModel.getDebrises();
                        if (debrises.size() == 0) {
                            if (null != loadMoreView) {
                                loadMoreView.setVisibility(View.GONE);
                            }
                            if (null != llnodata) {
                                llnodata.setVisibility(View.VISIBLE);
                            }
                            return;
                        }
                        loadMoreView.setRefreshFinish();
                        debrisAdapter.clear();
                        loadMoreView.setVisibility(View.VISIBLE);
                        debrisAdapter.addAll(debrises);
                    }
                });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_second_book, container, false);
        llnodata = (LinearLayout) view.findViewById(R.id.llnodata);
        loadMoreView = (LoadMoreView) view.findViewById(R.id.loadMoreView);
        wrapDebrisInfoItemViews = new HashSet<>();
        wrapSecondBookInfoItemViews = new HashSet<>();
        if (mParam1.equals(COLUMNSECONDBOOK)) {
            secondBooks = new ArrayList<>();
            secondBookAdapter = new SecondBookAdapter(secondBooks, getActivity());
            loadMoreView.setAdapter(secondBookAdapter);
        } else if (mParam1.equals(COLUMNDEBRIS)) {
            debrises = new ArrayList<>();
            debrisAdapter = new DebrisAdapter(debrises, getActivity());
            loadMoreView.setAdapter(debrisAdapter);
        } else {
            getFragmentManager().popBackStack();
        }
        loadMoreView.setOnrefreshListener(this);
        loadMoreView.setOnLoadMoreListen(this);
        baseApplication = (BaseApplication) getActivity().getApplication();
        onRefresh();
        return view;
    }
    @Override
    public void onLongClick(Object o) {
        ((BaseMySaleFrag) getParentFragment()).setVisibilty(View.VISIBLE);
        setCbVisibility(View.VISIBLE);


    }

    public void setCbVisibility(int i) {
        for (CheckBox cb : cbs) {
            cb.setVisibility(i);
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
                List<CheckBox> checkBoxes = new ArrayList<CheckBox>(cbs);
                for (int i = 0; i < checkBoxes.size(); i++) {
                    if (checkBoxes.get(i).isChecked()) {
                        ints.add(checkBoxes.get(i));
                        if (mParam1.equals(COLUMNSECONDBOOK)) {
                            SecondBook secondBook = secondBookAdapter.getAll().get(i);
                            if (null != secondBook) {
                                AppAnalytics.onEvent(getActivity(), AppAnalytics.R_SB);
                                final WrapSecondBookInfoItemView wrapSecondBookInfoItemView = new ArrayList<>(wrapSecondBookInfoItemViews).get(i);
                                RefreshAction.refresh(secondBook.getId(), new RefreshAction.OnRefreshCallBack() {
                                    @Override
                                    public void onRefreshSucc(CreatedAt createdAt) {
                                        AppAnalytics.onEvent(getActivity(), AppAnalytics.R_SB);
                                        wrapSecondBookInfoItemView.refresh();
                                    }
                                }, SecondBook.class);

                            }
                        } else if (mParam1.equals(COLUMNDEBRIS)) {
                            Debris debris = debrisAdapter.getAll().get(i);
                            if (null != debris) {
                                AppAnalytics.onEvent(getActivity(), AppAnalytics.R_DB);
                                final WrapDebrisInfoItemView wrapDebrisInfoItemView = new ArrayList<>(wrapDebrisInfoItemViews).get(i);
                                RefreshAction.refresh(debris.getId(), new RefreshAction.OnRefreshCallBack() {
                                    @Override
                                    public void onRefreshSucc(CreatedAt createdAt) {
                                        wrapDebrisInfoItemView.refresh();
                                    }
                                }, Debris.class);
                            }
                        }
                    }
                }
            }
        }).setNegativeButton(getString(R.string.cancel), null).create().show();


    }

    public void delete() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.tishi)).setMessage(getString(R.string.delete_cannot_find)).setPositiveButton(getString(R.string.ensure), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<SecondBook> secondBooks = new ArrayList<>();
                List<String> debrises = new ArrayList<>();
                List<CheckBox> ints = new ArrayList<>();
                List<WrapSecondBookInfoItemView> wraps = new ArrayList<>();
                List<WrapDebrisInfoItemView> wrapDebris = new ArrayList<>();
                List<CheckBox> checkBoxes = new ArrayList<CheckBox>(cbs);
                for (int i = 0; i < checkBoxes.size(); i++) {
                    if (checkBoxes.get(i).isChecked()) {
                        ints.add(checkBoxes.get(i));
                        if (mParam1.equals(COLUMNSECONDBOOK)) {

                            final SecondBook secondBook = secondBookAdapter.getAll().get(i);
                            if (null != secondBook) {
                                DeleteAction.delete(secondBook.getId(), new DeleteAction.OnDeleteCallBack() {
                                    @Override
                                    public void onDeleteSucc() {
                                        AppAnalytics.onEvent(getActivity(), AppAnalytics.D_SB);
                                        MyToast.show(getActivity(), secondBook.getBookInfo().getBookName() + getString(R.string.delete_success));
                                        secondBookAdapter.remove(secondBook);
                                        cbs.remove(secondBook);
                                        wrapSecondBookInfoItemViews.remove(secondBook);

                                    }

                                    @Override
                                    public void onDelteFail(String error) {
                                        MyToast.show(getActivity(), secondBook.getBookInfo().getBookName() + getString(R.string.delete_fail));
                                    }
                                }, SecondBook.class);


                            }
                        } else if (mParam1.equals(COLUMNDEBRIS)) {
//                        
                            final Debris debris = debrisAdapter.getAll().get(i);
                            if (null != debris) {
                                DeleteAction.delete(debris.getId(), new DeleteAction.OnDeleteCallBack() {
                                    @Override
                                    public void onDeleteSucc() {
                                        AppAnalytics.onEvent(getActivity(), AppAnalytics.D_DB);
                                        MyToast.show(getActivity(), debris.getTitle() + getString(R.string.delete_success));
                                        debrisAdapter.remove(debris);
                                        cbs.remove(debris);
                                        wrapSecondBookInfoItemViews.remove(debris);
                                    }

                                    @Override
                                    public void onDelteFail(String error) {
                                        MyToast.show(getActivity(), debris.getTitle() + getString(R.string.delete_fail));
                                    }
                                }, Debris.class);


                            }
                        }
                    }
                }
//                DeleteAction deleteAction = new DeleteAction(getActivity());
                if (mParam1.equals(COLUMNSECONDBOOK)) {
                    for (WrapSecondBookInfoItemView w : wraps) {
//                        ll.removeView(w);
                        wrapSecondBookInfoItemViews.remove(w);

                    }
                    for (CheckBox i : ints) {
                        cbs.remove(i);
                    }
                    if (wrapSecondBookInfoItemViews.size() == 0) {
                        ((BaseMySaleFrag) getParentFragment()).setVisibilty(View.GONE);
                        llnodata.setVisibility(View.VISIBLE);
                    }

//                    deleteAction.deleteSecondBook(secondBooks);
                } else if (mParam1.equals(COLUMNDEBRIS)) {
                    for (WrapDebrisInfoItemView w : wrapDebris) {
//                        ll.removeView(w);
//                        wrapDebrisInfoItemViews.remove(w);

                    }
                    for (CheckBox i : ints) {
                        cbs.remove(i);
                    }
//                    if (wrapDebrisInfoItemViews.size() == 0) {
//                        ((BaseMySaleFrag) getParentFragment()).setVisibilty(View.GONE);
//                        llnodata.setVisibility(View.VISIBLE);
//                    }

//                    deleteAction.deleteDebris(debrises);
                }

            }
        }).setNegativeButton(getString(R.string.cancel), null).create().show();


    }

    @Override
    public void onRefresh() {
        skip = 0;
        loadMoreView.setRefreshing();
        if (mParam1.equals(COLUMNSECONDBOOK)) {
            onRefreshSecondBook();
        } else if (mParam1.equals(COLUMNDEBRIS)) {
            onRefreshDebries();
        }
    }

    private void onRefreshSecondBook() {
        NetRxWrap.wrap(GetFunApi.getUserSecondBook(skip))
                .subscribe(new NormalSubscriber<SecondBookModel>() {
                    @Override
                    protected void onFail(ApiException ex) {
                        if (null != loadMoreView) {
                            loadMoreView.setVisibility(View.GONE);
                        }
                        if (null != llnodata) {
                            llnodata.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onNextResult(SecondBookModel secondBookModel) {
                        List<SecondBook> secondBooks = secondBookModel.getSecondBooks();
                        if (secondBooks.size() == 0) {
                            if (null != loadMoreView) {
                                loadMoreView.setVisibility(View.GONE);
                            }
                            if (null != llnodata) {
                                llnodata.setVisibility(View.VISIBLE);
                            }
                            return;
                        }
                        loadMoreView.setRefreshFinish();
                        secondBookAdapter.clear();
                        loadMoreView.setVisibility(View.VISIBLE);
                        secondBookAdapter.addAll(secondBooks);
                    }
                });
    }


    @Override
    public void onLoadMore() {
        if (mParam1.equals(COLUMNSECONDBOOK)) {
            onLoadMoreSecondBook();
        } else if (mParam1.equals(COLUMNDEBRIS)) {
            onLoadMoreDebries();
        }

    }

    private void onLoadMoreSecondBook() {
        NetRxWrap.wrap(GetFunApi.getUserSecondBook(++skip))
                .subscribe(new NormalSubscriber<SecondBookModel>() {
                    @Override
                    protected void onFail(ApiException ex) {

                    }

                    @Override
                    public void onNextResult(SecondBookModel secondBookModel) {
                        loadMoreView.setLoadMoreFinish();
                        List<SecondBook> secondBooks = secondBookModel.getSecondBooks();
                        if (secondBooks.size() == 0) {
                            return;
                        }
                        secondBookAdapter.addAll(secondBooks);
                    }
                });
    }

    private void onLoadMoreDebries() {
        NetRxWrap.wrap(GetFunApi.getUserDebris(++skip))
                .subscribe(new NormalSubscriber<DebrisModel>() {
                    @Override
                    protected void onFail(ApiException ex) {

                    }

                    @Override
                    public void onNextResult(DebrisModel debrisModel) {
                        loadMoreView.setLoadMoreFinish();
                        List<Debris> debrises = debrisModel.getDebrises();
                        if (debrises.size() == 0) {
                            return;
                        }
//                        loadMoreView.setVisibility(View.VISIBLE);
                        debrisAdapter.addAll(debrises);
                    }
                });
    }

    private class DebrisAdapter extends FooterLoadMoreRVAdapter<Debris> {

        public DebrisAdapter(List<Debris> datas, Context context) {
            super(datas, context);
        }

        @Override
        protected View getNormalView() {
            return new WrapDebrisInfoItemView(context);
        }


        @Override
        protected RecyclerView.ViewHolder getNormalViewHolder(View view, int viewType) {
            return new NormalViewHolder<Debris>(view, viewType) {
                @Override
                protected void setNormalContent(View itemView, Debris item, int viewType) {
                    if (itemView instanceof WrapDebrisInfoItemView) {
                        ((WrapDebrisInfoItemView) itemView).update(item);
                        ((WrapDebrisInfoItemView) itemView).setViewOnLongClickListener(MySaleFrag.this);
                        CheckBox cb = ((WrapDebrisInfoItemView) itemView).getCb();
                        cbs.add(cb);
                        wrapDebrisInfoItemViews.add((WrapDebrisInfoItemView) itemView);

                    }
                }
            };
        }
    }

    private class SecondBookAdapter extends FooterLoadMoreRVAdapter<SecondBook> {

        public SecondBookAdapter(List<SecondBook> datas, Context context) {
            super(datas, context);
        }

        @Override
        protected View getNormalView() {
            return new WrapSecondBookInfoItemView(context);
        }


        @Override
        protected RecyclerView.ViewHolder getNormalViewHolder(View view, int viewType) {
            return new NormalViewHolder<SecondBook>(view, viewType) {
                @Override
                protected void setNormalContent(View itemView, SecondBook item, int viewType) {
                    if (itemView instanceof WrapSecondBookInfoItemView) {
                        ((WrapSecondBookInfoItemView) itemView).update(item);
                        ((WrapSecondBookInfoItemView) itemView).setViewOnLongClickListener(MySaleFrag.this);
                        CheckBox cb = ((WrapSecondBookInfoItemView) itemView).getCb();
                        cbs.add(cb);
                        wrapSecondBookInfoItemViews.add((WrapSecondBookInfoItemView) itemView);
                    }
                }
            };
        }
    }
}
