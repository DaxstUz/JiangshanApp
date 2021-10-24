package com.jiangshan.knowledge.http.entity;

import java.io.Serializable;
import java.util.List;

/**
 * auth s_yz  2021/10/16
 */
public class SubjectCategory implements Serializable {

    private int id;
    private String categoryCode;
    private String categoryName;
    private List<Subject> subjectInfoList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<Subject> getSubjectInfoList() {
        return subjectInfoList;
    }

    public void setSubjectInfoList(List<Subject> subjectInfoList) {
        this.subjectInfoList = subjectInfoList;
    }
}
