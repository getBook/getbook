package com.xfzj.getbook.views.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.viewpagerindicator.CirclePageIndicator;
import com.xfzj.getbook.R;
import com.xfzj.getbook.common.ChatEmoji;
import com.xfzj.getbook.utils.FaceConversionUtil;
import com.xfzj.getbook.views.gridview.NoScrollGridView;
import com.xfzj.getbook.views.listview.BaseListViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

/**
 * Created by zj on 2016/5/7.
 */
public class EmojiView extends FrameLayout implements AdapterView.OnItemClickListener, ViewPager.OnPageChangeListener {


    @Bind(R.id.view_pager)
    AutoScrollViewPager viewPager;
    @Bind(R.id.pageIndicator)
    CirclePageIndicator pageIndicator;
    @Bind(R.id.ll)
    LinearLayout ll;
    private Context context;

    /**
     * 表情页的监听事件
     */
    private OnCorpusSelectedListener onCorpusSelectedListener;


    /**
     * 表情页界面集合
     */
    private ArrayList<View> pageViews;


    /**
     * 表情集合
     */
    private List<List<ChatEmoji>> emojis;


    /**
     * 表情数据填充器
     */
    private List<EmojiAdapter> faceAdapters;

    /**
     * 当前表情页
     */
    private int current = 0;


    public EmojiView(Context context) {
        this(context, null);
    }

    public EmojiView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmojiView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public EmojiView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.emojilayout, null);
        ButterKnife.bind(this, view);
        addView(view);
        initViewPager();
        viewPager.setAdapter(new MyAdapter());
        viewPager.addOnPageChangeListener(this);
        pageIndicator.setViewPager(viewPager);
        viewPager.setCurrentItem(0);

    }


    public void setOnCorpusSelectedListener(OnCorpusSelectedListener onCorpusSelectedListener) {
        this.onCorpusSelectedListener = onCorpusSelectedListener;
    }

    public void show() {
        if (null != ll && ll.getVisibility() != VISIBLE) {
            ll.setVisibility(VISIBLE);
        }
    }

    public void hidden() {
        if (null != ll && ll.getVisibility() == VISIBLE) {
            ll.setVisibility(INVISIBLE);
        }
    }

    private void initViewPager() {
     
        emojis = FaceConversionUtil.getInstace().emojiLists;
        pageViews = new ArrayList<>();
        // 中间添加表情页

        faceAdapters = new ArrayList<EmojiAdapter>();
        for (int i = 0; i < emojis.size(); i++) {
            NoScrollGridView view = new NoScrollGridView(context);
            EmojiAdapter adapter = new EmojiAdapter(emojis.get(i), context);
            view.setAdapter(adapter);
            faceAdapters.add(adapter);
            view.setOnItemClickListener(this);
            view.setNumColumns(7);
            view.setBackgroundColor(Color.TRANSPARENT);
            view.setHorizontalSpacing(1);
            view.setVerticalSpacing(1);
            view.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
            view.setCacheColorHint(0);
            view.setPadding(5, 0, 5, 0);
            view.setSelector(new ColorDrawable(Color.TRANSPARENT));
            view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT));
            view.setGravity(Gravity.CENTER);
            pageViews.add(view);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ChatEmoji emoji = (ChatEmoji) faceAdapters.get(viewPager.getCurrentItem()).getItem(position);
        if (emoji.getId() == R.drawable.face_del_icon) {
            if (null != onCorpusSelectedListener) {
                onCorpusSelectedListener.onCorpusDeleted();
            }
        }
        if (!TextUtils.isEmpty(emoji.getCharacter())) {
            SpannableString spannableString = FaceConversionUtil.getInstace()
                    .addFace(getContext(), emoji.getId(), emoji.getCharacter());
            if (null != onCorpusSelectedListener)
                onCorpusSelectedListener.onCorpusSelected(spannableString);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int arg0) {
//        current = arg0 - 1;
//        // 如果是第一屏或者是最后一屏禁止滑动，其实这里实现的是如果滑动的是第一屏则跳转至第二屏，如果是最后一屏则跳转到倒数第二屏.
//        if (arg0 == pageViews.size() - 3 || arg0 == 0) {
//            if (arg0 == 0) {
//                viewPager.setCurrentItem(arg0 + 1);// 第二屏 会再次实现该回调方法实现跳转.
//            } else {
//                viewPager.setCurrentItem(arg0 - 1);// 倒数第二屏
//            }
//        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    private class EmojiAdapter extends BaseListViewAdapter<ChatEmoji> {
        ImageView item_iv_face;
        View rootView;

        public EmojiAdapter(List lists, Context context) {
            super(lists, context);
        }

        @Override
        protected BaseViewHolder getViewHolder() {

            return new BaseViewHolder() {

                @Override
                protected View initView() {
                    rootView = LayoutInflater.from(context).inflate(R.layout.item_face, null);
                    item_iv_face = (ImageView) rootView.findViewById(R.id.item_iv_face);
                    return rootView;
                }

                @Override
                public void setData(ChatEmoji emoji) {
                    if (emoji.getId() == R.drawable.face_del_icon) {
                        rootView.setBackground(null);
                        item_iv_face.setImageResource(emoji.getId());
                    } else if (TextUtils.isEmpty(emoji.getCharacter())) {
                        rootView.setBackground(null);
                        item_iv_face.setImageDrawable(null);
                    } else {
                        item_iv_face.setTag(emoji);
                        item_iv_face.setImageResource(emoji.getId());
                    }
                }
            };
        }
    }

    private class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return pageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(pageViews.get(position));

            return pageViews.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    public interface OnCorpusSelectedListener {

        void onCorpusSelected(SpannableString emoji);

        void onCorpusDeleted();
    }
}

