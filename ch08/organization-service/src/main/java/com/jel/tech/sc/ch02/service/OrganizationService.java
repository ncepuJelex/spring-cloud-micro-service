package com.jel.tech.sc.ch02.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jel.tech.sc.ch02.event.source.OrganizationChangeModel;
import com.jel.tech.sc.ch02.event.source.SimpleSourceBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jel.tech.sc.ch02.model.Organization;
import com.jel.tech.sc.ch02.repository.OrganizationRepository;
import com.jel.tech.sc.ch02.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

//    @Resource
//    private SimpleSourceBean simpleSourceBean;

    @Value("${kafka.topic.org}")
    private String orgChangeTopic;

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    public Organization getOrg(String organizationId) {

        return orgRepository.findByOrganizationId(organizationId);
    }

    public void saveOrg(Organization org) throws JsonProcessingException {

        org.setOrganizationId( UUID.randomUUID().toString());

        orgRepository.save(org);

        // 推送数据变更通知
//        simpleSourceBean.publishOrgChange("SAVE", org.getOrganizationId());
        ObjectMapper mapper = new ObjectMapper();
        OrganizationChangeModel model = new OrganizationChangeModel(OrganizationChangeModel.class.getTypeName(),
                "SAVE", org.getOrganizationId(), UserContext.getCorrelationId());

        kafkaTemplate.send(orgChangeTopic, mapper.writeValueAsString(model));
    }

    public void updateOrg(Organization org) throws JsonProcessingException {
        orgRepository.save(org);
//        simpleSourceBean.publishOrgChange("UPDATE", org.getOrganizationId());

        ObjectMapper mapper = new ObjectMapper();
        OrganizationChangeModel model = new OrganizationChangeModel(OrganizationChangeModel.class.getTypeName(),
                "UPDATE", org.getOrganizationId(), UserContext.getCorrelationId());

        kafkaTemplate.send(orgChangeTopic, mapper.writeValueAsString(model));
    }

    public void deleteOrg(Organization org){
        orgRepository.deleteById(org.getOrganizationId());
    }
}