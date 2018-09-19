package com.chen.app.base;

/**
 * House status
 */
public enum HouseStatus {

    NOT_AUDITED(0, "To Verify"),
    PASSES(1, "Published"),
    RENTED(2, "Rented"),
    DELETED(3, "Deleted");

    private int value;

    private String desc;

    HouseStatus(int value, String desc){
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
