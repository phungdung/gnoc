/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.ws.provider;

import com.viettel.gnoc.commons.config.SpringApplicationContext;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.commons.utils.ws.PassProtector;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrUpdateMopStatusHisDTO;
import com.viettel.gnoc.cr.repository.CrRepository;
import com.viettel.vipa.MopDetailOutputDTO;
import com.viettel.vipa.MopOutputDTO;
import com.viettel.vipa.ProcedureDTO;
import com.viettel.vipa.ResultDTO;
import com.viettel.vipa.WSForGNOC;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WSVipaIpPort {

  WSForGNOC port = null;

  @Value("${application.ws.vipa_userService:null}")
  String userWS;

  @Value("${application.ws.vipa_passService:null}")
  String passWS;

  @Value("${application.ws.vipa_saltService:null}")
  String salt;

  @Autowired
  WSVipaIpPortFactory wsVipaIpPortFactory;


  @PostConstruct
  public void init() {
    try {
      userWS = PassProtector.decrypt(userWS, salt);
      passWS = PassProtector.decrypt(passWS, salt);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  private void createConnect() throws MalformedURLException, Exception {
    port = (WSForGNOC) wsVipaIpPortFactory.getWSFactory().borrowObject();

  }

  public MopOutputDTO getMopByUser(
      String userName, String procedureId, String lstWork) throws Exception {
    MopOutputDTO res = null;
    log.info("--- WSVIPAIP method getMopByUser----");
    try {
      if (port == null) {
        createConnect();
      }

      ProcedureDTO procedureDTO = new ProcedureDTO();
      procedureDTO.setProcedureId(
          StringUtils.isStringNullOrEmpty(procedureId) ? null : Long.parseLong(procedureId));
      if (!StringUtils.isStringNullOrEmpty(lstWork)) {
        List<String> lst = Arrays.asList(lstWork.split(","));
        for (String temp : lst) {
          if (StringUtils.isNotNullOrEmpty(temp)) {
            procedureDTO.getProcedureWorkFlowIds().add(Long.parseLong(temp.trim()));
          }
        }
      }
      if (port != null) {
        res = port.getMopByUserForGNOC(userWS, passWS, userName, procedureDTO);
        StringUtils.printLogData("CALL WS VIPA-IP: RESPONSE getMopByUserForGNOC ", res,
            MopOutputDTO.class);
        if (res.getResultCode() == 0 && (res.getMops() == null || res.getMops().size() == 0)) {
          res = port.getMopByUser(userWS, passWS, userName);
          StringUtils.printLogData("CALL WS VIPA-IP: RESPONSE getMopByUser Ver 2 ", res,
              MopOutputDTO.class);
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      try {
        if (null != port) {
          wsVipaIpPortFactory.getWSFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return res;
  }

  public MopDetailOutputDTO getMopInfo(String mopId) throws Exception {
    MopDetailOutputDTO res = null;
    log.info("-- WSVIPAIP method getMopInfo---");
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {

        res = port.getMopInfo(userWS, passWS, mopId);
        StringUtils.printLogData("RESPONSE", res, MopDetailOutputDTO.class);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      try {
        if (null != port) {
          wsVipaIpPortFactory.getWSFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return res;
  }

  public int updateCrCode(String mopId, String crNumber) throws Exception {
    int res = 1;
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        res = port.updateCrCode(userWS, passWS, mopId, crNumber).getResultCode();
        //res=0 la thanh cong
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      try {
        if (null != port) {
          wsVipaIpPortFactory.getWSFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return res;
  }

  public int updateCrCodeForMops(List<String> mopId, String crNumber) throws Exception {
    int res = 1;
    log.info("--- WSVIPAIP method updateCrCodeForMops: " + crNumber);
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        String strMop = "";
        for (String st : mopId) {
          strMop += st + ",";
        }
        if (strMop.endsWith(",")) {
          strMop = strMop.substring(0, strMop.length() - 1);
        }
        res = port.updateCrCodeForMops(userWS, passWS, strMop, crNumber).getResultCode();
        //res=0 la thanh cong
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      try {
        if (null != port) {
          wsVipaIpPortFactory.getWSFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return res;
  }

  public int updateCrStatus(String crNumber, String status) throws Exception {
    int res = 1;
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        res = port.updateCrStatus(userWS, passWS, crNumber, status).getResultCode();
        //res=0 la thanh cong
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      try {
        if (null != port) {
          wsVipaIpPortFactory.getWSFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return res;
  }

  public int updateCrStatusWithMops(
      String crNumber, String status, List<String> mopId) throws Exception {
    log.info("---- WSVIPAIP method updateCrStatusWithMops: " + crNumber);
    CrUpdateMopStatusHisDTO hisDTO = new CrUpdateMopStatusHisDTO();
    hisDTO.setCrNumber(crNumber);
    hisDTO.setCrStatus(status);

    int res = 1;
    try {
      String strMop = "";
      for (String st : mopId) {
        strMop += st + ",";
      }
      if (strMop.endsWith(",")) {
        strMop = strMop.substring(0, strMop.length() - 1);
      }
      hisDTO.setLstMop(strMop);

      if (port == null) {
        createConnect();
      }
      if (port != null) {

        res = port.updateCrStatusWithMops(userWS, passWS, crNumber, status, strMop).getResultCode();
        //res=0 la thanh cong
        hisDTO.setResult(String.valueOf(res));
        hisDTO.setDetail("Connect to VIPA IP success");
      } else {
        hisDTO.setResult("FAIL");
        hisDTO.setDetail("Can not Connect to VIPA IP ");
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      hisDTO.setDetail(ExceptionUtils.getStackTrace(e));
      throw e;
    } finally {
      try {
        if (null != port) {
          wsVipaIpPortFactory.getWSFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }

      try {
        CrRepository crRepository = SpringApplicationContext.bean(CrRepository.class);
        crRepository.insertMopUpdateHis(hisDTO);
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return res;
  }

  public ResultDTO updateRunAutoStatus(CrDTO crDTOSave, String dtCode, boolean isAuto,
      Long typeConfirmGNOC, Long typeRunGNOC, String crLinkGNOC)
      throws Exception {
    ResultDTO result = null;
    CrUpdateMopStatusHisDTO hisDTO = new CrUpdateMopStatusHisDTO();
    hisDTO.setCrNumber(crDTOSave.getCrNumber());
    hisDTO.setCrStatus("5");
    hisDTO.setLstMop(dtCode);
    log.info("--- CALL: WSVIPAIP method updateRunAutoStatus --- ");
    try {
      if (this.port == null) {
        createConnect();
      }
      if (this.port != null) {
        String crNumber = crDTOSave.getCrNumber();
        if ((crNumber == null) || (crNumber.trim().isEmpty())) {
          result = new ResultDTO();
          result.setResultCode(0);
          result.setResultMessage("CR number is null");
          return result;
        }
        hisDTO.setDetail(
            "typeConfirmGNOC " + typeConfirmGNOC + " typeRunGNOC " + typeRunGNOC + " crLinkGNOC "
                + crLinkGNOC + " runAuto " + (isAuto ? "1" : "0"));
        result = this.port
            .updateRunAutoStatusNew(this.userWS, this.passWS, crNumber.trim(), isAuto,
                typeConfirmGNOC, typeRunGNOC, crLinkGNOC);
        hisDTO.setResult(result.getResultCode() + "");
        StringUtils.printLogData("WS VIPAIP RESPONSE: method updateRunAutoStatusNew", result,
            ResultDTO.class);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      try {
        if (null != this.port) {
          wsVipaIpPortFactory.getWSFactory().returnObject(this.port);
        }
      } catch (Exception localException4) {
        log.error(localException4.getMessage(), localException4);
      }
      try {
        CrRepository crRepository = SpringApplicationContext.bean(CrRepository.class);
        crRepository.insertMopUpdateHis(hisDTO);
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }

    return result;
  }

}
