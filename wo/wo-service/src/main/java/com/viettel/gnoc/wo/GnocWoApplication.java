package com.viettel.gnoc.wo;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableDiscoveryClient
@EnableSwagger2
@EnableFeignClients(basePackages = {"com.viettel.gnoc.commons.proxy"})
@ComponentScan(basePackages = {"com.viettel.gnoc.commons", "com.viettel.tracing",
    "com.viettel.gnoc.wo", "com.viettel.gnoc.cr"})
@EntityScan(basePackages = {"com.viettel.gnoc.wo", "com.viettel.gnoc.commons",
    "com.viettel.gnoc.cr"})
public class GnocWoApplication {

  public static void main(String[] args) {
    SpringApplication.run(GnocWoApplication.class, args);
  }

}
