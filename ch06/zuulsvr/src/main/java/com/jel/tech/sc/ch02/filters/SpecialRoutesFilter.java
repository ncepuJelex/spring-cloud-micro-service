package com.jel.tech.sc.ch02.filters;

import com.jel.tech.sc.ch02.model.AbTestingRoute;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @Author zhenhua.xu
 * @Date 2020/11/6 15:38
 * @Description zuul route filter
 **/
@Component
public class SpecialRoutesFilter extends ZuulFilter {

    private static final int FILTER_ORDER =  1;
    private static final boolean SHOULD_FILTER =true;

    @Autowired
    FilterUtils filterUtils;

    @Resource
    RestTemplate restTemplate;

    @Override
    public String filterType() {
        return FilterUtils.ROUTE_FILTER_TYPE;
    }

    @Override
    public int filterOrder() {
        return FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return SHOULD_FILTER;
    }

    /**
     * The helper variable is an instance variable of type ProxyRequestHelper class.
     * This is a Spring Cloud class with helper functions for proxying service request
     */
    private ProxyRequestHelper helper = new ProxyRequestHelper();


    @Override
    public Object run() throws ZuulException {

        RequestContext ctx = RequestContext.getCurrentContext();

        AbTestingRoute route = getAbRoutingInfo(filterUtils.getServiceId());

        if (route != null && useSpecialRoute(route)) {

            String routeAddr = buildRouteAddr(ctx.getRequest().getRequestURI(), route.getEndpoint(),
                    filterUtils.getServiceId());

            forwardToSpecialRoute(routeAddr);
        }
        return null;
    }

    private void forwardToSpecialRoute(String routeAddr) {

        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();

        // 复制request header出来
        MultiValueMap<String, String> headers = this.helper
                .buildZuulRequestHeaders(request);

        // 复制request param出来
        MultiValueMap<String, String> params = this.helper
                .buildZuulRequestQueryParams(request);
        // 获取request method
        String verb = getVerb(request);

        // 复制request body
        InputStream requestEntity = getRequestBody(request);

        if (request.getContentLength() < 0) {
            context.setChunkedRequestBody();
        }

        this.helper.addIgnoredHeaders();
        HttpResponse response;

        try(CloseableHttpClient httpClient = HttpClients.createDefault()) {

            // 真正forward
            response = forward(httpClient, verb, routeAddr, request, headers,
                    params, requestEntity);
            // 设置响应
            setResponse(response);

        } catch (Exception ex ) {
            ex.printStackTrace();
        }
    }

    private void setResponse(HttpResponse response) throws IOException {

        this.helper.setResponse(response.getStatusLine().getStatusCode(),
                response.getEntity() == null ? null : response.getEntity().getContent(),
                revertHeaders(response.getAllHeaders()));
    }

    private MultiValueMap<String, String> revertHeaders(Header[] allHeaders) {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        for (Header header : allHeaders) {

            String name = header.getName();

            List<String> list = map.get(name);
            if (list == null) {
                list = new ArrayList<>();
                map.put(name, list);
            }
            list.add(header.getValue());
        }
        return map;
    }

    private HttpResponse forward(CloseableHttpClient httpClient, String verb, String routeAddr,
                                 HttpServletRequest request, MultiValueMap<String, String> headers,
                                 MultiValueMap<String, String> params, InputStream requestEntity) throws Exception {

        this.helper.debug(verb, routeAddr, headers, params,
                requestEntity);

        URL host = new URL(routeAddr);
        HttpHost httpHost = getHttpHost(host);

        HttpRequest httpRequest;
        int contentLength = request.getContentLength();
        InputStreamEntity entity = new InputStreamEntity(requestEntity, contentLength,
                request.getContentType() != null
                        ? ContentType.create(request.getContentType()) : null);

        switch (verb.toUpperCase()) {
            case "POST":
                HttpPost httpPost = new HttpPost(routeAddr);
                httpRequest = httpPost;
                httpPost.setEntity(entity);
                break;
            case "PUT":
                HttpPut httpPut = new HttpPut(routeAddr);
                httpRequest = httpPut;
                httpPut.setEntity(entity);
                break;
            case "PATCH":
                HttpPatch httpPatch = new HttpPatch(routeAddr);
                httpRequest = httpPatch;
                httpPatch.setEntity(entity);
                break;
            default:
                httpRequest = new BasicHttpRequest(verb, routeAddr);
        }
        try {
            httpRequest.setHeaders(convertHeaders(headers));

            HttpResponse zuulResponse = forwardRequest(httpClient, httpHost, httpRequest);

            return zuulResponse;
        } finally {
        }
    }

    private HttpResponse forwardRequest(CloseableHttpClient httpClient, HttpHost httpHost, HttpRequest httpRequest) throws IOException {

        return httpClient.execute(httpHost, httpRequest);
    }

    private Header[] convertHeaders(MultiValueMap<String, String> headers) {

        List<Header> list = new ArrayList<>();

        headers.forEach((k, v) -> v.forEach(e -> list.add(new BasicHeader(k, e))));

        return list.toArray(new BasicHeader[0]);
    }

    private HttpHost getHttpHost(URL host) {

        return new HttpHost(host.getHost(), host.getPort(),
                host.getProtocol());
    }

    private InputStream getRequestBody(HttpServletRequest request) {

        try {
            return request.getInputStream();
        } catch (IOException e) {
            // no request body is ok.
        }
        return null;
    }

    private String getVerb(HttpServletRequest request) {

        return request.getMethod().toUpperCase();
    }

    private String buildRouteAddr(String requestURI, String endpoint, String serviceName) {

        int index = requestURI.indexOf(serviceName);
        // assert index > -1
        String suffix = requestURI.substring(index + serviceName.length());
        System.out.println("New target route:" + String.format("%s/%s", endpoint, suffix));

        return String.format("%s/%s", endpoint, suffix);
    }

    private boolean useSpecialRoute(AbTestingRoute route) {

        if ("N".equals(route.getActive())) {
            return false;
        }
        Random random = new Random();

        int value = random.nextInt(10) + 1;

        if (route.getWeight() < value) {
            return true;
        }
        return false;
    }

    private AbTestingRoute getAbRoutingInfo(String serviceName) {

        ResponseEntity<AbTestingRoute> restExchange;

        try {
            restExchange = restTemplate.exchange("http://specialroutesservice/v1/route/abtesting/{serviceName}",
                    HttpMethod.GET,null, AbTestingRoute.class, serviceName);

            return restExchange.getBody();

        } catch (RestClientException e) {

            if (e instanceof HttpClientErrorException) {
                HttpClientErrorException ex = (HttpClientErrorException) e;
                if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                    return null;
                }
            }
            throw e;
        }
    }
}
