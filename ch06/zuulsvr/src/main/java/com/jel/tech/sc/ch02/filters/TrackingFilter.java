package com.jel.tech.sc.ch02.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

/**
 * @Author zhenhua.xu
 * @Date 2020/11/6 14:07
 * @Description zuul pre filter
 **/
@Component
public class TrackingFilter extends ZuulFilter {

    private static final int      FILTER_ORDER =  1;
    private static final boolean  SHOULD_FILTER = true;

    private static final Logger log = Logger.getLogger("TrackingFilter");

    @Autowired
    FilterUtils filterUtils;


    @Override
    public String filterType() {
        return FilterUtils.PRE_FILTER_TYPE;
    }

    @Override
    public int filterOrder() {
        return FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return SHOULD_FILTER;
    }

    @Override
    public Object run() throws ZuulException {
        if (isCorrelationIdPresent()) {
            log.info("tmx-correlation-id found in tracking filter:" + filterUtils.getCorrelationId());
        } else {
            filterUtils.setCorrelationId(generateCorrelationId());
            log.info("generate tmx-correlation-id in tracking filter: " + filterUtils.getCorrelationId());
        }
        RequestContext context = RequestContext.getCurrentContext();
        log.info("Process incoming request:" + context.getRequest().getRequestURI());

        return null;
    }

    private boolean isCorrelationIdPresent() {

        return filterUtils.getCorrelationId() != null;
    }

    private String generateCorrelationId(){
        return java.util.UUID.randomUUID().toString();
    }
}
