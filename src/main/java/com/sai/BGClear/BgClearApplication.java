package com.sai.BGClear;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BgClearApplication {

	public static void main(String[] args) {
		SpringApplication.run(BgClearApplication.class, args);
	}

}
