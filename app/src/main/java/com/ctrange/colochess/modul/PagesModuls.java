package com.ctrange.colochess.modul;

public class PagesModuls {


    private String m_site_page;
    private String m_rules_page;
    private String m_privacy_page;
    private String m_contact_page;


    public PagesModuls(String m_site_page, String m_rules_page, String m_privacy_page, String m_contact_page) {
        this.m_site_page = m_site_page;
        this.m_rules_page = m_rules_page;
        this.m_privacy_page = m_privacy_page;
        this.m_contact_page = m_contact_page;

    }


    public String getM_site_page() {
        return m_site_page;
    }

    public void setM_site_page(String m_site_page) {
        this.m_site_page = m_site_page;
    }

    public String getM_rules_page() {
        return m_rules_page;
    }

    public void setM_rules_page(String m_rules_page) {
        this.m_rules_page = m_rules_page;
    }

    public String getM_privacy_page() {
        return m_privacy_page;
    }

    public void setM_privacy_page(String m_privacy_page) {
        this.m_privacy_page = m_privacy_page;
    }

    public String getM_contact_page() {
        return m_contact_page;
    }

    public void setM_contact_page(String m_contact_page) {
        this.m_contact_page = m_contact_page;
    }
}
