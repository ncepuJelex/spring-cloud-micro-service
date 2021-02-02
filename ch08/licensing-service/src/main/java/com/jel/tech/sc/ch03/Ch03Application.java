package com.jel.tech.sc.ch03;

import com.jel.tech.sc.ch03.config.ServiceConfig;
import com.jel.tech.sc.ch03.event.OrganizationChangeModel;
import com.jel.tech.sc.ch03.utils.UserContextInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * The @EnableBinding annotation tells the service to the use the channels
 * defined in the Sink interface to listen for incoming messages
 */
//@EnableBinding(Sink.class)
@EnableCircuitBreaker
@EnableDiscoveryClient
@EnableFeignClients
@EnableEurekaClient
@RefreshScope
@SpringBootApplication
public class Ch03Application {

	public static void main(String[] args) {
		SpringApplication.run(Ch03Application.class, args);
	}

//	@StreamListener(Sink.INPUT)
//	public void logSink(OrganizationChangeModel model) {
//
//        System.out.println("licensing-service接收到了org的change事件推送：" + model);
//    }

    /**
     * ribbon client形式调用
     * @return
     */
	@LoadBalanced
	@Bean
	public RestTemplate getRestTemplate() {

		RestTemplate template = new RestTemplate();

        /**
         * 为了拦截outgoing 的client http request,register这么个interceptors到RestTemplate上
         * 而这个拦截器仅仅是为了传递上下文信息到downstream服务上
         */
        List<ClientHttpRequestInterceptor> interceptors = template.getInterceptors();

        if (interceptors == null) {
            template.setInterceptors(Collections.singletonList(new UserContextInterceptor()));
        } else {
            interceptors.add(new UserContextInterceptor());
            template.setInterceptors(interceptors);
        }
        return template;
    }
}
