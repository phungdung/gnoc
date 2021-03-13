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
import com.viettel.vmsa.ListMopDetailOutputDTO;
import com.viettel.vmsa.MopDetailOutputDTO;
import com.viettel.vmsa.MopGnoc;
import com.viettel.vmsa.MopOutputDTO;
import com.viettel.vmsa.ProcedureDTO;
import com.viettel.vmsa.ResultDTO;
import com.viettel.vmsa.ResultFileValidateDTO;
import com.viettel.vmsa.WSForGNOC;
import java.net.MalformedURLException;
import java.util.ArrayList;
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
public class WSVipaDdPort {

  WSForGNOC port = null;

  @Value("${application.ws.vipa_userService:null}")
  String userWS;

  @Value("${application.ws.vipa_passService:null}")
  String passWS;

  @Value("${application.ws.vipa_saltService:null}")
  String salt;

  @Autowired
  WSVipaDdPortFactory wsVipaDdPortFactory;


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
    port = (WSForGNOC) wsVipaDdPortFactory.getWSFactory().borrowObject();

  }

  public MopOutputDTO getMopByUser(
      String userName, String procedureId, String lstWork) throws Exception {
    log.info("CALL WS VIPADD- VMSA: method getMopByUser");
    MopOutputDTO res = null;
    try {
      if (port == null) {
        createConnect();
      }
      ProcedureDTO procedureDTO = new ProcedureDTO();
      procedureDTO.setProcedureId(Long.parseLong(procedureId));
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
        StringUtils.printLogData("CALL WS VIPADD- VMSA: RESPONSE getMopByUserForGNOC ", res,
            MopOutputDTO.class);
        if (res.getResultCode() == 0 && (res.getMops() == null || res.getMops().isEmpty())) {
          res = port.getMopByUser(userWS, passWS, userName);
          StringUtils.printLogData("CALL WS VIPADD- VMSA: RESPONSE getMopByUser Ver 2 ", res,
              MopOutputDTO.class);
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      try {
        if (null != port) {
          wsVipaDdPortFactory.getWSFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return res;
  }

  public MopDetailOutputDTO getMopInfo(String mopId) throws Exception {
    MopDetailOutputDTO res = null;
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
          wsVipaDdPortFactory.getWSFactory().returnObject(port);
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
          wsVipaDdPortFactory.getWSFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return res;
  }

  public int updateCrCodeForMops(List<String> mopId, String crNumber) throws Exception {
    int res = 1;
    log.info("WSVIPADD method updateCrCodeForMops");
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
          wsVipaDdPortFactory.getWSFactory().returnObject(port);
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
          wsVipaDdPortFactory.getWSFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return res;
  }

  public int updateCrStatusWithMops(
      String crNumber, String status, List<String> mopId) throws Exception {
    log.info("WSVIPADD method updateCrStatusWithMops");
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
        hisDTO.setDetail("Connect to VIPA DD success");
      } else {
        hisDTO.setResult("FAIL");
        hisDTO.setDetail("Can not Connect to VIPA DD ");
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      hisDTO.setDetail(ExceptionUtils.getStackTrace(e));
      throw e;
    } finally {
      try {
        if (null != port) {
          wsVipaDdPortFactory.getWSFactory().returnObject(port);
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

  public ResultFileValidateDTO validateFiles(
      Long crId, String crNumber, String userCreate,
      String businessCode, ResultFileValidateDTO fileValidateDTO) throws Exception {
    ResultFileValidateDTO res = null;
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        res = port.validateFileInput(userWS, passWS, crId, crNumber, userCreate, businessCode,
            fileValidateDTO);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      try {
        if (null != port) {
          wsVipaDdPortFactory.getWSFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return res;
  }

  public ListMopDetailOutputDTO getMOPsByCrNumber(
      String crNumber, Long dtCreateId) throws Exception {
    ListMopDetailOutputDTO res = null;
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        res = port.getMopInfoFromCrNumber(userWS, passWS, crNumber, dtCreateId);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      try {
        if (null != port) {
          wsVipaDdPortFactory.getWSFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return res;
  }

  public MopDetailOutputDTO getMopNotRunnedForCR(List<String> lstMop) throws Exception {
    MopDetailOutputDTO res = new MopDetailOutputDTO();
    StringUtils.printLogData("WS VIPADD: method updateRunAutoStatus", lstMop, ArrayList.class);
    try {

      if (port == null) {
        createConnect();
      }
      if (port != null) {
        res = port.getMopNotRunnedForCR(userWS, passWS, lstMop);
        StringUtils.printLogData("RESPONSE ", res, MopDetailOutputDTO.class);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      try {
        if (null != port) {
          wsVipaDdPortFactory.getWSFactory().returnObject(port);
        }
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
    StringUtils.printLogData("WS VIPADD: method updateRunAutoStatus", crDTOSave, CrDTO.class);
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
        result = this.port.updateRunAutoStatusNew(this.userWS, this.passWS, crNumber.trim(), isAuto,
            typeConfirmGNOC, typeRunGNOC, crLinkGNOC);
        hisDTO.setResult(result.getResultCode() + "");
        StringUtils.printLogData("RESPONSE ", result, ResultDTO.class);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      try {
        if (null != this.port) {
          wsVipaDdPortFactory.getWSFactory().returnObject(this.port);
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

  public MopOutputDTO getMopTestByUser(String userName, String type) throws Exception {
    MopOutputDTO res = null;
    try {
      StringUtils.printLogData("WS VIPADD: method getMopTestByUser", userName, String.class);
      if (port == null) {
        createConnect();
      }
      if (port != null) {

        res = port.getMopByUserAndType(userWS, passWS, userName, type);
        StringUtils.printLogData("RESPONSE ", res, MopOutputDTO.class);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      try {
        if (null != port) {
          wsVipaDdPortFactory.getWSFactory().returnObject(this.port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return res;
  }

  /**
   *
   * @param mopId
   * @param crNumber
   * @return
   * @throws Exception
   */
  public String updateWorkOrder(String crNumber, Long typeOperation, MopGnoc mopGnoc)
      throws Exception {
    String res = "SUCCESS";
    log.info("--- REQUEST METHOD updateWorkOrder--- ");
    log.info("crNumber: " + String.valueOf(crNumber));
    log.info("typeOperation: " + String.valueOf(typeOperation));
    StringUtils.printLogData("MopGnoc", mopGnoc, MopGnoc.class);
    CrUpdateMopStatusHisDTO hisDTO = new CrUpdateMopStatusHisDTO();
    hisDTO.setCrNumber(crNumber);
    hisDTO.setCrStatus("" + typeOperation);

    try {
      if (port == null) {
        createConnect();
      }
      String lst = "";
      for (com.viettel.vmsa.MopGnocDTO gnocDTO : mopGnoc.getMopGnoc()) {
        lst =
            lst + " \n " + "MopId " + gnocDTO.getMopId() + " WOCode " + gnocDTO.getWorkOrderCode();
      }
      lst = lst + " typeOperation " + typeOperation;
      hisDTO.setLstMop(lst);
      if (port != null) {
        ResultDTO result = port.updateWorkOrder(userWS, passWS, crNumber, typeOperation, mopGnoc);
        if (result != null) {
          if (result.getResultCode() == 0) {
            hisDTO.setResult("SUCCESS");
            hisDTO.setDetail("updateWorkOrder SUCCESS");
            return res;
          } else {
            hisDTO.setResult("FAIL");
            hisDTO.setDetail("updateWorkOrder " + result.getResultMessage());
            return result.getResultMessage();
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      try {
        if (null != port) {
          wsVipaDdPortFactory.getWSFactory().returnObject(this.port);
        }
        try {
          CrRepository crRepository = SpringApplicationContext.bean(CrRepository.class);
          crRepository.insertMopUpdateHis(hisDTO);
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return res;
  }
}
