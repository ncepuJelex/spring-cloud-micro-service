package com.jel.tech.sc.ch03.service;

import com.jel.tech.sc.ch03.client.OrganizationDiscoveryClient;
import com.jel.tech.sc.ch03.client.OrganizationFeignClient;
import com.jel.tech.sc.ch03.client.OrganizationRestTemplateClient;
import com.jel.tech.sc.ch03.config.ServiceConfig;
import com.jel.tech.sc.ch03.model.License;
import com.jel.tech.sc.ch03.model.Organization;
import com.jel.tech.sc.ch03.repository.LicenseRepository;
import com.jel.tech.sc.ch03.utils.UserContextFilter;
import com.jel.tech.sc.ch03.utils.UserContextHolder;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * @Author zhenhua.xu
 * @Date 2020/11/4 11:04
 * @Description
 **/
// 整个class级别，设置一个默认的hystrix配置
@DefaultProperties(commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")
})
@Service
public class LicenseService {

    private static final Logger logger = LoggerFactory.getLogger(LicenseService.class);

    @Autowired
    private LicenseRepository licenseRepository;

    @Autowired
    private ServiceConfig config;

    /**
     * 3种不同的调用方式
     */
    @Resource
    OrganizationFeignClient organizationFeignClient;

    @Autowired
    OrganizationRestTemplateClient organizationRestClient;

    @Autowired
    OrganizationDiscoveryClient organizationDiscoveryClient;

    public License getLicense(String organizationId, String licenseId) {

        License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
        if (license == null) {
            return null;
        }
        // 试下配置功能
        return license.withComment((config.getExampleProperty()));
    }

    /**
     * 12000ms后 超时，这个配置也只是为了演示功能而使用，生产不可能这么长时间，可能为1000ms
     * @param organizationId
     * @return
     */
    //    @HystrixCommand
    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "12000"),
            /**
             * With SEMAPHORE-based isolation, Hystrix manages the distributed call protected by the @HystrixCommand annotation
             * without starting a new thread and will interrupt the parent thread if the call times out.
             *
             * In a synchronous container server environment (Tomcat), interrupting the parent thread will cause an exception to be thrown
             * that cannot be caught by the developer. This can lead to unexpected consequences for the developer writing the code
             * because they can’t catch the thrown exception or do any resource cleanup or error handling.
             *
             * The SEMAPHORE isolation model is lighter-weight and should be used when you have a high-volume on your services and
             * are running in an asynchronous I/O programming model (you are using an asynchronous I/O container such as Netty).
             * */
            @HystrixProperty(name = "execution.isolation.strategy", value = "THREAD")
//            @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
    })
    public List<License> getLicensesByOrg(String organizationId) {

        /**
         * 在@HystrixProperty(name = "execution.isolation.strategy", value = "THREAD")前提下，
         * 本来输出是这样的： LicenseService.getLicensesByOrg  Correlation id:
         * 但是在配置上下文strategy输出是这样的： LicenseService.getLicensesByOrg  Correlation id: CORRELATION_ID  （有了上下文的传递后，由hystrix分配线程执行的本方法也能拿到CORRELATION_ID）
         */
        System.out.println("LicenseService.getLicensesByOrg  Correlation id: " + UserContextHolder.getContext().getCorrelationId());
        // mock slow query
        // 上面使用了 "execution.isolation.thread.timeoutInMilliseconds = 12000ms配置后，就再也不会超时了（12000 > 11000）
        randomRunLong(11000);

        return licenseRepository.findByOrganizationId( organizationId );
    }

    /**
     * 试下spring data 本身的crud功能
     * @param license
     */
    public void saveLicense(License license) {

        license.withId(UUID.randomUUID().toString());
        licenseRepository.save(license);
    }

    public License getLicense(String organizationId, String licenseId, String clientType) {

        License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
        if (license == null) {
            return null;
        }
        Organization org = retrieveOrgInfo(organizationId, clientType);

        if (org == null) {
            return license.withOrganizationId(organizationId).withComment(config.getExampleProperty());
        }
        return license.withOrganizationId(organizationId)
                .withOrganizationName(org.getName())
                .withContactName(org.getContactName())
                .withContactEmail(org.getContactEmail())
                .withContactPhone(org.getContactPhone())
                .withComment(config.getExampleProperty());
    }

    private Organization retrieveOrgInfo(String organizationId, String clientType) {

        Organization organization;

        switch(clientType) {
            case "feign":
                System.out.println("I am using the feign client");
                organization = organizationFeignClient.getOrganization(organizationId);
                break;
            case "rest":
                System.out.println("I am using the rest client");
                organization = organizationRestClient.getOrganization(organizationId);
                break;
            case "discovery":
                System.out.println("I am using the discovery client");
                organization = organizationDiscoveryClient.getOrganization(organizationId);
                break;
            default:
                organization = organizationRestClient.getOrganization(organizationId);

        }
        return organization;
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
