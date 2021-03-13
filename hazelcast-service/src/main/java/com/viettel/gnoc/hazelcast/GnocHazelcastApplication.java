package com.viettel.gnoc.hazelcast;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author TungPV
 */
@SpringBootApplication
@EnableCaching
@EnableDiscoveryClient
public class GnocHazelcastApplication {

  public static void main(String[] args) {
    SpringApplication.run(GnocHazelcastApplication.class, args);
  }

}
