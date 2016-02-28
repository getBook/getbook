package com.xfzj.getbook.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xfzj.getbook.R;
import com.xfzj.getbook.async.BaseAsyncTask;
import com.xfzj.getbook.common.BookInfo;
import com.xfzj.getbook.net.BaseHttp;
import com.xfzj.getbook.net.HttpHelper;
import com.xfzj.getbook.utils.MyToast;
import com.xfzj.getbook.views.view.BookInfoView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookInfoFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookInfoFrag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_PARAM1 = "BookInfoFrag";

    // TODO: Rename and change types of parameters
    private String mParam1;


    private String isbn;

    private String url= BaseHttp.GetBookInfo;

    BookInfoView bookInfoView;
    public BookInfoFrag() {
        
        // Required empty public constructor
    }

    public String getIsbn() {
        return isbn;
    }

    public void setISbn(String isbn) {
        this.isbn = isbn;
        this.url += isbn;
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
        new BaseAsyncTask<Void, Void, BookInfo>(getActivity()) {
            @Override
            protected void onPost(BookInfo bookInfo) {
                if (null != bookInfo) {
                    bookInfoView.updateBookInfo(bookInfo);
                }else {
                    MyToast.show(getActivity(), getResources().getString(R.string.get_bookinfo_fail));
                    
                }
            }

            @Override
            protected BookInfo doExcute(Void[] params) {
              
                try {
                    byte[] bytes=new HttpHelper().DoConnection(url);
                    if (null==bytes) {
                        return null;
                    }else {
                        String str = new String(bytes, "utf-8");
                        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                        BookInfo bookInfo = gson.fromJson(str, BookInfo.class);
                        bookInfo.setIsbn(getIsbn());
                        return bookInfo;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }.execute();


    }


}
