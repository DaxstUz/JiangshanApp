package com.jiangshan.knowledge.http.api;

import com.hjq.http.annotation.HttpRename;
import com.hjq.http.config.IRequestApi;

public final class ExamStartApi implements IRequestApi {

    String api = "/user/exam/start";

    @Override
    public String getApi() {
        return api + "/" + subjectCode + "/" + courseCode+"?examCode="+examCode+"&examType="+examType;
    }

    private String subjectCode;
    private String courseCode;

    public ExamStartApi setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
        return this;
    }


    public ExamStartApi setCourseCode(String courseCode) {
        this.courseCode = courseCode;
        return this;
    }

    @HttpRename("examType")
    private int examType;

    public ExamStartApi setExamType(int examType) {
        this.examType = examType;
        return this;
    }

    @HttpRename("examCode")
    private String examCode;
    public ExamStartApi setExamCode(String examCode) {
        this.examCode = examCode;
        return this;
    }

}