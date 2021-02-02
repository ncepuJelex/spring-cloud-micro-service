package com.jel.tech.sc.ch03.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Author zhenhua.xu
 * @Date 2020/11/4 11:10
 * @Description
 **/
@Configuration
public class ServiceConfig {

    @Value("${example.property}")
    private String exampleProperty;

    @Value("${signing.key}")
    private String signingKey;

    public String getExampleProperty(){
        return exampleProperty;
    }

    public String getSigningKey() {
        return signingKey;
    }
}
