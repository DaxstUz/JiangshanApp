package com.jiangshan.knowledge.http.api;

import com.hjq.http.annotation.HttpRename;
import com.hjq.http.config.IRequestApi;

/**
 * 获取错题和收藏数量
 * auth s_yz  2021/10/26
 */
public class GetMarkCountApi implements IRequestApi {

    String api = "/user/exam/markCount/";

    @Override
    public String getApi() {
        return api + subjectCode + "/" + courseCode;
    }

    private String subjectCode;
    private String courseCode;

    public GetMarkCountApi setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
        return this;
    }


    public GetMarkCountApi setCourseCode(String courseCode) {
        this.courseCode = courseCode;
        return this;
    }

    @HttpRename("examType")
    private int examType;

    public GetMarkCountApi setExamType(int examType) {
        this.examType = examType;
        return this;
    }

}
