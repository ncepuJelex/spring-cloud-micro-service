package com.jel.tech.sc.ch03.client;

import com.jel.tech.sc.ch03.model.Organization;
import com.netflix.discovery.converters.Auto;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

/**
 * @Author zhenhua.xu
 * @Date 2020/11/5 11:34
 * @Description ribbon client 形式调用
 **/
@Component
public class OrganizationRestTemplateClient {

    @Autowired
    private RestTemplate restTemplate;

//    @HystrixCommand(fallbackMethod = "fallbackForGetOrganization")

    /**
     *  if you set the maxQueueSize to -1, a Java SynchronousQueue will be used to hold all incoming requests,
     *  which enforces that you can never have more requests in process then the number
     *  of threads available in the thread pool.
     *  set it to a value greater than one will cause Hystrix to use a Java LinkedBlockingQueue
     *
     *   What’s the proper sizing for a custom thread pool? Netflix recommends the following formula:
     *
     *   (requests per second at peak when the service is healthy * 99th percentile latency in seconds)
     *   + small amount of extra threads for overhead.
     *
     *    A key indicator that the thread pool properties need to be adjusted is
     *    when a service call is timing out even if the targeted remote resource is healthy.
     *
     * @param organizationId
     * @return
     */
    @HystrixCommand(fallbackMethod = "fallbackForGetOrganization",
            threadPoolKey = "getOrganizationRestPool",
            threadPoolProperties = {
                @HystrixProperty(name = "coreSize", value = "30"),
                @HystrixProperty(name = "maxQueueSize", value = "10")
            },
            /**
             * 1. controls the amount of consecutive calls that must occur within a default(10-second window) or config value before Hystrix will consider tripping the circuit breaker for the call
             *
             * 2. percentage of calls that must fail (due to timeouts, an exception being thrown,
             * or a HTTP 500 being returned) after the circuitBreaker.requestVolumeThreshold value
             * has been passed before the circuit breaker it tripped
             *
             * 3. amount of time Hystrix will sleep once the circuit breaker is tripped before Hystrix will allow another call through to see if the service is healthy again
             *
             * 4. control the size of the window that will be used by Hystrix to monitor for problems with a service call
             *
             * 5. controls the number of times statistics are collected in the window you’ve defined
             **/
            commandProperties = {
                @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
                @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "75"),
                @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "7000"),
                @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "15000"),
                @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "5")
            }
    )
    public Organization getOrganization(String organizationId) {

        // mock possible slow
        randomRunLong(11000);

        // organizationservice 为 serviceId(即applicationId, organization项目中bootstrap.yaml下spring.application.name对应的值)
        ResponseEntity<Organization> exchange = restTemplate.exchange("http://organizationservice/v1/organizations/{organizationId}",
                HttpMethod.GET, null, Organization.class, organizationId);

        return exchange.getBody();
    }

    /**
     * 上面方法的备胎（alternative choice）
     * @param organizationId
     *  说明： 如果下面这个fallback方法又去调用其他微服务（结果就不可控了），那这个方法上也可以加上@HystrixCommand，
     *          并且给它也配置一个fallback
     * @return
     */
    private Organization fallbackForGetOrganization(String organizationId) {

        Organization org = new Organization();
        org.setId(organizationId);
        org.setName("devil");
        org.setContactEmail("110@xx.com");
        org.setContactPhone("110");
        return org;
    }


    private void randomRunLong(long ms) {

        Random random = new Random();

        int num = random.nextInt(3) + 1;

        if (num == 3) {
            safeSleep(ms);
        }
    }

    private void safeSleep(long ms) {

        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
