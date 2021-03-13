/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.wo.utils;

import com.viettel.cc.webserivce.CompCause;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.StringUtils;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author thanhlv12
 */
@Service
public class WS_CC_Direction {

  @Autowired
  WSCC2_MYT_Port wscc2_myt_port;

  @Autowired
  WSCC2Port wscc2Port;

  @Autowired
  WS_BCCS_CC3_Port ws_bccs_cc3_port;

  @Value("${application.ws.bccs_cc2_myt.url:null}")
  public String SERVICE_URL_BCCS_CC2;

  /**
   * Ham thuc hien lay danh sach nguyen nhan qua han
   */
  public List<CompCause> getListReasonOverdue(Long parentId, String nationCode) {

    List<CompCause> res = null;

    if (StringUtils.isStringNullOrEmpty(nationCode)
        || "VNM".equals(nationCode)) {

      res = wscc2Port.getListReasonOverdue(parentId);

    } else if ("MVT".equals(nationCode) || "VTP".equals(nationCode)) {
      WS_BCCS_CC3_Port gPort = ws_bccs_cc3_port.getInstance(nationCode,
          Constants.REST_FUNC.CC_GET_CAUSE_EXPIRE);
      res = gPort.getCauseExpires(parentId);

    } else if ("MYT".equals(nationCode)) {
      res = wscc2_myt_port.getListReasonOverdue(SERVICE_URL_BCCS_CC2, "MYT", parentId);
    } else {
      res = wscc2Port.getListReasonOverdue(parentId);
    }
    if (res == null || res.size() == 0) {
      res = wscc2Port.getListReasonOverdue(parentId);
    }

    return res;
  }
}
