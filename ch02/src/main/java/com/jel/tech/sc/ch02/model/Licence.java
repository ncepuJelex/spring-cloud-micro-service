package com.jel.tech.sc.ch02.model;

/**
 * @Author zhenhua.xu
 * @Date 2020/11/3 15:59
 * @Description
 **/
public class Licence {

    private String id;
    private String organizationId;
    private String productName;
    private String licenseType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType;
    }

    public Licence withId(String id){
        this.setId( id );
        return this;
    }

    public Licence withOrganizationId(String organizationId){
        this.setOrganizationId(organizationId);
        return this;
    }

    public Licence withProductName(String productName){
        this.setProductName(productName);
        return this;
    }

    public Licence withLicenseType(String licenseType){
        this.setLicenseType(licenseType);
        return this;
    }
}
