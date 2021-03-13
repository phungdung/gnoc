package com.viettel.gnoc.cr.config;


import com.viettel.gnoc.commons.interceptor.BasicAuthAuthorizationIntercepter;
import com.viettel.gnoc.cr.service.CrAutoServiceImpl;
import com.viettel.gnoc.cr.service.CrAutoServiceImplForSR;
import com.viettel.gnoc.cr.service.CrCableServiceImpl;
import com.viettel.gnoc.cr.service.CrDtServiceImpl;
import com.viettel.gnoc.cr.service.CrForNocProServiceImpl;
import com.viettel.gnoc.cr.service.CrForOtherSystemServiceImpl;
import com.viettel.gnoc.cr.service.CrGeneralServiceForMobileImpl;
import com.viettel.gnoc.cr.service.CrGeneralServiceImpl;
import com.viettel.gnoc.cr.service.CrMobileServiceImpl;
import com.viettel.gnoc.cr.service.CrProcessServiceImpl;
import com.viettel.gnoc.cr.service.CrServiceImpl;
import com.viettel.gnoc.cr.service.interceptors.AppInboundInterceptor;
import com.viettel.gnoc.cr.service.interceptors.AppOutboundInterceptor;
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
  CrForNocProServiceImpl crForNocProService;

  @Autowired
  CrForOtherSystemServiceImpl crForOtherSystemService;

  @Autowired
  CrAutoServiceImpl crAutoService;

  @Autowired
  CrServiceImpl crService;

  @Autowired
  CrGeneralServiceForMobileImpl crGeneralServiceForMobile;

  @Autowired
  CrMobileServiceImpl crMobileService;

  @Autowired
  CrDtServiceImpl crDtService;

  @Autowired
  CrAutoServiceImplForSR crAutoServiceForSR;

  @Autowired
  CrGeneralServiceImpl crGeneralService;

  @Autowired
  CrCableServiceImpl crCableService;

  @Autowired
  CrProcessServiceImpl crProcessService;
//
//  @Autowired
//  CrForMrServiceImpl crServiceForMr;

  @Autowired
  BasicAuthAuthorizationIntercepter basicAuthAuthorizationIntercepter;

  @Bean
  public ServletRegistrationBean dispatherServlet() {
    return new ServletRegistrationBean(new CXFServlet(), "/GnocCrOutsideServices/*");
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
  public Endpoint crForNocProServiceEndpoint() {
    EndpointImpl endpoint = new EndpointImpl(springBus(), crForNocProService);
    endpoint.getFeatures().add(new LoggingFeature());
    endpoint.publish("/CrForNocProService");
    return endpoint;
  }

  @Bean
  public Endpoint CrAutoServiceForSrEndpoint() {
    EndpointImpl endpoint = new EndpointImpl(springBus(), crAutoServiceForSR);
    endpoint.getFeatures().add(new LoggingFeature());
    endpoint.publish("/CrAutoServiceForSR");
    return endpoint;
  }

  @Bean
  public Endpoint crAutoServiceEndpoint() {
    EndpointImpl endpoint = new EndpointImpl(springBus(), crAutoService);
    endpoint.getFeatures().add(new LoggingFeature());
    endpoint.publish("/CrAutoService");
    return endpoint;
  }

  @Bean
  public Endpoint crForOtherSystemServiceEndpoint() {
    EndpointImpl endpoint = new EndpointImpl(springBus(), crForOtherSystemService);
    endpoint.getFeatures().add(new LoggingFeature());
    endpoint.publish("/CrForOtherSystemService");
    return endpoint;
  }

  @Bean
  public Endpoint crServiceEndpoint() {
    EndpointImpl endpoint = new EndpointImpl(springBus(), crService);
    endpoint.getFeatures().add(new LoggingFeature());
    endpoint.publish("/CrService");
    return endpoint;
  }

  @Bean
  public Endpoint crMobileServiceEndpoint() {
    EndpointImpl endpoint = new EndpointImpl(springBus(), crMobileService);
    endpoint.getFeatures().add(new LoggingFeature());
    endpoint.publish("/CrMobileService");
    return endpoint;
  }

  @Bean
  public Endpoint crGeneralServiceForMobileEndpoint() {
    EndpointImpl endpoint = new EndpointImpl(springBus(), crGeneralServiceForMobile);
    endpoint.getFeatures().add(new LoggingFeature());
    endpoint.publish("/CrGeneralServiceForMobile");
    return endpoint;
  }

  // anhlp add
  @Bean
  public Endpoint crDtServiceEndpoint() {
    EndpointImpl endpoint = new EndpointImpl(springBus(), crDtService);
    endpoint.getFeatures().add(new LoggingFeature());
    endpoint.publish("/CrDtService");
    return endpoint;
  }

  // anhlp add
  @Bean
  public Endpoint crGeneralServiceEndpoint() {
    EndpointImpl endpoint = new EndpointImpl(springBus(), crGeneralService);
    endpoint.getFeatures().add(new LoggingFeature());
    endpoint.publish("/CrGeneralService");
    return endpoint;
  }


  // tiennv add
  @Bean
  public Endpoint crCableServiceEndpoint() {
    EndpointImpl endpoint = new EndpointImpl(springBus(), crCableService);
    endpoint.getFeatures().add(new LoggingFeature());
    endpoint.publish("/CrCableService");
    return endpoint;
  }

  // tiennv add
  @Bean
  public Endpoint crCProcessServiceEndpoint() {
    EndpointImpl endpoint = new EndpointImpl(springBus(), crProcessService);
    endpoint.getFeatures().add(new LoggingFeature());
    endpoint.publish("/CrProcessService");
    return endpoint;
  }

}
