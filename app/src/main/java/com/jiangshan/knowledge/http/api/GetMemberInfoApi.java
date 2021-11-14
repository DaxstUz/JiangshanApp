package com.jiangshan.knowledge.http.api;

import com.hjq.http.config.IRequestApi;

public final class GetMemberInfoApi implements IRequestApi {

    String api = "/member/memberInfo/";

    @Override
    public String getApi() {
        return api+subjectCode;
    }

    private String subjectCode;

    public GetMemberInfoApi setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
        return this;
    }
}