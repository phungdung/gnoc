package com.viettel.gnoc.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author TungPV
 */
@Configuration
@EnableWebSecurity(debug = true)
@Order(1)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  public void configure(WebSecurity web) {
    web.ignoring()
        .antMatchers(HttpMethod.OPTIONS, "/**")
        .antMatchers("/app/**/*.{js,html}")
        .antMatchers("/i18n/**")
        .antMatchers("/content/**")
        .antMatchers("/h2-console/**")
        .antMatchers("/swagger-ui/index.html")
        .antMatchers("/test/**");
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        .authorizeRequests()
        .antMatchers("/swagger-ui.html", "/swagger-resources/**", "/webjars/**", "/v2/api-docs/**")
        .permitAll()
        .antMatchers("/oauthVsa/**").permitAll()
        .anyRequest().permitAll()
//      .anyRequest().authenticated()
        .and()
        .addFilterBefore(new CustomVsaFilter(), UsernamePasswordAuthenticationFilter.class);
  }
}
