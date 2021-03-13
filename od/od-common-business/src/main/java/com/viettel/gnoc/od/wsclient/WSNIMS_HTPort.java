/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.od.wsclient;

import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.ws.PassProtector;
import com.viettel.nims.webservice.ht.CableInfoForm;
import com.viettel.nims.webservice.ht.InfraWS;
import com.viettel.nims.webservice.ht.InputUpdateStationAuditedStatusForm;
import com.viettel.nims.webservice.ht.JsonResponseBO;
import com.viettel.nims.webservice.ht.ParameterBO;
import com.viettel.nims.webservice.ht.RequestInputBO;
import com.viettel.nims.webservice.ht.ResultDesignForm;
import com.viettel.nims.webservice.ht.ResultGetDepartmentByLocationForm;
import com.viettel.nims.webservice.ht.ResultUpdateStationAuditedStatusForm;
import com.viettel.nims.webservice.ht.UpdateUniqueLaneStatusForm;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author TamdX
 */

@Service
@Slf4j
public class WSNIMS_HTPort {

  InfraWS port = null;

  @Value("${application.ws.userNimsHT}")
  String userNimsHtEncript;
  @Value("${application.ws.passNimsHT}")
  String passNimsHtEncript;

  private String userNimsHt;
  private String passNimsHt;
  @Autowired
  WSNIMS_HTPortFactory wsnims_htPortFactory;

  @PostConstruct
  public void init() {
    try {
      userNimsHt = PassProtector.decrypt(userNimsHtEncript, Constants.WS_USERS.SALT);
      passNimsHt = PassProtector.decrypt(passNimsHtEncript, Constants.WS_USERS.SALT);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }


  private void createConnect() throws MalformedURLException, Exception {
    log.info("Start create connect SpmServiceImpl WS ");
    long startTime = Calendar.getInstance().getTimeInMillis();
    port = (InfraWS) wsnims_htPortFactory.getWsFactory().borrowObject();
    log.info("End create connect SpmServiceImpl WS: " + (Calendar.getInstance().getTimeInMillis()
        - startTime));

  }

  public List<ResultGetDepartmentByLocationForm> getSubscriptionInfo(String locationCode) {
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        List<ResultGetDepartmentByLocationForm> res = port
            .getDepartmentByLocation(userNimsHt, passNimsHt, locationCode);
        return res;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (null != port) {
          wsnims_htPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;

  }

  public List<CableInfoForm> getListCableInfo(Long searchType, String cableCode) throws Exception {
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        ResultDesignForm res = port.getListCableInfo(userNimsHt, passNimsHt, searchType, cableCode);
        if (res != null) {
          return res.getLstCableInfo();
        }
      } else {
        throw new Exception("[NIMS] Error communicate WS NIMS");
      }
    } catch (SocketTimeoutException e) {
      log.error(e.getMessage(), e);
      throw new Exception("[NIMS] Timeout call WS NIMS");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      try {
        if (null != port) {
          wsnims_htPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;

  }


  public ResultUpdateStationAuditedStatusForm updateSubmittingOrderStatus(
      InputUpdateStationAuditedStatusForm input) throws Exception {
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        ResultUpdateStationAuditedStatusForm res = port
            .updateSubmittingOrderStatus(userNimsHt, passNimsHt, input);
        if (res != null) {
          return res;
        }
      } else {
        throw new Exception("[NIMS] Error communicate WS NIMS");
      }
    } catch (SocketTimeoutException e) {
      log.error(e.getMessage(), e);
      throw new Exception("[NIMS] Timeout call WS NIMS");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      try {
        if (null != port) {
          wsnims_htPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;

  }

  public boolean checkCloseOd(String odCode) throws Exception {
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {

        RequestInputBO requestBO = new RequestInputBO();
        requestBO.setCode(Constants.AP_PARAM.CHECK_UNIQUE_LANE_OD);

        ParameterBO param = new ParameterBO();
        param.setName("odCode");
        param.setType("STRING");
        param.setValue(odCode);
        requestBO.getParams().add(param);

        JsonResponseBO res = port.getDataJson(userNimsHt, passNimsHt, requestBO);

        int response = res.getTotalDataJson();
        if (response == 0) {
          return true;
        }
      } else {
        throw new Exception("[NIMS] Error communicate WS NIMS");
      }
    } catch (SocketTimeoutException e) {
      log.error(e.getMessage(), e);
      throw new Exception("[NIMS] Timeout call WS NIMS");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (null != port) {
          wsnims_htPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return false;
  }

  public boolean updateTuyenDocDao(String odCode) throws Exception {
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {

        UpdateUniqueLaneStatusForm form = new UpdateUniqueLaneStatusForm();
        form.setOdCode(odCode);

        UpdateUniqueLaneStatusForm res = port.updateUniqueLaneStatus(userNimsHt, passNimsHt, form);

        if ("OK".equals(res.getResult())) {
          return true;
        }
      } else {
        throw new Exception("[NIMS] Error communicate WS NIMS");
      }
    } catch (SocketTimeoutException e) {
      log.error(e.getMessage(), e);
      throw new Exception("[NIMS] Timeout call WS NIMS");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (null != port) {
          wsnims_htPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return false;
  }

}
