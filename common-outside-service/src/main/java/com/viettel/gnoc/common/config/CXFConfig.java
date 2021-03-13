package com.viettel.gnoc.common.config;

import com.viettel.gnoc.common.service.CatItemServiceImpl;
import com.viettel.gnoc.common.service.CatLocationServiceImpl;
import com.viettel.gnoc.common.service.CategoryServiceImpl;
import com.viettel.gnoc.common.service.MessagesServiceImpl;
import com.viettel.gnoc.common.service.UnitServiceImpl;
import com.viettel.gnoc.common.service.UsersServiceImpl;
import com.viettel.gnoc.common.service.VSmartWSImpl;
import com.viettel.gnoc.common.service.interceptors.AppInboundInterceptor;
import com.viettel.gnoc.common.service.interceptors.AppOutboundInterceptor;
import com.viettel.gnoc.commons.interceptor.BasicAuthAuthorizationIntercepter;
import javax.xml.ws.Endpoint;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.feature.LoggingFeature;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class CXFConfig {

  @Autowired
  MessagesServiceImpl messagesServiceImpl;

  @Autowired
  BasicAuthAuthorizationIntercepter basicAuthAuthorizationIntercepter;

  @Autowired
  UsersServiceImpl usersService;

  @Autowired
  UnitServiceImpl unitService;

  @Autowired
  CatItemServiceImpl catItemService;

  @Autowired
  CategoryServiceImpl categoryService;

  @Autowired
  CatLocationServiceImpl catLocationService;

  @Autowired
  VSmartWSImpl vSmartWS;

  @Bean
  public ServletRegistrationBean dispatherServlet() {
    return new ServletRegistrationBean(new CXFServlet(), "/GnocCommonOutsideServices/*");
  }

  @Bean(name = Bus.DEFAULT_BUS_ID)
  public SpringBus springBus() {
    SpringBus springBus = new SpringBus();
    springBus.getInInterceptors().add(new AppInboundInterceptor());
    springBus.getInInterceptors().add(basicAuthAuthorizationIntercepter);
    springBus.getOutInterceptors().add(new AppOutboundInterceptor());
    return springBus;
  }

  @Bean
  public Endpoint MessagesServiceEndpoint() {
    EndpointImpl endpoint = new EndpointImpl(springBus(), messagesServiceImpl);
    endpoint.getFeatures().add(new LoggingFeature());
    endpoint.publish("/MessagesService");
    return endpoint;
  }

  @Bean
  public Endpoint UsersServiceEndpoint() {
    EndpointImpl endpoint = new EndpointImpl(springBus(), usersService);
    endpoint.getFeatures().add(new LoggingFeature());
    endpoint.publish("/UsersService");
    return endpoint;
  }

  @Bean
  public Endpoint UnitServiceEndpoint() {
    EndpointImpl endpoint = new EndpointImpl(springBus(), unitService);
    endpoint.getFeatures().add(new LoggingFeature());
    endpoint.publish("/UnitService");
    return endpoint;
  }

  @Bean
  public Endpoint CatItemServiceEndpoint() {
    EndpointImpl endpoint = new EndpointImpl(springBus(), catItemService);
    endpoint.getFeatures().add(new LoggingFeature());
    endpoint.publish("/CatItemService");
    return endpoint;
  }

  @Bean
  public Endpoint CategoryServiceEndpoint() {
    EndpointImpl endpoint = new EndpointImpl(springBus(), categoryService);
    endpoint.getFeatures().add(new LoggingFeature());
    endpoint.publish("/CategoryService");
    return endpoint;
  }

  @Bean
  public Endpoint CatLocationServiceEndpoint() {
    EndpointImpl endpoint = new EndpointImpl(springBus(), catLocationService);
    endpoint.getFeatures().add(new LoggingFeature());
    endpoint.publish("/CatLocationService");
    return endpoint;
  }

  @Bean
  public Endpoint VSmartWSEndpoint() {
    EndpointImpl endpoint = new EndpointImpl(springBus(), vSmartWS);
    endpoint.getFeatures().add(new LoggingFeature());
    endpoint.publish("/VSmartWS");
    return endpoint;
  }
}
