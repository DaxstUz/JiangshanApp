package com.jiangshan.knowledge.http.api;

import com.hjq.http.annotation.HttpRename;
import com.hjq.http.config.IRequestApi;
import com.jiangshan.knowledge.http.entity.QuetionCount;

import java.util.List;

public final class ExamRandomStartApi implements IRequestApi {

    String api = "/user/exam/startRandExam";

    @Override
    public String getApi() {
        return api + "/" + subjectCode + "/" + courseCode;
    }

    private String subjectCode;
    private String courseCode;

    @HttpRename("questionTypeQtySet")
    private Object questionTypeQtySet;

    public ExamRandomStartApi setQuestionTypeQtySet(Object questionTypeQtySet) {
        this.questionTypeQtySet = questionTypeQtySet;
        return this;
    }

    public ExamRandomStartApi setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
        return this;
    }


    public ExamRandomStartApi setCourseCode(String courseCode) {
        this.courseCode = courseCode;
        return this;
    }

    @HttpRename("examType")
    private int examType;

    public ExamRandomStartApi setExamType(int examType) {
        this.examType = examType;
        return this;
    }

//    @HttpRename("examCode")
//    private String examCode;
//    public ExamRandomStartApi setExamCode(String examCode) {
//        this.examCode = examCode;
//        return this;
//    }

}