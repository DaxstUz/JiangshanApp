package com.jiangshan.knowledge.http.api;

import com.hjq.http.annotation.HttpRename;
import com.hjq.http.config.IRequestApi;

import java.util.List;

public final class SearchArticleCatetoryApi implements IRequestApi {

    @Override
    public String getApi() {
        return "/article/categoryTree";
    }

    /**
     * 搜索关键字
     */
    @HttpRename("k")
    private String keyword;

    public SearchArticleCatetoryApi setKeyword(String keyword) {
        this.keyword = keyword;
        return this;
    }

    public final static class Bean {
        private int id;
        private String categoryName;
        private String categoryDesc;
        private List<Bean> children;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getCategoryDesc() {
            return categoryDesc;
        }

        public void setCategoryDesc(String categoryDesc) {
            this.categoryDesc = categoryDesc;
        }

        public List<Bean> getChildren() {
            return children;
        }

        public void setChildren(List<Bean> children) {
            this.children = children;
        }
    }
}