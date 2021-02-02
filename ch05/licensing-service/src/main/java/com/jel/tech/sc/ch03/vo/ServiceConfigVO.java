package com.jel.tech.sc.ch03.vo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Author zhenhua.xu
 * @Date 2020/11/4 11:10
 * @Description
 **/
public class ServiceConfigVO {

    private String exampleProperty;

    private String signingKey;

    public String getExampleProperty() {
        return exampleProperty;
    }

    public void setExampleProperty(String exampleProperty) {
        this.exampleProperty = exampleProperty;
    }

    public String getSigningKey() {
        return signingKey;
    }

    public void setSigningKey(String signingKey) {
        this.signingKey = signingKey;
    }
}
