package com.jiangshan.knowledge.http.entity;

import java.io.Serializable;

/**
 * auth s_yz  2021/10/26
 */
public class Answer implements Serializable {

    private int lastQuestionIndex;
    private String memo;
    private String optionNo;
    private int questionId;
    private int billId;

    public int getLastQuestionIndex() {
        return lastQuestionIndex;
    }

    public void setLastQuestionIndex(int lastQuestionIndex) {
        this.lastQuestionIndex = lastQuestionIndex;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getOptionNo() {
        return optionNo;
    }

    public void setOptionNo(String optionNo) {
        this.optionNo = optionNo;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }
}
