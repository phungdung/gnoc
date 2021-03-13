package com.viettel.gnoc.wo;


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
    "com.viettel.gnoc.cr", "com.viettel.gnoc.ws", "com.viettel.gnoc.maintenance",
    "com.viettel.gnoc.common", "com.viettel.sr", "com.viettel.gnoc.risk", "com.viettel.gnoc.wo",
    "com.viettel.gnoc.incident"})
@EntityScan(basePackages = {"com.viettel.gnoc.commons", "com.viettel.gnoc.cr",
    "com.viettel.gnoc.ws", "com.viettel.gnoc.maintenance",
    "com.viettel.gnoc.common", "com.viettel.sr", "com.viettel.gnoc.risk", "com.viettel.gnoc.wo",
    "com.viettel.gnoc.incident"})
public class GnocWoOutsideApplication {

  public static void main(String[] args) {
    SpringApplication.run(GnocWoOutsideApplication.class, args);
  }

}
