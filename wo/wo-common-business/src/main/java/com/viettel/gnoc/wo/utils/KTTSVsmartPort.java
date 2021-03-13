package com.viettel.gnoc.wo.utils;

import com.viettel.gnoc.commons.utils.Constants.WS_USERS;
import com.viettel.gnoc.commons.utils.ws.PassProtector;
import com.viettel.qldtktts.service2.CertificateRegistrationBO;
import com.viettel.qldtktts.service2.EntityBO;
import com.viettel.qldtktts.service2.Kttsbo;
import com.viettel.qldtktts.service2.MerEntityBO;
import com.viettel.qldtktts.service2.MerchandiseBO;
import com.viettel.qldtktts.service2.MerchandiseOrderBO;
import com.viettel.qldtktts.service2.VSmartService;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KTTSVsmartPort {

  VSmartService port = null;
  @Value("${application.ws.userKtts2:null}")
  private String user;
  @Value("${application.ws.passKtts2:null}")
  private String pass;

  @Autowired
  KTTSVsmartPortFactory kttsVsmartPortFactory;

  @PostConstruct
  public void init() {
    try {
      user = PassProtector.decrypt(user, WS_USERS.SALT);
      pass = PassProtector.decrypt(pass, WS_USERS.SALT);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  private void createConnect() throws MalformedURLException, Exception {
    System.out.println("Start create connect KTTS WS ");
    long startTime = Calendar.getInstance().getTimeInMillis();
    port = (VSmartService) kttsVsmartPortFactory.getWsFactory().borrowObject();
    System.out.println(
        "End create connect KTTS WS: " + (Calendar.getInstance().getTimeInMillis() - startTime));
  }

  public Kttsbo confirmAssetMoveDeliveryNation(Long woId, List<EntityBO> list, String employeeCode,
      String employeePosition, String nation) throws Exception {
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        Kttsbo res = port
            .confirmAssetMoveDeliveryNation(woId, list, employeeCode, employeePosition, user, pass,
                nation);
        if (res != null) {
          return res;
        }
      } else {
        throw new Exception("[KTTS] Error communicate WS KTTS");
      }
    } catch (SocketTimeoutException e) {
      log.error(e.getMessage(), e);
      throw new Exception("[KTTS] Timeout call WS KTTS");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (null != port) {
          kttsVsmartPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;
  }

  public Kttsbo rejectAssetMoveConfirmNation(Long woId, Long type, String cause, String nation)
      throws Exception {
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        Kttsbo res = port.rejectAssetMoveConfirmNation(woId, type, cause, user, pass, nation);
        if (res != null) {
          return res;
        }
      } else {
        throw new Exception("[KTTS] Error communicate WS KTTS");
      }
    } catch (SocketTimeoutException e) {
      log.error(e.getMessage(), e);
      throw new Exception("[KTTS] Timeout call WS KTTS");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (null != port) {
          kttsVsmartPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;
  }

  public Kttsbo confirmAssetMoveRecvNation(Long woIdUp, String employeeCode, String nation)
      throws Exception {
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        Kttsbo res = port.confirmAssetMoveRecvNation(woIdUp, employeeCode, user, pass, nation);
        if (res != null) {
          return res;
        }
      } else {
        throw new Exception("[KTTS] Error communicate WS KTTS");
      }
    } catch (SocketTimeoutException e) {
      log.error(e.getMessage(), e);
      throw new Exception("[KTTS] Timeout call WS KTTS");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (null != port) {
          kttsVsmartPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;
  }

  public Kttsbo createImpReqNation(String warehouseCode, String stationCode, String employeeCode,
      List<MerEntityBO> lstMerBO, CertificateRegistrationBO cerBO, String nation) throws Exception {
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        Kttsbo res = port
            .createImpReqNation(user, pass, warehouseCode, stationCode, employeeCode, lstMerBO,
                cerBO, nation);
        if (res != null) {
          return res;
        }
      } else {
        throw new Exception("[KTTS] Error communicate WS KTTS");
      }
    } catch (SocketTimeoutException e) {
      log.error(e.getMessage(), e);
      throw new Exception("[KTTS] Timeout call WS KTTS");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (null != port) {
          kttsVsmartPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;
  }

  public Kttsbo updateAcceptanceUCTTNation(Long woId, String handoverUser, String receiveUser,
      String reason, List<MerchandiseOrderBO> list, Long scriptId, String nation) throws Exception {
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        Kttsbo res = port
            .updateAcceptanceUCTTNation(user, pass, woId, handoverUser, receiveUser, reason, list,
                scriptId, nation);
        if (res != null) {
          return res;
        }
      } else {
        throw new Exception("[KTTS] Error communicate WS KTTS");
      }
    } catch (SocketTimeoutException e) {
      log.error(e.getMessage(), e);
      throw new Exception("[KTTS] Timeout call WS KTTS");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (null != port) {
          kttsVsmartPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;
  }

  public Kttsbo checkListMerEntityNation(List<MerchandiseBO> lstMer, String stationCode,
      String nation) {
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        Kttsbo res = port.checkListMerEntityNation(lstMer, stationCode, user, pass, nation);
        if (res != null) {
          return res;
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (null != port) {
          kttsVsmartPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;
  }

  public Kttsbo checkMerchandiseCodeNation(String lstCode, String nation) {
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        Kttsbo res = port.checkMerchandiseCodeNation(user, pass, lstCode, nation);
        if (res != null) {
          return res;
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (null != port) {
          kttsVsmartPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;
  }

  public Kttsbo checkOffStationNation(String stationCode, Long type, String nation)
      throws Exception {
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        Kttsbo res = port.checkOffStationNation(user, pass, type, stationCode, nation);
        if (res != null) {
          return res;
        }
      } else {
        throw new Exception("[KTTS] Error communicate WS KTTS");
      }
    } catch (SocketTimeoutException e) {
      log.error(e.getMessage(), e);
      throw new Exception("[KTTS] Timeout call WS KTTS");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (null != port) {
          kttsVsmartPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;
  }

  public Kttsbo checkWorkOrderNation(Long woId, String nation) throws Exception {
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        Kttsbo res = port.checkWorkOrderNation(woId, user, pass, nation);
        if (res != null) {
          return res;
        }
      } else {
        throw new Exception("[KTTS] Error communicate WS KTTS");
      }
    } catch (SocketTimeoutException e) {
      log.error(e.getMessage(), e);
      throw new Exception("[KTTS] Timeout call WS KTTS");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (null != port) {
          kttsVsmartPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;
  }

  public Kttsbo createAssetLostNation(List<EntityBO> list, CertificateRegistrationBO crbo,
      String nationCode) throws Exception {
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        Kttsbo res = port.createAssetLostNation(list, crbo, user, pass, nationCode);
        if (res != null) {
          return res;
        }
      } else {
        throw new Exception("[KTTS] Error communicate WS KTTS");
      }
    } catch (SocketTimeoutException e) {
      log.error(e.getMessage(), e);
      throw new Exception("[KTTS] Timeout call WS KTTS");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (null != port) {
          kttsVsmartPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;
  }

  public Kttsbo createAssetMoveCmdNation(CertificateRegistrationBO woUp,
      CertificateRegistrationBO woDown,
      List<MerchandiseBO> lstMerBO, String employeeCode, String nationCode) throws Exception {
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        Kttsbo res = port
            .createAssetMoveCmdNation(woDown, woUp, lstMerBO, employeeCode, user, pass, nationCode);
        if (res != null) {
          return res;
        }
      } else {
        throw new Exception("[KTTS] Error communicate WS KTTS");
      }
    } catch (SocketTimeoutException e) {
      log.error(e.getMessage(), e);
      throw new Exception("[KTTS] Timeout call WS KTTS");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (null != port) {
          kttsVsmartPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;
  }

  public Kttsbo createOrderExportNation(String warehouseCode, String employeeCode,
      String stationCode, String constructionCode, List<MerchandiseOrderBO> list,
      CertificateRegistrationBO crbo, String nation) throws Exception {
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        Kttsbo res = port
            .createOrderExportNation(user, pass, warehouseCode, employeeCode, stationCode,
                constructionCode, list, crbo, nation);
        if (res != null) {
          return res;
        }
      } else {
        throw new Exception("[KTTS] Error communicate WS KTTS");
      }
    } catch (SocketTimeoutException e) {
      log.error(e.getMessage(), e);
      throw new Exception("[KTTS] Timeout call WS KTTS");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (null != port) {
          kttsVsmartPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;
  }

  public Kttsbo getStationListNation(String stationCode, String fromDate, String nation) {
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        Kttsbo res = port.getStationListNation(stationCode, fromDate, user, pass, nation);
        if (res != null) {
          return res;
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (null != port) {
          kttsVsmartPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;
  }

  public Kttsbo getListWarehouseNation(String warehouseCode, String warehouseName, String woType,
      String staffCode, String nation) {
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        Kttsbo res = port
            .getListWarehouseNation(user, pass, warehouseCode, warehouseName, woType, staffCode,
                nation);
        if (res != null) {
          return res;
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (null != port) {
          kttsVsmartPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;
  }

  public Kttsbo getListContractFromConstrNation(String constrtCode, String nation) {
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        Kttsbo res = port.getListContractFromConstrNation(constrtCode, user, pass, nation);
        if (res != null) {
          return res;
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (null != port) {
          kttsVsmartPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;
  }

  public Kttsbo getConstructionListNation(String stationCode, String nation) {
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        Kttsbo res = port.getConstructionListNation(stationCode, user, pass, nation);
        if (res != null) {
          return res;
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (null != port) {
          kttsVsmartPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;
  }

  public Kttsbo updateWareExpNoteNation(Long workOrderId, Long status, String reason,
      Long isDestroy, String nation) throws Exception {
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        Kttsbo res = port
            .updateWareExpNoteNation(user, pass, workOrderId, status.intValue(), reason, isDestroy,
                nation);
        if (res != null) {
          return res;
        }
      } else {
        throw new Exception("[KTTS] Error communicate WS KTTS");
      }
    } catch (SocketTimeoutException e) {
      log.error(e.getMessage(), e);
      throw new Exception("[KTTS] Timeout call WS KTTS");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (null != port) {
          kttsVsmartPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;
  }
}
