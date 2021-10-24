package com.jiangshan.knowledge.http.api;

import com.hjq.http.annotation.HttpRename;
import com.hjq.http.config.IRequestApi;

import java.util.List;

public final class NewsApi implements IRequestApi {

    @Override
    public String getApi() {
        return "/article/categoryTree";
    }

    /**
     * 搜索关键字
     */
    @HttpRename("k")
    private String keyword;

    public NewsApi setKeyword(String keyword) {
        this.keyword = keyword;
        return this;
    }

    public final static class Bean {
        private int id;
        private String title;
        private String content;

        public Bean(String title) {
            this.title = title;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}