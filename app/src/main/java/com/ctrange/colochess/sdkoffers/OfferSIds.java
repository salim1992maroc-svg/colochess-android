package com.ctrange.colochess.sdkoffers;

public class OfferSIds {

    public String network_name;
    public String ids_;
    public String placem;


    public OfferSIds(String network_name, String ids_, String placem) {
        this.network_name = network_name;
        this.ids_ = ids_;
        this.placem = placem;
    }

    public String getNetwork_name() {
        return network_name;
    }

    public void setNetwork_name(String network_name) {
        this.network_name = network_name;
    }

    public String getIds_() {
        return ids_;
    }

    public void setIds_(String ids_) {
        this.ids_ = ids_;
    }

    public String getPlacem() {
        return placem;
    }

    public void setPlacem(String placem) {
        this.placem = placem;
    }
}
