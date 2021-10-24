package com.jiangshan.knowledge.http.api;

import com.hjq.http.config.IRequestApi;

public final class SubjectCategoryApi implements IRequestApi {

    @Override
    public String getApi() {
        return "/subject/categoryTree";
    }

}