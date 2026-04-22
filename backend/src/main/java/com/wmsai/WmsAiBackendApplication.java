package com.wmsai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class WmsAiBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(WmsAiBackendApplication.class, args);
	}

}
