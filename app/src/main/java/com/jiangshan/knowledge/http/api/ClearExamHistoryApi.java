package com.jiangshan.knowledge.http.api;

import com.hjq.http.config.IRequestApi;

public final class ClearExamHistoryApi implements IRequestApi {

    String api = "/user/exam/clearExamHistory/";

    @Override
    public String getApi() {
        return api + subjectCode + "/" + courseCode;
    }

    private String subjectCode;
    private String courseCode;

    public ClearExamHistoryApi setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
        return this;
    }


    public ClearExamHistoryApi setCourseCode(String courseCode) {
        this.courseCode = courseCode;
        return this;
    }


}