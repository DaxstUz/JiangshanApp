package com.jiangshan.knowledge.http.api;

import com.hjq.http.annotation.HttpRename;
import com.hjq.http.config.IRequestApi;

public final class ExamAnswerApi implements IRequestApi {

    String api = "/user/exam/answer/";

    @Override
    public String getApi() {
        return api + billId;
    }

    private int billId;

    public ExamAnswerApi setBillId(int billId) {
        this.billId = billId;
        return this;
    }

    @HttpRename("lastQuestionIndex")
    private int lastQuestionIndex;
    public ExamAnswerApi setLastQuestionIndex(int lastQuestionIndex) {
        this.lastQuestionIndex = lastQuestionIndex;
        return this;
    }

    @HttpRename("questionId")
    private int questionId;
    public ExamAnswerApi setQuestionId(int questionId) {
        this.questionId = questionId;
        return this;
    }

    @HttpRename("memo")
    private String memo;
    public ExamAnswerApi setMemo(String memo) {
        this.memo = memo;
        return this;
    }

    @HttpRename("optionNo")
    private String optionNo;
    public ExamAnswerApi setOptionNo(String optionNo) {
        this.optionNo = optionNo;
        return this;
    }
}