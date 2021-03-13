package com.viettel.gnoc.wo.utils;

import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.nims.webservice.cd.bccs2.SubscriptionInfoForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WS_NIMS_CD_Direction {

  @Autowired
  WSNIMS_CD_BCCS2_Port wsNims;

  @Autowired
  WSNIMS_CD_GLOBAL_Port gPort;

  public SubscriptionInfoForm getSubscriptionInfo(String accountIsdn,
      Long pointNumber, String nationCode) {

    SubscriptionInfoForm res = null;

    // gọi link VNM
    if (StringUtils.isStringNullOrEmpty(nationCode) || "VNM".equals(nationCode)) {
      return wsNims.getSubscriptionInfo(accountIsdn, pointNumber);
    }
    // goi link global co truyen nationCode
    else {
      gPort.setNationCoce(nationCode);
      com.viettel.webservice.nims_cd_global.SubscriptionInfoForm gRes
          = gPort.getSubscriptionInfo(accountIsdn, pointNumber);
      if (gRes != null) {
        return (SubscriptionInfoForm) DataUtil.updateObjectData(gRes,
            new SubscriptionInfoForm());
      }
    }
    return null;
  }

}
