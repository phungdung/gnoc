package com.viettel.gnoc.wo.config;


import com.viettel.gnoc.commons.interceptor.BasicAuthAuthorizationIntercepter;
import com.viettel.gnoc.od.service.interceptors.AppInboundInterceptor;
import com.viettel.gnoc.od.service.interceptors.AppOutboundInterceptor;
import com.viettel.gnoc.wo.service.WoCdGroupServiceImpl;
import com.viettel.gnoc.wo.service.WoServicesImpl;
import com.viettel.gnoc.wo.service.WoTypeServiceImpl;
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
  WoServicesImpl woServices;

  @Autowired
  WoCdGroupServiceImpl woCdGroupService;

  @Autowired
  WoTypeServiceImpl woTypeService;

  @Autowired
  BasicAuthAuthorizationIntercepter basicAuthAuthorizationIntercepter;


  @Bean
  public ServletRegistrationBean dispatherServlet() {
    return new ServletRegistrationBean(new CXFServlet(), "/GnocWoOutsideServices/*");
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
  public Endpoint woServicesEndpoint() {
    EndpointImpl endpoint = new EndpointImpl(springBus(), woServices);
    endpoint.getFeatures().add(new LoggingFeature());
    endpoint.publish("/WoServices");
    return endpoint;
  }

  @Bean
  public Endpoint woCdGroupServiceEndpoint() {
    EndpointImpl endpoint = new EndpointImpl(springBus(), woCdGroupService);
    endpoint.getFeatures().add(new LoggingFeature());
    endpoint.publish("/WoCdGroupService");
    return endpoint;
  }

  @Bean
  public Endpoint woTypeServiceEndpoint() {
    EndpointImpl endpoint = new EndpointImpl(springBus(), woTypeService);
    endpoint.getFeatures().add(new LoggingFeature());
    endpoint.publish("/WoTypeService");
    return endpoint;
  }
}
