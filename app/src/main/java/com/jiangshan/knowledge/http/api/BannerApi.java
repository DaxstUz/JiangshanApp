package com.jiangshan.knowledge.http.api;

import com.hjq.http.annotation.HttpRename;
import com.hjq.http.config.IRequestApi;

/**
 * auth s_yz  2021/10/13
 */
public class BannerApi implements IRequestApi {
    @Override
    public String getApi() {
        return "/article/queryAticleListForBanner";
    }

    @HttpRename("pageNum")
    private int pageNum;
    public BannerApi setPageNum(int pageNum) {
        this.pageNum=pageNum;
        return this;
    }

    @HttpRename("pageSize")
    private int pageSize=10;
    public BannerApi setPageSize(int pageSize) {
        this.pageSize=pageSize;
        return this;
    }
}
