package com.jel.tech.sc.ch03.client;

import com.jel.tech.sc.ch03.model.Organization;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author zhenhua.xu
 * @Date 2020/11/5 11:23
 * @Description organizationservice 为applicationId(spring.application.name对应的值)
 **/
@FeignClient("organizationservice")
public interface OrganizationFeignClient {

    /**
     * feign client
     * @param organizationId
     * @return
     */
    @GetMapping(value = "/v1/organizations/{organizationId}", consumes = "application/json")
    Organization getOrganization(@PathVariable("organizationId") String organizationId);
}
