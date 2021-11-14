package com.jiangshan.knowledge.http.api;

import com.hjq.http.config.IRequestApi;

public final class GetPayHistoryApi implements IRequestApi {

    String api = "/member/orderList";

    @Override
    public String getApi() {
        return api;
    }


}