package com.jel.tech.sc.ch03.event;

/**
 * @Author zhenhua.xu
 * @Date 2020/11/9 11:55
 * @Description
 **/
public class OrganizationChangeModel {

    private String type;

    private String action;

    private String orgId;

    private String correlationId;

    public OrganizationChangeModel(String type, String action, String orgId, String correlationId) {

        this.type = type;
        this.action = action;
        this.orgId = orgId;
        this.correlationId = correlationId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    @Override
    public String toString() {
        return "OrganizationChangeModel{" +
                "type='" + type + '\'' +
                ", action='" + action + '\'' +
                ", orgId='" + orgId + '\'' +
                ", correlationId='" + correlationId + '\'' +
                '}';
    }
}
