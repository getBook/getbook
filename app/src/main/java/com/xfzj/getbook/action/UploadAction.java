package com.xfzj.getbook.action;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import com.xfzj.getbook.R;
import com.xfzj.getbook.common.BookInfo;
import com.xfzj.getbook.common.Debris;
import com.xfzj.getbook.common.SecondBook;
import com.xfzj.getbook.common.User;
import com.xfzj.getbook.utils.MyLog;
import com.xfzj.getbook.utils.MyToast;
import com.xfzj.getbook.utils.MyUtils;
import com.xfzj.getbook.utils.SharedPreferencesUtils;
import com.xfzj.getbook.views.view.NavigationHeaderView;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by zj on 2016/2/28.
 */
public class UploadAction extends BaseAction {
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

    public UploadAction() {
    }

    private void setProgressDialog(String string) {
        pd = ProgressDialog.show(context, "", string);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
    }

    public static  void saveHeader(Context context, String header) {
        SharedPreferencesUtils.saveUserHeader(context, header);
        context.sendBroadcast(new Intent(NavigationHeaderView.ACTION));
    }
    public void uploadHeader(final Context context, final User user, String str) {
        uploadHeader(context, user, new File(str));
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
                        saveHeader(context,bmobFile.getFileUrl(context));
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
    public void publishDebris(final UploadListener uploadListener) {

        pd.show();
        BmobFile.uploadBatch(context, debris.getPics(), new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> list, List<String> list1) {
                if (list1.size() == debris.getPics().length) {
                    String[] str = list1.toArray(new String[debris.getPics().length]);
                    debris.setFiles(list);
                    debris.save(context, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            onSucc(uploadListener);
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            MyToast.show(context, "发布失败，请重试" + i + s);
                            onFail(uploadListener);
                        }
                    });
                }
            }

            @Override
            public void onProgress(int i, int i1, int i2, int i3) {
//                MyLog.print("onProgress","当前："+i+" 完成"+i1+" 总共"+i2+"完成"+i3);
            }

            @Override
            public void onError(int i, String s) {
                MyToast.show(context, "上传图片失败，请重试" + s);
                onFail(uploadListener);
            }
        });
    }


    public void publishSecondBook(final UploadListener uploadListener) {

        pd.show();
        BmobQuery<BookInfo> query = new BmobQuery<>();
        query.addWhereEqualTo("isbn", bookInfo.getIsbn());
        query.findObjects(context, new FindListener<BookInfo>() {
            @Override
            public void onSuccess(List<BookInfo> list) {
                if (null != list && list.size() > 0) {
                    secondBook.setBookInfo(list.get(0));
                    uploadSecondBook(uploadListener);
                }else{
                    File file = MyUtils.getDiskCacheDir(context, bookInfo.getIsbn() + ".jpg");

                    final BmobFile bmobFile = new BmobFile(file);

                    bmobFile.uploadblock(context, new UploadFileListener() {
                        @Override
                        public void onSuccess() {
                            bookInfo.setBmobImage(bmobFile);
                            secondBook.setBookInfo(bookInfo);
                            bookInfo.save(context, new SaveListener() {
                                @Override
                                public void onSuccess() {
                                    uploadSecondBook(uploadListener);
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    if (i == 105) {
                                        uploadSecondBook(uploadListener);
                                    } else {
                                        MyToast.show(context, "发布失败，请重试" + i + s);
                                        onFail(uploadListener);
                                    }
                                }
                            });
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            MyToast.show(context, "上传图片失败，请重试" + i + s);
                            onFail(uploadListener);
                        }
                    });
                }
            }

            @Override
            public void onError(int i, String s) {
              
            }
        });


    }

    private void onSucc(UploadListener uploadListener) {
        if (pd.isShowing()) {
            pd.dismiss();
        }
        if (null != uploadListener) {
            uploadListener.onSuccess();

        }
    }

    private void onFail(UploadListener uploadListener) {
        if (pd.isShowing()) {
            pd.dismiss();
        }
        if (null != uploadListener) {
            uploadListener.onFail();
        }
    }

    private void uploadSecondBook(final UploadListener uploadListener) {
        MyLog.print("pics", Arrays.toString(secondBook.getPictures()));
        BmobFile.uploadBatch(context, secondBook.getPictures(), new cn.bmob.v3.listener.UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> list, List<String> list1) {
                if (list1.size() == secondBook.getPictures().length) {
                    String[] str = list1.toArray(new String[list1.size()]);
                    secondBook.setFiles(list);
                    secondBook.save(context, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            onSucc(uploadListener);
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            MyToast.show(context, "发布失败，请重试");
                            onFail(uploadListener);
                        }
                    });
                }
            }

            @Override
            public void onProgress(int i, int i1, int i2, int i3) {

            }

            @Override
            public void onError(int i, String s) {
                MyToast.show(context, "上传图片失败，请重试" + i + s);
                onFail(uploadListener);
            }
        });
    }


    public interface UploadListener {
        void onSuccess();

        void onFail();
    }


}
