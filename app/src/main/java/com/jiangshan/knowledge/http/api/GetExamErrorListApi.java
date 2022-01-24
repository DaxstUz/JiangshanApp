package com.jiangshan.knowledge.http.api;

import com.hjq.http.annotation.HttpRename;
import com.hjq.http.config.IRequestApi;

public final class GetExamErrorListApi implements IRequestApi {

    String api="/user/exam/wrongQuestionList/";

    @Override
    public String getApi() {
        return api+subjectCode+"/"+courseCode+"?examCode="+examCode+"&pageNum="+pageNum+"&pageSize="+pageSize;
    }

    private String subjectCode;
    private String courseCode;

    private String examCode;

    public GetExamErrorListApi setExamCode(String examCode) {
        this.examCode = examCode;
        return this;
    }

    public GetExamErrorListApi setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
        return this;
    }


    public GetExamErrorListApi setCourseCode(String courseCode) {
        this.courseCode = courseCode;
        return this;
    }


    @HttpRename("pageNum")
    private int pageNum;
    public GetExamErrorListApi setPageNum(int pageNum) {
        this.pageNum=pageNum;
        return this;
    }

    @HttpRename("pageSize")
    private int pageSize=10;
    public GetExamErrorListApi setPageSize(int pageSize) {
        this.pageSize=pageSize;
        return this;
    }
}