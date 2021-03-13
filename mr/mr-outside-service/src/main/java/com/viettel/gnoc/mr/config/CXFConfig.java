package com.viettel.gnoc.mr.config;

import com.viettel.gnoc.commons.interceptor.BasicAuthAuthorizationIntercepter;
import com.viettel.gnoc.mr.service.MrDeviceBtsServiceImpl;
import com.viettel.gnoc.mr.service.MrMaterialDisplacementServiceImpl;
import com.viettel.gnoc.mr.service.MrServiceImpl;
import com.viettel.gnoc.mr.service.WorkLogServiceImpl;
import com.viettel.gnoc.mr.service.interceptors.AppInboundInterceptor;
import com.viettel.gnoc.mr.service.interceptors.AppOutboundInterceptor;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.feature.LoggingFeature;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;


@Configuration
public class CXFConfig {

  /*@Autowired
  MrServiceImpl mrService;*/

  @Autowired
  BasicAuthAuthorizationIntercepter basicAuthAuthorizationIntercepter;
@Autowired
MrDeviceBtsServiceImpl mrDeviceBtsService;

  @Autowired
  MrMaterialDisplacementServiceImpl mrMaterialDisplacementService;

  @Autowired
  MrServiceImpl mrService;

  @Autowired
  WorkLogServiceImpl workLogService;

  @Bean
  public ServletRegistrationBean dispatherServlet() {
    return new ServletRegistrationBean(new CXFServlet(), "/GnocMrOutsideServices/*");
  }

  @Bean(name = Bus.DEFAULT_BUS_ID)
  public SpringBus springBus() {
    SpringBus springBus = new SpringBus();
    springBus.getInInterceptors().add(new AppInboundInterceptor());
    springBus.getInInterceptors().add(basicAuthAuthorizationIntercepter);
    springBus.getOutInterceptors().add(new AppOutboundInterceptor());
    return springBus;
  }

  /*@Bean
  public Endpoint odServiceEndpoint() {
    EndpointImpl endpoint = new EndpointImpl(springBus(), odService);
    endpoint.getFeatures().add(new LoggingFeature());
    endpoint.publish("/MrService");
    return endpoint;
  }*/

  @Bean
  public Endpoint MrDeviceBtsServiceEndpoint() {
    EndpointImpl endpoint = new EndpointImpl(springBus(), mrDeviceBtsService);
    endpoint.getFeatures().add(new LoggingFeature());
    endpoint.publish("/MrDeviceBtsService");
    return endpoint;
  }

  @Bean
  public Endpoint MrMaterialDisplacementServiceEndpoint() {
    EndpointImpl endpoint = new EndpointImpl(springBus(), mrMaterialDisplacementService);
    endpoint.getFeatures().add(new LoggingFeature());
    endpoint.publish("/MrMaterialDisplacementService");
    return endpoint;
  }

  @Bean
  public Endpoint mrServiceEndpoint() {
    EndpointImpl endpoint = new EndpointImpl(springBus(), mrService);
    endpoint.getFeatures().add(new LoggingFeature());
    endpoint.publish("/MrService");
    return endpoint;
  }

  @Bean
  public Endpoint workLogServiceEndpoint(){
    EndpointImpl endpoint = new EndpointImpl(springBus(), workLogService);
    endpoint.getFeatures().add(new LoggingFeature());
    endpoint.publish("/WorkLogService");
    return endpoint;
  }
}
