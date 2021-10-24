package com.jiangshan.knowledge.http.api;

import com.hjq.http.annotation.HttpRename;
import com.hjq.http.config.IRequestApi;

import java.io.Serializable;
import java.util.Date;

public final class SearchArticleApi implements IRequestApi {

    String api="/article/list/";

    @Override
    public String getApi() {
        return api;
    }

    public SearchArticleApi setCategoryId(String categoryId) {
        api= api+ categoryId;
        return this;
    }

    @HttpRename("pageNum")
    private int pageNum;
    public SearchArticleApi setPageNum(int pageNum) {
        this.pageNum=pageNum;
        return this;
    }

    @HttpRename("pageSize")
    private int pageSize=10;
    public SearchArticleApi setPageSize(int pageSize) {
        this.pageSize=pageSize;
        return this;
    }
}