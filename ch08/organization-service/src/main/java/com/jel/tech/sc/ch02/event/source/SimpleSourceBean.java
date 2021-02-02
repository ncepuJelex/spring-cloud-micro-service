package com.jel.tech.sc.ch02.event.source;

import com.jel.tech.sc.ch02.utils.UserContext;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.logging.Logger;

/**
 * @Author zhenhua.xu
 * @Date 2020/11/9 11:39
 * @Description
 **/
//@Component
public class SimpleSourceBean {


    private static final Logger log = Logger.getLogger("SimpleSourceBean");

    @Resource
    private Source source;

    public void publishOrgChange(String action,String orgId) {

        log.info(String.format("org data changed, action:%s, orgId:%s", action, orgId));

        OrganizationChangeModel model = new OrganizationChangeModel(OrganizationChangeModel.class.getTypeName(),
                action, orgId, UserContext.getCorrelationId());

        source.output().send(MessageBuilder.withPayload(model).build());

    }
}
