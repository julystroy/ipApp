package com.cartoon.volley;

import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.cartoon.account.AccountHelper;
import com.cartoon.http.GsonResponse;
import com.cartton.library.utils.DebugLog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GsonRequest<T> extends JsonRequest<T> {
    private static final String SET_COOKIE_KEY = "Set-Cookie";
    private static final String COOKIE_KEY = "Cookie";

    private MultipartEntity entity = new MultipartEntity();
    private List<File> mFileParts;
    private List<String> mFilePartNames;
    private Map<String, String> mParams;

    Gson gson = new GsonBuilder()
            .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
            .create();

    private Class<T> mClass;

    public GsonRequest(String url, Class<T> cls, Listener<T> listener, ErrorListener errorListener) {
        this(Method.GET, url, null, cls, listener, errorListener);
    }

    public GsonRequest(int method, String url, Class<T> cls, Listener<T> listener, ErrorListener errorListener) {
        this(method, url, null, cls, listener, errorListener);
    }

    public GsonRequest(String url, String requestBody, Class<T> cls, Listener<T> listener, ErrorListener errorListener) {
        this(Method.GET, url, requestBody, cls, listener, errorListener);
    }

    public GsonRequest(int method, String url, String requestBody, Class<T> cls, Listener<T> listener, ErrorListener errorListener) {
        super(method, url, requestBody, listener, errorListener);

        mClass = cls;
    }

    //单个文件
    public GsonRequest(String url, String paramName, File file, Map<String, String> params, Class<T> cls, Listener<T> listener, ErrorListener errorListener) {
        super(Method.POST, url, null, listener, errorListener);

        mFileParts = new ArrayList<File>();
        mFilePartNames = new ArrayList<String>();
        if (file != null) {
            mFileParts.add(file);
        }
        mClass = cls;
        if (paramName != null) {
            mFilePartNames.add(paramName);
        }
        mParams = params;
        buildMultipartEntity();
    }

    //多个文件
    public GsonRequest(String url, ArrayList<String> paramNames, ArrayList<File> files, Map<String, String> params, Class<T> cls, Listener<T> listener, ErrorListener errorListener) {
        super(Method.POST, url, null, listener, errorListener);

        mFileParts = files;
        mFilePartNames = paramNames;
        mClass = cls;
        mParams = params;
        buildMultipartEntity();
    }

    private int min(int a, int b) {
        return a > b ? b : a;
    }

    private void buildMultipartEntity() {
        if (mFileParts != null && mFileParts.size() > 0) {
            int size = min(mFilePartNames.size(), mFileParts.size());
            for (int i = 0; i < size; i++) {
                entity.addPart(mFilePartNames.get(i), new FileBody(mFileParts.get(i)));
            }
            long l = entity.getContentLength();
            DebugLog.d(mFileParts.size() + "个，长度：" + l);
        }

        try {
            if (mParams != null && mParams.size() > 0) {
                for (Map.Entry<String, String> entry : mParams.entrySet()) {
                    entity.addPart(
                            entry.getKey(),
                            new StringBody(entry.getValue(), Charset
                                    .forName("UTF-8")));
                }
            }
        } catch (UnsupportedEncodingException e) {
            VolleyLog.e("UnsupportedEncodingException");
        }
    }

    @Override
    protected void deliverResponse(T response) {
        if (response instanceof GsonResponse) {
            GsonResponse gsonResponse = GsonResponse.class.cast(response);
            if (gsonResponse != null && gsonResponse.errorCode == 0) {
                super.deliverResponse(response);
            } else if (gsonResponse != null) {
                //onResponseError;
                String errorMsg = "server business error";
                if (!TextUtils.isEmpty(gsonResponse.error)) {
                    errorMsg = gsonResponse.error;
                }
                super.deliverError(new BusinessError(gsonResponse.errorCode, errorMsg));
            } else {
                // 老接口
                super.deliverResponse(response);
            }
        } else {
            // UpdateInfo // 老接口
            super.deliverResponse(response);
        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();

        if (headers == null
                || headers.equals(Collections.emptyMap())) {
            headers = new HashMap<String, String>();
        }
        //从Sharepreference中获取到cookie，并把cookie添加到header中
        String cookie = AccountHelper.getCookie();
        if (!TextUtils.isEmpty(cookie)) {
            headers.put(COOKIE_KEY, cookie);
        }

        return headers;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String url = getOriginUrl();
            DebugLog.d("parseNetworkResponse. url = " + url);

            if ((url.contains("/api/users/register") || url.contains("/api/users/login") || url.contains("api/users/logout"))
                    && response.headers.containsKey(SET_COOKIE_KEY)) {
                String cookieFromResponse;
                String headers = response.headers.toString();
                DebugLog.d("get headers in parseNetworkResponse = " + response.headers.toString());
                //使用正则表达式从reponse的头中提取cookie内容的子串
                Pattern pattern = Pattern.compile("Set-Cookie.*?;");
                Matcher m = pattern.matcher(headers);
                if (m.find()) {
                    cookieFromResponse = m.group();
                    DebugLog.d("cookie from server = " + cookieFromResponse);
                    //去掉cookie末尾的分号
                    cookieFromResponse = cookieFromResponse.substring(11, cookieFromResponse.length() - 1);
                    DebugLog.d("cookie substring = " + cookieFromResponse);

                    //save register cookie
                    AccountHelper.instance().saveCookie(cookieFromResponse);
                }
            }
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
            return Response.success(gson.fromJson(json, mClass), HttpHeaderParser.parseCacheHeaders(response));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    public byte[] getBody() {
        if (mFileParts != null) {
            DebugLog.d("getBody() rewrite");
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                entity.writeTo(bos);
            } catch (IOException e) {
                VolleyLog.e("IOException writing to ByteArrayOutputStream");
            }
            return bos.toByteArray();
        }
        return super.getBody();
    }

    @Override
    public String getBodyContentType() {
        if (mFileParts != null) {
            return entity.getContentType().getValue();
        }
        return super.getBodyContentType();
    }
}
