package com.jiangshan.knowledge.http.entity;

import java.io.Serializable;
import java.util.List;

/**
 * auth s_yz  2021/10/20
 */
public class Chapter implements Serializable {

    private int id;
    private String chapterCode;
    private int rank;
    private String chapterName;
    private String intro;
    private int questionQty;
    private int answerQuestionQty;
    private int starFlag;
    private int memberType;

    private List<Chapter> children;

    private boolean Open;

    public boolean isOpen() {
        return Open;
    }

    public void setOpen(boolean open) {
        Open = open;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChapterCode() {
        return chapterCode;
    }

    public void setChapterCode(String chapterCode) {
        this.chapterCode = chapterCode;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public int getQuestionQty() {
        return questionQty;
    }

    public void setQuestionQty(int questionQty) {
        this.questionQty = questionQty;
    }

    public int getAnswerQuestionQty() {
        return answerQuestionQty;
    }

    public void setAnswerQuestionQty(int answerQuestionQty) {
        this.answerQuestionQty = answerQuestionQty;
    }

    public int getStarFlag() {
        return starFlag;
    }

    public void setStarFlag(int starFlag) {
        this.starFlag = starFlag;
    }

    public int getMemberType() {
        return memberType;
    }

    public void setMemberType(int memberType) {
        this.memberType = memberType;
    }

    public List<Chapter> getChildren() {
        return children;
    }

    public void setChildren(List<Chapter> children) {
        this.children = children;
    }
}
