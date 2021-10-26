package com.jiangshan.knowledge.http.entity;

import java.io.Serializable;

/**
 * auth s_yz  2021/10/26
 */
public class HistoryStatistics implements Serializable {

    private int answerCount;
    private int sumTime;
    private int finishCount;

    public int getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(int answerCount) {
        this.answerCount = answerCount;
    }

    public int getSumTime() {
        return sumTime;
    }

    public void setSumTime(int sumTime) {
        this.sumTime = sumTime;
    }

    public int getFinishCount() {
        return finishCount;
    }

    public void setFinishCount(int finishCount) {
        this.finishCount = finishCount;
    }
}
