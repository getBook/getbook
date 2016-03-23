package com.xfzj.getbook.net;

/**
 * Created by zj on 2016/1/28.
 */
public class BaseHttp {
    public static final String SignInAndGetUserPlus = "http://ucard.nuist.edu.cn:8070/Api/Account/SignInAndGetUserPlus";
    public static final String GetMyPhoto = "http://ucard.nuist.edu.cn:8070/Api/Card/GetMyPhoto ";
    public static final String GetBookInfo = "https://api.douban.com/v2/book/isbn/";
    public static final String QUERYSCORE = "http://ucard.nuist.edu.cn:8070/Api/Score/Query ";
    public static final String GETCARDINFO = "http://ucard.nuist.edu.cn:8070/Api/Card/GetCardInfo";
    public static final String GETNEWS="http://jwc.nuist.edu.cn/Show.aspx?CI=1";
    public static final String GETNEWSITEM="http://jwc.nuist.edu.cn/Show";
    public static final String DOWNLOADHOST="http://jwc.nuist.edu.cn";
    public static final String BANKTRANSFER=" http://ucard.nuist.edu.cn:8070/Api/Card/BankTransfer";
    public static final String CHANGEQUERYPWD = "http://ucard.nuist.edu.cn:8070/Api/Card/ChangeQueryPwd";
    public BaseHttp() {

    }

}
