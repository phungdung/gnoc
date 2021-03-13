package com.viettel.gnoc.wo.utils;

import com.viettel.webservice.inventory2.global.BaseMessage;
import com.viettel.webservice.inventory2.global.InventoryService;
import com.viettel.webservice.inventory2.global.StockTotalFullDTO;
import com.viettel.webservice.inventory2.global.StockTotalResult;
import com.viettel.webservice.inventory2.global.StockTransDetailDTO;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WSIMInventory_MYT_Port {

  InventoryService port = null;

  @Autowired
  WSIMInventory_MYT_PortFactory wsimInventory_myt_portFactory;

  String wsLink;
  String nationCode;

  @PostConstruct
  public void init() {
  }

  public void setInstance(String wsLink, String nationCode) {
    this.wsLink = wsLink;
    this.nationCode = nationCode;
  }

  private void createConnect() throws MalformedURLException, Exception {
    log.info("Start create connect SpmServiceImpl WS ");
    long startTime = Calendar.getInstance().getTimeInMillis();
    port = (InventoryService) wsimInventory_myt_portFactory.init(wsLink, nationCode).borrowObject();
    log.info(
        "End create connect SpmServiceImpl WS: " + (Calendar.getInstance().getTimeInMillis()
            - startTime));
  }

  public BaseMessage restoreAPDeploymentByIdNo(String cmt, String account, String troubleType,
      List<StockTransDetailDTO> lstMaterial, String transactionId) {
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        BaseMessage res = port
            .restoreAPDeploymentByIdNo(cmt, account, troubleType, lstMaterial, transactionId);
        if (res != null) {
          return res;
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (null != port) {
          wsimInventory_myt_portFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;
  }

  public BaseMessage saveAPDeploymentWithWODatetime(String cmt, String account, String troubleType,
      List<StockTransDetailDTO> lstMaterial, String transactionId, Date date) {
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {

        GregorianCalendar c = new GregorianCalendar();
        c.setTime(date);
        XMLGregorianCalendar date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);

        BaseMessage res = port
            .saveAPDeploymentWithWODatetime(cmt, account, troubleType, lstMaterial, transactionId,
                date2);
        if (res != null) {
          return res;
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (null != port) {
          wsimInventory_myt_portFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;
  }

  public StockTotalResult validateStockTotalByStaffIdNo(String cmt,
      List<StockTotalFullDTO> lstMaterial) {
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        StockTotalResult res = port.validateStockTotalByStaffIdNo(cmt, lstMaterial);
        if (res != null) {
          return res;
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (null != port) {
          wsimInventory_myt_portFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;
  }
}
