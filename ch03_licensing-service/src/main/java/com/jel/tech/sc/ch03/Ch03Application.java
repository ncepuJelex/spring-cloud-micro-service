package com.jel.tech.sc.ch03;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@RefreshScope
@SpringBootApplication
public class Ch03Application {

	public static void main(String[] args) {
		SpringApplication.run(Ch03Application.class, args);
	}

}
