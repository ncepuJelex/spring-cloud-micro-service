package com.jel.tech.sc.ch03.controller;

import com.jel.tech.sc.ch03.config.ServiceConfig;
import com.jel.tech.sc.ch03.model.License;
import com.jel.tech.sc.ch03.service.LicenseService;
import com.jel.tech.sc.ch03.vo.ServiceConfigVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author zhenhua.xu
 * @Date 2020/11/3 15:58
 * @Description
 **/
@RestController
@RequestMapping("/v1/org/{orgId}/licenses")
public class LicenseServiceController {

    @Resource
    private LicenseService licenseService;

    @Resource
    private ServiceConfig serviceConfig;

    @RequestMapping(value="/",method = RequestMethod.GET)
    public List<License> getLicenses(@PathVariable("orgId") String orgId) {

        return licenseService.getLicensesByOrg(orgId);
    }

    @GetMapping("/{licenceId}")
    public License getLicences(@PathVariable("orgId") String orgId,
                               @PathVariable("licenceId") String licenceId) {

        return licenseService.getLicense(orgId, licenceId);
    }

    @GetMapping("/config")
    public ServiceConfigVO getServiceConfig() {

        ServiceConfigVO vo = new ServiceConfigVO();
        vo.setExampleProperty(serviceConfig.getExampleProperty());
        vo.setSigningKey(serviceConfig.getSigningKey());

        return vo;
    }

    @RequestMapping(value="/",method = RequestMethod.POST)
    public void saveLicenses(@RequestBody License license) {
        licenseService.saveLicense(license);
    }
}
