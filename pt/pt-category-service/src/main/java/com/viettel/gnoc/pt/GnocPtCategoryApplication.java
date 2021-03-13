package com.viettel.gnoc.pt;


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
    "com.viettel.gnoc.pt"})
@EntityScan(basePackages = {"com.viettel.gnoc.pt", "com.viettel.gnoc.commons"})
public class GnocPtCategoryApplication {

  public static void main(String[] args) {
    SpringApplication.run(GnocPtCategoryApplication.class, args);
  }

}
