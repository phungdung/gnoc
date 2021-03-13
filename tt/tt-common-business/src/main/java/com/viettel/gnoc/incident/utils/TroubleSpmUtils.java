
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.incident.utils;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.incident.dto.TroublesDTO;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;
import com.viettel.gnoc.incident.model.TroublesEntity;
import com.viettel.soc.spm.service.ResultDTO;
import com.viettel.soc.spm.service.ServiceProblemInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TroubleSpmUtils {

  @Autowired
  WSSPMPort wsspmPort;

  public ResultDTO updateSpmCompleteNotCheck(
      TroublesInSideDTO tForm, TroublesEntity trouble) throws Exception {
    ResultDTO res = null;
    try {
      //WSSPMPort port = new WSSPMPort();

      ServiceProblemInfoDTO spmDto = new ServiceProblemInfoDTO();
      spmDto.setInsertSource("TT");
      spmDto.setCode(trouble.getTroubleCode());
      spmDto.setSpmCode(trouble.getSpmCode());
      spmDto.setReasonDetail(trouble.getRootCause());
      spmDto.setUnitNameExecute(tForm.getProcessingUnitName());
      spmDto.setUnitIdExecute(tForm.getProcessingUnitId());
      spmDto.setUserNameExecute(tForm.getProcessingUserName());
      spmDto.setContactPhone(tForm.getProcessingUserPhone());
      //nguyen nhan 3 cap
      spmDto.setReasonL1Id(trouble.getReasonLv1Id());
      spmDto.setReasonL1Name(trouble.getReasonLv1Name());
      spmDto.setReasonL2Id(trouble.getReasonLv2Id());
      spmDto.setReasonL2Name(trouble.getReasonLv2Name());
      spmDto.setReasonL3Id(trouble.getReasonLv3Id());
      spmDto.setReasonL3Name(trouble.getReasonLv3Name());

      spmDto.setStatusId(trouble.getState() != null ? trouble.getState().toString() : null);
      //nguyen nhan qua han
      spmDto.setReasonOverdueL1(trouble.getReasonOverdueId());
      spmDto.setReasonOverdueL2(trouble.getReasonOverdueId2());
      spmDto.setReasonOverdueNameL1(trouble.getReasonOverdueName());
      spmDto.setReasonOverdueNameL2(trouble.getReasonOverdueName2());
      spmDto.setSolution(trouble.getWorkArround());

      if ("MOBILE".equals(tForm.getSpmName())) {
        spmDto.setTotalPendingTimeGnoc(tForm.getTotalPendingTimeGnoc());
        spmDto.setTotalProcessTimeGnoc(tForm.getTotalProcessTimeGnoc());
        spmDto.setEvaluateGnoc(tForm.getEvaluateGnoc());
        spmDto.setStatusName(tForm.getStateWo());
      } else {
        spmDto.setTotalPendingTimeGnoc("0");
        spmDto.setTotalProcessTimeGnoc(
            tForm.getTimeUsed() == null ? null : tForm.getTimeUsed().toString());
        if (tForm.getRemainTime() != null && Double.parseDouble(tForm.getRemainTime()) < 0) {
          spmDto.setEvaluateGnoc("0");
        } else {
          spmDto.setEvaluateGnoc("1");
        }

        spmDto.setStatusName(Constants.TT_STATE.CLOSED);
      }
      spmDto.setIsCheck(tForm.getIsCheck());
      spmDto.setFtInfo(tForm.getFtInfo());
      if (tForm.getFileDocumentByteArray() != null && tForm.getArrFileName() != null) {
        System.out.println("tForm.getArrFileName()" + tForm.getArrFileName());
        spmDto.getFileDocumentByteArray().addAll(tForm.getFileDocumentByteArray());
        spmDto.getFileDocumentName().addAll(tForm.getArrFileName());
      }

      res = wsspmPort.updateSpmCompleteNotCheck(spmDto);
      if (res.getKey() != null && "FAIL".equals(res.getKey())) {
        throw new Exception(I18n.getLanguage("incident.complete.to.spm") + ": " + res.getMessage());
      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }

    return res;
  }

  public ResultDTO updateSpmAction(TroublesInSideDTO tForm, TroublesEntity trouble, String itemName)
      throws Exception {
    ResultDTO res = null;
    try {

      //WSSPMPort port = new WSSPMPort();
      ServiceProblemInfoDTO spmDto = new ServiceProblemInfoDTO();
      if ("7".equals(tForm.getStateWo())) {
        spmDto.setActionType("2");  //2: tam dong
        spmDto.setPendingTime(DateUtil.date2ddMMyyyyHHMMss(trouble.getDeferredTime()));
        spmDto.setPendingReason(trouble.getDeferredReason());
        spmDto.setContact(tForm.getProcessingUserName());
      } else if ("5".equals(tForm.getStateWo())) {
        spmDto.setActionType("3");  //3: mo tam dong
      } else {
        spmDto.setActionType("1");  //1: cap nhat
      }

      spmDto.setSpmCode(trouble.getSpmCode()); //SPM code.OK
      spmDto.setReasonDetail(trouble.getRootCause());
      //nguyen nhan 3 cap
      spmDto.setReasonL1Id(trouble.getReasonLv1Id());
      spmDto.setReasonL1Name(trouble.getReasonLv1Name());
      spmDto.setReasonL2Id(trouble.getReasonLv2Id());
      spmDto.setReasonL2Name(trouble.getReasonLv2Name());
      spmDto.setReasonL3Id(trouble.getReasonLv3Id());
      spmDto.setReasonL3Name(trouble.getReasonLv3Name());
      //nguyen nhan qua han
      spmDto.setReasonOverdueL1(trouble.getReasonOverdueId());
      spmDto.setReasonOverdueL2(trouble.getReasonOverdueId2());
      spmDto.setReasonOverdueNameL1(trouble.getReasonOverdueName());
      spmDto.setReasonOverdueNameL2(trouble.getReasonOverdueName2());
      spmDto.setSolution(trouble.getWorkArround());
      spmDto.setStatusId(trouble.getState() == null ? null : trouble.getState().toString());
      spmDto.setCode(trouble.getTroubleCode());
      spmDto.setSystemCode(trouble.getTroubleCode());
      spmDto.setSystemId("INCIDENT");
      spmDto.setInsertSource("TT"); //OK

      spmDto.setUnitNameExecute(tForm.getProcessingUnitName());
      spmDto.setUnitIdExecute(tForm.getProcessingUnitId());
      spmDto.setUserNameExecute(tForm.getProcessingUserName());
      spmDto.setContactPhone(tForm.getProcessingUserPhone());
      spmDto.setIsCheck(tForm.getIsCheck());
      //
      if (tForm.getFileDocumentByteArray() != null && tForm.getArrFileName() != null) {
        System.out.println("tForm.getArrFileName()" + tForm.getArrFileName());
        spmDto.getFileDocumentByteArray().addAll(tForm.getFileDocumentByteArray());
        spmDto.getFileDocumentName().addAll(tForm.getArrFileName());
      }

      String strProcessContent = I18n.getLanguage("problem.update") + " " + itemName;
      if (tForm.getWorkLog() != null && !"".equals(tForm.getWorkLog())) {
        strProcessContent = strProcessContent + " " + tForm.getWorkLog();
      }
      //
      if (trouble.getNumPending() != null) {
        spmDto.setPendingNumber(trouble.getNumPending().toString());
      }
      if ("MOBILE".equals(tForm.getSpmName())) {
        spmDto.setTotalPendingTimeGnoc(tForm.getTotalPendingTimeGnoc());
        spmDto.setTotalProcessTimeGnoc(tForm.getTotalProcessTimeGnoc());
        spmDto.setEvaluateGnoc(tForm.getEvaluateGnoc());
        spmDto.setStatusName(tForm.getStateWo());
        spmDto.setProcess(strProcessContent);

      } else {
        spmDto.setTotalPendingTimeGnoc("0");
        spmDto.setTotalProcessTimeGnoc(
            tForm.getTimeUsed() == null ? null : tForm.getTimeUsed().toString());
        if (tForm.getRemainTime() != null && Double.parseDouble(tForm.getRemainTime()) < 0) {
          spmDto.setEvaluateGnoc("0");
        } else {
          spmDto.setEvaluateGnoc("1");
        }
        spmDto.setStatusName(itemName);
        spmDto.setProcess(strProcessContent);
      }
      spmDto.setFtInfo(tForm.getFtInfo());

      res = wsspmPort.updateSpmAction(spmDto);
      if (res.getKey() != null && "FAIL".equals(res.getKey())) {
        throw new Exception("[INCIDENT] Completed call spm fail: " + res.getMessage());
      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }
    return res;
  }

  public ResultDTO updateSpmCompleteNotCheckFromWo(TroublesDTO tForm) throws Exception {
    ResultDTO res = null;
    try {
      //WSSPMPort port = new WSSPMPort();

      ServiceProblemInfoDTO spmDto = new ServiceProblemInfoDTO();
      spmDto.setInsertSource("TT");
      spmDto.setCode(tForm.getTroubleCode());
      spmDto.setSpmCode(tForm.getSpmCode());
      spmDto.setReasonDetail(tForm.getRootCause());
      spmDto.setUnitNameExecute(tForm.getProcessingUnitName());
      spmDto.setUnitIdExecute(tForm.getProcessingUnitId());
      spmDto.setUserNameExecute(tForm.getProcessingUserName());
      spmDto.setContactPhone(tForm.getProcessingUserPhone());
      //nguyen nhan 3 cap
      spmDto.setReasonL1Id(tForm.getReasonLv1Id());
      spmDto.setReasonL1Name(tForm.getReasonLv1Name());
      spmDto.setReasonL2Id(tForm.getReasonLv2Id());
      spmDto.setReasonL2Name(tForm.getReasonLv2Name());
      spmDto.setReasonL3Id(tForm.getReasonLv3Id());
      spmDto.setReasonL3Name(tForm.getReasonLv3Name());
      spmDto.setStatusId(tForm.getState());
      //nguyen nhan qua han
      spmDto.setReasonOverdueL1(tForm.getReasonOverdueId());
      spmDto.setReasonOverdueL2(tForm.getReasonOverdueId2());
      spmDto.setReasonOverdueNameL1(tForm.getReasonOverdueName());
      spmDto.setReasonOverdueNameL2(tForm.getReasonOverdueName2());
      spmDto.setSolution(tForm.getWorkArround());
      spmDto.setIsCheck(tForm.getIsCheck());
      spmDto.setFtInfo(tForm.getFtInfo());

      //
      if (tForm.getFileDocumentByteArray() != null && tForm.getArrFileName() != null) {
        System.out.println("tForm.getArrFileName()" + tForm.getArrFileName());
        spmDto.getFileDocumentByteArray().addAll(tForm.getFileDocumentByteArray());
        spmDto.getFileDocumentName().addAll(tForm.getArrFileName());
      }
      spmDto.setTotalPendingTimeGnoc(tForm.getTotalPendingTimeGnoc());
      spmDto.setTotalProcessTimeGnoc(tForm.getTotalProcessTimeGnoc());
      spmDto.setEvaluateGnoc(tForm.getEvaluateGnoc());
      spmDto.setStatusName(tForm.getStateWo());
      StringUtils
          .printLogData("updateSpmCompleteNotCheckFromWo", spmDto, ServiceProblemInfoDTO.class);
      res = wsspmPort.updateSpmCompleteNotCheck(spmDto);
      if (res.getKey() != null && "FAIL".equals(res.getKey())) {
        throw new Exception(I18n.getLanguage("incident.complete.to.spm") + ": " + res.getMessage());
      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }

    return res;
  }

  public ResultDTO updateSpmActionFromWo(TroublesDTO tForm) throws Exception {
    ResultDTO res = null;
    try {

      //WSSPMPort port = new WSSPMPort();
      ServiceProblemInfoDTO spmDto = new ServiceProblemInfoDTO();
      if ("7".equals(tForm.getState())) {
        spmDto.setActionType("2");  //2: tam dong
        spmDto.setPendingTime(tForm.getDeferredTime());
        spmDto.setPendingReason(tForm.getDeferredReason());
        spmDto.setContact(tForm.getProcessingUserName());
      } else if ("5".equals(tForm.getState())) {
        spmDto.setActionType("3");  //3: mo tam dong
      } else {
        spmDto.setActionType("1");  //1: cap nhat
      }
      spmDto.setSpmCode(tForm.getSpmCode()); //SPM code.OK
      spmDto.setReasonDetail(tForm.getRootCause());
      //nguyen nhan 3 cap
      spmDto.setReasonL1Id(tForm.getReasonLv1Id());
      spmDto.setReasonL1Name(tForm.getReasonLv1Name());
      spmDto.setReasonL2Id(tForm.getReasonLv2Id());
      spmDto.setReasonL2Name(tForm.getReasonLv2Name());
      spmDto.setReasonL3Id(tForm.getReasonLv3Id());
      spmDto.setReasonL3Name(tForm.getReasonLv3Name());
      //nguyen nhan qua han
      spmDto.setReasonOverdueL1(tForm.getReasonOverdueId());
      spmDto.setReasonOverdueL2(tForm.getReasonOverdueId2());
      spmDto.setReasonOverdueNameL1(tForm.getReasonOverdueName());
      spmDto.setReasonOverdueNameL2(tForm.getReasonOverdueName2());
      spmDto.setSolution(tForm.getWorkArround());
      spmDto.setStatusId(tForm.getState() != null ? String.valueOf(tForm.getState()) : null);
      spmDto.setCode(tForm.getTroubleCode());
      spmDto.setSystemCode(tForm.getTroubleCode());
      spmDto.setSystemId("WFM");
      spmDto.setInsertSource("WFM"); //OK

      spmDto.setUnitNameExecute(tForm.getProcessingUnitName());
      spmDto.setUnitIdExecute(tForm.getProcessingUnitId());
      spmDto.setUserNameExecute(tForm.getProcessingUserName());
      spmDto.setContactPhone(tForm.getProcessingUserPhone());
      spmDto.setIsCheck(tForm.getIsCheck());
      //
      if (tForm.getFileDocumentByteArray() != null && tForm.getArrFileName() != null) {
        System.out.println("tForm.getArrFileName()" + tForm.getArrFileName());
        spmDto.getFileDocumentByteArray().addAll(tForm.getFileDocumentByteArray());
        spmDto.getFileDocumentName().addAll(tForm.getArrFileName());
      }
      String strProcessContent = tForm.getWorkLog();
      //
      spmDto.setPendingNumber(
          tForm.getNumPending() != null ? String.valueOf(tForm.getNumPending()) : null);
      spmDto.setTotalPendingTimeGnoc(tForm.getTotalPendingTimeGnoc());
      spmDto.setTotalProcessTimeGnoc(tForm.getTotalProcessTimeGnoc());
      spmDto.setEvaluateGnoc(tForm.getEvaluateGnoc());
      spmDto.setStatusName(tForm.getStateWo());
      spmDto.setProcess(strProcessContent);
      spmDto.setFtInfo(tForm.getFtInfo());
      log.info("updateSPMAction FROM WO: {}", spmDto);
      StringUtils.printLogData("updateSPMAction", spmDto, ServiceProblemInfoDTO.class);
      res = wsspmPort.updateSpmAction(spmDto);
      if (res.getKey() != null && "FAIL".equals(res.getKey())) {
        throw new Exception("[INCIDENT] Completed call spm fail: " + res.getMessage());
      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }
    return res;

  }
}
