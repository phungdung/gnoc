package com.viettel.gnoc.wo.utils;

import com.viettel.bccs.inventory.service.BaseMessage;
import com.viettel.bccs.inventory.service.InventoryService;
import com.viettel.bccs.inventory.service.StockTotalFullDTO;
import com.viettel.bccs.inventory.service.StockTotalResult;
import com.viettel.bccs.inventory.service.StockTransDetailDTO;
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
public class WSIMInventoryPort {

  InventoryService port = null;

  @Autowired
  WSIMInventoryPortFactory wsimInventoryPortFactory;

  @PostConstruct
  public void init() {
  }

  private void createConnect() throws MalformedURLException, Exception {
    System.out.println("Start create connect im inventory WS ");
    long startTime = Calendar.getInstance().getTimeInMillis();
    port = (InventoryService) wsimInventoryPortFactory.getWsFactory().borrowObject();
    System.out.println(
        "End create connect im inventory WS: " + (Calendar.getInstance().getTimeInMillis()
            - startTime));
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
          wsimInventoryPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;
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
          wsimInventoryPortFactory.getWsFactory().returnObject(port);
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
          wsimInventoryPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;
  }

}
