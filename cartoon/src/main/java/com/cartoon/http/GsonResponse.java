package com.cartoon.http;

public class GsonResponse<T> {
    public String apiVersion;
    public String error;
    public int errorCode;

    public T data;
}

