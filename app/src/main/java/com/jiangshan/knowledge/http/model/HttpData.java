package com.jiangshan.knowledge.http.model;

public class HttpData<T> {

    private int code;

    private boolean success;

    private String msg;

    private String ver;

    private boolean tokenFailure;

    /**
     * 数据
     */
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean getTokenFailure() {
        return tokenFailure;
    }

    public void setTokenFailure(boolean tokenFailure) {
        this.tokenFailure = tokenFailure;
    }
}