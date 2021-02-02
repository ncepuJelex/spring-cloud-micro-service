package com.jel.tech.sc.ch02.controller;

import com.jel.tech.sc.ch02.model.Licence;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author zhenhua.xu
 * @Date 2020/11/3 15:58
 * @Description
 **/
@RestController
@RequestMapping("/v1/org/{orgId}/licenses")
public class LicenseServiceController {

    @GetMapping("/{licenceId}")
    public Licence getLicences(@PathVariable("orgId") String orgId,
                               @PathVariable("licenceId") String licenceId) {

        return new Licence().withId(licenceId).withProductName("iphone12")
                .withLicenseType("rubbish").withOrganizationId(orgId);
    }
}
