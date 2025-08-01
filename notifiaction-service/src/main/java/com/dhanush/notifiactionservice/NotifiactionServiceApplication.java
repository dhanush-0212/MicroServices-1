package com.dhanush.notifiactionservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@Slf4j
public class NotifiactionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotifiactionServiceApplication.class, args);
	}

	@KafkaListener(topics = "notificationTopic")
	public void handleNotification(OrderPlacedEvent record) {
		log.info("Received order placed event: {}", record);

	}

}
