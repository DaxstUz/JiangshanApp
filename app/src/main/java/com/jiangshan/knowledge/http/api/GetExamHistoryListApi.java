package com.jiangshan.knowledge.http.api;

import com.hjq.http.annotation.HttpRename;
import com.hjq.http.config.IRequestApi;

public final class GetExamHistoryListApi implements IRequestApi {

    String api = "/user/exam/historyList";

    @Override
    public String getApi() {
        api=api + "/" + subjectCode + "/" + courseCode + "?pageNum=" + pageNum + "&pageSize=" + pageSize;
        if(examType>0){
            api=api+"&examType"+examType;
        }
        if(examCode!=null){
            api=api+"&examCode"+examCode;
        }
        return api;
    }

    private String subjectCode;
    private String courseCode;

    public GetExamHistoryListApi setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
        return this;
    }


    public GetExamHistoryListApi setCourseCode(String courseCode) {
        this.courseCode = courseCode;
        return this;
    }

    @HttpRename("examType")
    private int examType;


    @HttpRename("examCode")
    private String examCode;

    public GetExamHistoryListApi setExamCode(String examCode) {
        this.examCode = examCode;
        return this;
    }

    public GetExamHistoryListApi setExamType(int examType) {
        this.examType = examType;
        return this;
    }

    @HttpRename("pageNum")
    private int pageNum;

    public GetExamHistoryListApi setPageNum(int pageNum) {
        this.pageNum = pageNum;
        return this;
    }

    @HttpRename("pageSize")
    private int pageSize = 10;

    public GetExamHistoryListApi setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }
}