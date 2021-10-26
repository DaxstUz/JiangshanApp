package com.jiangshan.knowledge.http.api;

import com.hjq.http.config.IRequestApi;

public final class ExamEndApi implements IRequestApi {

    String api = "/user/exam/finish/";

    @Override
    public String getApi() {
        return api + billId;
    }

    private int billId;

    public ExamEndApi setBillId(int billId) {
        this.billId = billId;
        return this;
    }


}