package com.jiangshan.knowledge.http.api;

import com.hjq.http.annotation.HttpRename;
import com.hjq.http.config.IRequestApi;

/**
 * auth s_yz  2021/10/16
 */
public class SmsCodeApi implements IRequestApi {

    private String path = "/passport/sendSmsCode/";

    @Override
    public String getApi() {
        return path + ticket;
    }

    private String ticket;

    public String getTicket() {
        return ticket;
    }

    public SmsCodeApi setTicket(String ticket) {
        this.ticket = ticket;
        return this;
    }

    @HttpRename("captchaCode")
    private String captchaCode;

    @HttpRename("openid")
    private String openid;

    @HttpRename("mobileNumber")
    private String mobileNumber;

    public SmsCodeApi setCaptchaCode(String captchaCode) {
        this.captchaCode = captchaCode;
        return this;
    }

    public SmsCodeApi setOpenid(String openid) {
        this.openid = openid;
        return this;
    }

    public SmsCodeApi setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
        return this;
    }
}
