package com.viettel.gnoc.maintenance;


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
@ComponentScan(basePackages = {"com.viettel.gnoc.common", "com.viettel.gnoc.commons",
    "com.viettel.gnoc.maintenance"})
@EntityScan(basePackages = {"com.viettel.gnoc.maintenance", "com.viettel.gnoc.commons"})
public class GnocMrApplication {

  public static void main(String[] args) {
    SpringApplication.run(GnocMrApplication.class, args);
  }

}
