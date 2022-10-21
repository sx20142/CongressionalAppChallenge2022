package com.example.shareme.templates;

public class NotificationsTemplate {
    String otherName, otherPhone, itemName, lendOrBorrow;

    public NotificationsTemplate() {
    }

    public NotificationsTemplate(String otherName, String otherPhone, String itemName, String lendOrBorrow) {
        this.otherName = otherName;
        this.otherPhone = otherPhone;
        this.itemName = itemName;
        this.lendOrBorrow = lendOrBorrow;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public String getOtherPhone() {
        return otherPhone;
    }

    public void setOtherPhone(String otherPhone) {
        this.otherPhone = otherPhone;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getLendOrBorrow() {
        return lendOrBorrow;
    }

    public void setLendOrBorrow(String lendOrBorrow) {
        this.lendOrBorrow = lendOrBorrow;
    }
}
