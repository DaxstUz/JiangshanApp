package com.jiangshan.knowledge.http.api;

import com.hjq.http.annotation.HttpRename;
import com.hjq.http.config.IRequestApi;

/**
 * auth s_yz  2021/10/16
 */
public class ChangePsdApi implements IRequestApi {

    private String path = "/user/changePassword";

    @Override
    public String getApi() {
        return path;
    }

    @HttpRename("oldPassword")
    private String oldPassword;

    @HttpRename("firstNewPassword")
    private String firstNewPassword;

    @HttpRename("secondNewPassword")
    private String secondNewPassword;


    public ChangePsdApi setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
        return this;
    }

    public ChangePsdApi setFirstNewPassword(String firstNewPassword) {
        this.firstNewPassword = firstNewPassword;
        return this;
    }

    public ChangePsdApi setSecondNewPassword(String secondNewPassword) {
        this.secondNewPassword = secondNewPassword;
        return this;
    }
}
