package com.xfzj.getbook.activity;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.xfzj.getbook.R;
import com.xfzj.getbook.action.UploadAction;
import com.xfzj.getbook.common.Post;
import com.xfzj.getbook.utils.InputMethodManagerUtils;
import com.xfzj.getbook.utils.MyToast;
import com.xfzj.getbook.utils.MyUtils;
import com.xfzj.getbook.views.ResizeLayout;
import com.xfzj.getbook.views.view.BaseEditText;
import com.xfzj.getbook.views.view.EmojiView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zj on 2016/4/16.
 */
public class PublishPostAty extends BasePublishAty implements BaseEditText.OnEmojiClickListener, EmojiView.OnCorpusSelectedListener {
    @Bind(R.id.betTopic)
    BaseEditText betTopic;
    @Bind(R.id.betContent)
    BaseEditText betContent;
    @Bind(R.id.resizeLayout)
    ResizeLayout resizeLayout;
    private String[] topics;
    /**
     * emoji弹出框
     */
    private PopupWindow popupWindow;
    private EmojiView emojiView;
    /**
     * 键盘是否显示
     */
    private boolean isKeyBoardShow;


    @Override
    protected void onSetContentView() {

        OPTIONS = 4;
        setContentView(R.layout.post_publish_layout);

    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {
        super.onCreateView(savedInstanceState);
        betContent.setOnEmojiClickListener(this);
        resizeLayout.setOnResizeListener(new ResizeLayout.OnResizeListener() {
            @Override
            public void onResize(int w, int h, int oldw, int oldh) {
                if (h < oldh) {
                    picAddView.post(new Runnable() {
                        @Override
                        public void run() {
                            picAddView.setVisibility(View.GONE);
                            isKeyBoardShow = true;
                        }
                    });
                } else {
                    picAddView.post(new Runnable() {
                        @Override
                        public void run() {
                            picAddView.setVisibility(View.VISIBLE);
                            isKeyBoardShow = false;
                        }
                    });
                }
            }
        });
    }


    @Override
    protected boolean canPublish() {
        String topic = betTopic.getText();
        if (!TextUtils.isEmpty(topic)) {
            topics = topic.split("\\s+");
            if (topics.length > 3) {
                MyToast.show(getApplicationContext(), "最多3个话题");
                return false;
            }
            for (int i = 0; i < topics.length; i++) {
                if (topics[i].length() > 6) {
                    MyToast.show(getApplicationContext(), "每个话题不能超过6字符");
                    return false;
                }
            }

        }
        if (!betContent.isLessMaxCount()) {
            MyToast.show(getApplicationContext(), "字数超过限制");
            return false;
        }
        String content = betContent.getText();
        if (TextUtils.isEmpty(content)) {
            MyToast.show(getApplicationContext(), getString(R.string.please_to_input, getString(R.string.content)));
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    @Override
    protected void doPublish() {
        List<String> lists = picAddView.getPath();
        String[] str = (String[]) lists.toArray(new String[lists.size()]);
        Post post = new Post(betContent.getText(), topics, null, str);
        UploadAction uploadAction = new UploadAction(PublishPostAty.this, post);
        uploadAction.publishPost(this);
    }

    @Override
    public void onFail() {

    }

    @Override
    public void onEmojiClick() {
        if (isKeyBoardShow) {
            InputMethodManagerUtils.hide(this, betContent);
        }
        if (null == popupWindow) {
            popupWindow = new PopupWindow(this);
            popupWindow.setHeight((int) MyUtils.dp2px(getApplicationContext(), 130f));
            popupWindow.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
            if (null == emojiView) {
                emojiView = new EmojiView(this);
            }
            popupWindow.setContentView(emojiView);
            popupWindow.setBackgroundDrawable(null);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
            popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                        popupWindow.dismiss();
                        return true;
                    }
                    return false;
                }
            });
            emojiView.setOnCorpusSelectedListener(this);
            popupWindow.showAtLocation(betContent.getImageView(), Gravity.BOTTOM, 0, 0);
        } else if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else {
            popupWindow.showAtLocation(betContent.getImageView(), Gravity.BOTTOM, 0, 0);
        }
    }

    @Override
    public void onBackPressed() {
        if (null != popupWindow && popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else {
            super.onBackPressed();

        }
    }

    @Override
    public void onCorpusSelected(SpannableString emoji) {
        betContent.etrequestFocus();
        betContent.append(emoji);
    }

    @Override
    public void onCorpusDeleted() {
        betContent.deleteSpannaleString("[em]", "[/em]");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
