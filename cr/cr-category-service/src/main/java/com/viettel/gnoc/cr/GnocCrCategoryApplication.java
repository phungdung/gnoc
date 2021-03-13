package com.viettel.gnoc.cr;


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
@EnableFeignClients(basePackages = {"com.viettel.gnoc.commons.proxy"})
@ComponentScan(basePackages = {"com.viettel.gnoc.commons", "com.viettel.tracing",
    "com.viettel.gnoc.cr", "com.viettel.gnoc.wfm"})
@EntityScan(basePackages = {"com.viettel.gnoc.commons", "com.viettel.gnoc.cr"})
public class GnocCrCategoryApplication {

  public static void main(String[] args) {
    SpringApplication.run(GnocCrCategoryApplication.class, args);
  }


}
