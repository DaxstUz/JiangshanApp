package com.jiangshan.knowledge.http.api;

import com.hjq.http.annotation.HttpRename;
import com.hjq.http.config.IRequestApi;
import com.jiangshan.knowledge.http.entity.UserWeixin;

/**
 * auth s_yz  2021/10/16
 */
public class LoginWeixinApi implements IRequestApi {

    private String path = "/passport/login/wxAuth";

    @Override
    public String getApi() {
        return path;
    }

//
//    @HttpRename("mobileNumber")
//    private String mobileNumber;
//    public LoginWeixinApi setMobileNumber(String mobileNumber) {
//        this.mobileNumber = mobileNumber;
//        return this;
//    }
//
//    @HttpRename("userPassword")
//    private String userPassword;
//    public LoginWeixinApi setUserPassword(String userPassword) {
//        this.userPassword = userPassword;
//        return this;
//    }

//    @HttpRename("")
//    private UserWeixin user;
//
//    public UserWeixin getUser() {
//        return user;
//    }
//
//    public LoginWeixinApi setUser(UserWeixin user) {
//        this.user = user;
//        return this;
//    }
}
