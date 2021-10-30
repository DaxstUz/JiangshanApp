package com.jiangshan.knowledge.http.api;

import com.hjq.http.config.IRequestApi;

public final class GetPassportApi implements IRequestApi {

    String api = "/passport/init/config";

    @Override
    public String getApi() {
        return api;
    }

}