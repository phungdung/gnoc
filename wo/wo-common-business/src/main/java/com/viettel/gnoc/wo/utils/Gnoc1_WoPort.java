package com.viettel.gnoc.wo.utils;

import com.viettel.gnoc1.service.ResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.Calendar;
import java.lang.Exception;

@Slf4j
@Service
public class Gnoc1_WoPort {

  com.viettel.gnoc1.service.WoServices port = null;

  @Autowired
  Gnoc1_WoPortFactory gnoc1_WoPortFactory;


  private void createConnect() throws MalformedURLException, Exception {
    System.out.println("Start create connect GNOC1 WS ");
    long startTime = Calendar.getInstance().getTimeInMillis();
    port = (com.viettel.gnoc1.service.WoServices) gnoc1_WoPortFactory.getWsFactory().borrowObject();
    System.out.println(
        "End create connect GNOC1 WS: " + (Calendar.getInstance().getTimeInMillis() - startTime));
  }

  public java.util.List<com.viettel.gnoc1.service.ObjFile> getFileFromWo(java.lang.String woId, java.util.List<java.lang.String> lstFileName) throws Exception {

    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        java.util.List<com.viettel.gnoc1.service.ObjFile> res = port
            .getFileFromWo(woId, lstFileName);
        if (res != null) {
          return res;
        }
      } else {
        throw new Exception("[GNOC1] Error communicate WS GNOC1");
      }
    } catch (SocketTimeoutException e) {
      log.error(e.getMessage(), e);
      throw new Exception("[GNO1] Timeout call WS");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (null != port) {
          gnoc1_WoPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;
  }

  public ResultDTO updateFileForWo(com.viettel.gnoc1.service.WoDTO woDTO) throws Exception {
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        ResultDTO res = port
            .updateFileForWo(woDTO);
        if (res != null) {
          return res;
        }
      } else {
        throw new Exception("[GNOC1] Error communicate WS GNOC1");
      }
    } catch (SocketTimeoutException e) {
      log.error(e.getMessage(), e);
      throw new Exception("[GNO1] Timeout call WS");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (null != port) {
          gnoc1_WoPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;
  }
}
