package com.jiangshan.knowledge.http.api;

import com.hjq.http.annotation.HttpRename;
import com.hjq.http.config.IRequestApi;

public final class GetExamFouseApi implements IRequestApi {

    String api="/article/examFocusList/";

    @Override
    public String getApi() {
        return api+subjectCode+"/"+courseCode;
    }

    private String subjectCode;
    private String courseCode;

    public GetExamFouseApi setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
        return this;
    }


    public GetExamFouseApi setCourseCode(String courseCode) {
        this.courseCode = courseCode;
        return this;
    }


    @HttpRename("pageNum")
    private int pageNum;
    public GetExamFouseApi setPageNum(int pageNum) {
        this.pageNum=pageNum;
        return this;
    }

    @HttpRename("pageSize")
    private int pageSize=10;
    public GetExamFouseApi setPageSize(int pageSize) {
        this.pageSize=pageSize;
        return this;
    }
}