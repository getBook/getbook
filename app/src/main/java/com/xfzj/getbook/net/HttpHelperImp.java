package com.xfzj.getbook.net;

import com.xfzj.getbook.utils.MyLog;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.X509TrustManager;

/**
 * Created by zj on 2016/1/28.
 */
public class HttpHelperImp implements IHttpHelper {
    @Override
    public byte[] DoConnection(String url) throws Exception {
        return DoConnection(url, IHttpHelper.METHOD_GET, null);
    }

    @Override
    public byte[] DoConnection(String strUrl, int requestType, Map<String, String> params) throws Exception {
        HttpURLConnection urlConnection = getHttpURLConnection(new URL(strUrl));
        byte[] bytes = doConect(urlConnection, requestType, params);
        if (null == bytes) {
            MyLog.print("url", strUrl);
            throw new Exception(IHttpHelper.NET_ERROR.toString());
        } else {
            return bytes;
        }
    }

    private byte[] doConect(HttpURLConnection urlConnection, int requestType, Map<String, String> params) throws Exception {

        if (requestType == IHttpHelper.METHOD_POST) {
            if (null != params) {
                urlConnection.setDoOutput(true);
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
        MyLog.print(getClass().getName(), urlConnection.getResponseCode() + " ");
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

        urlConnection.setReadTimeout(30000);
        urlConnection.setConnectTimeout(300000);
        urlConnection.setUseCaches(false);
        urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//        urlConnection.setRequestProperty("Content-Type", "application/x-jpg");
        urlConnection.setRequestProperty("Accept", " text/html, application/xhtml+xml, image/jxr, */*");
        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586");
        return urlConnection;
    }

    private HttpsURLConnection getHttpsURLConnection(URL url) throws Exception {
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
        urlConnection.setReadTimeout(30000);
        urlConnection.setConnectTimeout(300000);
        urlConnection.setUseCaches(false);
        urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;");
        return urlConnection;
    }

    private class TrustAllManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
            // TODO Auto-generated method stub  

        }

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
            // TODO Auto-generated method stub  

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            // TODO Auto-generated method stub  
            return null;
        }
    }
}
