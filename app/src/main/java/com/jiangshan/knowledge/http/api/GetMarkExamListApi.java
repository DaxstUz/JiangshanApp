package com.jiangshan.knowledge.http.api;

import com.hjq.http.annotation.HttpRename;
import com.hjq.http.config.IRequestApi;

/**
 * 获取错题和收藏数量
 * auth s_yz  2021/10/26
 */
public class GetMarkExamListApi implements IRequestApi {

    String api = "/user/exam/markExamList/";

    @Override
    public String getApi() {
        return api + subjectCode + "/" + courseCode + "?examType=" + examType + "&markType=" + markType;
    }

    private String subjectCode;
    private String courseCode;

    public GetMarkExamListApi setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
        return this;
    }


    public GetMarkExamListApi setCourseCode(String courseCode) {
        this.courseCode = courseCode;
        return this;
    }

    @HttpRename("examType")
    private int examType;

    public GetMarkExamListApi setExamType(int examType) {
        this.examType = examType;
        return this;
    }

    @HttpRename("markType")
    private int markType;

    public GetMarkExamListApi setMarkType(int markType) {
        this.markType = markType;
        return this;
    }

}
