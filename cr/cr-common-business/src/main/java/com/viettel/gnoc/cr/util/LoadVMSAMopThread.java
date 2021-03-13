/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.cr.util;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.VMSAMopDetailDTO;
import com.viettel.gnoc.cr.repository.CrDtRepository;
import com.viettel.gnoc.ws.provider.WSVipaDdPort;
import com.viettel.vmsa.ListMopDetailOutputDTO;
import com.viettel.vmsa.MopDetailDTO;
import com.viettel.vmsa.NodeDTO;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author thanhlong
 */
@Slf4j
@Service
public class LoadVMSAMopThread {

  @Autowired
  CrDtRepository crDtRepository;

  @Autowired
  WSVipaDdPort wsVipaDdPort;

  private Long objectToLong(Object txt) {
    if (txt == null || txt.toString().trim().isEmpty()) {
      return null;
    }
    try {
      return Long.parseLong(txt.toString());
    } catch (NumberFormatException ex) {
      log.error(ex.getMessage(), ex);
    }
    return null;
  }

  public void LoadVMSAMop(CrInsiteDTO crDTO) {
    Long crId = objectToLong(crDTO.getCrId());
    if (crId == null) {
      return;
    }

    Long vMSAKey = objectToLong(crDTO.getVMSAValidateKey());
    if (vMSAKey == null) {
      return;
    }
    try {

      String crNumber = crDTO.getCrNumber();
      if (crNumber == null || crNumber.trim().isEmpty()) {
        return;
      }

      crNumber = crNumber.trim();

      ListMopDetailOutputDTO mopData = wsVipaDdPort.getMOPsByCrNumber(crNumber, vMSAKey);
      if (mopData == null || mopData.getResultCode() != CrConfig.VMSA_SUCCESS_KEY
          || mopData.getMopDetailDTO() == null || mopData.getMopDetailDTO().isEmpty()) {
        String msg = "Can not get Mop from VMSA";
        if (mopData != null && mopData.getResultMessage() != null) {
          msg = mopData.getResultMessage();
        }
        ResultDTO dto = crDtRepository
            .insertVMSADT(crId, vMSAKey, CrConfig.CRGNOC_KEY, CrConfig.VMSA_FAIL_KEY, msg,
                new ArrayList<>(), "VNM", null);
        System.out.println("InsertVMSADT Msg: " + dto.getMessage());
        return;
      }
      List<VMSAMopDetailDTO> mopDTOList = new ArrayList<>();
      for (MopDetailDTO detailDTO : mopData.getMopDetailDTO()) {

        if (detailDTO.getMopFileName() == null || detailDTO.getMopFileContent() == null) {

          ResultDTO dto = crDtRepository
              .insertVMSADT(crId, vMSAKey, CrConfig.CRGNOC_KEY, CrConfig.VMSA_FAIL_KEY,
                  "MOP File Null", mopDTOList, "VNM", null);
          System.out.println("InsertVMSADT Msg: " + dto.getMessage());
          return;
        }

        VMSAMopDetailDTO dto = new VMSAMopDetailDTO();
        dto.setCreateTime(crNumber);
        for (NodeDTO node : detailDTO.getNodes()) {
          if (node.getNodeIp() != null && !node.getNodeIp().trim().isEmpty()) {
            dto.getIpList().add(node.getNodeIp().trim());
          }
        }
        dto.setMopFileName(detailDTO.getMopFileName());
        dto.setMopFileContent(detailDTO.getMopFileContent());
        dto.setMopFileType(detailDTO.getMopFileType());
        dto.setMopId(detailDTO.getMopId());
        dto.setMopName(detailDTO.getMopName());
        mopDTOList.add(dto);
      }

      ResultDTO dto = crDtRepository
          .insertVMSADT(crId, vMSAKey, CrConfig.CRGNOC_KEY, CrConfig.VMSA_SUCCESS_KEY, "",
              mopDTOList, "VNM", null);
      System.out.println("InsertVMSADT Msg: " + dto.getMessage());
    } catch (Exception e) {
      crDtRepository.insertVMSADT(crId, vMSAKey, CrConfig.CRGNOC_KEY, CrConfig.VMSA_FAIL_KEY,
          "Can not get Mop from VMSA", new ArrayList<>(), "VNM", null);
      log.error(e.getMessage(), e);
    }
  }

}
