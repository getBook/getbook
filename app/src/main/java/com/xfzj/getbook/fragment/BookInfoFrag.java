package com.xfzj.getbook.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xfzj.getbook.R;
import com.xfzj.getbook.activity.AppActivity;
import com.xfzj.getbook.async.BaseAsyncTask;
import com.xfzj.getbook.async.GetBookInfoAsync;
import com.xfzj.getbook.common.BookInfo;
import com.xfzj.getbook.utils.MyToast;
import com.xfzj.getbook.views.view.BookInfoView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookInfoFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookInfoFrag extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_PARAM1 = "BookInfoFrag";

    // TODO: Rename and change types of parameters
    private String mParam1;


    private String isbn;

    private BookInfo bookInfo;

    BookInfoView bookInfoView;
    public BookInfoFrag() {
        
        // Required empty public constructor
    }

    public String getIsbn() {
        return isbn;
    }

    public void setISbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment BookInfoFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static BookInfoFrag newInstance(String param1) {
        BookInfoFrag fragment = new BookInfoFrag();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View view=inflater.inflate(R.layout.fragment_book_info, container, false);
        bookInfoView = (BookInfoView) view.findViewById(R.id.bookInfoView);
        return bookInfoView;
    }

    public void setContent(String isbn) {
        setISbn(isbn);
        update();
    }

    public void update() {
        GetBookInfoAsync getBookInfoAsync = new GetBookInfoAsync(getActivity());
        getBookInfoAsync.executeOnExecutor(((AppActivity)getActivity()).getThreadPoolExecutor(),getIsbn());
        getBookInfoAsync.setOnTaskListener(new BaseAsyncTask.onTaskListener<BookInfo>() {
            @Override
            public void onSuccess(BookInfo bookInfo) {
                BookInfoFrag.this.bookInfo = bookInfo;
                bookInfoView.updateBookInfo(bookInfo);
            }

            @Override
            public void onFail() {
                MyToast.show(getActivity(), getResources().getString(R.string.get_bookinfo_fail));
            }
        });
    }

    public String getBookName() {
        return bookInfoView.getBookName();
    }
    
    
    public BookInfo getBookInfo() {
        return bookInfo;
    }
}
