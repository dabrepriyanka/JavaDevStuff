package com.example.sendemail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * author pdabre Email Sender application.
 */

@SpringBootApplication
@ComponentScan(basePackages = "com.example.sendemail")
public class SendemailApplication {

	public static void main(String[] args) {
		SpringApplication.run(SendemailApplication.class, args);
	}

}
