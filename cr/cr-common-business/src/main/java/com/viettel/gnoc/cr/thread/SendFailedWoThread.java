/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.cr.thread;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.proxy.WoServiceProxy;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.wo.dto.WoUpdateStatusForm;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * @author longlt6
 */
@Slf4j
public class SendFailedWoThread extends Thread {

  private String username;
  private String reason;
  private List<String> woCodes;
  private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
  private final Long WO_FAILT_TYPE = 0l;
  private WoServiceProxy woServiceProxy;

  public SendFailedWoThread(String username, String reason, List<String> woCodes,
      WoServiceProxy woServiceProxy) {
    this.username = username;
    this.reason = reason;
    this.woCodes = woCodes;
    this.woServiceProxy = woServiceProxy;
  }

  @Override
  public void run() {
    try {
      if (woCodes == null || woCodes.isEmpty()) {
        return;
      }
      for (String woCode : woCodes) {
        WoUpdateStatusForm updateForm = new WoUpdateStatusForm();
        updateForm.setFinishTime(dateFormat.format(new Date()));
        updateForm.setNewStatus(Long.valueOf(Constants.WO_STATUS.CLOSED_CD));
        updateForm.setReasonChange(reason + " " + woCode);
        updateForm.setSystemChange("CR");
        updateForm.setUserChange(username);
        updateForm.setWoCode(woCode);
        updateForm.setResultClose(WO_FAILT_TYPE);
        ResultDTO resultDTO = woServiceProxy.changeStatusWoProxy(updateForm);
        System.out.println(woCodes + " - " + resultDTO.getKey());

      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

}
