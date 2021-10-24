package com.jiangshan.knowledge.http.api;

import com.hjq.http.annotation.HttpRename;
import com.hjq.http.config.IRequestApi;

public final class GetRankApi implements IRequestApi {

    public static String rankTypeDay="day";
    public static String rankTypeWeek="week";
    public static String rankTypeYear="year";

    private String api="/rank/list/";

    @Override
    public String getApi() {
        return api;
    }

    public GetRankApi setSujectCode(String subjectCode) {
        api= api+ subjectCode;
        return this;

    } public GetRankApi setRankType(String rankType) {
        api= api+"/"+ rankType;
        return this;
    }

    @HttpRename("pageNum")
    private int pageNum;
    public GetRankApi setPageNum(int pageNum) {
        this.pageNum=pageNum;
        return this;
    }

    @HttpRename("pageSize")
    private int pageSize=10;
    public GetRankApi setPageSize(int pageSize) {
        this.pageSize=pageSize;
        return this;
    }
}