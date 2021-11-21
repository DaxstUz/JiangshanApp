package com.jiangshan.knowledge.http.api;

import com.hjq.http.config.IRequestApi;

public final class GetMyRankApi implements IRequestApi {

    public static String rankTypeDay = "day";
    public static String rankTypeWeek = "week";
    public static String rankTypeYear = "year";

    private String api = "/rank/myRank/";

    @Override
    public String getApi() {
        return api;
    }

    public GetMyRankApi setSujectCode(String subjectCode) {
        api = api + subjectCode;
        return this;

    }

    public GetMyRankApi setRankType(String rankType) {
        api = api + "/" + rankType;
        return this;
    }

}