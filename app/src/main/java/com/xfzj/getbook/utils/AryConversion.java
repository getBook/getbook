package com.xfzj.getbook.utils;

/**
 * Created by zj on 2016/1/28.
 *  AryConversion.binary2Hex(Des.encrypt(URLEncoder.encode(this.str_pwd, "utf-8"), "synjones")).toUpperCase();
 */
public class AryConversion {
    public static String binary2Hex(String str) throws Exception {
        byte[] paramArrayOfByte = Des.encrypt(str);
        StringBuilder localStringBuilder = new StringBuilder();
        int i = 0;
        while (true) {
            if (i >= paramArrayOfByte.length)
                return localStringBuilder.toString().toUpperCase();
            localStringBuilder.append(String.valueOf("0123456789ABCDEF".charAt((paramArrayOfByte[i] & 0xF0) >> 4)));
            localStringBuilder.append(String.valueOf("0123456789ABCDEF".charAt(paramArrayOfByte[i] & 0xF)));
            i += 1;
        }
    }
}
