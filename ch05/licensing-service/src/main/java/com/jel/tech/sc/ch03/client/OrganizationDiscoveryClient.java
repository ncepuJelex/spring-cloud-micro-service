package com.jel.tech.sc.ch03.client;

import com.jel.tech.sc.ch03.model.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @Author zhenhua.xu
 * @Date 2020/11/5 11:26
 * @Description
 **/
@Component
public class OrganizationDiscoveryClient {

    // 注意是springcloud下的包
    @Autowired
    private DiscoveryClient discoveryClient;

    public Organization getOrganization(String organizationId) {

        // 因为在容器中的RestTemplate对象会被ribbon拦截,不纯粹
        RestTemplate restTemplate = new RestTemplate();

        List<ServiceInstance> list = discoveryClient.getInstances("organizationservice");

        if (list.isEmpty()) {
            return null;
        }
        String serviceUri = String.format("%s/v1/organizations/%s",
                list.get(0).getUri().toString(),
                organizationId);

        ResponseEntity<Organization> entity = restTemplate.exchange(serviceUri, HttpMethod.GET, null, Organization.class, organizationId);

        return entity.getBody();
    }
}
