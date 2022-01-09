package com.jiangshan.knowledge.http.api;

import com.hjq.http.annotation.HttpRename;
import com.hjq.http.config.IRequestApi;

/**
 * auth s_yz  2021/10/30
 */
public class QuestionMarkApi implements IRequestApi {

    String api = "/exam/questionMark/";

    @Override
    public String getApi() {
        return api + examCode+"?questionId="+questionId+"&markType="+markType ;
    }

    private int questionId;
    public QuestionMarkApi setQuestionId(int questionId) {
        this.questionId = questionId;
        return this;
    }

    private String markType;

    public QuestionMarkApi setMarkType(String markType) {
        this.markType = markType;
        return this;
    }

    private String examCode;
    public QuestionMarkApi setExamCode(String examCode) {
        this.examCode = examCode;
        return this;
    }

}