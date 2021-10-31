package com.jiangshan.knowledge.http.entity;

import java.io.Serializable;

/**
 * auth s_yz  2021/10/21
 */
public class ExamHistory implements Serializable {

    private int id;
    private int questionQty;
    private int rightQty;
    private int answerQty;
    private String examCode;
    private String examName;
    private Long examTime;
    private int rightRate;
    private int examType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuestionQty() {
        return questionQty;
    }

    public void setQuestionQty(int questionQty) {
        this.questionQty = questionQty;
    }

    public int getRightQty() {
        return rightQty;
    }

    public void setRightQty(int rightQty) {
        this.rightQty = rightQty;
    }

    public int getAnswerQty() {
        return answerQty;
    }

    public void setAnswerQty(int answerQty) {
        this.answerQty = answerQty;
    }

    public String getExamCode() {
        return examCode;
    }

    public void setExamCode(String examCode) {
        this.examCode = examCode;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public Long getExamTime() {
        return examTime;
    }

    public void setExamTime(Long examTime) {
        this.examTime = examTime;
    }

    public int getRightRate() {
        return rightRate;
    }

    public void setRightRate(int rightRate) {
        this.rightRate = rightRate;
    }

    public int getExamType() {
        return examType;
    }

    public void setExamType(int examType) {
        this.examType = examType;
    }
}
