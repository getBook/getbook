package com.xfzj.getbook.newnet;

import com.xfzj.getbook.Constants;
import com.xfzj.getbook.common.Avator;
import com.xfzj.getbook.common.BookInfo;
import com.xfzj.getbook.common.CreatedAt;
import com.xfzj.getbook.common.Debris;
import com.xfzj.getbook.common.DebrisModel;
import com.xfzj.getbook.common.ImageModel;
import com.xfzj.getbook.common.Post;
import com.xfzj.getbook.common.SecondBook;
import com.xfzj.getbook.common.SecondBookModel;
import com.xfzj.getbook.common.User;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by zj on 2016/6/16.
 */
public class GetFunApi extends BaseApi {
    //定义接口
    private interface UserService {
        @FormUrlEncoded
        @POST("user/login")
        Observable<User> login(@Field("sno") String sno, @Field("password") String password);

        @FormUrlEncoded
        @PATCH("user/nickname")
        Observable<String> updateHuaName(@Field("nickname") String huaName);

        @Multipart
        @POST("user/avator")
        Observable<Avator> updateAvator(@PartMap Map<String, RequestBody> params);

        @Multipart
        @POST("user/avator")
        Observable<Avator> updateAvator2(@Part MultipartBody.Part image);

        @GET("user/goods")
        Observable<DebrisModel> getUserDebris(@Query("limit") int limit, @Query("offset") int offset);

        @GET("user/bookrecords")
        Observable<SecondBookModel> getUserSecondBook(@Query("limit") int limit, @Query("offset") int offset);
        @GET("goods")
        Observable<DebrisModel> getDebris(@Query("limit") int limit, @Query("offset") int offset);

        @GET("bookrecords")
        Observable<SecondBookModel> getSecondBook(@Query("limit") int limit, @Query("offset") int offset);
        @FormUrlEncoded
        @POST("goods")
        Observable<String> publishDebris(@FieldMap Map<String, String> param, @Field("pictures") String[] pics);

        @Multipart
        @POST("photos")
        Observable<List<ImageModel>> uploadFiles(@PartMap Map<String, RequestBody> params);

        @FormUrlEncoded
        @PATCH("user/goods/{id}/createtime")
        Observable<CreatedAt> refreshDebries(@Path("id") String id1, @Field("id") String id2);

        @FormUrlEncoded
        @PATCH("user/bookrecords/{id}/createtime")
        Observable<CreatedAt> refreshSecondBook(@Path("id") String id1, @Field("id") String id2);

        @FormUrlEncoded
        @POST("bookinfos")
        Observable<String> uploadBookInfo(@FieldMap Map<String, String> param, @Field("author") String[] author);

        @FormUrlEncoded
        @POST("bookrecords")
        Observable<String> publishSecondBook(@FieldMap Map<String, String> param, @Field("pictures") String[] pics);

        @FormUrlEncoded
        @DELETE("user/bookrecords/{id}")
        Observable<String> deleteSecondBook(@Path("id") String id1, @Field("id") String id);

        @FormUrlEncoded
        @DELETE("user/goods/{id}")
        Observable<String> deleteDebries(@Path("id") String id1, @Field("id") String id);
        
        @FormUrlEncoded
        @POST("posts")
        Observable<String> publishPost(@Field("topic") String[] topic,@Field("content") String content,@Field("pictures") String[] pictures);
    }


    protected static final UserService service = getRetrofit(GETFUN_API_SERVER).create(UserService.class);


    public static Observable<User> login(String sno, String password) {
        return service.login(sno, password);
    }

    public static Observable<String> updateHuaName(String huaName) {
        return service.updateHuaName(huaName);
    }

    public static Observable<Avator> updateAvator(File filepath) {
        Map<String, RequestBody> map = new HashMap<>();
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), filepath);
        map.put("avator\"; filename=\"" + filepath.getName() + "", fileBody);
        return service.updateAvator(map);
    }

    public static Observable<Avator> updateAvator2(File filepath) {
        // 创建 RequestBody，用于封装 请求RequestBody
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), filepath);
// MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("avator", filepath.getName(), requestFile);
        return service.updateAvator2(body);
    }


    public static Observable<DebrisModel> getUserDebris(int offset) {
        return service.getUserDebris(Constants.DEBRIS_LIMIT, offset);
    }

    public static Observable<SecondBookModel> getUserSecondBook(int offset) {
        return service.getUserSecondBook(Constants.SECONDBOOK_LIMIT, offset);
    }

    public static Observable<DebrisModel> getDebris( int offset) {
        return service.getDebris(Constants.DEBRIS_LIMIT, offset);
    }

    public static Observable<SecondBookModel> getSecondBook( int offset) {
        return service.getSecondBook(Constants.SECONDBOOK_LIMIT, offset);
    }
    /**
     * title	String	杂货名称
     * originPrice	Number	原价
     * description	String	杂货描述
     * newold	Number	新旧(1-10)
     * discount	Number	售价
     * count	Number	数量
     * telePhone	String	手机号码
     * pictures
     *
     * @param debris
     * @return
     */
    public static Observable<String> publishDebris(Debris debris) {
        Map<String, String> map = new HashMap<>();
        map.put("title", debris.getTitle());
        map.put("originPrice", debris.getOriginPrice());
        map.put("description", debris.getTips());
        map.put("newold", debris.getNewold());
        map.put("discount", debris.getDiscount());
        map.put("count", debris.getCount() + "");
        map.put("telePhone", debris.getTele());
        return service.publishDebris(map, debris.getPics());
    }

    public static Observable<List<ImageModel>> uploadFiles(String[] pics) {
        Map<String, RequestBody> map = new HashMap<>();
        for (String pic : pics) {
            File file = new File(pic);
            RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            map.put("images\"; filename=\"" + file.getName() + "", fileBody);
        }
        return service.uploadFiles(map);
    }


    public static Observable<CreatedAt> refreshDebries(String id) {
        return service.refreshDebries(id, id);
    }

    public static Observable<CreatedAt> refreshSecondBook(String id) {
        return service.refreshSecondBook(id, id);
    }

    /**
     * | 名字 | 类型 | 详细描述 |
     * | ----- | ----- | -------- |
     * | isbn | String | 书籍的isbn号 |
     * | originPrice | Number | 原价 |
     * | publish | String | 出版社 |
     * | author | Array | 书籍作者 |
     * | bookName | String | 书籍名称 |
     * | coverImage | String | 封面图片 |
     *
     * @param bookInfo
     * @return
     */
    public static Observable<String> uploadBookInfo(BookInfo bookInfo) {
        Map<String, String> map = new HashMap<>();
        map.put("isbn", bookInfo.getIsbn());
        map.put("originPrice", bookInfo.getOriginPrice());
        map.put("publish", bookInfo.getPublish());
        map.put("bookName", bookInfo.getBookName());
        map.put("coverImage", bookInfo.getImage());
        return service.uploadBookInfo(map, bookInfo.getAuthor());
    }

    /**
     * isbn	String	书籍的isbn号
     * newold	Number	新旧(1-10)
     * discount	Number	售价
     * count	Number	数量
     * telePhone	String	手机号码
     * pictures	Array	二手书照片
     *
     * @param secondBook
     * @return
     */
    public static Observable<String> publishSecondBook(SecondBook secondBook) {
        Map<String, String> map = new HashMap<>();
        map.put("isbn", secondBook.getBookInfo().getIsbn());
        map.put("newold", secondBook.getNewold());
        map.put("discount", secondBook.getDiscount());
        map.put("count", secondBook.getCount() + "");
        map.put("telePhone", secondBook.getTelePhone());
        return service.publishSecondBook(map, secondBook.getPictures());
    }

    public static Observable<String> deleteSecondBook(String id) {
        return service.deleteSecondBook(id, id);
    }

    public static Observable<String> deleteDebries(String id) {
        return service.deleteDebries(id, id);
    }

    public static Observable<String> publishPost(Post post) {
        return service.publishPost(post.getTopic(), post.getContent(), post.getFiles());
    }
}