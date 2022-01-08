package com.jiangshan.knowledge.http.api;

import com.hjq.http.annotation.HttpRename;
import com.hjq.http.config.IRequestApi;

/**
 * auth s_yz  2021/10/16
 */
public class ChangePhoneApi implements IRequestApi {

    private String path = "/passport/login/wxAuthPhone";

    @Override
    public String getApi() {
        return path;
    }

    @HttpRename("mobileNumber")
    private String mobileNumber;

    @HttpRename("openid")
    private String openid;

    @HttpRename("smsCode")
    private String smsCode;

    public ChangePhoneApi setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
        return this;
    }

    public ChangePhoneApi setOpenid(String openid) {
        this.openid = openid;
        return this;
    }

    public ChangePhoneApi setSmsCode(String smsCode) {
        this.smsCode = smsCode;
        return this;
    }
}
