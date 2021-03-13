/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.cr.thread;

import com.viettel.gnoc.commons.incident.provider.WSSecurityPort;
import com.viettel.gnoc.cr.dto.CrImpactedNodesDTO;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResetPasswordAfterCompleteCRImpactThread extends Thread {

  private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
  private List<CrImpactedNodesDTO> lstCrImpactedNodesDTOs;
  private String crNumber;
  private String userEmail;
  private WSSecurityPort wsSecurityPort;

  public ResetPasswordAfterCompleteCRImpactThread(WSSecurityPort wsSecurityPort,
      List<CrImpactedNodesDTO> lstCrImpactedNodesDTOs, String crNumber, String userEmail) {
    this.lstCrImpactedNodesDTOs = lstCrImpactedNodesDTOs;
    this.crNumber = crNumber;
    this.userEmail = userEmail;
    this.wsSecurityPort = wsSecurityPort;
  }

  @Override
  public void run() {

    log.info("bat dau Goi WSSecurityPort khi  hoan thanh CR " + this.dateFormat.format(new Date()));
    try {
      if (lstCrImpactedNodesDTOs != null && !lstCrImpactedNodesDTOs.isEmpty()) {
        for (CrImpactedNodesDTO dto : lstCrImpactedNodesDTOs) {
          String ip = dto.getIp();
          String rs = wsSecurityPort.resetPasswordAfterCompleteCRImpact(ip, crNumber, userEmail);
          log.info("CrNumber: " + crNumber + "; result: " + rs);
        }
      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
      //e.printStackTrace();
    } finally {
      log.info(
          "Ket thuc goi WSSecurityPort khi hoan thanh CR : " + this.dateFormat.format(new Date()));
    }
  }
}
