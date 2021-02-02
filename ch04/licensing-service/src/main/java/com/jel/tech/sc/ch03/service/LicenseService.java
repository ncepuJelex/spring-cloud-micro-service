package com.jel.tech.sc.ch03.service;

import com.jel.tech.sc.ch03.client.OrganizationDiscoveryClient;
import com.jel.tech.sc.ch03.client.OrganizationFeignClient;
import com.jel.tech.sc.ch03.client.OrganizationRestTemplateClient;
import com.jel.tech.sc.ch03.config.ServiceConfig;
import com.jel.tech.sc.ch03.model.License;
import com.jel.tech.sc.ch03.model.Organization;
import com.jel.tech.sc.ch03.repository.LicenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

/**
 * @Author zhenhua.xu
 * @Date 2020/11/4 11:04
 * @Description
 **/
@Service
public class LicenseService {

    @Autowired
    private LicenseRepository licenseRepository;

    @Autowired
    private ServiceConfig config;

    /**
     * 3种不同的调用方式
     */
    @Resource
    OrganizationFeignClient organizationFeignClient;

    @Autowired
    OrganizationRestTemplateClient organizationRestClient;

    @Autowired
    OrganizationDiscoveryClient organizationDiscoveryClient;

    public License getLicense(String organizationId, String licenseId) {

        License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
        if (license == null) {
            return null;
        }
        // 试下配置功能
        return license.withComment((config.getExampleProperty()));
    }

    public List<License> getLicensesByOrg(String organizationId) {

        return licenseRepository.findByOrganizationId( organizationId );
    }

    /**
     * 试下spring data 本身的crud功能
     * @param license
     */
    public void saveLicense(License license) {

        license.withId(UUID.randomUUID().toString());
        licenseRepository.save(license);
    }

    public License getLicense(String organizationId, String licenseId, String clientType) {

        License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
        if (license == null) {
            return null;
        }
        Organization org = retrieveOrgInfo(organizationId, clientType);

        if (org == null) {
            return license.withOrganizationId(organizationId).withComment(config.getExampleProperty());
        }
        return license.withOrganizationId(organizationId)
                .withOrganizationName(org.getName())
                .withContactName(org.getContactName())
                .withContactEmail(org.getContactEmail())
                .withContactPhone(org.getContactPhone())
                .withComment(config.getExampleProperty());
    }

    private Organization retrieveOrgInfo(String organizationId, String clientType) {

        Organization organization;

        switch(clientType) {
            case "feign":
                System.out.println("I am using the feign client");
                organization = organizationFeignClient.getOrganization(organizationId);
                break;
            case "rest":
                System.out.println("I am using the rest client");
                organization = organizationRestClient.getOrganization(organizationId);
                break;
            case "discovery":
                System.out.println("I am using the discovery client");
                organization = organizationDiscoveryClient.getOrganization(organizationId);
                break;
            default:
                organization = organizationRestClient.getOrganization(organizationId);

        }
        return organization;
    }
}
