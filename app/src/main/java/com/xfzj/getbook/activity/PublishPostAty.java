package com.xfzj.getbook.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.xfzj.getbook.R;
import com.xfzj.getbook.action.UploadAction;
import com.xfzj.getbook.common.Post;
import com.xfzj.getbook.utils.MyToast;
import com.xfzj.getbook.views.view.BaseEditText;

import java.util.List;

import butterknife.Bind;

/**
 * Created by zj on 2016/4/16.
 */
public class PublishPostAty extends BasePublishAty {
    @Bind(R.id.betTopic)
    BaseEditText betTopic;
    @Bind(R.id.betContent)
    BaseEditText betContent;
    private String[] topics;
    

    @Override
    protected void onSetContentView() {

        OPTIONS = 4;
        setContentView(R.layout.post_publish_layout);

    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {
        super.onCreateView(savedInstanceState);
    }


    @Override
    protected boolean canPublish() {
        String topic = betTopic.getText();
        if (!TextUtils.isEmpty(topic)) {
             topics = topic.split("\\s+");
            if (topics.length> 3) {
                MyToast.show(getApplicationContext(), "最多3个话题");
                return false;
            }
            for (int i=0;i<topics.length;i++) {
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
        Post post = new Post(betContent.getText(), topics, null,str);
        UploadAction uploadAction = new UploadAction(PublishPostAty.this, post);
        uploadAction.publishPost(this);
    }

    @Override
    public void onFail() {

    }
}
