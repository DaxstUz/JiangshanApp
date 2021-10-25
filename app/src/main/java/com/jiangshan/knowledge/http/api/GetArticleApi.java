package com.jiangshan.knowledge.http.api;

import com.hjq.http.config.IRequestApi;

public final class GetArticleApi implements IRequestApi {

    String api = "/article/detail/";

    @Override
    public String getApi() {
        return api + articleId;
    }

    private int articleId;

    public GetArticleApi setArticleId(int articleId) {
        this.articleId = articleId;
        return this;
    }

}