package com.jiangshan.knowledge.http.entity;

import java.io.Serializable;

/**
 * auth s_yz  2021/10/17
 */
public class Rank implements Serializable {

    private int no;
    private int userId;
    private String nickname;
    private String figureUrl;
    private int rightQty;
    private int rise;
    private int rightRate;
    private int answerQty;

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getFigureUrl() {
        return figureUrl;
    }

    public void setFigureUrl(String figureUrl) {
        this.figureUrl = figureUrl;
    }

    public int getRightQty() {
        return rightQty;
    }

    public void setRightQty(int rightQty) {
        this.rightQty = rightQty;
    }

    public int getRise() {
        return rise;
    }

    public void setRise(int rise) {
        this.rise = rise;
    }

    public int getRightRate() {
        return rightRate;
    }

    public void setRightRate(int rightRate) {
        this.rightRate = rightRate;
    }

    public int getAnswerQty() {
        return answerQty;
    }

    public void setAnswerQty(int answerQty) {
        this.answerQty = answerQty;
    }

}
