package com.xfzj.getbook.action;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import com.xfzj.getbook.BaseApplication;
import com.xfzj.getbook.R;
import com.xfzj.getbook.common.Avator;
import com.xfzj.getbook.common.BookInfo;
import com.xfzj.getbook.common.Debris;
import com.xfzj.getbook.common.ImageModel;
import com.xfzj.getbook.common.Post;
import com.xfzj.getbook.common.SecondBook;
import com.xfzj.getbook.common.User;
import com.xfzj.getbook.newnet.ApiException;
import com.xfzj.getbook.newnet.BaseSubscriber;
import com.xfzj.getbook.newnet.GetFunApi;
import com.xfzj.getbook.newnet.NetRxWrap;
import com.xfzj.getbook.newnet.NormalSubscriber;
import com.xfzj.getbook.utils.MyToast;
import com.xfzj.getbook.utils.SharedPreferencesUtils;
import com.xfzj.getbook.views.view.NavigationHeaderView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.bmob.v3.listener.UploadFileListener;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zj on 2016/2/28.
 */
public class UploadAction extends BaseAction {
    private Post post;
    private Context context;
    private SecondBook secondBook;
    private ProgressDialog pd;
    private BookInfo bookInfo;
    private Debris debris;

    public UploadAction(Context context, SecondBook secondBook, BookInfo bookInfo) {

        this.context = context;
        this.secondBook = secondBook;
        this.bookInfo = bookInfo;
        setProgressDialog(context.getString(R.string.publishing));
    }

    public UploadAction(Context context, Debris debris) {

        this.context = context;
        this.debris = debris;
        setProgressDialog(context.getString(R.string.publishing));
    }

    public UploadAction(Context context, Post post) {

        this.context = context;
        this.post = post;
        setProgressDialog(context.getString(R.string.publishing));
    }

    public UploadAction() {
    }

    private void setProgressDialog(String string) {
        pd = ProgressDialog.show(context, "", string);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
    }

    public static void saveHeader(Context context, String header) {
        SharedPreferencesUtils.saveUserHeader(context, header);
        context.sendBroadcast(new Intent(NavigationHeaderView.ACTION));
    }

    public static void uploadHeader(final Context context, File file) {
        NetRxWrap.wrap(GetFunApi.updateAvator(file))
                .subscribe(new BaseSubscriber<Avator>() {
                    @Override
                    protected void onError(ApiException ex) {
                        MyToast.show(context, ex.getDisplayMessage());
                    }

                    @Override
                    protected void onPermissionError(ApiException ex) {
                        MyToast.show(context, ex.getDisplayMessage());
                    }

                    @Override
                    protected void onResultError(ApiException ex) {
                        MyToast.show(context, ex.getDisplayMessage());
                    }

                    @Override
                    public void onNextResult(Avator avator) {
                        MyToast.show(context, context.getString(R.string.update_success));
                        saveHeader(context, avator.getAvator());
                    }
                });
    }

    public void uploadHeader(final Context context, final User user, File file) {
        final BmobFile bmobFile = new BmobFile(file);
        bmobFile.uploadblock(context, new UploadFileListener() {
            @Override
            public void onSuccess() {

                user.setBmobHeader(bmobFile);

                user.update(context, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        saveHeader(context, bmobFile.getFileUrl(context));
                    }

                    @Override
                    public void onFailure(int i, String s) {

                    }
                });
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }

    public static void publishDebris(final Context context, final Debris debris, final UploadListener uploadListener) {
        NetRxWrap.wrap(GetFunApi.uploadFiles(debris.getPics())).flatMap(new Func1<List<ImageModel>, Observable<String[]>>() {
            @Override
            public Observable<String[]> call(List<ImageModel> imageModels) {
                List<String> url = new ArrayList<>();
                for (ImageModel imageModel : imageModels) {
                    url.add(imageModel.getId());
                }
                return Observable.just(url.toArray(new String[url.size()]));
            }
        }).subscribe(new NormalSubscriber<String[]>(context, null, context.getString(R.string.publishing), true) {
            @Override
            protected void onNextResult(String[] imageModels) {
                debris.setPics(imageModels);
                NetRxWrap.wrap(GetFunApi.publishDebris(debris)).subscribe(new NormalSubscriber<String>() {
                    @Override
                    protected void onNextResult(String aVoid) {

                        onSuccess(context, uploadListener);
                    }

                    @Override
                    protected void onFail(ApiException ex) {
                        onFailure(context, uploadListener, context.getString(R.string.publish_fail), ex.getDisplayMessage());
                    }
                });
            }

            @Override
            protected void onFail(ApiException ex) {
                onFailure(context, uploadListener, context.getString(R.string.upload_image_fail), ex.getDisplayMessage());
            }
        });
    }

    public void publishPost(final UploadListener uploadListener) {

        pd.show();
        if (post.getPics() == null || post.getPics().length == 0) {
            uploadPost(uploadListener);
            return;
        }
        BmobFile.uploadBatch(context, post.getPics(), new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> list, List<String> list1) {
                if (list1.size() == post.getPics().length) {
                    String[] str = list1.toArray(new String[post.getPics().length]);
                    post.setFiles(list);
                    uploadPost(uploadListener);
                }
            }

            @Override
            public void onProgress(int i, int i1, int i2, int i3) {
//                MyLog.print("onProgress","当前："+i+" 完成"+i1+" 总共"+i2+"完成"+i3);
            }

            @Override
            public void onError(int i, String s) {
                MyToast.show(context, "上传图片失败，请重试" + s);
//                onFailure(uploadListener);
            }
        });
    }

    private void uploadPost(final UploadListener uploadListener) {
        post.save(context, new SaveListener() {
            @Override
            public void onSuccess() {
                BaseApplication baseApplication = ((BaseApplication) context.getApplicationContext());
                if (null != baseApplication) {
                    User user = baseApplication.getUser();
                    if (null != user) {
                        SharedPreferencesUtils.saveFocusPost(context, post.getObjectId(), 0);
                    }
                }

//                UploadAction.this.onSuccess(uploadListener);
            }

            @Override
            public void onFailure(int i, String s) {
                MyToast.show(context, "发布失败，请重试" + i + s);
//                UploadAction.this.onFailure(uploadListener);
            }
        });
    }

    public static void publishSecondBook(final Context context, final SecondBook secondBook, final UploadListener uploadListener) {
        if (null == secondBook || null == secondBook.getBookInfo()) {
            onFailure(context, uploadListener, context.getString(R.string.publish_fail), "信息错误，请重填");
            return;
        }
        NetRxWrap.wrap(GetFunApi.uploadBookInfo(secondBook.getBookInfo())).subscribe(new NormalSubscriber<String>() {
            @Override
            protected void onFail(ApiException ex) {
                onFailure(context, uploadListener, context.getString(R.string.upload_bookinfo_fail), ex.getDisplayMessage());
            }

            @Override
            protected void onNextResult(String s) {
                NetRxWrap.wrap(GetFunApi.uploadFiles(secondBook.getPictures())).flatMap(new Func1<List<ImageModel>, Observable<String[]>>() {
                    @Override
                    public Observable<String[]> call(List<ImageModel> imageModels) {
                        List<String> url = new ArrayList<>();
                        for (ImageModel imageModel : imageModels) {
                            url.add(imageModel.getId());
                        }
                        return Observable.just(url.toArray(new String[url.size()]));
                    }
                }).subscribe(new NormalSubscriber<String[]>() {
                    @Override
                    protected void onFail(ApiException ex) {
                        onFailure(context, uploadListener, context.getString(R.string.upload_image_fail), ex.getDisplayMessage());
                    }

                    @Override
                    protected void onNextResult(String[] strings) {
                        secondBook.setPictures(strings);
                        NetRxWrap.wrap(GetFunApi.publishSecondBook(secondBook)).subscribe(new NormalSubscriber<String>() {
                            @Override
                            protected void onFail(ApiException ex) {
                                onFailure(context, uploadListener, context.getString(R.string.publish_fail), ex.getDisplayMessage());
                            }

                            @Override
                            protected void onNextResult(String s) {
                                onSuccess(context, uploadListener);
                            }
                        });
                    }
                });
            }

        });
    }

    public static void onSuccess(Context context, UploadListener uploadListener) {
        MyToast.show(context, context.getString(R.string.publish_success));
        if (null != uploadListener) {
            uploadListener.onSuccess();

        }
    }

    public static void onFailure(Context context, UploadListener uploadListener, String... str) {
        MyToast.show(context, str[0] + ":" + str[1]);
        if (null != uploadListener) {
            uploadListener.onFail();
        }
    }

    public interface UploadListener {
        void onSuccess();

        void onFail();
    }


}
