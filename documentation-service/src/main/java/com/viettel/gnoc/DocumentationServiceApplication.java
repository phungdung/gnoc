package com.viettel.gnoc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import springfox.documentation.swagger2.annotations.EnableSwagger2;
/**
 * 
 * @author satish sharma
 *
 */
@EnableSwagger2
@EnableScheduling
@SpringBootApplication
public class DocumentationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocumentationServiceApplication.class, args);
	}
}
