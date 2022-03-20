package com.iiht.tweetapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TweetServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TweetServiceApplication.class, args);
	}

}
