package com.viettel.gnoc.pt;


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
@ServletComponentScan
@EnableFeignClients(basePackages = {"com.viettel.gnoc.commons.proxy"})
@ComponentScan(basePackages = {"com.viettel.gnoc.commons", "com.viettel.gnoc.cr",
    "com.viettel.tracing", "com.viettel.gnoc.pt", "com.viettel.gnoc.kedb"})
@EntityScan(basePackages = {"com.viettel.gnoc.commons", "com.viettel.gnoc.pt",
    "com.viettel.gnoc.kedb"})
public class GnocPtOutsideApplication {

  public static void main(String[] args) {
    SpringApplication.run(GnocPtOutsideApplication.class, args);
  }
}
