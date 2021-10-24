package com.jiangshan.knowledge.http.api;

import com.hjq.http.annotation.HttpRename;
import com.hjq.http.config.IRequestApi;

public final class GetExamApi implements IRequestApi {

    String api="/exam/list";

    @Override
    public String getApi() {
        return api + "/" + subjectCode + "/" + courseCode;
    }

    private String subjectCode;
    private String courseCode;

    public GetExamApi setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
        return this;
    }


    public GetExamApi setCourseCode(String courseCode) {
        this.courseCode = courseCode;
        return this;
    }

    @HttpRename("examType")
    private int examType;
    public GetExamApi setExamType(int examType) {
        this.examType=examType;
        return this;
    }
    @HttpRename("pageNum")
    private int pageNum;
    public GetExamApi setPageNum(int pageNum) {
        this.pageNum=pageNum;
        return this;
    }

    @HttpRename("pageSize")
    private int pageSize=10;
    public GetExamApi setPageSize(int pageSize) {
        this.pageSize=pageSize;
        return this;
    }
}