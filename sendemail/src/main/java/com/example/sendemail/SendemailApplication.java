package com.example.sendemail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.autoconfigure.jdbc.*;

/**
 * author pdabre Email Sender application.
 */

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = "com.example.sendemail")
public class SendemailApplication {

	public static void main(String[] args) {
		SpringApplication.run(SendemailApplication.class, args);
	}

}
