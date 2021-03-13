package com.viettel.gnoc.gateway_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.viettel.gnoc.security.proxy"})
@ComponentScan(basePackages = {"com.viettel.gnoc.gateway_service", "com.viettel.gnoc.security"})
public class GatewayServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(GatewayServiceApplication.class, args);
  }

}
