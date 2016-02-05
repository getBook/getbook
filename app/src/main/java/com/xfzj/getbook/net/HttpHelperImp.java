package com.xfzj.getbook.net;

import com.xfzj.getbook.utils.MyLog;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by zj on 2016/1/28.
 */
public class HttpHelperImp implements IHttpHelper {
    @Override
    public byte[] DoConnection(String url) {
        return DoConnection(url, IHttpHelper.METHOD_GET, null);
    }

    @Override
    public byte[] DoConnection(String url, int requestType, Map<String, String> params) {
        try {
            HttpURLConnection urlConnection = getHttpURLConnection(new URL(url));

            if (requestType == IHttpHelper.METHOD_POST) {
                if (null != params) {
                    byte[] bytes = getParams(params);
                    urlConnection.setRequestMethod("POST");
                    OutputStream ops = urlConnection.getOutputStream();
                    ops.write(bytes);
                    ops.flush();
                    ops.close();
                }
            } else {
                urlConnection.setRequestMethod("GET");
            }
            MyLog.print(getClass().getName(), urlConnection.getResponseCode() + "");
            if (urlConnection.getResponseCode() == 200) {
                InputStream ips = urlConnection.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = ips.read(bytes)) != -1) {
                    bos.write(bytes, 0, len);
                }
                bos.flush();
                bos.close();
                return bos.toByteArray();
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解析post请求需要的参数
     *
     * @param params
     * @return
     */
    private byte[] getParams(Map<String, String> params) {
        if (null == params || params.size() == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue();
            sb.append("&" + k + "=" + v);
        }
        MyLog.print(getClass().getName(), sb.toString());
        return sb.toString().getBytes();
    }

    /**
     * 返回HttpURLConnection对象
     *
     * @param url
     * @return
     */
    private HttpURLConnection getHttpURLConnection(URL url) throws Exception {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
        urlConnection.setReadTimeout(30000);
        urlConnection.setConnectTimeout(300000);
        urlConnection.setUseCaches(false);
        urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;");
        return urlConnection;
    }
}
