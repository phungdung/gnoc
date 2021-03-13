package com.viettel.gnoc.wo.config;

import com.viettel.gnoc.wo.service.VSmartWSImpl;
import com.viettel.gnoc.wo.service.interceptors.AppInboundInterceptor;
import com.viettel.gnoc.wo.service.interceptors.AppOutboundInterceptor;
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
  VSmartWSImpl vSmartWS;

  @Bean
  public ServletRegistrationBean dispatherServlet() {
    return new ServletRegistrationBean(new CXFServlet(), "/GnocVsmartServices/*");
  }

  @Bean(name = Bus.DEFAULT_BUS_ID)
  public SpringBus springBus() {
    SpringBus springBus = new SpringBus();
    springBus.getInInterceptors().add(new AppInboundInterceptor());
    springBus.getOutInterceptors().add(new AppOutboundInterceptor());
    return springBus;
  }

  @Bean
  public Endpoint vSmartWSServiceEndpoint() {
    EndpointImpl endpoint = new EndpointImpl(springBus(), vSmartWS);
    endpoint.getFeatures().add(new LoggingFeature());
    endpoint.publish("/VSmartWS");
    return endpoint;
  }
}
