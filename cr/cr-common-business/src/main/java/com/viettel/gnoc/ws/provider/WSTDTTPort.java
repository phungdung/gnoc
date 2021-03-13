/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.ws.provider;

import com.viettel.aam.AppGroupResult;
import com.viettel.aam.IpServiceResult;
import com.viettel.aam.LinkCrResult;
import com.viettel.aam.MopFileResult;
import com.viettel.aam.MopResult;
import com.viettel.aam.ProcedureDTO;
import com.viettel.aam.TdttWebservice;
import com.viettel.gnoc.commons.config.SpringApplicationContext;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.commons.utils.ws.PassProtector;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrUpdateMopStatusHisDTO;
import com.viettel.gnoc.cr.repository.CrRepository;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author thanhlv12
 */
@Slf4j
@Service
public class WSTDTTPort {

  TdttWebservice port = null;

  @Value("${application.ws.tdtt_userService:null}")
  String userWS;

  @Value("${application.ws.tdtt_passService:null}")
  String passWS;

  @Value("${application.ws.tdtt_saltService:null}")
  String salt;

  @Autowired
  WSTDTTPortFactory wstdttPortFactory;

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
//        long startTime = Calendar.getInstance().getTimeInMillis();
    port = (TdttWebservice) wstdttPortFactory.getWsFactory().borrowObject();

  }

  public MopResult getListMop(String userName, String procedureId, String lstWork)
      throws Exception {
    MopResult res = null;
    ProcedureDTO procedureDTO = new ProcedureDTO();
    procedureDTO.setProcedureId(
        StringUtils.isStringNullOrEmpty(procedureId) ? null : Long.parseLong(procedureId));
    if (!StringUtils.isStringNullOrEmpty(lstWork)) {
      List<String> lst = Arrays.asList(lstWork.split(","));
      for (String temp : lst) {
        if (temp != null && !"".equals(temp.trim())) {
          procedureDTO.getProcedureWorkFlowIds().add(Long.parseLong(temp.trim()));
        }
      }
    }
    log.info("--- WSTDTT getListMop --- ");
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        res = port.getListMopForGNOC(userWS, passWS, userName, procedureDTO);
        StringUtils.printLogData("WSTDTT getListMopForGNOC", res, MopResult.class);
        if (res.getStatus() != null && res.getStatus() == 1 && (res.getMopInfos() == null || res
            .getMopInfos().isEmpty())) {
          res = port.getListMop(userWS, passWS, userName);
          StringUtils.printLogData("WSTDTT getListMop ", res, MopResult.class);
        }
//        res = port.getListMop(userWS, passWS, userName, crProcess, crType);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      try {
        if (null != port) {
          wstdttPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return res;
  }

  public MopFileResult getMopFile(String mopCode, String crNumber) throws Exception {
    MopFileResult res = null;
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        res = port.getMopFile(userWS, passWS, mopCode, crNumber);
        StringUtils.printLogData("WSTDTT getMopFile", res, MopFileResult.class);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      try {
        if (null != port) {
          wstdttPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return res;
  }

  public LinkCrResult linkCr(String crNumber, String mopCode, String userName,
      String crName, String startTime, String endTime, Long crState) throws Exception {
    LinkCrResult res = null;
    log.info("--------FUNC LINK CR --------- \n");
    log.info(" REQUEST PARAM: ------ \n ");
    log.info(" userName: " + String.valueOf(userName) + "\n");
    log.info(" crNumber: " + String.valueOf(crNumber) + "\n");
    log.info(" crName: " + String.valueOf(crName) + "\n");
    log.info(" startTime: " + String.valueOf(startTime) + "\n");
    log.info(" crState: " + String.valueOf(crState) + "\n");
    log.info(" mopCode: " + String.valueOf(mopCode) + "\n");

    CrUpdateMopStatusHisDTO hisDTO = new CrUpdateMopStatusHisDTO();
    hisDTO.setCrNumber(crNumber);
    hisDTO.setCrStatus(String.valueOf(crState));
    hisDTO.setLstMop(mopCode);

    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {

        res = port.linkCr(userWS, passWS, crNumber, mopCode,
            userName, crName, startTime, endTime, crState);
        StringUtils.printLogData("WSTDTT linkCr", res, LinkCrResult.class);

        hisDTO.setResult(String.valueOf(res.getStatus()));
        hisDTO.setDetail("Connect to AAM success");
      } else {
        hisDTO.setResult("FAIL");
        hisDTO.setDetail("Can not Connect to AAM ");
      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
      hisDTO.setDetail(ExceptionUtils.getStackTrace(e));
      throw e;
    } finally {
      try {
        if (null != port) {
          wstdttPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
      try {
        if (res == null || (res != null && res.getStatus() != null && res.getStatus() != 0)) {
          CrRepository crRepository = SpringApplicationContext.bean(CrRepository.class);
          crRepository.insertMopUpdateHis(hisDTO);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return res;
  }

  public AppGroupResult getListAppGroup() throws Exception {
    AppGroupResult res = null;
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {

        res = port.getListAppGroup(userWS, passWS);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      try {
        if (null != port) {
          wstdttPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return res;
  }

  public IpServiceResult getListIpGroup(long appGroupId) throws Exception {
    IpServiceResult res = null;
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {

        res = port.getListIpGroup(userWS, passWS, appGroupId);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      try {
        if (null != port) {
          wstdttPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return res;
  }

  public LinkCrResult updateRunAutoStatus(CrDTO crDTOSave, String dtCode, boolean isAuto,
      Long typeConfirmGNOC, Long typeRunGNOC, String crLinkGNOC)
      throws Exception {
    LinkCrResult result = null;
    CrUpdateMopStatusHisDTO hisDTO = new CrUpdateMopStatusHisDTO();
    hisDTO.setCrNumber(crDTOSave.getCrNumber());
    hisDTO.setCrStatus("5");
    hisDTO.setLstMop(dtCode);
    log.info("--- WSTDTT updateRunAutoStatusNew ---");
    try {
      if (this.port == null) {
        createConnect();
      }
      if (this.port != null) {
        String crNumber = crDTOSave.getCrNumber();
        if ((crNumber == null) || (crNumber.trim().isEmpty())) {
          result = new LinkCrResult();
          result.setStatus(0);
          result.setMessage("CR number is null");
          return result;
        }
        hisDTO.setDetail(
            "typeConfirmGNOC " + typeConfirmGNOC + " typeRunGNOC " + typeRunGNOC + " crLinkGNOC "
                + crLinkGNOC + " runAuto " + (isAuto ? "1" : "0"));
        result = this.port
            .updateRunAutoStatusNew(this.userWS, this.passWS, crNumber.trim(), dtCode, isAuto,
                typeConfirmGNOC, typeRunGNOC, crLinkGNOC);
        StringUtils.printLogData("RESPONSE", result, LinkCrResult.class);
      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      try {
        if (null != this.port) {
          wstdttPortFactory.getWsFactory().returnObject(this.port);
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
    return result;
  }

  public MopResult getMopInfo(String mopCode) throws Exception {
    MopResult res = null;
    log.info("--- WSTDTT getMopInfo ---");
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        res = port.getMopInfo(userWS, passWS, mopCode);
        StringUtils.printLogData("RESPONSE", res, MopFileResult.class);
      }
    } catch (Exception e) {
      log.info(e.getMessage(), e);
      throw e;
    } finally {
      try {
        if (null != port) {
          wstdttPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return res;
  }
}
