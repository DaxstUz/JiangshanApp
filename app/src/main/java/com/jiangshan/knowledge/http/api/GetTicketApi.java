package com.jiangshan.knowledge.http.api;

import com.hjq.http.annotation.HttpRename;
import com.hjq.http.config.IRequestApi;

/**
 * auth s_yz  2021/10/16
 */
public class GetTicketApi implements IRequestApi {

    private String path="/passport/ticket";

    @Override
    public String getApi() {
        return path;
    }

    @HttpRename("appId")
    private String appId="andriod_zhuren_app_1";
    public GetTicketApi setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    @HttpRename("appVersion")
    private String appVersion="1.0.0";
    public GetTicketApi setAppVersion(String appVersion) {
        this.appVersion = appVersion;
        return this;
    }

    @HttpRename("code")
    private String code="10";
    public GetTicketApi setCode(String code) {
        this.code = code;
        return this;
    }
}
