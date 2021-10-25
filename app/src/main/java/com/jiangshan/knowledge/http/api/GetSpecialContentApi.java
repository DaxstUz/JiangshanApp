package com.jiangshan.knowledge.http.api;

import com.hjq.http.config.IRequestApi;

public final class GetSpecialContentApi implements IRequestApi {

    String api = "/article/specialContent/";

    @Override
    public String getApi() {
        return api + specialTypeId;
    }

    private int specialTypeId;

    public GetSpecialContentApi setSpecialTypeId(int specialTypeId) {
        this.specialTypeId = specialTypeId;
        return this;
    }

}