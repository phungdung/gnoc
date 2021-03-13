package com.viettel.gnoc.wo.utils;

import com.viettel.bccs.cc.service.CauseErrorExpireDTO;
import com.viettel.bccs.cc.service.SpmRespone;
import com.viettel.bccs.cc.service.SpmServiceImpl;
import com.viettel.cc.webserivce.CompCause;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WSCC2Port {

  SpmServiceImpl port = null;

  @Autowired
  WSCC2PortFactory wscc2PortFactory;

  @PostConstruct
  public void init() {
  }

  private void createConnect() throws MalformedURLException, Exception {
    System.out.println("Start create connect SpmServiceImpl WS ");
    long startTime = Calendar.getInstance().getTimeInMillis();
    port = (SpmServiceImpl) wscc2PortFactory.getWsFactory().borrowObject();
    System.out.println(
        "End create connect SpmServiceImpl WS: " + (Calendar.getInstance().getTimeInMillis()
            - startTime));
  }

  public List<CompCause> getListReasonOverdue(Long parentId) {
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        List<CompCause> lstCompCause = new ArrayList<>();
        List<Long> lst1 = new ArrayList<>();
        lst1.add(0L);
        lst1.add(1L);
        lst1.add(2L);
        //webServiceType
        //1-lay all nguyen nhan qua han
        //2-lay theo nguyen nhan qua han cha
        //3-lay theo nguyen nhan loi con
        int webServiceType = 2;
        if (parentId != null) {
          webServiceType = 2;
        }

        SpmRespone res = port.getListCauseErrorExpire(webServiceType, lst1, parentId);

        if ("00".equals(res.getErrorCode())) {
          List<CauseErrorExpireDTO> lst = res.getListCauseErrorExpire();
          if (lst != null) {
            for (CauseErrorExpireDTO expireDTO : lst) {
              CompCause cause = new CompCause();
              cause.setCompCauseId(expireDTO.getCauseErrExpId());
              cause.setCode(expireDTO.getCode());
              cause.setName(expireDTO.getName());
              lstCompCause.add(cause);
            }
          }
        }
        return lstCompCause;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (null != port) {
          wscc2PortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;
  }

}
