package com.viettel.vsmart.admin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableAdminServer
@EnableDiscoveryClient
@SpringBootApplication
public class AdminApplication {

  public static void main(String[] args) {
    new SpringApplicationBuilder(AdminApplication.class).run(args);
  }

  @Configuration
  public static class SecurityPermitAllConfig extends WebSecurityConfigurerAdapter {

    @Value("${spring.security.white-list}")
    private String whiteListCidr;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      // For white-list IP range, use following snippet:
      // http.authorizeRequests().anyRequest().hasIpAddress(whiteListCidr);
      http
          .authorizeRequests()
          .anyRequest()
          .permitAll()
          .and()
          .csrf()
          .disable();
    }
  }
}
