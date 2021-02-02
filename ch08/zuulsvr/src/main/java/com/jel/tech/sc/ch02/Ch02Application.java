package com.jel.tech.sc.ch02;

import com.jel.tech.sc.ch02.utils.UserContextInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

/**
 * The Zuul proxy server is designed by default to work on the Spring products.
 * As such, Zuul will automatically use Eureka to look up services by their service IDs and
 * then use Netflix Ribbon to do client-side load balancing of requests from within Zuul
 */
@EnableZuulProxy
@SpringBootApplication
public class Ch02Application {

	public static void main(String[] args) {
		SpringApplication.run(Ch02Application.class, args);
	}

	/**
	 * ribbon client形式调用
	 * @return
	 */
	@LoadBalanced
	@Bean
	public RestTemplate restTemplate() {

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
