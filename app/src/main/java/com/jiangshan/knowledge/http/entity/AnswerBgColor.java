package com.jiangshan.knowledge.http.entity;

import java.io.Serializable;

/**
 * auth s_yz  2021/12/26
 */
public class AnswerBgColor implements Serializable {

    private String corlorStr;

    public AnswerBgColor(String corlorStr) {
        this.corlorStr = corlorStr;
    }

    public String getCorlorStr() {
        return corlorStr;
    }

    public void setCorlorStr(String corlorStr) {
        this.corlorStr = corlorStr;
    }
}
