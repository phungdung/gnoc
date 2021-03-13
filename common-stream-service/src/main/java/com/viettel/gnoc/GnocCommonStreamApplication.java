package com.viettel.gnoc;


import lombok.extern.slf4j.Slf4j;
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
@ComponentScan(basePackages = {"com.viettel.gnoc", "com.viettel.tracing"})
@EntityScan(basePackages = {"com.viettel.gnoc"})
@Slf4j
public class GnocCommonStreamApplication {

  public static void main(String[] args) {
    SpringApplication.run(GnocCommonStreamApplication.class, args);
    try {
      //NativeUtils.loadLibraryFromJar("/MonthCore.dll");
      //NativeUtils.loadLibraryFromJar("/MonthAdapter.dll");
//      NativeUtils.loadLibraryFromJar("/libMonthCore.so");
//      NativeUtils.loadLibraryFromJar("/libMonthAdapter.so");
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
  }

}
