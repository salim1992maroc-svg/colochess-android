package com.ctrange.colochess.transaction;

public class TransModule {

    private String tv_trans_status;
    private String tv_username;
    private String tv_email;
    private String tv_date;
    private String tv_character;
    private String tv_withdrawal;
    private String tv_transid;


    public TransModule(String tv_trans_status, String tv_username, String tv_email, String tv_date, String tv_character, String tv_withdrawal, String tv_transid) {
        this.tv_trans_status = tv_trans_status;
        this.tv_username = tv_username;
        this.tv_email = tv_email;
        this.tv_date = tv_date;
        this.tv_character = tv_character;
        this.tv_withdrawal = tv_withdrawal;
        this.tv_transid = tv_transid;
    }

    public String getTv_trans_status() {
        return tv_trans_status;
    }

    public void setTv_trans_status(String tv_trans_status) {
        this.tv_trans_status = tv_trans_status;
    }

    public String getTv_username() {
        return tv_username;
    }

    public void setTv_username(String tv_username) {
        this.tv_username = tv_username;
    }

    public String getTv_email() {
        return tv_email;
    }

    public void setTv_email(String tv_email) {
        this.tv_email = tv_email;
    }

    public String getTv_date() {
        return tv_date;
    }

    public void setTv_date(String tv_date) {
        this.tv_date = tv_date;
    }

    public String getTv_character() {
        return tv_character;
    }

    public void setTv_character(String tv_character) {
        this.tv_character = tv_character;
    }

    public String getTv_withdrawal() {
        return tv_withdrawal;
    }

    public void setTv_withdrawal(String tv_withdrawal) {
        this.tv_withdrawal = tv_withdrawal;
    }

    public String getTv_transid() {
        return tv_transid;
    }

    public void setTv_transid(String tv_transid) {
        this.tv_transid = tv_transid;
    }


}
