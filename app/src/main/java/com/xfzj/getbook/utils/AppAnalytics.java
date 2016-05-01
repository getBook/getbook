package com.xfzj.getbook.utils;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;
import com.xfzj.getbook.activity.AppActivity;

import java.util.HashMap;

/**
 * Created by zj on 2016/3/30.
 */
public class AppAnalytics {
    public static final String LOGIN = "login";
    public static final String LOGIN_SUCCESS = "login_success";
    public static final String LOGIN_FAIL = "login_fail";
    public static final String CLICK_SECONDBOOK = "click_secondbook";
    public static final String CLICK_DEBRIS = "click_debris";
    public static final String CLICK_SMS_SECONDBOOK = "click_sms_secondbook";
    public static final String CLICK_SMS_DEBRIS = "click_sms_debris";
    public static final String SMS_SECONDBOOK_SUCCESS = "sms_secondbook_success";
    public static final String SMS_DEBRIS_SUCCESS = "sms_debris_success";
    public static final String LOGIN_MODIFY = "login_modify";
    
    public static final String CLICK_SEARCH = "click_search";
    public static final String SB_SEARCH = "secondbook_search";
    public static final String DB_SEARCH = "debris_search";
    public static final String C_PUBLISH_SB = "click_publish_secondbook";
    public static final String C_PUBLISH_DB = "click_publish_debris";
    public static final String C_M_SB = "click_my_seconfbook";
    public static final String C_M_DB = "click_my_debris";
    public static final String C_M_SA = "click_my_schoolannounce";
    public static final String C_M_L = "click_my_library";
    public static final String C_M_C = "click_my_card";
    public static final String C_M_S = "click_my_scres";
    public static final String C_T = "click_shudong";
    public static final String D_SB = "delete_secondbook";


    public static final String D_DB = "delete_debris";
    public static final String R_SB = "refresh_secondbook";
    public static final String R_DB = "refresh_debris";
    public static final String C_SA_DET = "click_schoolannounce_detail";
    public static final String C_SA_DOWN = "click_schoolannounce_download";
    public static final String C_CZ = "card_chongzhi";
    public static final String C_LSCX = "click_liushuichaxcun";
    public static final String C_BZCX = "click_buzhuchaxcun";
    public static final String C_CP = "click_changepassword";
    public static final String C_GS = "click_guashi";
    public static final String CZ_F = "chongzhi_fail";
    public static final String CZ_S = "chongzhi_success";
    public static final String CP_F = "changepassword_fail";
    public static final String CP_S = "changepassword_success";
    public static final String GS_F = "guashii_fail";
    public static final String GS_S = "guashi_success";
    public static final String C_Q = "click_query";
    public static final String C_Q_T = "click_query_today";
    public static final String C_DWCY = "click_daiwochuanyue";
    public static final String S_F = "score_fail";
    public static final String S_S = "score_success";
    public static final String C_L_C_P = "change_library_pwd";
    public static final String E_L_E_A = "exit_library_account";
    public static void onResume(AppActivity appActivity) {
        MobclickAgent.onResume(appActivity);
    }

    public static void onPause(AppActivity appActivity) {
        MobclickAgent.onPause(appActivity);
    }

    public static void onPageStart(String simpleName) {
        MobclickAgent.onPageStart(simpleName);

    }

    public static void onPageEnd(String simpleName) {
        MobclickAgent.onPageEnd(simpleName);
    }

    public static void onEvent(Context context, String eventId) {
        MobclickAgent.onEvent(context, eventId);
    }

    public static void onEvent(Context context, String eventId, String key, String value) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(key, value);
        MobclickAgent.onEvent(context, eventId, map);
    }
}
