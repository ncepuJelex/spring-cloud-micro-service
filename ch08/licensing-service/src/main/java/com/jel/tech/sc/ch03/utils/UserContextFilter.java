package com.jel.tech.sc.ch03.utils;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * @Author zhenhua.xu
 * @Date 2020/11/5 17:28
 * @Description pre controller 处理，把请求头中的数据封装到当前线程下的javabean
 **/
@Component
public class UserContextFilter implements Filter {

    private static final Logger logger = Logger.getLogger("UserContextFilter");

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        UserContextHolder.getContext().setCorrelationId(request.getHeader(UserContext.CORRELATION_ID));
        UserContextHolder.getContext().setUserId(request.getHeader(UserContext.USER_ID));
        UserContextHolder.getContext().setAuthToken(request.getHeader(UserContext.AUTH_TOKEN));
        UserContextHolder.getContext().setOrgId(request.getHeader(UserContext.ORG_ID));

        logger.info("UserContextFilter Correlation id: " + UserContextHolder.getContext().getCorrelationId());

        filterChain.doFilter(request, servletResponse);
    }
}
