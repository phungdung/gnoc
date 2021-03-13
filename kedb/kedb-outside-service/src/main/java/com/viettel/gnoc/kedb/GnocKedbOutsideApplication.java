package com.viettel.gnoc.kedb;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
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
@ServletComponentScan
@ComponentScan(basePackages = {"com.viettel.gnoc.commons", "com.viettel.tracing",
    "com.viettel.gnoc.pt", "com.viettel.gnoc.kedb"})
@EntityScan(basePackages = {"com.viettel.gnoc.commons", "com.viettel.gnoc.pt",
    "com.viettel.gnoc.kedb"})
public class GnocKedbOutsideApplication {

  public static void main(String[] args) {
    SpringApplication.run(GnocKedbOutsideApplication.class, args);
  }
}
