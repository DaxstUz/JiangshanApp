package com.jiangshan.knowledge.http.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * auth s_yz  2021/10/21
 */
public class Question implements Serializable {

    private int id;
    private int billId;
    private String examCode;
    private String questionNo;
    private String content;
    private String contextTitle;
    private String choiceAnswer;
    private Set<String> choiceAnswerList;
    private int rank;
    private Integer wrongFlag;
    private Integer collectFlag;
    private String userAnswer;
    private Set<String> userAnswerList;
    private String answerAnalysis;
    private String comment;
    private int questionType;
    private String questionTypeDesc;
    private int score;

    private String yearMonth;
    private Date examTime;
    private int focusFlag;
    private int easyFlag;
    private int starFlag;
    private String mark;
    private int questionQty;
    private int answerQty;
    private String chapterName;
    private int answerCount;
    private int rightRate;
    private String easyWrongChoice;

    private List<QuestionOption> questionOptionList;

    private boolean showAnswer;

//    private boolean hasAnswer;

//    public boolean isHasAnswer() {
//        return hasAnswer;
//    }
//
//    public void setHasAnswer(boolean hasAnswer) {
//        this.hasAnswer = hasAnswer;
//    }

    public boolean isShowAnswer() {
        return showAnswer;
    }

    public void setShowAnswer(boolean showAnswer) {
        this.showAnswer = showAnswer;
    }

    private int total;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public String getExamCode() {
        return examCode;
    }

    public void setExamCode(String examCode) {
        this.examCode = examCode;
    }

    public String getQuestionNo() {
        return questionNo;
    }

    public void setQuestionNo(String questionNo) {
        this.questionNo = questionNo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContextTitle() {
        return contextTitle;
    }

    public void setContextTitle(String contextTitle) {
        this.contextTitle = contextTitle;
    }

    public String getChoiceAnswer() {
        return choiceAnswer;
    }

    public void setChoiceAnswer(String choiceAnswer) {
        this.choiceAnswer = choiceAnswer;
    }

    public Set<String> getChoiceAnswerList() {
        return choiceAnswerList;
    }

    public void setChoiceAnswerList(Set<String> choiceAnswerList) {
        this.choiceAnswerList = choiceAnswerList;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public Integer getWrongFlag() {
        return wrongFlag;
    }

    public void setWrongFlag(Integer wrongFlag) {
        this.wrongFlag = wrongFlag;
    }

    public Integer getCollectFlag() {
        return collectFlag;
    }

    public void setCollectFlag(Integer collectFlag) {
        this.collectFlag = collectFlag;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public Set<String> getUserAnswerList() {
        return userAnswerList;
    }

    public void setUserAnswerList(Set<String> userAnswerList) {
        if(null==userAnswerList){
            this.userAnswerList=new HashSet<>();
        }else{
            this.userAnswerList = userAnswerList;
        }

    }

    public String getAnswerAnalysis() {
        return answerAnalysis;
    }

    public void setAnswerAnalysis(String answerAnalysis) {
        this.answerAnalysis = answerAnalysis;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getQuestionType() {
        return questionType;
    }

    public void setQuestionType(int questionType) {
        this.questionType = questionType;
    }

    public String getQuestionTypeDesc() {
        return questionTypeDesc;
    }

    public void setQuestionTypeDesc(String questionTypeDesc) {
        this.questionTypeDesc = questionTypeDesc;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }

    public Date getExamTime() {
        return examTime;
    }

    public void setExamTime(Date examTime) {
        this.examTime = examTime;
    }

    public int getFocusFlag() {
        return focusFlag;
    }

    public void setFocusFlag(int focusFlag) {
        this.focusFlag = focusFlag;
    }

    public int getEasyFlag() {
        return easyFlag;
    }

    public void setEasyFlag(int easyFlag) {
        this.easyFlag = easyFlag;
    }

    public int getStarFlag() {
        return starFlag;
    }

    public void setStarFlag(int starFlag) {
        this.starFlag = starFlag;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public int getQuestionQty() {
        return questionQty;
    }

    public void setQuestionQty(int questionQty) {
        this.questionQty = questionQty;
    }

    public int getAnswerQty() {
        return answerQty;
    }

    public void setAnswerQty(int answerQty) {
        this.answerQty = answerQty;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public int getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(int answerCount) {
        this.answerCount = answerCount;
    }

    public int getRightRate() {
        return rightRate;
    }

    public void setRightRate(int rightRate) {
        this.rightRate = rightRate;
    }

    public String getEasyWrongChoice() {
        return easyWrongChoice;
    }

    public void setEasyWrongChoice(String easyWrongChoice) {
        this.easyWrongChoice = easyWrongChoice;
    }

    public List<QuestionOption> getQuestionOptionList() {
        return questionOptionList;
    }

    public void setQuestionOptionList(List<QuestionOption> questionOptionList) {
        this.questionOptionList = questionOptionList;
    }


    //    {
//        "id" : 76,
//            "billId" : null,
//            "examCode" : "XG201811",
//            "questionNo" : 20181101,
//            "content" : "<p>????????????<strong><span style=\"color: rgb(255,
//        0,
//                0);\">?????????</span></strong>?????????????????????????????????????????????????????????????????????</p><p><br/></p><p><br/></p><p>?????????????????????????????????????????????????????????????????????????????????????????????????????????????????? <strong class=\"ask\">(<strong class=\"ask\">?</strong>)</strong>???</p>",
//            "contextTitle" : null,
//            "choiceAnswer" : "C",
//            "choiceAnswerList" : [
//        "C"
//    				],
//        "rank" : 1,
//            "wrongFlag" : 0,
//            "collectFlag" : 0,
//            "userAnswer" : null,
//            "userAnswerList" : null,
//            "answerAnalysis" : "<p>???????????????????????????????????????????????????<strong><span style=\"color: rgb(255,
//        0,
//                0);\">????????????????????????5G???</span></strong>??????????????????</p><p><br/></p><p><br/></p><p>?????????????????????5G?????????</p>",
//            "comment" : null,
//            "questionType" : 1,
//            "questionTypeDesc" : "?????????",
//            "score" : 1,
//            "yearMonth" : "2018",
//            "examTime" : null,
//            "focusFlag" : 0,
//            "easyFlag" : null,
//            "starFlag" : null,
//            "mark" : null,
//            "questionQty" : null,
//            "answerQty" : null,
//            "questionOptionList" : [
//        {
//            "id" : 86558,
//                "subjectCode" : null,
//                "courseCode" : null,
//                "examCode" : "XG201811",
//                "questionNo" : 20181101,
//                "rank" : null,
//                "optionNo" : "A",
//                "content" : "<p>??????<strong><span style=\"color: rgb(255,
//            0,
//                    0);\">??????</span></strong></p>",
//                "createTime" : null,
//                "updateTime" : null
//        },
//        {
//            "id" : 86559,
//                "subjectCode" : null,
//                "courseCode" : null,
//                "examCode" : "XG201811",
//                "questionNo" : 20181101,
//                "rank" : null,
//                "optionNo" : "B",
//                "content" : "<p>??????????????????</p>",
//                "createTime" : null,
//                "updateTime" : null
//        },
//        {
//            "id" : 86560,
//                "subjectCode" : null,
//                "courseCode" : null,
//                "examCode" : "XG201811",
//                "questionNo" : 20181101,
//                "rank" : null,
//                "optionNo" : "C",
//                "content" : "<p>?????????????????????</p>",
//                "createTime" : null,
//                "updateTime" : null
//        },
//        {
//            "id" : 86561,
//                "subjectCode" : null,
//                "courseCode" : null,
//                "examCode" : "XG201811",
//                "questionNo" : 20181101,
//                "rank" : null,
//                "optionNo" : "D",
//                "content" : "<p>???????????????</p>",
//                "createTime" : null,
//                "updateTime" : null
//        }
//    				],
//        "chapterName" : "",
//            "answerCount" : 200,
//            "rightRate" : 0,
//            "easyWrongChoice" : "B"
//    }
}
