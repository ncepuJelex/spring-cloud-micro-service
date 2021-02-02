package com.jel.tech.sc.ch02.service;

import com.jel.tech.sc.ch02.model.Organization;
import com.jel.tech.sc.ch02.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @Author zhenhua.xu
 * @Date 2020/11/5 11:50
 * @Description
 **/
@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository orgRepository;

    public Organization getOrg(String organizationId) {

        return orgRepository.findByOrganizationId(organizationId);
    }

    public void saveOrg(Organization org){

        org.setOrganizationId( UUID.randomUUID().toString());

        orgRepository.save(org);
    }

    public void updateOrg(Organization org){
        orgRepository.save(org);
    }

    public void deleteOrg(Organization org){
        orgRepository.deleteById(org.getOrganizationId());
    }
}