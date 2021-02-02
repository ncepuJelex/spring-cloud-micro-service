package com.jel.tech.sc.ch03.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;

/**
 * @Author zhenhua.xu
 * @Date 2020/11/9 15:34
 * @Description
 **/
public class KafkaService {

    @KafkaListener(containerFactory = "kafkaListenerContainerFactory", topics = "orgChangeTopic")
 	public void process(String msg, @Header("kafka_partition") int partition) {

        System.out.println("msg:" + msg);
        System.out.println("partition: " + partition);
 	}
}
