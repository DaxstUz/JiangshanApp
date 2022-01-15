package com.jiangshan.knowledge.http.entity;

import java.io.Serializable;

/**
 * auth s_yz  2022/1/15
 */
public class QuetionCount implements Serializable {

    public QuetionCount(int id, int count) {
        this.id = id;
        this.count = count;
    }

    private int id;

    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
