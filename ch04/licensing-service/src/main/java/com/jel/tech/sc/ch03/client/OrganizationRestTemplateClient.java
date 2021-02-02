package com.jel.tech.sc.ch03.client;

import com.jel.tech.sc.ch03.model.Organization;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @Author zhenhua.xu
 * @Date 2020/11/5 11:34
 * @Description ribbon client 形式调用
 **/
@Component
public class OrganizationRestTemplateClient {

    @Autowired
    private RestTemplate restTemplate;

    public Organization getOrganization(String organizationId) {

        // organizationservice 为 serviceId(即applicationId, organization项目中bootstrap.yaml下spring.application.name对应的值)
        ResponseEntity<Organization> exchange = restTemplate.exchange("http://organizationservice/v1/organizations/{organizationId}",
                HttpMethod.GET, null, Organization.class, organizationId);

        return exchange.getBody();
    }
}
