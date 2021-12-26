package com.jiangshan.knowledge.http.api;

import com.hjq.http.annotation.HttpRename;
import com.hjq.http.config.IRequestApi;

public final class QuestionFeedbackApi implements IRequestApi {

    String api = "/exam/questionFeedback/";

    @Override
    public String getApi() {
        return api + examCode+"?questionId="+questionId+"&feedbackType="+feedbackType+"&content="+content;
    }

    @HttpRename("examCode")
    private String examCode;

    public QuestionFeedbackApi setExamCode(String examCode) {
        this.examCode = examCode;
        return this;
    }

    @HttpRename("content")
    private String content;
    public QuestionFeedbackApi setContent(String content) {
        this.content = content;
        return this;
    }

    @HttpRename("feedbackType")
    private String feedbackType;
    public QuestionFeedbackApi setFeedbackType(String feedbackType) {
        this.feedbackType = feedbackType;
        return this;
    }

    @HttpRename("questionId")
    private int questionId;
    public QuestionFeedbackApi setQuestionId(int questionId) {
        this.questionId = questionId;
        return this;
    }

}