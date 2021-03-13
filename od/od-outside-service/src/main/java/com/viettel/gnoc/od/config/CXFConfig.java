package com.viettel.gnoc.od.config;

import com.viettel.gnoc.commons.interceptor.BasicAuthAuthorizationIntercepter;
import com.viettel.gnoc.od.service.OdServiceImpl;
import com.viettel.gnoc.od.service.interceptors.AppInboundInterceptor;
import com.viettel.gnoc.od.service.interceptors.AppOutboundInterceptor;
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
  OdServiceImpl odService;

  @Autowired
  BasicAuthAuthorizationIntercepter basicAuthAuthorizationIntercepter;

  @Bean
  public ServletRegistrationBean dispatherServlet() {
    return new ServletRegistrationBean(new CXFServlet(), "/GnocODOutsideServices/*");
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
  public Endpoint odServiceEndpoint() {
    EndpointImpl endpoint = new EndpointImpl(springBus(), odService);
    endpoint.getFeatures().add(new LoggingFeature());
    endpoint.publish("/OdService");
    return endpoint;
  }
}
