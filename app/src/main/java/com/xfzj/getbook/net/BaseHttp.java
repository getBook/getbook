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
    public static final String GETNEWS = "http://jwc.nuist.edu.cn/Show.aspx?CI=1";
    public static final String GETNEWSITEM = "http://jwc.nuist.edu.cn/Show";
    public static final String DOWNLOADHOST = "http://jwc.nuist.edu.cn";
    public static final String BANKTRANSFER = " http://ucard.nuist.edu.cn:8070/Api/Card/BankTransfer";
    public static final String CHANGEQUERYPWD = "http://ucard.nuist.edu.cn:8070/Api/Card/ChangeQueryPwd";
    public static final String GetHistoryTrjn = "http://ucard.nuist.edu.cn:8070/Api/Card/GetHistoryTrjn";
    public static final String GetCurrentTrjn = "http://ucard.nuist.edu.cn:8070/Api/Card/GetCurrentTrjn";
    public static final String GetTrjnCount = "http://ucard.nuist.edu.cn:8070/Api/Card/GetTrjnCount";
    public static final String GetMyBill = "http://ucard.nuist.edu.cn:8070/Api/Card/GetMyBill";
    public static final String GetAllSubsidyTrjn = "http://ucard.nuist.edu.cn:8070/Api/Card/GetAllSubsidyTrjn";
    public static final String SetCardLost = "http://ucard.nuist.edu.cn:8070/Api/Card/SetCardLost";
    public static final String LOGINLIBRARY = "http://lib2.nuist.edu.cn/reader/login.php";
    public static final String GETLIBRARY = "http://lib2.nuist.edu.cn/reader/hwthau.php";
    public static final String REDEINFO = "http://lib2.nuist.edu.cn/reader/redr_info.php";
    public static final String CAPTCHA = "http://lib2.nuist.edu.cn/reader/captcha.php";
    public static final String LIBRARYVERFY = "http://lib2.nuist.edu.cn/reader/redr_verify.php";
    public static final String GETLIBRARYSEARCH = "http://lib2.nuist.edu.cn/opac/openlink.php?dept=ALL&doctype=ALL&lang_code=ALL&match_flag=forward&displaypg=20&showmode=list&orderby=DESC&sort=CATA_DATE&onlylendable=no&with_ebook=&page=2&title=android";

    public static final String VERIFYLIBRARYNAME = "http://lib2.nuist.edu.cn/reader/redr_con_result.php";
    public static final String CHANGELIBRARYPWD = "http://lib2.nuist.edu.cn/reader/change_passwd_result.php";
    public static final String GETLIBRARYBOOKGUANCANGINFO = "http://lib2.nuist.edu.cn/opac/ajax_";
    public static final String GETBOOKLIST = "http://lib2.nuist.edu.cn/reader/book_lst.php";
    public static final String RENEWBOOK = "http://lib2.nuist.edu.cn/reader/ajax_renew.php?";
    public static final String GETASORDLIST = "http://lib2.nuist.edu.cn/reader/asord_lst.php";
    public static final String RECOMMENDBOOK = "http://lib2.nuist.edu.cn/asord/asord_redr.php?click_type=commit";
    public static final String SHAREBILL = "http://139.129.7.80/sharebill?";
    public static final String PAPERSEARCH = "http://idoc.duohuo.org/api/v1/search/docs?q=";


    public BaseHttp() {

    }

}
