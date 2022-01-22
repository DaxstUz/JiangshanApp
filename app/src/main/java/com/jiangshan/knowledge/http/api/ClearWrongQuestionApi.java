package com.jiangshan.knowledge.http.api;

import com.hjq.http.config.IRequestApi;

public final class ClearWrongQuestionApi implements IRequestApi {

    String api = "/exam/clearWrongQuestion/";

    @Override
    public String getApi() {
        return api + subjectCode + "/" + courseCode;
    }

    private String subjectCode;
    private String courseCode;

    public ClearWrongQuestionApi setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
        return this;
    }


    public ClearWrongQuestionApi setCourseCode(String courseCode) {
        this.courseCode = courseCode;
        return this;
    }


}