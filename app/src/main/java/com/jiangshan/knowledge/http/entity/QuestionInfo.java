package com.jiangshan.knowledge.http.entity;

import java.io.Serializable;

/**
 * auth s_yz  2021/10/21
 */
public class QuestionInfo implements Serializable {

    private int questionQty;
    private boolean isNew;
    private int lastQuestionIndex;
    private int billId;
    private boolean status;

    public int getQuestionQty() {
        return questionQty;
    }

    public void setQuestionQty(int questionQty) {
        this.questionQty = questionQty;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public int getLastQuestionIndex() {
        return lastQuestionIndex;
    }

    public void setLastQuestionIndex(int lastQuestionIndex) {
        this.lastQuestionIndex = lastQuestionIndex;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
