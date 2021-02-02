package com.jel.tech.sc.ch02.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jel.tech.sc.ch02.model.Organization;
import com.jel.tech.sc.ch02.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * @Author zhenhua.xu
 * @Date 2020/11/5 11:49
 * @Description
 **/
@RestController
@RequestMapping(value="v1/organizations")
public class OrganizationServiceController {

    @Autowired
    private OrganizationService orgService;


    @RequestMapping(value="/{organizationId}",method = RequestMethod.GET)
    public Organization getOrganization( @PathVariable("organizationId") String organizationId) {
        return orgService.getOrg(organizationId);
    }

    @RequestMapping(value="/{organizationId}",method = RequestMethod.PUT)
    public void updateOrganization( @PathVariable("organizationId") String orgId, @RequestBody Organization org) throws JsonProcessingException {
        orgService.updateOrg(org);
    }

    @RequestMapping(value="/{organizationId}",method = RequestMethod.POST)
    public void saveOrganization(@RequestBody Organization org) throws JsonProcessingException {

        orgService.saveOrg(org);
    }

    @RequestMapping(value="/{organizationId}",method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrganization(@PathVariable("orgId") String orgId, @RequestBody Organization org) {
        orgService.deleteOrg( org );
    }
}
