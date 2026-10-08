package com.ctrange.colochess.modul;

public class User {

    private String name;
    private String number;
    private String email;
    private String points;
    private String isBLocked;
    private String referraled_with;
    private String userReferCode;
    private String device_id;

    public User() {
    }

    public User(String name, String number, String email, String points, String isBLocked, String referraled_with, String userReferCode,String device_id) {

        this.name = name;
        this.number = number;
        this.email = email;
        this.points = points;
        this.referraled_with = referraled_with;
        this.isBLocked = isBLocked;
        this.userReferCode = userReferCode;
        this.device_id = device_id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getIsBLocked() {
        return isBLocked;
    }

    public void setIsBLocked(String isBLocked) {
        this.isBLocked = isBLocked;
    }

    public String getReferraled_with() {
        return referraled_with;
    }

    public void setReferraled_with(String referraled_with) {
        this.referraled_with = referraled_with;
    }

    public String getUserReferCode() {
        return userReferCode;
    }

    public void setUserReferCode(String userReferCode) {
        this.userReferCode = userReferCode;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }
}
