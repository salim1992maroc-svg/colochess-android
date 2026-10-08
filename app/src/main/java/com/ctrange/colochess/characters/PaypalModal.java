package com.ctrange.colochess.characters;

public class PaypalModal {



    private String model_dollar_value;
    private String model_payment_methods;
    private String model_discreption_redeem;
    private String model_image_url;
    private String model_minimum_redeem;

    public PaypalModal(String model_dollar_value, String model_payment_methods, String model_discreption_redeem, String model_image_url, String model_minimum_redeem) {
        this.model_dollar_value = model_dollar_value;
        this.model_payment_methods = model_payment_methods;
        this.model_discreption_redeem = model_discreption_redeem;
        this.model_image_url = model_image_url;
        this.model_minimum_redeem = model_minimum_redeem;
    }

    public String getModel_dollar_value() {
        return model_dollar_value;
    }

    public void setModel_dollar_value(String model_dollar_value) {
        this.model_dollar_value = model_dollar_value;
    }

    public String getModel_payment_methods() {
        return model_payment_methods;
    }

    public void setModel_payment_methods(String model_payment_methods) {
        this.model_payment_methods = model_payment_methods;
    }

    public String getModel_discreption_redeem() {
        return model_discreption_redeem;
    }

    public void setModel_discreption_redeem(String model_discreption_redeem) {
        this.model_discreption_redeem = model_discreption_redeem;
    }

    public String getModel_image_url() {
        return model_image_url;
    }

    public void setModel_image_url(String model_image_url) {
        this.model_image_url = model_image_url;
    }

    public String getModel_minimum_redeem() {
        return model_minimum_redeem;
    }

    public void setModel_minimum_redeem(String model_minimum_redeem) {
        this.model_minimum_redeem = model_minimum_redeem;
    }
}
