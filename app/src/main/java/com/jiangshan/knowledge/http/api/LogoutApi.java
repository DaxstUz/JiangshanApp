package com.jiangshan.knowledge.http.api;

import com.hjq.http.config.IRequestApi;

/**
 * auth s_yz  2021/10/16
 */
public class LogoutApi implements IRequestApi {

    private String path = "/user/logOut";

    @Override
    public String getApi() {
        return path;
    }

}
