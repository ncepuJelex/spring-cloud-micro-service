package com.jel.tech.sc.ch02.controller;

import com.jel.tech.sc.ch02.model.Organization;
import com.jel.tech.sc.ch02.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

/**
 * @Author zhenhua.xu
 * @Date 2020/11/5 11:49
 * @Description
 **/
@RestController
@RequestMapping(value="v1/organizations")
public class OrganizationServiceController {

    private static final Logger log = Logger.getLogger("OrganizationServiceController");

    @Autowired
    private OrganizationService orgService;


    @GetMapping(value="/{organizationId}")
    public Organization getOrganization(@PathVariable("organizationId") String organizationId) {

        log.info("new org service accept request for : " + organizationId);
        Organization org = orgService.getOrg(organizationId);

        if (org == null) {
            return null;
        }
        org.setContactName("NEW::" + org.getContactName());
        return org;
    }

    @RequestMapping(value="/{organizationId}",method = RequestMethod.PUT)
    public void updateOrganization( @PathVariable("organizationId") String orgId, @RequestBody Organization org) {
        orgService.updateOrg(org);
    }

    @RequestMapping(value="/{organizationId}",method = RequestMethod.POST)
    public void saveOrganization(@RequestBody Organization org) {

        orgService.saveOrg(org);
    }

    @RequestMapping(value="/{organizationId}",method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrganization(@PathVariable("orgId") String orgId, @RequestBody Organization org) {
        orgService.deleteOrg( org );
    }
}
