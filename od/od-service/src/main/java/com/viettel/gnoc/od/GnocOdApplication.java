package com.viettel.gnoc.od;


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
@ComponentScan(basePackages = {"com.viettel.gnoc.commons", "com.viettel.gnoc.cr", "com.viettel.gnoc.sr",
    "com.viettel.tracing", "com.viettel.gnoc.od"})
@EntityScan(basePackages = {"com.viettel.gnoc.commons", "com.viettel.gnoc.od"})
public class GnocOdApplication {

  public static void main(String[] args) {
    SpringApplication.run(GnocOdApplication.class, args);
  }

//  @Bean
//  public Docket swaggerApi() {
//    return new Docket(DocumentationType.SWAGGER_2)
//        .select()
//        .apis(RequestHandlerSelectors.basePackage("com.viettel.gnoc.od.controller"))
//        .paths(PathSelectors.any())
//        .build()
//        .apiInfo(new ApiInfoBuilder().version("1.0").title("Gnoc OD API").description("Documentation Gnoc OD API v1.0").build());
//  }
}
