package com.jel.tech.sc.ch03.service;

import com.jel.tech.sc.ch03.config.ServiceConfig;
import com.jel.tech.sc.ch03.model.License;
import com.jel.tech.sc.ch03.repository.LicenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
