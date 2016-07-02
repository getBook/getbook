package com.xfzj.getbook.newnet;

import com.xfzj.getbook.BaseApplication;
import com.xfzj.getbook.common.Score;
import com.xfzj.getbook.common.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by zj on 2016/6/16.
 */
public class CardApi extends BaseApi {
    private static BaseApplication baseApplication;
    private static User user;
    private static String msg;
    private static Map<String, String> m = new HashMap<>();
    static {
        baseApplication = BaseApplication.newInstance();
        if (null != baseApplication) {
            user = baseApplication.getUser();
            if (null != user) {
                m.put("schoolCode", "nuist");
                m.put("iPlanetDirectoryPro", user.getMsg());
            }
        }

    }

    
    //定义接口
    private interface UserService {
//        //GET注解不可用@FormUrlEncoded，要用@Query注解引入请求参数
//        @GET("user/user_queryProfile")
//        Observable<UserProfileResp> queryProfile(@Query("userId") int userId);


        @FormUrlEncoded
        @POST("Score/Query")
        Observable<List<Score>> queryScore(@FieldMap Map<String, String> map);
    }

    protected static final UserService service = getRetrofit(CARD_API_SERVER).create(UserService.class);

//    //查询用户信息接口
//    public static Observable<UserProfileResp> queryProfile(int userId) {
//        return service.queryProfile(userId);
//    }

    //更新用户名接口
    public static Observable<List<Score>> queryScore(int pageIndex) {
        Map<String, String> map = new HashMap<>();
        map.putAll(m);
        map.put("pageIndex", pageIndex + "");
        map.put("xn", "");
        return service.queryScore(map);
    }
    
}