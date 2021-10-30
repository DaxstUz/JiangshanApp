package com.jiangshan.knowledge.http.api;

import com.hjq.http.config.IRequestApi;

/**
 * auth s_yz  2021/10/30
 */
public class UnCollectApi implements IRequestApi {

    String api = "/exam/unCollect/";

    @Override
    public String getApi() {
        return api + questionId;
    }

    private int questionId;

    public UnCollectApi setQuestionId(int questionId) {
        this.questionId = questionId;
        return this;
    }

}