package com.viettel.gnoc.incident.config;

import com.viettel.gnoc.commons.interceptor.BasicAuthAuthorizationIntercepter;
import com.viettel.gnoc.incident.service.CatReasonServiceImpl;
import com.viettel.gnoc.incident.service.CfgTimeTroubleProcessServiceImpl;
import com.viettel.gnoc.incident.service.TroubleMopDtServiceImpl;
import com.viettel.gnoc.incident.service.TroublesServiceForCCImpl;
import com.viettel.gnoc.incident.service.TroublesServiceForVSmartImpl;
import com.viettel.gnoc.incident.service.TroublesServiceImpl;
import com.viettel.gnoc.incident.service.interceptors.AppInboundInterceptor;
import com.viettel.gnoc.incident.service.interceptors.AppOutboundInterceptor;
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
  TroublesServiceImpl troublesService;

  @Autowired
  TroublesServiceForVSmartImpl troublesServiceForVSmart;

  @Autowired
  CatReasonServiceImpl catReasonService;

  @Autowired
  TroubleMopDtServiceImpl troubleMopDtService;

  @Autowired
  TroublesServiceForCCImpl troublesServiceForCC;

  @Autowired
  CfgTimeTroubleProcessServiceImpl cfgTimeTroubleProcessService;

  @Autowired
  BasicAuthAuthorizationIntercepter basicAuthAuthorizationIntercepter;

  @Bean
  public ServletRegistrationBean dispatherServlet() {
    return new ServletRegistrationBean(new CXFServlet(), "/GnocTTOutsideServices/*");
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
  public Endpoint troublesServiceEndpoint() {
    EndpointImpl endpoint = new EndpointImpl(springBus(), troublesService);
    endpoint.getFeatures().add(new LoggingFeature());
    endpoint.publish("/TroublesService");
    return endpoint;
  }

  @Bean
  public Endpoint catReasonServiceEndpoint() {
    EndpointImpl endpoint = new EndpointImpl(springBus(), catReasonService);
    endpoint.getFeatures().add(new LoggingFeature());
    endpoint.publish("/CatReasonService");
    return endpoint;
  }

  @Bean
  public Endpoint troubleMopDtServiceEndpoint() {
    EndpointImpl endpoint = new EndpointImpl(springBus(), troubleMopDtService);
    endpoint.getFeatures().add(new LoggingFeature());
    endpoint.publish("/TroubleMopDtService");
    return endpoint;
  }

  @Bean
  public Endpoint TroublesServiceForCCEndpoint() {
    EndpointImpl endpoint = new EndpointImpl(springBus(), troublesServiceForCC);
    endpoint.getFeatures().add(new LoggingFeature());
    endpoint.publish("/TroublesServiceForCC");
    return endpoint;
  }

  @Bean
  public Endpoint CfgTimeTroubleProcessEndpoint() {
    EndpointImpl endpoint = new EndpointImpl(springBus(), cfgTimeTroubleProcessService);
    endpoint.getFeatures().add(new LoggingFeature());
    endpoint.publish("/CfgTimeTroubleProcessService");
    return endpoint;
  }

  @Bean
  public Endpoint TroublesServiceForVSmartEndpoint() {
    EndpointImpl endpoint = new EndpointImpl(springBus(), troublesServiceForVSmart);
    endpoint.getFeatures().add(new LoggingFeature());
    endpoint.publish("/TroublesServiceForVSmart");
    return endpoint;
  }
}
