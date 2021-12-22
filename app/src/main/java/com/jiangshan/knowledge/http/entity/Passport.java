package com.jiangshan.knowledge.http.entity;

import java.io.Serializable;
import java.util.List;

/**
 * auth s_yz  2021/10/30
 */
public class Passport implements Serializable {

//    private User userInfo;

    private String appName;
    private String appNo;
    private String appVersion;
    private String appVersionDesc;
    private String appPath;
    private int appUpgradeFlag;
    private String welcomePicPath;
    private String noticeInfo;

    private List<SubjectInfo> subjectInfoList;

//    public User getUserInfo() {
//        return userInfo;
//    }
//
//    public void setUserInfo(User userInfo) {
//        this.userInfo = userInfo;
//    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppNo() {
        return appNo;
    }

    public void setAppNo(String appNo) {
        this.appNo = appNo;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getAppVersionDesc() {
        return appVersionDesc;
    }

    public void setAppVersionDesc(String appVersionDesc) {
        this.appVersionDesc = appVersionDesc;
    }

    public String getAppPath() {
        return appPath;
    }

    public void setAppPath(String appPath) {
        this.appPath = appPath;
    }

    public int getAppUpgradeFlag() {
        return appUpgradeFlag;
    }

    public void setAppUpgradeFlag(int appUpgradeFlag) {
        this.appUpgradeFlag = appUpgradeFlag;
    }

    public String getWelcomePicPath() {
        return welcomePicPath;
    }

    public void setWelcomePicPath(String welcomePicPath) {
        this.welcomePicPath = welcomePicPath;
    }

    public String getNoticeInfo() {
        return noticeInfo;
    }

    public void setNoticeInfo(String noticeInfo) {
        this.noticeInfo = noticeInfo;
    }

    public List<SubjectInfo> getSubjectInfoList() {
        return subjectInfoList;
    }

    public void setSubjectInfoList(List<SubjectInfo> subjectInfoList) {
        this.subjectInfoList = subjectInfoList;
    }
}
