package com.xfzj.getbook.utils;

import java.net.URLEncoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * Created by zj on 2016/1/28.
 */
public class Des {
    public static byte[] encrypt(String paramString1)
            throws Exception {
        paramString1 = URLEncoder.encode(paramString1, "utf-8");
        Cipher localCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        DESKeySpec localDESKeySpec = new DESKeySpec("synjones".getBytes("UTF-8"));
        localCipher.init(1, SecretKeyFactory.getInstance("DES").generateSecret(localDESKeySpec), new IvParameterSpec("synjones".getBytes("UTF-8")));
        return localCipher.doFinal(paramString1.getBytes("UTF-8"));
    }
}
