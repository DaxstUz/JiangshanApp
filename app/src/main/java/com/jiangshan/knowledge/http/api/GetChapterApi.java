package com.jiangshan.knowledge.http.api;

import com.hjq.http.config.IRequestApi;

/**
 * auth s_yz  2021/10/20
 */
public class GetChapterApi implements IRequestApi {

    private String path = "/exam/chapterTree/";

    @Override
    public String getApi() {
        return path + "/" + subjectCode + "/" + courseCode;
    }

    private String subjectCode;
    private String courseCode;

    public GetChapterApi setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
        return this;
    }


    public GetChapterApi setCourseCode(String courseCode) {
        this.courseCode = courseCode;
        return this;
    }

}
