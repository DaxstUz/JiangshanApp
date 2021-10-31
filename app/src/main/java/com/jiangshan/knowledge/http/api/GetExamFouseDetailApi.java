package com.jiangshan.knowledge.http.api;

import com.hjq.http.config.IRequestApi;

public final class GetExamFouseDetailApi implements IRequestApi {

    String api = "/article/examFocusDetail/";

    @Override
    public String getApi() {
        return api + articleId;
    }

    private int articleId;

    public GetExamFouseDetailApi setArticleId(int articleId) {
        this.articleId = articleId;
        return this;
    }

}