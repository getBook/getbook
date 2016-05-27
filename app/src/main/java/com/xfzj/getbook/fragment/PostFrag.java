package com.xfzj.getbook.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xfzj.getbook.R;
import com.xfzj.getbook.common.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zj on 2016/3/16.
 */
public class PostFrag extends BaseFragment implements PostShowFrag.OnPostClickListener {

    private PostShowFrag postShowFrag, postTopicSshowFrag;
    private FragmentManager fm;
    private PostDetailFrag postDetailFrag;
    public static final String ARG_PARAM1 = "PostFrag.class";
    public static final String POSTDETAIL = "postdetail";
    private ImageView iv;
    private String mParam1;
    private List<Fragment> frags = new ArrayList<>();
    private Post post;
    private boolean empty;

    public static PostFrag newInstance(String param1) {
        PostFrag fragment = new PostFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public PostDetailFrag getPostDetailFrag() {
        return postDetailFrag;
    }

    public PostFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
        fm = getChildFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_post, container, false);
        if (TextUtils.isEmpty(mParam1)) {
            initPostShowFrag();
        } else if (POSTDETAIL.equals(mParam1)) {
            initPostDetailFrag(post,false);
        }

        return view;
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            return;
        }
        if (empty) {
//            initPostShowFrag();
        } else {
            initPostDetailFrag(post,false);
        }
    }

    public void hideEmoji() {
        if (null != postDetailFrag) {
            postDetailFrag.hideEmoji();
        }
    }

    public boolean isEmojiShow() {
        if (null == postDetailFrag) {
            return false;
        }
        return postDetailFrag.isEmojiShow();
    }

    
    
    public void initPostShowFrag() {

        postShowFrag = (PostShowFrag) fm.findFragmentByTag(PostShowFrag.PARAM);
        if (null == postShowFrag || postShowFrag.isDetached()) {
            postShowFrag = PostShowFrag.newInstance(null);
        }
        postShowFrag.setOnPostClickListener(this);
        showFrag(postShowFrag, PostShowFrag.PARAM);

    }

    private void initPostTopicFrag(String topic) {
        postTopicSshowFrag = (PostShowFrag) fm.findFragmentByTag(PostShowFrag.TOPIC_PARAM);
        if (null == postTopicSshowFrag || postTopicSshowFrag.isDetached()) {
            postTopicSshowFrag = PostShowFrag.newInstance(topic);
        }
        postTopicSshowFrag.setOnPostClickListener(this);
        showFrag(postTopicSshowFrag, PostShowFrag.TOPIC_PARAM);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
    }

    private void showFrag(Fragment frag, String tag) {
        showFrag(frag, tag, true);

    }

    private void showFrag(Fragment frag, String tag, boolean b) {
        FragmentTransaction ft = fm.beginTransaction();

        if (!frag.isAdded()) {
            frags.add(frag);
            ft.add(R.id.fram1, frag, tag);
            ft.addToBackStack(null);
        }
        for (Fragment f : frags) {
            if (f == frag) {
                ft.show(frag);
            } else {
                ft.hide(f);
            }
        }
      
           
        
        ft.commit();
        fm.executePendingTransactions();

    }

    private void initPostDetailFrag(Post post,boolean b) {
        postDetailFrag = (PostDetailFrag) fm.findFragmentByTag(PostDetailFrag.PARAM);
        if (null == postDetailFrag || postDetailFrag.isDetached()) {
            postDetailFrag = PostDetailFrag.newInstance(PostDetailFrag.PARAM);
        }
        showFrag(postDetailFrag, PostDetailFrag.PARAM,b);
        postDetailFrag.setPost(post);

    }

//    private void initDownloadFrag() {
//        downloadFrag = (DownloadFrag) fm.findFragmentByTag(DownloadFrag.PARAM);
//        if (null == downloadFrag || downloadFrag.isDetached()) {
//            downloadFrag = DownloadFrag.newInstance(DownloadFrag.PARAM);
//        }
//        showFrag(downloadFrag, null);
//
//    }

//    @Override
//    public void onClick(View v) {
//        initDownloadFrag();
//    }


    @Override
    public void onPostDeatilClick(Post post) {
        initPostDetailFrag(post,true);

    }

    @Override
    public void onTopicClick(String topic) {
        initPostTopicFrag(topic);
    }

    public void isInit(boolean empty) {
        this.empty = empty;
    }
}
