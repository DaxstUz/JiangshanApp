package com.jiangshan.knowledge.http.api;

import com.hjq.http.annotation.HttpRename;
import com.hjq.http.config.IRequestApi;

public final class GetExamCollectListApi implements IRequestApi {

    String api = "/user/exam/collectQuestionList/";

    @Override
    public String getApi() {
        return api + subjectCode + "/" + courseCode + "?pageNum=" + pageNum + "&pageSize=" + pageSize;
    }

    private String subjectCode;
    private String courseCode;

    public GetExamCollectListApi setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
        return this;
    }


    public GetExamCollectListApi setCourseCode(String courseCode) {
        this.courseCode = courseCode;
        return this;
    }


    @HttpRename("pageNum")
    private int pageNum;

    public GetExamCollectListApi setPageNum(int pageNum) {
        this.pageNum = pageNum;
        return this;
    }

    @HttpRename("pageSize")
    private int pageSize = 10;

    public GetExamCollectListApi setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

}