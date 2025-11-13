package com.aqi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AqiMonitoringSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(AqiMonitoringSystemApplication.class, args);
	}

}
