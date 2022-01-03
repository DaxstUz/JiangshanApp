package com.jiangshan.knowledge.http.api;

import com.hjq.http.annotation.HttpRename;
import com.hjq.http.config.IRequestApi;

/**
 * auth s_yz  2021/10/16
 */
public class LoginApi implements IRequestApi {

    private String path="/passport/login/";

    @Override
    public String getApi() {
        return path+ticket;
    }

    private String ticket;

    public LoginApi setTicket(String ticket) {
        this.ticket = ticket;
        return this;
    }

    @HttpRename("captchaCode")
    private String captchaCode;
    public LoginApi setCaptchaCode(String captchaCode) {
        this.captchaCode = captchaCode;
        return this;
    }

    @HttpRename("mobileNumber")
    private String mobileNumber;
    public LoginApi setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
        return this;
    }

    @HttpRename("userPassword")
    private String userPassword;
    public LoginApi setUserPassword(String userPassword) {
        this.userPassword = userPassword;
        return this;
    }

}
