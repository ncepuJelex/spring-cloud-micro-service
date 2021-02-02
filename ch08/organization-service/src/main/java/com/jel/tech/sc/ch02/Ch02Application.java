package com.jel.tech.sc.ch02;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;

/**
 *  you need to tell your application that it’s going to bind to a Spring Cloud Stream message broker
 *
 *  The use of Source.class in the @EnableBinding annotation tells Spring Cloud Stream that this service
 *  will communicate with the message broker via a set of channels defined on the Source class
 *
 *  channels sit above a message queue. Spring Cloud Stream has a default set of channels that
 *  can be configured to speak to a message broker
 */
//@EnableBinding(Source.class)
@EnableEurekaClient
@SpringBootApplication
public class Ch02Application {

	public static void main(String[] args) {
		SpringApplication.run(Ch02Application.class, args);
	}

}
