package com.ctrange.colochess.offers_trans;

public class Offers_TransModule {

    private String t_date_tv;
    private String t_type_tv;
    private String t_trans_id;
    private String t_point_tv;


    public Offers_TransModule(String t_date_tv, String t_type_tv, String t_trans_id, String t_point_tv) {
        this.t_date_tv = t_date_tv;
        this.t_type_tv = t_type_tv;
        this.t_trans_id = t_trans_id;
        this.t_point_tv = t_point_tv;
    }

    public String getT_date_tv() {
        return t_date_tv;
    }

    public void setT_date_tv(String t_date_tv) {
        this.t_date_tv = t_date_tv;
    }

    public String getT_type_tv() {
        return t_type_tv;
    }

    public void setT_type_tv(String t_type_tv) {
        this.t_type_tv = t_type_tv;
    }

    public String getT_trans_id() {
        return t_trans_id;
    }

    public void setT_trans_id(String t_trans_id) {
        this.t_trans_id = t_trans_id;
    }

    public String getT_point_tv() {
        return t_point_tv;
    }

    public void setT_point_tv(String t_point_tv) {
        this.t_point_tv = t_point_tv;
    }
}
