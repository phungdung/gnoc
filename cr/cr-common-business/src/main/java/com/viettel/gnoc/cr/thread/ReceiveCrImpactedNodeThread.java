/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.cr.thread;

import com.viettel.gnoc.commons.incident.provider.WSSecurityPort;
import com.viettel.gnoc.cr.dto.CrImpactedNodesDTO;
import com.viettel.security.service.CrsImpactForm;
import com.viettel.security.service.NodeImpactForm;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * @author longlt6
 */
@Slf4j
public class ReceiveCrImpactedNodeThread extends Thread {

  private CrsImpactForm crBO;
  private List<CrImpactedNodesDTO> lstCrImpactedNodesDTOs;
  private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
  private WSSecurityPort wsSecurityPort;

  public ReceiveCrImpactedNodeThread(WSSecurityPort wsSecurityPort, CrsImpactForm crBO,
      List<CrImpactedNodesDTO> lstCrImpactedNodesDTOs) {
    this.crBO = crBO;
    this.lstCrImpactedNodesDTOs = lstCrImpactedNodesDTOs;
    this.wsSecurityPort = wsSecurityPort;
  }

  @Override
  public void run() {
    try {

      //longlt6 bo ngay 26-04-2017
      log.info("bat dau Goi WSSecurityPort khi nhan CR " + this.dateFormat.format(new Date()));
      if (lstCrImpactedNodesDTOs == null || crBO == null) {
        return;
      }
      List<NodeImpactForm> lstNode = new ArrayList<>();
      if (lstCrImpactedNodesDTOs != null) {
        for (CrImpactedNodesDTO dto : lstCrImpactedNodesDTOs) {
          NodeImpactForm f = new NodeImpactForm();
          f.setExchangeCode(dto.getDeviceCode());
          f.setExchangeIp(dto.getIp());
          lstNode.add(f);
        }
        String rt = wsSecurityPort.receviceCRImpact(crBO, lstNode);
        log.info("Receive WSSecurityPort result : " + rt);
      }
      //longlt6 bo ngay 26-04-2017

    } catch (Exception e) {
      log.error(e.getMessage(), e);
      //e.printStackTrace();
    } finally {
      log.info("Ket thuc goi WSSecurityPort khi nhaan CR : " + this.dateFormat.format(new Date()));
    }
  }
}
