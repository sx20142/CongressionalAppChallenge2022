package com.example.shareme.templates;

public class BorrowPostTemplate {
    String pId, pCategory, pUrgency, pTitle, pDescr, pImage, pTime, uid, uEmail, uName, uPhone;
    //uDp

    public BorrowPostTemplate() {
    }

    public BorrowPostTemplate(String pId, String pCategory, String pUrgency, String pTitle, String pDescr, String pImage, String pTime, String uid, String uEmail, String uName, String uPhone) { //String uDp
        this.pId = pId;
        this.pCategory = pCategory;
        this.pUrgency = pUrgency;
        this.pTitle = pTitle;
        this.pDescr = pDescr;
        this.pImage = pImage;
        this.pTime = pTime;
        this.uid = uid;
        this.uEmail = uEmail;
        this.uName = uName;
        this.uPhone = uPhone;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getpCategory() {
        return pCategory;
    }

    public void setpCategory(String pCategory) {
        this.pCategory = pCategory;
    }

    public String getpUrgency() {
        return pUrgency;
    }

    public void setpUrgency(String pUrgency) {
        this.pUrgency = pUrgency;
    }

    public String getpTitle() {
        return pTitle;
    }

    public void setpTitle(String pTitle) {
        this.pTitle = pTitle;
    }

    public String getpDescr() {
        return pDescr;
    }

    public void setpDescr(String pDescr) {
        this.pDescr = pDescr;
    }

    public String getpImage() {
        return pImage;
    }

    public void setpImage(String pImage) {
        this.pImage = pImage;
    }

    public String getpTime() {
        return pTime;
    }

    public void setpTime(String pTime) {
        this.pTime = pTime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getuEmail() {
        return uEmail;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuPhone() {
        return uPhone;
    }

    public void setuPhone(String uPhone) {
        this.uName = uPhone;
    }
}
