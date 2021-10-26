package com.jiangshan.knowledge.http.entity;

import java.io.Serializable;

/**
 * auth s_yz  2021/10/26
 */
public class MarkCount implements Serializable {

    private int wrongTotal;
    private int colletTotal;

    public int getWrongTotal() {
        return wrongTotal;
    }

    public void setWrongTotal(int wrongTotal) {
        this.wrongTotal = wrongTotal;
    }

    public int getColletTotal() {
        return colletTotal;
    }

    public void setColletTotal(int colletTotal) {
        this.colletTotal = colletTotal;
    }
}
