package com.xfzj.getbook.activity;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.xfzj.getbook.R;
import com.xfzj.getbook.action.UploadAction;
import com.xfzj.getbook.common.Post;
import com.xfzj.getbook.utils.InputMethodManagerUtils;
import com.xfzj.getbook.utils.MyToast;
import com.xfzj.getbook.utils.SharedPreferencesUtils;
import com.xfzj.getbook.views.ResizeLayout;
import com.xfzj.getbook.views.view.BaseEditText;
import com.xfzj.getbook.views.view.EmojiView;

import java.util.List;

import butterknife.Bind;

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
    @Bind(R.id.emojiView)
    EmojiView emojiView;
    private String[] topics;

    /**
     * 键盘是否显示
     */
    private boolean isKeyBoardShow;
    /**
     * emoji是否显示
     */
    private boolean isEmojiShow;
    private Post post;

    @Override
    protected void onSetContentView() {

        OPTIONS = 4;
        setContentView(R.layout.post_publish_layout);

    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {
        super.onCreateView(savedInstanceState);
        betContent.setOnEmojiClickListener(this);
        emojiView.setOnCorpusSelectedListener(this);
        emojiView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                emojiView.getViewTreeObserver().removeOnPreDrawListener(this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.height = SharedPreferencesUtils.getSoftKeyBoard(getApplicationContext());
                emojiView.setLayoutParams(lp);
                return true;
            }

        });
        resizeLayout.setOnResizeListener(new ResizeLayout.OnResizeListener() {
            @Override
            public void onResize(int w, int h, int oldw, int oldh) {
                if (h < oldh) {
                    if (isEmojiShow) {
                        hideEmojiView(true);
                    }
                    picAddView.post(new Runnable() {
                        @Override
                        public void run() {
                            picAddView.setVisibility(View.GONE);
                        }
                    });
                } else {
                    if (!isEmojiShow) {
                        picAddView.post(new Runnable() {
                            @Override
                            public void run() {
                                picAddView.setVisibility(View.VISIBLE);
                            }
                        });
                    }
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
                betTopic.setError("最多3个话题");
                return false;
            } else {
                betTopic.setErrorEnable(false);
            }
            for (int i = 0; i < topics.length; i++) {
                if (topics[i].length() > 6) {
                    betTopic.setError("每个话题不能超过6字符");
                    return false;
                } else {
                    betTopic.setErrorEnable(false);
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
            betContent.etrequestFocus();
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
        post = new Post(betContent.getText(), topics, null, str);
        UploadAction uploadAction = new UploadAction(PublishPostAty.this, post);
        uploadAction.publishPost(this);
    }

    @Override
    public void onSuccess() {
        super.onSuccess();
        if (null != post) {
            SharedPreferencesUtils.saveFocusPost(getApplicationContext(), post.getObjectId(), 0);
        }
    }

    @Override
    public void onFail() {

    }

    private void showEmojiView() {
        if (null != emojiView && emojiView.getVisibility() != View.VISIBLE) {
            emojiView.post(new Runnable() {
                @Override
                public void run() {
                    emojiView.setVisibility(View.VISIBLE);
                }
            });
            isEmojiShow = true;
        }
        if (null != picAddView && picAddView.getVisibility() == View.VISIBLE) {
            picAddView.post(new Runnable() {
                @Override
                public void run() {
                    picAddView.setVisibility(View.GONE);
                }
            });
        }
        InputMethodManagerUtils.hide(getApplicationContext(), betContent);
    }

    private void hideEmojiView(boolean isPicAddShow) {
        if (null != emojiView && emojiView.getVisibility() == View.VISIBLE) {
            emojiView.post(new Runnable() {
                @Override
                public void run() {
                    emojiView.setVisibility(View.GONE);
                }
            });
            isEmojiShow = false;
        }
        if (null != picAddView && picAddView.getVisibility() != View.VISIBLE && !isPicAddShow) {
            picAddView.post(new Runnable() {
                @Override
                public void run() {
                    picAddView.setVisibility(View.VISIBLE);
                }
            });
        }
//        InputMethodManagerUtils.hide(getApplicationContext(), betContent);
    }


    @Override
    public void onEmojiClick() {
        if (isEmojiShow) {
            hideEmojiView(false);
        } else {
            showEmojiView();
        }
        if (isKeyBoardShow) {
            InputMethodManagerUtils.hide(this, betContent);
        }

    }

    @Override
    public void onBackPressed() {
        if (isEmojiShow) {
            hideEmojiView(false);
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

}
