package com.jel.tech.sc.ch02.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

/**
 * @Author zhenhua.xu
 * @Date 2020/11/6 15:22
 * @Description zuul post filter
 *  After the target service  is invoked, the response back from it will flow back through any Zuul post filter
 **/
@Component
public class ResponseFilter extends ZuulFilter {

    private static final int      FILTER_ORDER =  1;
    private static final boolean  SHOULD_FILTER = true;

    private static final Logger log = Logger.getLogger("ResponseFilter");

    @Autowired
    FilterUtils filterUtils;

    @Override
    public String filterType() {
        return FilterUtils.POST_FILTER_TYPE;
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

        RequestContext context = RequestContext.getCurrentContext();

        log.info("ResponseFilter add correlation id to outbound headers:" + filterUtils.getCorrelationId());

        context.getResponse().addHeader(FilterUtils.CORRELATION_ID, filterUtils.getCorrelationId());

        log.info("finish outgoing request for:" + context.getRequest().getRequestURI());

        return null;
    }
}
