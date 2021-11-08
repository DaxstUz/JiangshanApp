package com.jiangshan.knowledge.http.api;

import com.hjq.http.config.IRequestApi;

public final class MemberBuyApi implements IRequestApi {

    String api = "/member/buy/";

    @Override
    public String getApi() {
        return api + subjectCode + "?policyId=" + policyId;
    }

    private String subjectCode;
    private int policyId;

    public MemberBuyApi setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
        return this;
    }

    public MemberBuyApi setPolicyId(int policyId) {
        this.policyId = policyId;
        return this;
    }

}