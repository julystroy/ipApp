package com.cartoon.volley;

import com.android.volley.VolleyError;

/**
 * 网络200时的业务逻辑错误
 * <p>
 * Created by shidu on 2017/5/4.
 */
public class BusinessError extends VolleyError {
    private long errorCode;
    private String errorMsg;

    public BusinessError(long errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
    }

    public long getCode() {
        return errorCode;
    }
}
