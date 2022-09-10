package com.example.shareme.templates;

public class LendPostTemplate {
    String pId, pCategory, pDuration, pTitle, pDescr, pImage, pTime, uid, uEmail, uName;
    //uDp

    public LendPostTemplate() {
    }

    public LendPostTemplate(String pId, String pCategory, String pDuration, String pTitle, String pDescr, String pImage, String pTime, String uid, String uEmail, String uName) { //String uDp
        this.pId = pId;
        this.pCategory = pCategory;
        this.pDuration = pDuration;
        this.pTitle = pTitle;
        this.pDescr = pDescr;
        this.pImage = pImage;
        this.pTime = pTime;
        this.uid = uid;
        this.uEmail = uEmail;
        //this.uDp = uDp;
        this.uName = uName;
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

    public String getpDuration() {
        return pDuration;
    }

    public void setpDuration(String pDuration) {
        this.pDuration = pDuration;
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

    /*public String getuDp() {
        return uDp;
    }

    public void setuDp(String uDp) {
        this.uDp = uDp;
    }*/

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }
}
