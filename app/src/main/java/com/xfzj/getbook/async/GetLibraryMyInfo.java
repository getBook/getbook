package com.xfzj.getbook.async;

import android.content.Context;
import android.text.TextUtils;

import com.xfzj.getbook.R;
import com.xfzj.getbook.common.LibraryInfo;
import com.xfzj.getbook.common.LibraryUserInfo;
import com.xfzj.getbook.utils.SharedPreferencesUtils;

/**
 * Created by zj on 2016/3/29.
 */
public class GetLibraryMyInfo extends BaseGetLibraryInfoAsyc<LibraryUserInfo> {

    public GetLibraryMyInfo(Context context) {
        super(context);
        setProgressDialog(null, context.getString(R.string.getting_library_info));
    }

    @Override
    protected LibraryUserInfo parse(String[] params, String result) {
        LibraryInfo libraryInfo = SharedPreferencesUtils.getLibraryUserInfo(context);
        if (!TextUtils.isEmpty(result) && result.contains(libraryInfo.getAccount())) {
            return LoginParse.parse(context, result, libraryInfo.getAccount(), libraryInfo.getPassword(), cookie);
        }
        
        return null;
    }
}
