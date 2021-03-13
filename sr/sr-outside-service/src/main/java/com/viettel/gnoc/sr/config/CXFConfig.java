package com.viettel.gnoc.sr.config;

import com.viettel.gnoc.commons.interceptor.BasicAuthAuthorizationIntercepter;
import com.viettel.gnoc.sr.service.SRAddOnServiceImpl;
import com.viettel.gnoc.sr.service.SRAomServiceImpl;
import com.viettel.gnoc.sr.service.SRCatalogServiceImpl;
import com.viettel.gnoc.sr.service.SRNocProServiceImpl;
import com.viettel.gnoc.sr.service.SROutSideServiceImpl;
import com.viettel.gnoc.sr.service.SRVsmartServiceImpl;
import com.viettel.gnoc.sr.service.SRWoTickHelpServiceImpl;
import com.viettel.gnoc.sr.service.interceptors.AppInboundInterceptor;
import com.viettel.gnoc.sr.service.interceptors.AppOutboundInterceptor;
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
  SROutSideServiceImpl srService;

  @Autowired
  SRAddOnServiceImpl srAddOnService;

  @Autowired
  SRAomServiceImpl srAomService;

  @Autowired
  SRNocProServiceImpl srNocProService;

  @Autowired
  SRVsmartServiceImpl srVsmartService;

  @Autowired
  SRWoTickHelpServiceImpl srWoTickHelpService;

  @Autowired
  SRCatalogServiceImpl srCatalogService;

  @Autowired
  BasicAuthAuthorizationIntercepter basicAuthAuthorizationIntercepter;

  @Bean
  public ServletRegistrationBean dispatherServlet() {
    return new ServletRegistrationBean(new CXFServlet(), "/GnocSrOutsideServices/*");
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
  public Endpoint srServiceEndpoint() {
    EndpointImpl endpoint = new EndpointImpl(springBus(), srService);
    endpoint.getFeatures().add(new LoggingFeature());
    endpoint.publish("/SROutSideService");
    return endpoint;
  }

  @Bean
  public Endpoint srAddOnServiceEndpoint() {
    EndpointImpl endpoint = new EndpointImpl(springBus(), srAddOnService);
    endpoint.getFeatures().add(new LoggingFeature());
    endpoint.publish("/SRAddOnService");
    return endpoint;
  }

  @Bean
  public Endpoint srAomServiceEndpoint() {
    EndpointImpl endpoint = new EndpointImpl(springBus(), srAomService);
    endpoint.getFeatures().add(new LoggingFeature());
    endpoint.publish("/SRAomService");
    return endpoint;
  }

  @Bean
  public Endpoint srNocProServiceEndpoint() {
    EndpointImpl endpoint = new EndpointImpl(springBus(), srNocProService);
    endpoint.getFeatures().add(new LoggingFeature());
    endpoint.publish("/SRNocProService");
    return endpoint;
  }

  @Bean
  public Endpoint srVsmartServiceEndpoint() {
    EndpointImpl endpoint = new EndpointImpl(springBus(), srVsmartService);
    endpoint.getFeatures().add(new LoggingFeature());
    endpoint.publish("/SRVsmartService");
    return endpoint;
  }

  @Bean
  public Endpoint srWoTickHelpServiceEndpoint() {
    EndpointImpl endpoint = new EndpointImpl(springBus(), srWoTickHelpService);
    endpoint.getFeatures().add(new LoggingFeature());
    endpoint.publish("/SRWoTickHelpService");
    return endpoint;
  }

  @Bean
  public Endpoint srCatalogServiceEndpoint() {
    EndpointImpl endpoint = new EndpointImpl(springBus(), srCatalogService);
    endpoint.getFeatures().add(new LoggingFeature());
    endpoint.publish("/SRCatalogService");
    return endpoint;
  }
}
