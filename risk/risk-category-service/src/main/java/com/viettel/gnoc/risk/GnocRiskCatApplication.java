package com.viettel.gnoc.risk;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author TungPV
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableSwagger2
@ComponentScan(basePackages = {"com.viettel.gnoc.commons", "com.viettel.tracing",
    "com.viettel.gnoc.risk"})
@EnableFeignClients(basePackages = {"com.viettel.gnoc.commons.proxy"})
@EntityScan(basePackages = {"com.viettel.gnoc.commons", "com.viettel.gnoc.risk"})
public class GnocRiskCatApplication {

  public static void main(String[] args) {
    SpringApplication.run(GnocRiskCatApplication.class, args);
  }
}
