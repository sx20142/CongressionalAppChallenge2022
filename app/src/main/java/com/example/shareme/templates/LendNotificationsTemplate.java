package com.example.shareme.templates;

public class LendNotificationsTemplate {
    String otherName, otherPhone, itemName;

    public LendNotificationsTemplate() {
    }

    public LendNotificationsTemplate(String otherName, String otherPhone, String itemName) {
        this.otherName = otherName;
        this.otherPhone = otherPhone;
        this.itemName = itemName;
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
}
