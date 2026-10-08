package com.ctrange.colochess.sdkoffers;

public class ModelList {

    private String name_network;
    private String desc_network;
    private String icon_network;
    private String ids_network;
    private String placement_network;
    private String type;


    public ModelList(String name_network, String desc_network, String icon_network, String ids_network, String placement_network, String type) {
        this.name_network = name_network;
        this.desc_network = desc_network;
        this.icon_network = icon_network;
        this.ids_network = ids_network;
        this.placement_network = placement_network;
        this.type = type;
    }

    public String getName_network() {
        return name_network;
    }

    public void setName_network(String name_network) {
        this.name_network = name_network;
    }

    public String getDesc_network() {
        return desc_network;
    }

    public void setDesc_network(String desc_network) {
        this.desc_network = desc_network;
    }

    public String getIcon_network() {
        return icon_network;
    }

    public void setIcon_network(String icon_network) {
        this.icon_network = icon_network;
    }

    public String getIds_network() {
        return ids_network;
    }

    public void setIds_network(String ids_network) {
        this.ids_network = ids_network;
    }

    public String getPlacement_network() {
        return placement_network;
    }

    public void setPlacement_network(String placement_network) {
        this.placement_network = placement_network;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
