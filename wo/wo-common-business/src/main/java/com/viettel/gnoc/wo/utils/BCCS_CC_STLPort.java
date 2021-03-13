package com.viettel.gnoc.wo.utils;

import com.viettel.webservice.cc_stl.Input;
import com.viettel.webservice.cc_stl.Output;
import com.viettel.webservice.cc_stl.ServiceManagement;
import java.net.MalformedURLException;
import java.util.Calendar;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BCCS_CC_STLPort {

  ServiceManagement port = null;

  @Autowired
  BCCS_CC_STLPortFactory bccs_cc_stlPortFactory;

  @PostConstruct
  public void init() {
  }

  private void createConnect() throws MalformedURLException, Exception {
    System.out.println("Start create connect CC_STL WS ");
    long startTime = Calendar.getInstance().getTimeInMillis();
    port = (ServiceManagement) bccs_cc_stlPortFactory.getWsFactory().borrowObject();
    System.out.println(
        "End create connect CC_STL WS: " + (Calendar.getInstance().getTimeInMillis() - startTime));
  }

  public Output updateComplain(Input input) {
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        Output res = port.gwOperation(input);
        if (res != null) {
          return res;
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (null != port) {
          bccs_cc_stlPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;
  }

}
